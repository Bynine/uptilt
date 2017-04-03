package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.InputHandler;
import moves.Move;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Entity{

	public static final int commandNone		=-1, commandAttack	= 0, commandSpecial	= 1;
	public static final int commandJumpX	= 2, commandJumpY	= 3, commandBlock	= 4;
	public static final int commandGrab		= 5;
	public static final int commandStickUp = 20, commandStickBack = 21, commandStickForward = 22, commandStickDown = 23;

	private final float unregisteredInputMax = 0.2f;
	private float wallJumpStrengthX = 7.2f;
	private float wallJumpStrengthY = 6.6f;
	private float wallSlideSpeed = -1f;
	private float dJumpStrength = 7.5f;

	boolean doubleJumped, boosted = false;
	private int queuedCommand = commandNone;
	private final Timer queueTimer = new Timer(6, false), wallJumpTimer = new Timer(8, false), attackTimer = new Timer(0, false);
	private final Timer caughtTimer = new Timer(0, false), grabbingTimer = new Timer(0, false);
	float prevStickX = 0, stickX = 0, prevStickY = 0, stickY = 0;

	private TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.PNG")));
	private Fighter caughtFighter = null;
	Move activeMove = null;
	float percentage = 0;
	float weight = 100;
	final InputHandler inputHandler;
	final Vector2 spawnPoint;

	public Fighter(float posX, float posY, InputHandler inputHandler) {
		super(posX, posY);
		spawnPoint = new Vector2(posX, posY);
		this.inputHandler = inputHandler;
		timerList.addAll(Arrays.asList(queueTimer, wallJumpTimer, attackTimer, caughtTimer, grabbingTimer));
		image = new Sprite(defaultTexture);
		state = State.STAND;
	}

	@Override
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		if (null != inputHandler){
			stickX = inputHandler.getXInput();
			stickY = inputHandler.getYInput();
		}
		super.update(rectangleList, entityList, deltaTime);
		updateImage();

		if (queueTimer.timeUp()) queuedCommand = commandNone;
		if (!queueTimer.timeUp()) handleCommand(queuedCommand);
		if (!grabbingTimer.timeUp()) handleThrow();
		if (null != activeMove) handleMove();

		prevStickX = stickX;
		prevStickY = stickY;
	}

	private void handleThrow(){
		activeMove = selectThrow();
		if (null != activeMove) {
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

	private final float minRunFlick = 0.65f;
	@Override
	void updateState(){
		if (isGrounded()){
			if (!inGroundedState()) ground();
			if (stickY > 0.5f) state = State.CROUCH;
			else if (Math.abs(stickX) > 0.5f){
				if (state == State.RUN || Math.abs(stickX - prevStickX) > minRunFlick) state = State.RUN;
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
		if (groundedAttacking() || !canMove()) return;
		switch (state){
		case WALK: addSpeed(walkSpeed, walkAcc); break;
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

	private void addSpeed(float speed, float acc){ velocity.x += Math.signum(stickX) * acc; }
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

	private final int wjDistance = 1;
	private boolean isWallSliding() {
		if (isGrounded() || velocity.y > 0) return false;
		boolean canWJ = false;
		if (direction == Direction.LEFT && prevStickX < -unregisteredInputMax) canWJ = checkCollision(position.x - wjDistance, position.y);
		if (direction == Direction.RIGHT && prevStickX > unregisteredInputMax) canWJ = checkCollision(position.x + wjDistance, position.y);
		return canWJ;
	}

	private void wallJump(){
		wallJumpTimer.restart();
		flip();
		velocity.x = wallJumpStrengthX * direct();
		velocity.y = wallJumpStrengthY;
	}

	private void doubleJump(){
		getVelocity().y = dJumpStrength;
		if (prevStickX < -unregisteredInputMax && velocity.x > 0) velocity.x = 0; 
		if (prevStickX >  unregisteredInputMax && velocity.x < 0) velocity.x = 0; 
		doubleJumped = true;
	}

	private boolean tryNormal(){
		startAttack(selectNormalMove());
		return true;
	}

	private boolean trySpecial(){
		startAttack(selectSpecialMove());
		return true;
	}

	private boolean tryGrab(){
		if (!isGrounded()) return false;
		startAttack(new Move.Grab(this));
		return true;
	}

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
			if (state == State.RUN) return new Move.DashAttack(this);
			else if (holdUp()) return new Move.UpTilt(this);
			else if (holdDown()) return new Move.DownTilt(this);
			else if (holdForward()) return new Move.ForwardTilt(this);
			else return new Move.Jab(this);
		}
		else {
			if (holdUp()) return new Move.Uair(this);
			else if (holdDown()) return new Move.Dair(this);
			else if (holdForward()) return new Move.Fair(this);
			else if (holdBack()) return new Move.Bair(this);
			else return new Move.Nair(this);
		}
	}

	private Move selectSpecialMove(){
		if (holdUp()) return new Move.UpSpecial(this);
		else if (holdDown()) return new Move.DownSpecial(this);
		else if (holdForward()) return new Move.SideSpecial(this);
		else if (holdBack()) {
			flip();
			return new Move.SideSpecial(this);
		}
		else return new Move.NeutralSpecial(this);
	}

	private Move selectThrow(){
		if (holdUp()) return new Move.UThrow(this);
		else if (holdDown()) return new Move.DThrow(this);
		else if (holdForward()) return new Move.FThrow(this);
		else if (holdBack()) return new Move.BThrow(this);
		else return null;
	}

	public void takeKnockback(Vector2 knockback, float DAM, int hitstun) {
		velocity.set(knockback);
		percentage += DAM;
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.restart();
	}

	public float getPercentage() { return percentage; }
	public float getWeight() { return weight; }

	private boolean canAct(){  return attackTimer.timeUp() && state != State.HELPLESS && canMove(); }
	private boolean canMove(){ return caughtTimer.timeUp() && grabbingTimer.timeUp(); } 

	private final List<State> groundedStates = new ArrayList<State>(Arrays.asList(State.STAND, State.WALK, State.RUN, State.CROUCH));
	private boolean inGroundedState() {
		return groundedStates.contains(state);
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
	}

	public void handleJumpCommand(boolean button) {
		if (state == State.JUMP && !button) jumpTimer.end();
	}

	public InputHandler getInputHandler() { return inputHandler; }

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

	private final float caughtDistance = 18;
	public void setCaught(Fighter user, Fighter target, int caughtTime) {
		user.setGrabbing(target, caughtTime);
		float newPosX = user.position.x + (caughtDistance * user.direct());
		float newPosY = user.position.y + caughtDistance/2;
		if (!checkCollision(newPosX, newPosY)) position.set(newPosX, newPosY);
		else position.y = newPosY; // won't set X if it puts target in a wall
		caughtTimer.setEndTime(caughtTime);
		caughtTimer.restart();
	}

	public void setGrabbing(Fighter target, int caughtTime){
		caughtFighter = target;
		grabbingTimer.setEndTime(caughtTime);
		grabbingTimer.restart();
	}

}
