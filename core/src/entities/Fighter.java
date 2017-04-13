package entities;

import input.InputHandler;
import input.InputHandlerKeyboard;

import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.SFX;
import moves.Hitbox;
import moves.Move;
import moves.MoveList;
import moves.MoveList_Kicker;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Entity{

	private final float unregisteredInputMax = 0.2f;
	float wallJumpStrengthX = 8f;
	float wallJumpStrengthY = 7.2f;
	float wallSlideSpeed = -1f;
	float doubleJumpStrength = 8.5f;

	boolean doubleJumped, blockHeld = false;
	public int queuedCommand = InputHandler.commandNone;
	public final Timer inputQueueTimer = new Timer(8), wallJumpTimer = new Timer(10), attackTimer = new Timer(0);
	final Timer caughtTimer = new Timer(0), grabbingTimer = new Timer(0), dashTimer = new Timer(20);
	private final Timer knockIntoTimer = new Timer(20), invincibleTimer = new Timer(0), blockTimer = new Timer(8);
	float prevStickX = 0, stickX = 0, prevStickY = 0, stickY = 0;

	float fallSpeed = -7f, walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	float jumpStrength = 5f, dashStrength = 5f;
	float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;

	private TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));
	private Fighter caughtFighter = null;
	Move activeMove = null; 
	float percentage = 0, armor = 0, weight = 100;
	private InputHandler inputHandler = new InputHandlerKeyboard(this);
	private float initialHitAngle = 0;
	private final int randomAnimationDisplacement;
	final Vector2 spawnPoint;
	private int team = 0, stocks = 1;
	MoveList moveList = new MoveList_Kicker(this);

	public Fighter(float posX, float posY, int team) {
		super(posX, posY);
		this.team = team;
		spawnPoint = new Vector2(posX, posY);
		this.setInputHandler(inputHandler);
		timerList.addAll(Arrays.asList(inputQueueTimer, wallJumpTimer, attackTimer, caughtTimer,
				grabbingTimer, dashTimer, knockIntoTimer, invincibleTimer, blockTimer));
		image = new Sprite(defaultTexture);
		state = State.STAND;
		randomAnimationDisplacement = (int) (8 * Math.random());
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
		activeMove = moveList.selectThrow();
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
			boolean fighterGoingFastEnough = knockbackIntensity(fi.velocity) > 9;
			boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && getTeam() == fi.getTeam() && !fi.hitstunTimer.timeUp();
			if (knockInto && isTouching(fi, 0) && knockbackIntensity(fi.velocity) > knockbackIntensity(velocity)) knockBack(fi);
		}

	}

	private final float pushForce = 0.04f;
	private void pushAway(Entity e){
		float dirPush = Math.signum(e.position.x - this.position.x);
		velocity.x -= dirPush * pushForce;
	}

	public void knockBack(Fighter fi){
		Vector2 knockIntoVector = new Vector2(fi.velocity.x, fi.velocity.y);
		System.out.println("Missile velocity:" + knockIntoVector);
		Hitbox h = new Hitbox(fi, (fi.weight/100.0f) * 3, 1.5f, knockbackIntensity(knockIntoVector), 
				knockIntoVector.angle(), 0, 0, 0, null);
		knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
		knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
		takeKnockIntoKnockback(knockIntoVector, h.getDamage() / 2, Hitbox.hitstunFormula(h.getDamage()) );
		fi.knockIntoTimer.restart();
		knockIntoTimer.restart();
		new SFX.LightHit().play();
		System.out.println("Target velocity:" + knockIntoVector);
	}

	void updateState(){
		if (isGrounded()) updateGroundedState();
		else updateAerialState();
		prevState = state;
	}

	private float minCrouchHold = 0.9f;
	private float minRunHold = 0.5f;
	private void updateGroundedState(){
		if (jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) {
			state = State.JUMP;
			jump();
		}
		else if (!inGroundedState()) ground();

		if (!jumpSquatTimer.timeUp()) {
			if (state != State.JUMPSQUAT) preJumpSquatState = state;
			state = State.JUMPSQUAT;
		}
		else if (state == State.DODGE && !blockTimer.timeUp() || blockHeld) state = State.DODGE;
		else if (Math.abs(stickX) < minRunHold && stickY > minCrouchHold) {
			if (blockHeld) activateBlock();
			else state = State.CROUCH;
		}
		else if (Math.abs(stickX) > minRunHold){
			if (isRun() || (!inGroundedState(prevState)) ) state = State.RUN;
			else if (state == State.DASH && !dashTimer.timeUp()) state = State.DASH;
			else state = State.WALK;
			boolean noWalls = (!doesCollide(position.x - 2, position.y) && direction == Direction.LEFT)
					|| (!doesCollide(position.x + 2, position.y) && direction == Direction.RIGHT);
			if (canAct() && noWalls && Math.signum(velocity.x) != direct()) flip();
		}
		else if (blockHeld) activateBlock();
		else state = State.STAND;
		if (canAttack() && prevState == State.RUN && state != State.RUN) startAttack(moveList.skid());
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
		if (!isGrounded() || !inGroundedState() || isRun()) return false;
		dashTimer.restart();
		velocity.x = Math.signum(stickX) * dashStrength;
		state = State.DASH;
		MapHandler.addEntity(new Graphic.DustCloud(this, position.x, position.y));
		return true;
	}

	private void activateBlock(){
		if (state != State.DODGE) {
			state = State.DODGE;
			blockTimer.restart();
		}
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
		if (Math.abs(stickX) < unregisteredInputMax || !canMove() || !isGrounded() || state == State.CROUCH || state == State.DODGE || state == State.JUMPSQUAT) return;
		if (null != activeMove){
			boolean startAttackTurn = !attackTimer.timeUp() && attackTimer.getCounter() < minFrameTurn && !activeMove.isNoTurn();
			if (!canAct() && !startAttackTurn) return;
		}
		boolean turnLeft = stickX < -minTurn && (prevStickX > -minTurn) && getDirection() == Direction.RIGHT;
		boolean turnRight = stickX > minTurn && (prevStickX < minTurn)  && getDirection() == Direction.LEFT;
		if (turnLeft || turnRight) flip();
	}

	void handleMovement(){
		if (groundedAttacking() || !canMove() || !hitstunTimer.timeUp() || state == State.DODGE) return;
		switch (state){
		case WALK: addSpeed(walkSpeed, walkAcc); break;
		case DASH:
		case RUN: addSpeed(runSpeed, runAcc); break;
		case JUMP: velocity.y += jumpAcc; break;
		default: break;
		}
		if (!isGrounded() && wallJumpTimer.timeUp() && Math.abs(stickX) > unregisteredInputMax) {
			if (Math.abs(velocity.x) > airSpeed) addSpeed(runSpeed, airAcc);
			else addSpeed(airSpeed, airAcc);
		}
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
			return true;
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
		if (stickX < 0) MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		else MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		if (velocity.y < 0) velocity.y = 0;
		velocity.x += 2 * stickX;
		getVelocity().y += jumpStrength;
	}

	private final int wallSlideDistance = 1;
	private boolean isWallSliding() {
		if (isGrounded() || velocity.y > 0 || !canAct()) return false;
		boolean canWS = false;
		if (prevStickX < -unregisteredInputMax) {
			canWS = doesCollide(position.x - wallSlideDistance, position.y) && doesCollide(position.x - wallSlideDistance, position.y + image.getHeight()/2);
			if (direction == Direction.RIGHT && canWS) flip();
		}
		if (prevStickX > unregisteredInputMax) {
			canWS = doesCollide(position.x + wallSlideDistance, position.y) && doesCollide(position.x + wallSlideDistance, position.y + image.getHeight()/2);
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
		velocity.x += 1 * stickX;
		doubleJumped = true;
	}

	public boolean tryNormal(){
		startAttack(moveList.selectNormalMove());
		return true;
	}

	public boolean trySpecial(){
		startAttack(moveList.selectSpecialMove());
		return true;
	}

	public boolean tryDodge(){
		if (!canAct()) return true;
		Move block = moveList.selectBlock();
		if (null != block){
			startAttack(moveList.selectBlock());
			return true;
		}
		else return true;
	}

	public boolean tryStickForward(){
		if (state == State.DODGE) {
			if (direction == Direction.LEFT) startAttack(moveList.rollBack());
			else startAttack(moveList.rollForward());
		}
		else tryDash();
		return true;
	}

	public boolean tryStickBack(){ 
		if (state == State.DODGE) {
			if (direction == Direction.LEFT) startAttack(moveList.rollForward());
			else startAttack(moveList.rollBack());
		}
		else tryDash();
		return true;
	}
	

	public boolean tryStickUp(){
		return true; 
	}

	public boolean tryStickDown(){
		if (state == State.DODGE) {
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
			startAttack(moveList.dodge());
		}
		return true; 
	}


	public boolean tryGrab(){
		startAttack(moveList.selectGrab()); 
		return true; 
	}

	public boolean tryCharge(){
		startAttack(moveList.selectCharge()); 
		return true; 
	}

	public boolean tryTaunt() {
		if (!isGrounded() || !canAct()) return false;
		startAttack(moveList.selectTaunt()); 
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
	
	private void takeKnockIntoKnockback(Vector2 knockback, float DAM, int hitstun){
		knockbackHelper(knockback, DAM, hitstun, knockbackIntensity(velocity) < knockbackIntensity(knockback));
	}

	public void takeKnockback(Vector2 knockback, float DAM, int hitstun) {
		knockbackHelper(knockback, DAM, hitstun, true);
	}
	
	private void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean tryy){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		if (tryy) velocity.set(knockback);
		initialHitAngle = knockback.angle();
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.restart();
		percentage += DAM;
		if (state == State.HELPLESS) state = State.FALL;
		if (getArmor() <= 0 && knockbackIntensity(knockback) <= 0){
			activeMove = null;
			attackTimer.end();
		}
	}

	private final float diStrength = 8;
	private float directionalInfluenceAngle(Vector2 knockback){
		if (getInputHandler().getXInput() < unregisteredInputMax && getInputHandler().getXInput() < unregisteredInputMax) return knockback.angle();
		Vector2 di = new Vector2(getInputHandler().getXInput(), getInputHandler().getYInput());
		float parallelAngle = Math.round(knockback.angle() - di.angle());
		double sin = Math.sin(parallelAngle * Math.PI/180);
		int signMod = 1;
		if (knockback.angle() > 90 && knockback.angle() <= 270) signMod *= -1;
		if (di.x < 0) signMod *= -1;
		double diModifier = signMod * Math.signum(180 - di.angle()) * diStrength * Math.pow(sin, 2);
		knockback.setAngle((float) (knockback.angle() + diModifier));
		return knockback.angle();
	}

	private final float heldDistance = 24;
	public void takeGrab(Fighter user, Fighter target, int caughtTime) {
		user.isNowGrabbing(target, caughtTime);
		float newPosX = user.position.x + (heldDistance * user.direct());
		float newPosY = user.position.y + image.getHeight()/4;
		if (!doesCollide(newPosX, newPosY)) position.set(newPosX, newPosY);
		else if (!doesCollide(position.x, newPosY)) position.set(position.x, newPosY);
		caughtTimer.setEndTime(caughtTime);
		caughtTimer.restart();
		endAttack();
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
		if (null != activeMove && !activeMove.continuesOnLanding()) endAttack();
		if (activeMove == null) startAttack(moveList.land());
		doubleJumped = false;
	}
	
	private void endAttack(){
		activeMove = null;
		attackTimer.end();
	}

	public void handleJumpHeld(boolean button) {
		if (isGrounded() && button && state != State.JUMPSQUAT && jumpSquatTimer.timeUp()) jumpSquatTimer.restart();
		if (state == State.JUMP && !button) jumpTimer.end();
	}

	public void handleBlockHeld(boolean block) {
		blockHeld = block;
	}

	public void respawn() {
		stocks -= 1;
		new SFX.Explode().play();
		position.set(spawnPoint);
		percentage = 0;
		velocity.x = 0;
		velocity.y = 0;
		state = State.FALL;
		doubleJumped = false;
		for (Timer t: timerList) t.end();
		invincibleTimer.setEndTime(60);
		invincibleTimer.restart();
	}

	public void setRespawnPoint(Vector2 startPosition) {
		spawnPoint.set(startPosition);
	}

	public float getPercentage() { return percentage; }
	public float getWeight() { return weight; }
	public float getStickX() { return stickX; }
	public float getStickY() { return stickY; }
	public InputHandler getInputHandler() { return inputHandler; }
	public static float knockbackIntensity(Vector2 knockback) { return Math.abs(knockback.x) + Math.abs(knockback.y); }
	public boolean canAttack() { return canAttackBlock() && state != State.DODGE; }
	public boolean canAttackBlock() { return canAct() && state != State.WALLSLIDE; }
	public boolean canAct(){ return hitstunTimer.timeUp() && attackTimer.timeUp() && state != State.HELPLESS && canMove(); }
	public boolean canMove(){ return caughtTimer.timeUp() && grabbingTimer.timeUp(); } 
	public boolean isDashing() { return state == State.RUN || state == State.DASH; }
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0 || !invincibleTimer.timeUp(); }
	public boolean isInHitstun() { return !hitstunTimer.timeUp(); }
	public boolean isCharging() {
		if (null == getInputHandler()) return false;
		else return !attackTimer.timeUp() && getInputHandler().isCharging();
	}
	public InputPackage getInputPackage() { return new InputPackage(this); }
	public void setArmor(float armor) { this.armor = armor; }
	public float getArmor() { 
		float addedMoveArmor = 0;
		if (null != activeMove) addedMoveArmor = activeMove.getArmor();
		return armor + addedMoveArmor; 
	}
	public void setToFall() { state = State.FALL; }
	public void setInputHandler(InputHandler inputHandler) { this.inputHandler = inputHandler; }
	public int getTeam() { return team; }
	public State getState() { return state; } 
	protected TextureRegion getFrame(Animation anim, float deltaTime) { return anim.getKeyFrame(deltaTime + randomAnimationDisplacement); }
	public int getStocks() { return stocks; }
	public void setStocks(int i) { stocks = i; }

	public void setInvincible(int i) { 
		invincibleTimer.restart();
		invincibleTimer.setEndTime(i);
	}

}
