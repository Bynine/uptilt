package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.InputHandler;
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

	public static final int commandNone		=-1, commandAttack	= 0, commandSpecial	= 1;
	public static final int commandJumpX	= 2, commandJumpY	= 3, commandBlock	= 4, commandGrab = 5;
	public static final int commandStickUp 	=20, commandStickBack	= 21, commandStickForward	= 22,commandStickDown	= 23;
	public static final int commandCUp		=30, commandCBack 		= 31, commandCForward		= 32,commandCDown 		= 33;

	private final float unregisteredInputMax = 0.2f;
	float wallJumpStrengthX = 7.2f;
	float wallJumpStrengthY = 6.6f;
	float wallSlideSpeed = -1f;
	float doubleJumpStrength = 7.5f;

	boolean doubleJumped, boosted, fastFall = false;
	private int queuedCommand = commandNone;
	private final Timer queueTimer = new Timer(6, false), wallJumpTimer = new Timer(8, false);
	public final Timer attackTimer = new Timer(0, false);
	private final Timer caughtTimer = new Timer(0, false), grabbingTimer = new Timer(0, false), dashTimer = new Timer(24, false);
	float prevStickX = 0, stickX = 0, prevStickY = 0, stickY = 0;

	int jumpSquatFrames = 4;
	float fallSpeed = -7f, fastFallSpeed = -8f, walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	float jumpStrength = 5f, dashStrength = 2.4f;
	float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;

	private TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.PNG")));
	private Fighter caughtFighter = null;
	Move activeMove = null;
	float percentage = 0;
	float weight = 100;
	final InputHandler inputHandler;
	final Vector2 spawnPoint;
	MoveList moveList = new MoveList_Kicker();

	public Fighter(float posX, float posY, InputHandler inputHandler) {
		super(posX, posY);
		spawnPoint = new Vector2(posX, posY);
		this.inputHandler = inputHandler;
		timerList.addAll(Arrays.asList(queueTimer, wallJumpTimer, attackTimer, caughtTimer, grabbingTimer, dashTimer));
		image = new Sprite(defaultTexture);
		state = State.STAND;
	}

	private final float minFastFallStickFlick = 0.85f;
	@Override
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		stickX = inputHandler.getXInput();
		stickY = inputHandler.getYInput();
		inputHandler.update(this);
		boolean canFastFall = canMove() && velocity.y < 0 && Math.abs(stickY - prevStickY) > minFastFallStickFlick && stickY > 0;
		if (canFastFall) activateFastFall();

		super.update(rectangleList, entityList, deltaTime);
		updateImage();

		if (queueTimer.timeUp()) queuedCommand = commandNone;
		if (!queueTimer.timeUp()) handleCommand(queuedCommand);
		if (!grabbingTimer.timeUp()) handleThrow();
		if (null != activeMove) handleMove();

		prevStickX = stickX;
		prevStickY = stickY;
	}

	private void activateFastFall(){
		velocity.y = fastFallSpeed;
		fastFall = true;
	}

	private void handleThrow(){
		activeMove = selectThrow();
		if (null != activeMove) {
			startAttack(activeMove);
			grabbingTimer.end();
			caughtFighter.caughtTimer.end();
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

	private final float minStickFlick = 0.5f;
	@Override
	void updateState(){
		if (isGrounded()){
			if (!inGroundedState()) ground();
			if (stickY > 0.5f) state = State.CROUCH;
			else if (Math.abs(stickX) > 0.5f){
				boolean turningAround = (Math.signum(velocity.x) != Math.signum(stickX));
				boolean enterRun = (state == State.DASH && dashTimer.timeUp());
				if ((state == State.RUN && !turningAround) || enterRun) state = State.RUN;
				else if (state == State.RUN && turningAround) {} // TODO: add skid "move"
				else if (attackTimer.timeUp() && Math.abs(stickX - prevStickX) > minStickFlick) startDash();
				else if (state == State.DASH && !dashTimer.timeUp()) state = State.DASH;
				else state = State.WALK;
			}
			else state = State.STAND;
		}
		else{
			if (state == State.HELPLESS) return;
			if (isWallSliding()) state = State.WALLSLIDE;
			else if (!jumpTimer.timeUp()) state = State.JUMP;
			else state = State.FALL;
		}
	}

	private void startDash(){
		dashTimer.restart();
		velocity.x = Math.signum(stickX) * dashStrength;
		state = State.DASH;
	}

	void updateImage(){
		/* */
	}

	@Override
	void handleGravity(){
		if (state == State.WALLSLIDE) velocity.y = wallSlideSpeed;
		else velocity.y += gravity;
	}

	@Override
	void handleDirection(){
		if (Math.abs(stickX) < unregisteredInputMax || !canAct() || !isGrounded()) return;
		if ((stickX < 0 && getDirection() == Direction.RIGHT) || (stickX > 0 && getDirection() == Direction.LEFT)) flip();
	}

	@Override
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

	@Override
	void updatePosition(){
		if (canMove()) super.updatePosition();
	}

	@Override
	void limitSpeeds(){
		if (hitstunTimer.timeUp()) {
			if (!fastFall && velocity.y < fallSpeed) velocity.y = fallSpeed;
			if (fastFall && velocity.y < fastFallSpeed) velocity.y = fastFallSpeed;
		}
	}

	private void addSpeed(float speed, float acc){ 
		if (Math.abs(velocity.x) < Math.abs(speed)) velocity.x += Math.signum(stickX) * acc; 
	}

	private boolean groundedAttacking() { return !attackTimer.timeUp() && isGrounded(); }

	public void handleCommand(int command){
		boolean wasCommandAccepted = false;

		if (canAct()){
			switch (command){
			case commandJumpX: 
			case commandJumpY:		wasCommandAccepted = tryJump(); break;
			case commandAttack:		wasCommandAccepted = tryNormal(); break;
			case commandSpecial:	wasCommandAccepted = trySpecial(); break;
			case commandGrab:		wasCommandAccepted = tryGrab(); break;
			case commandCUp:		wasCommandAccepted = tryCUp(); break;
			case commandCForward:	wasCommandAccepted = tryCForward(); break;
			case commandCBack:		wasCommandAccepted = tryCBack(); break;
			case commandCDown:		wasCommandAccepted = tryCDown(); break;
			default:				wasCommandAccepted = true; break;
			}
		}

		if (!wasCommandAccepted && queueTimer.timeUp()) {
			queuedCommand = command;
			queueTimer.restart();
		}
		else if (wasCommandAccepted) queuedCommand = commandNone;
	}

	private boolean tryJump(){
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
		getVelocity().y += jumpStrength;
	}

	private final int wallSlideDistance = 1;
	private boolean isWallSliding() {
		if (isGrounded() || velocity.y > 0) return false;
		boolean canWS = false;
		if (prevStickX < -unregisteredInputMax) {
			canWS = doesCollide(position.x - wallSlideDistance, position.y);
			if (direction == Direction.RIGHT) flip();
		}
		if (prevStickX > unregisteredInputMax) {
			canWS = doesCollide(position.x + wallSlideDistance, position.y);
			if (direction == Direction.LEFT) flip();
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
		doubleJumped = true;
	}

	private boolean tryNormal(){
		startAttack(selectNormalMove());
		return true;
	}

	private boolean trySpecial(){
		fastFall = false;
		startAttack(selectSpecialMove());
		return true;
	}

	private boolean tryGrab(){ startAttack(selectGrab()); return true; }
	private boolean tryCUp(){ startAttack(selectC(moveList.uCharge(this), moveList.uAir(this))); return true; }
	private boolean tryCForward(){ startAttack(selectC(moveList.fCharge(this), moveList.fAir(this))); return true; }
	private boolean tryCBack(){ startAttack(selectC(moveList.bCharge(this), moveList.bAir(this))); return true; }
	private boolean tryCDown(){ startAttack(selectC(moveList.dCharge(this), moveList.dAir(this))); return true; }

	private void startAttack(Move m){
		activeMove = m;
		attackTimer.setEndTime(m.getDuration() + 1);
		attackTimer.restart();
	}

	private final float minDirect = 0.9f;
	private boolean holdUp() 		{ return -stickY > minDirect; }
	private boolean holdDown()		{ return stickY > minDirect; }
	private boolean holdForward() 	{ return Math.signum(stickX) == direct() && Math.abs(stickX) > minDirect; }
	private boolean holdBack() 		{ return Math.signum(stickX) != direct() && Math.abs(stickX) > minDirect; }
	private Move selectNormalMove(){
		if (isGrounded()) {
			if (isDashing()) return moveList.slide(this);
			else if (holdUp()) return moveList.uWeak(this);
			else if (holdDown()) return moveList.dWeak(this);
			else if (holdForward()) return moveList.sWeak(this);
			else return moveList.nWeak(this);
		}
		else {
			if (holdUp()) return moveList.uAir(this);
			else if (holdDown()) return moveList.dAir(this);
			else if (holdForward()) return moveList.fAir(this);
			else if (holdBack()) return moveList.bAir(this);
			else return moveList.nAir(this);
		}
	}

	private Move selectSpecialMove(){
		if (holdUp()) return moveList.uSpecial(this);
		else if (holdDown()) return moveList.dSpecial(this);
		else if (holdForward()) return moveList.sSpecial(this);
		else if (holdBack()) {
			flip();
			return moveList.sSpecial(this);
		}
		else return moveList.nSpecial(this);
	}

	private Move selectThrow(){
		if (isGrounded()){
			if (holdUp()) return moveList.uThrow(this);
			else if (holdDown()) return moveList.dThrow(this);
			else if (holdForward()) return moveList.fThrow(this);
			else if (holdBack()) return moveList.bThrow(this);
		}
		else{
			if (holdUp()) return moveList.uAirThrow(this);
			else if (holdDown()) return moveList.dAirThrow(this);
			else if (holdForward()) return moveList.fAirThrow(this);
			else if (holdBack()) return moveList.bAirThrow(this);
		}
		return null;
	}

	private Move selectGrab(){
		if (!isGrounded()) return moveList.airGrab(this);
		else if (isDashing()) return moveList.dashGrab(this);
		else return moveList.grab(this);
	}

	private Move selectC(Move a, Move b){
		if (isGrounded()) return a;
		else return b;
	}

	public void takeKnockback(Vector2 knockback, float DAM, int hitstun) {
		knockback.setAngle(directionalInfluenceAngle(knockback));
		velocity.set(knockback);
		percentage += DAM;
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.restart();
	}

	private final int influenceDiluter = 10;
	private float directionalInfluenceAngle(Vector2 knockback){
		Vector2 di = new Vector2(inputHandler.getXInput(), inputHandler.getYInput());
		float diAngle = di.angle();
		System.out.println(diAngle + " " + knockback.angle() + " " + knockback.angle(di) + " " + di.angle(knockback));
		knockback.setAngle((((influenceDiluter - 1) * knockback.angle()) + diAngle) / influenceDiluter);
		return knockback.angle();
	}

	private final float heldDistance = 18;
	public void takeGrab(Fighter user, Fighter target, int caughtTime) {
		user.isNowGrabbing(target, caughtTime);
		float newPosX = user.position.x + (heldDistance * user.direct());
		float newPosY = user.position.y + image.getHeight()/2;
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

	@Override
	public void ground(){
		super.ground();
		if (null != activeMove && !activeMove.isSpecial()) {
			activeMove = null;
			attackTimer.end();
		}
		if (hitstunTimer.getCounter() > 1) hitstunTimer.end();
		doubleJumped = false;
		boosted = false;
		fastFall = false;
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
	public InputHandler getInputHandler() { return inputHandler; }

	private boolean canAct(){  return attackTimer.timeUp() && state != State.HELPLESS && canMove(); }
	private boolean canMove(){ return caughtTimer.timeUp() && grabbingTimer.timeUp(); } 
	private boolean isDashing() { return state == State.RUN || state == State.DASH; }

	private final List<State> groundedStates = new ArrayList<State>(Arrays.asList(State.STAND, State.WALK, State.RUN, State.DASH, State.CROUCH));
	private boolean inGroundedState() { return groundedStates.contains(state);}

	public boolean isCharging() {
		if (null == inputHandler) return false;
		else return inputHandler.isCharging();
	}

}
