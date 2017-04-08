package entities;

import input.InputHandler;
import input.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.SFX;
import moves.Hitbox;
import moves.Move;
import moves.MoveList;
import moves.MoveList_Kicker;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Entity{

	private final float unregisteredInputMax = 0.2f;
	float wallJumpStrengthX = 7.2f;
	float wallJumpStrengthY = 6.6f;
	float wallSlideSpeed = -1f;
	float doubleJumpStrength = 8.5f;

	boolean doubleJumped = false;
	public int queuedCommand = InputHandler.commandNone;
	public final Timer inputQueueTimer = new Timer(6, false), wallJumpTimer = new Timer(8, false), attackTimer = new Timer(0, false);
	private final Timer caughtTimer = new Timer(0, false), grabbingTimer = new Timer(0, false), dashTimer = new Timer(18, false);
	private final Timer knockIntoTimer = new Timer(20, false);
	float prevStickX = 0, stickX = 0, prevStickY = 0, stickY = 0;

	int jumpSquatFrames = 4;
	float fallSpeed = -7f, walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	float jumpStrength = 5f, dashStrength = 5f;
	float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;

	private TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.PNG")));
	private Fighter caughtFighter = null;
	Move activeMove = null;
	float percentage = 0;
	float weight = 100;
	float armor = 0;
	private InputHandler inputHandler = new InputHandlerKeyboard(this);
	private float initialHitAngle = 0;
	final Vector2 spawnPoint;
	int team = 0;
	MoveList moveList = new MoveList_Kicker();

	public Fighter(float posX, float posY) {
		super(posX, posY);
		spawnPoint = new Vector2(posX, posY);
		this.setInputHandler(inputHandler);
		timerList.addAll(Arrays.asList(inputQueueTimer, wallJumpTimer, attackTimer, caughtTimer, grabbingTimer, dashTimer, knockIntoTimer));
		image = new Sprite(defaultTexture);
		state = State.STAND;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		stickX = getInputHandler().getXInput();
		stickY = getInputHandler().getYInput();
		getInputHandler().update();

		if (!grabbingTimer.timeUp()) handleThrow();
		if (null != activeMove) handleMove();

		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);

		prevStickX = stickX;
		prevStickY = stickY;
	}

	private void handleThrow(){
		activeMove = moveList.selectThrow(this);
		if (null != activeMove) {
			startAttack(activeMove);
			grabbingTimer.end();
			caughtFighter.caughtTimer.end();
			caughtFighter.hitstunTimer.setEndTime(5);
			caughtFighter.hitstunTimer.restart();
			caughtFighter = null;
		}
	}

	private void handleMove(){
		activeMove.update();
		if (activeMove.done()) {
			if (activeMove.causesHelpless()) state = State.HELPLESS;
			activeMove = null;
		}
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			int pushDistance = 16 + 2 * ((int) image.getWidth() - defaultTexture.getRegionWidth());
			if (isTouching(fi, pushDistance) && Math.abs(fi.velocity.x) < 1 && Math.abs(this.velocity.x) < 1
					&& e.isGrounded() && isGrounded()){
				pushAway(fi);
				((Fighter) fi).pushAway(this);
			}
			boolean fighterGoingFastEnough = knockbackIntensity(fi.velocity) > 3;
			boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && team == fi.team && !fi.hitstunTimer.timeUp();
			if (knockInto && isTouching(fi, 0) && knockbackIntensity(fi.velocity) > knockbackIntensity(velocity)) knockBack(fi);
		}

	}

	private final float pushForce = 0.04f;
	private void pushAway(Entity e){
		float dirPush = Math.signum(e.position.x - this.position.x);
		velocity.x -= dirPush * pushForce;
	}

	private final float knockbackDilute = 0.75f;
	private void knockBack(Fighter fi){
		Vector2 knockIntoVector = new Vector2(fi.velocity.x, fi.velocity.y);
		knockIntoVector.x *= knockbackDilute;
		knockIntoVector.y *= knockbackDilute;
		float damage = (Math.abs(fi.velocity.x) +  Math.abs(fi.velocity.y)) / 3;
		knockIntoVector.setAngle((fi.velocity.angle() + 90) / 2);
		takeKnockback(knockIntoVector, damage, Hitbox.hitstunFormula(damage) );
		fi.knockIntoTimer.restart();
		knockIntoTimer.restart();
		new SFX.LightHit().play();
	}

	void updateState(){
		if (isGrounded()) updateGroundedState();
		else updateAerialState();
		prevState = state;
	}

	private float minCrouchHold = 0.9f;
	private float minRunHold = 0.5f;
	private void updateGroundedState(){
		if (!inGroundedState()) ground();
		if (stickY > minCrouchHold) state = State.CROUCH;
		else if (Math.abs(stickX) > minRunHold){
			if (isRun()) state = State.RUN;
			else if (state == State.DASH && !dashTimer.timeUp()) state = State.DASH;
			else state = State.WALK;
			boolean noWalls = (!doesCollide(position.x - 2, position.y) && direction == Direction.LEFT)
					|| (!doesCollide(position.x + 2, position.y) && direction == Direction.RIGHT);
			if (canAct() && noWalls && Math.signum(velocity.x) != direct()) flip();
		}
		else state = State.STAND;
		if (canAttack() && prevState == State.RUN && (Math.abs(stickX) < minRunHold)) startAttack(moveList.skid(this));
	}

	private boolean isRun(){
		boolean turningAround = (Math.signum(velocity.x) != Math.signum(stickX));
		boolean enterRun = (state == State.DASH && dashTimer.timeUp());
		return (state == State.RUN || enterRun) && !turningAround;
	}

	private void updateAerialState(){
		if (state == State.HELPLESS) return;
		if (isWallSliding()) state = State.WALLSLIDE;
		else if (!jumpTimer.timeUp()) state = State.JUMP;
		else state = State.FALL;
	}

	private boolean tryDash(){
		if (!inGroundedState() || isRun()) return false;
		dashTimer.restart();
		velocity.x = Math.signum(stickX) * dashStrength;
		state = State.DASH;
		return true;
	}

	void updateImage(float deltaTime){
		/* */
	}

	void handleGravity(){
		if (state == State.WALLSLIDE) velocity.y = wallSlideSpeed;
		else velocity.y += gravity;
	}

	private final float minTurn = 0.2f;
	private final int minFrameTurn = 3;
	void handleDirection(){
		if (Math.abs(stickX) < unregisteredInputMax || !canMove() || !isGrounded() || state == State.CROUCH) return;
		if (null != activeMove){
			boolean startAttackTurn = !attackTimer.timeUp() && attackTimer.getCounter() < minFrameTurn && !activeMove.isNoTurn();
			if (!canAct() && !startAttackTurn) return;
		}
		boolean turnLeft = stickX < -minTurn && (prevStickX > -minTurn) && getDirection() == Direction.RIGHT;
		boolean turnRight = stickX > minTurn && (prevStickX < minTurn)  && getDirection() == Direction.LEFT;
		if (turnLeft || turnRight) flip();
	}

	void handleMovement(){
		if (groundedAttacking() || !canMove() || !hitstunTimer.timeUp()) return;
		switch (state){
		case WALK: addSpeed(walkSpeed, walkAcc); break;
		case DASH:
		case RUN: addSpeed(runSpeed, runAcc); break;
		case JUMP: velocity.y += jumpAcc; break;
		default: break;
		}
		if (!isGrounded() && wallJumpTimer.timeUp() && Math.abs(stickX) > unregisteredInputMax) addSpeed(airSpeed, airAcc);
	}

	void updatePosition(){
		if (canMove()) super.updatePosition();
	}

	void limitSpeeds(){
		boolean notAMeteor = initialHitAngle > 0 && initialHitAngle < 180;
		if ((hitstunTimer.timeUp() || notAMeteor) && velocity.y < fallSpeed) velocity.y = fallSpeed;
	}

	private void addSpeed(float speed, float acc){ 
		if (Math.abs(velocity.x) < Math.abs(speed)) velocity.x += Math.signum(stickX) * acc; 
	}

	private boolean groundedAttacking() { return !attackTimer.timeUp() && isGrounded(); }

	public boolean tryJump(){
		if (isGrounded()) {
			jump(); return true;
		}
		else if (isWallSliding()) {
			wallJump(); return true;
		}
		else if (!doubleJumped && state != State.JUMP){
			doubleJump(); return true;
		}
		return false;
	}

	private void jump(){
		jumpTimer.restart();
		if (velocity.y < 0) velocity.y = 0;
		velocity.x += 2 * stickX;
		getVelocity().y += jumpStrength;
	}

	private final int wallSlideDistance = 1;
	private boolean isWallSliding() {
		if (isGrounded() || velocity.y > 0 || !canAct()) return false;
		boolean canWS = false;
		if (prevStickX < -unregisteredInputMax) {
			canWS = doesCollide(position.x - wallSlideDistance, position.y);
			if (direction == Direction.RIGHT && canWS) flip();
		}
		if (prevStickX > unregisteredInputMax) {
			canWS = doesCollide(position.x + wallSlideDistance, position.y);
			if (direction == Direction.LEFT && canWS) flip();
		}
		return canWS;
	}

	private void wallJump(){
		wallJumpTimer.restart();
		flip();
		velocity.x = wallJumpStrengthX * direct();
		velocity.y = wallJumpStrengthY;
	}

	private void doubleJump(){
		getVelocity().y = doubleJumpStrength;
		if (prevStickX < -unregisteredInputMax && velocity.x > 0) velocity.x = 0; 
		if (prevStickX >  unregisteredInputMax && velocity.x < 0) velocity.x = 0; 
		velocity.x += 4 * stickX;
		doubleJumped = true;
	}

	public boolean tryNormal(){
		startAttack(moveList.selectNormalMove(this));
		return true;
	}

	public boolean trySpecial(){
		startAttack(moveList.selectSpecialMove(this));
		return true;
	}

	public boolean tryBoost(){
		if (isGrounded() || !canAct()) return false;
		startAttack(moveList.boost(this));
		return true;
	}

	public boolean tryStickForward(){
		return tryDash();
	}

	public boolean tryStickBack(){ 
		return tryDash();
	}

	public boolean tryGrab(){
		startAttack(moveList.selectGrab(this)); 
		return true; 
	}

	public boolean tryCharge(){
		startAttack(moveList.selectCharge(this)); 
		return true; 
	}

	public boolean tryStickUp(){
		return true; 
	}

	public boolean tryStickDown(){
		return true; 
	}

	private final float minDirect = 0.85f;
	public boolean holdUp() 		{ return -stickY > minDirect; }
	public boolean holdDown()		{ return stickY > minDirect; }
	public boolean holdForward() 	{ return Math.signum(stickX) == direct() && Math.abs(stickX) > minDirect; }
	public boolean holdBack() 		{ return Math.signum(stickX) != direct() && Math.abs(stickX) > minDirect; }

	private void startAttack(Move m){
		activeMove = m;
		attackTimer.setEndTime(m.getDuration() + 1);
		attackTimer.restart();
	}

	public void takeKnockback(Vector2 knockback, float DAM, int hitstun) {
		knockback.setAngle(directionalInfluenceAngle(knockback));
		velocity.set(knockback);
		initialHitAngle = knockback.angle();
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.restart();
		percentage += DAM;
		if (state == State.HELPLESS) state = State.FALL;
		activeMove = null;
		attackTimer.end();
	}

	private float directionalInfluenceAngle(Vector2 knockback){
		Vector2 di = new Vector2(getInputHandler().getXInput(), getInputHandler().getYInput());
		float parallelAngle = Math.round(knockback.angle() - di.angle());
		double sin = Math.sin(parallelAngle * Math.PI/180);
		int signMod = 1;
		if (knockback.angle() > 90 && knockback.angle() <= 270) signMod *= -1;
		if (di.x < 0) signMod *= -1;
		double diModifier = signMod * Math.signum(180 - di.angle()) * 18 * Math.pow(sin, 2);
		knockback.setAngle((float) (knockback.angle() + diModifier));
		return knockback.angle();
	}

	private final float heldDistance = 24;
	public void takeGrab(Fighter user, Fighter target, int caughtTime) {
		user.isNowGrabbing(target, caughtTime);
		float newPosX = user.position.x + (heldDistance * user.direct());
		float newPosY = user.position.y + image.getHeight()/4;
		if (!doesCollide(newPosX, newPosY)) position.set(newPosX, newPosY);
		else position.y = newPosY; // won't move target's X coordinate if that would put target in a wall
		caughtTimer.setEndTime(caughtTime);
		caughtTimer.restart();
	}

	public void isNowGrabbing(Fighter target, int caughtTime){
		caughtFighter = target;
		grabbingTimer.setEndTime(caughtTime);
		grabbingTimer.restart();
	}

	public void ground(){
		if (!hitstunTimer.timeUp() && hitstunTimer.getCounter() > 1) {
			// ????
			return;
		}
		super.ground();
		if (null != activeMove && !activeMove.continuesOnLanding()) {
			activeMove = null;
			attackTimer.end();
		}
		if (activeMove == null) startAttack(moveList.land(this));
		doubleJumped = false;
	}

	public void handleJumpCommand(boolean button) {
		if (state == State.JUMP && !button) jumpTimer.end();
	}

	public void respawn() {
		position.set(spawnPoint);
		percentage = 0;
		velocity.x = 0;
		velocity.y = 0;
		state = State.FALL;
		doubleJumped = false;
	}

	public void setRespawnPoint(Vector2 startPosition) {
		startPosition.x *= GlobalRepo.TILE;
		startPosition.y *= GlobalRepo.TILE;
		spawnPoint.set(startPosition);
	}

	public float getPercentage() { return percentage; }
	public float getWeight() { return weight; }
	public float getStickX() { return stickX; }
	public float getStickY() { return stickY; }
	public InputHandler getInputHandler() { return inputHandler; }
	private float knockbackIntensity(Vector2 knockback) { return Math.abs(knockback.x) + Math.abs(knockback.y); }

	public boolean canAttack() { return canAct() && state != State.WALLSLIDE; }
	public boolean canAct(){  return hitstunTimer.timeUp() && attackTimer.timeUp() && state != State.HELPLESS && canMove(); }
	private boolean canMove(){ return caughtTimer.timeUp() && grabbingTimer.timeUp(); } 
	public boolean isDashing() { return state == State.RUN || state == State.DASH; }
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0; }
	private boolean inGroundedState() { return groundedStates.contains(state);}
	public boolean isCharging() {
		if (null == getInputHandler()) return false;
		else return getInputHandler().isCharging();
	}
	public InputPackage getInputPackage() { return new InputPackage(this); }
	public void setArmor(float armor) { this.armor = armor; }
	public float getArmor() { return armor; }

	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	private final List<State> groundedStates = new ArrayList<State>(Arrays.asList(State.STAND, State.WALK, State.RUN, State.DASH, State.CROUCH));

}
