package entities;

import inputs.InputHandler;
import inputs.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import movelists.MoveList_Advanced;
import movelists.M_Mook;
import movelists.MoveList;
import moves.IDMove;
import moves.Move;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Hittable{

	protected final float unregisteredInputMax = 0.2f;
	protected boolean doubleJumped, blockHeld;
	public int queuedCommand = InputHandler.commandNone;
	protected final Timer inputQueueTimer = new Timer(8), wallJumpTimer = new Timer(10), attackTimer = new Timer(0), grabbingTimer = new Timer(0), 
			dashTimer = new Timer(20), invincibleTimer = new Timer(0), dodgeTimer = new Timer(1), footStoolTimer = new Timer(20), slowedTimer = new Timer(0),
			doubleJumpTimer = new Timer (12);
	protected float prevStickX = 0, stickX = 0, stickY = 0;

	private ShaderProgram palette = null;
	private final Vector2 spawnPoint;
	private int team = 0, lives = 1;

	public static final int SPECIALMETERMAX = 16;
	private float specialMeter = SPECIALMETERMAX;

	protected Vector2 footStoolKB = new Vector2(0, 0);
	protected int footStoolDuration = 30;
	protected int footStoolDamage = 0;

	protected TextureRegion defaultIcon = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconalien.png")));

	private Hittable caughtFighter = null;
	private IDMove activeMove = null; 

	private InputHandler inputHandler = new InputHandlerKeyboard(this);
	private final int randomAnimationDisplacement;
	protected MoveList moveList = new M_Mook(this);
	
	private final List<IDMove> staleMoveQueue = new ArrayList<IDMove>();
	public static final int staleMoveQueueSize = 5;

	public Fighter(float posX, float posY, int team) {
		super(posX, posY);
		this.team = team;
		spawnPoint = new Vector2(posX, posY);
		this.setInputHandler(inputHandler);
		timerList.addAll(Arrays.asList(inputQueueTimer, wallJumpTimer, attackTimer, grabbingTimer, dashTimer, invincibleTimer,
				dodgeTimer, footStoolTimer, slowedTimer, doubleJumpTimer));
		state = State.STAND;
		randomAnimationDisplacement = (int) (8 * Math.random());
		baseHurtleBK = 8;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		int slowMod = 4;
		if (!slowedTimer.timeUp() && deltaTime % slowMod != 0) return;
		stickX = getInputHandler().getXInput();
		stickY = getInputHandler().getYInput();

		if (!grabbingTimer.timeUp()) handleThrow();
		if (null != getActiveMove()) {
			if (getActiveMove().id != MoveList_Advanced.noStaleMove && getActiveMove().move.connected() && !staleMoveQueue.contains(getActiveMove())){
				staleMoveQueue.add(getActiveMove());
				if (staleMoveQueue.size() > staleMoveQueueSize) staleMoveQueue.remove(0);
			}
			handleMove();
		}

		super.update(rectangleList, entityList, deltaTime);
		prevStickX = stickX;
	}

	private void handleThrow(){
		setActiveMove(moveList.selectThrow());
		if (null != getActiveMove()) {
			startAttack(getActiveMove());
			dropTarget();
		}
	}
	
	private void dropTarget(){
		grabbingTimer.end();
		caughtFighter.caughtTimer.end();
		caughtFighter.hitstunTimer.setEndTime(5);
		caughtFighter.hitstunTimer.restart();
		caughtFighter = null;
	}

	private void handleMove(){
		getActiveMove().move.update();
		if (null == getActiveMove()) return;
		if (getActiveMove().move.stopsInAir() && !isGrounded()) getActiveMove().move.setDone();
		if (getActiveMove().move.done()) {
			if (getActiveMove().move.causesHelpless()) state = State.HELPLESS;
			setActiveMove(null);
			attackTimer.end();
		}
	}

	void updateState(){
		if (canMove() && jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) {
			state = State.JUMP;
			jump();
		}
		if (isGrounded()) updateGroundedState();
		else updateAerialState();
		prevState = state;
	}

	private void updateGroundedState(){
		float minCrouchHold = 0.9f;
		float minRunHold = 0.5f;
		float minRunFromAirHold = 0.9f;
		if (!(jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) && !inGroundedState()) ground();

		if (state == State.FALLEN) state = State.FALLEN;
		else if (!jumpSquatTimer.timeUp()) {
			if (state != State.JUMPSQUAT) preJumpSquatState = state;
			state = State.JUMPSQUAT;
		}
		else if ((state == State.DODGE && !dodgeTimer.timeUp() || blockHeld) && attackTimer.timeUp()) state = State.DODGE;
		else if (Math.abs(stickX) < minRunHold && stickY > minCrouchHold) {
			if (blockHeld) activateBlock();
			else state = State.CROUCH;
		}
		else if (Math.abs(stickX) > minRunHold){
			boolean fallToDash = !inGroundedState(prevState) && Math.abs(stickX) > minRunFromAirHold;
			if (fallToDash) state = State.DASH;
			else if (isRun()) state = State.RUN;
			else if (state == State.DASH && !dashTimer.timeUp()) state = State.DASH;
			else state = State.WALK;
			boolean noWalls = (!doesCollide(position.x - 2, position.y) && direction == Direction.LEFT)
					|| (!doesCollide(position.x + 2, position.y) && direction == Direction.RIGHT);
			if (canAct() && noWalls && Math.signum(velocity.x) != direct()) flip();
		}
		else if (blockHeld) activateBlock();
		else state = State.STAND;
		if (canAttack() && prevState == State.RUN && state != State.RUN) startAttack(new IDMove(moveList.skid(), MoveList_Advanced.noStaleMove));
	}

	protected void updateAerialState(){
		if (state == State.HELPLESS) return;
		if (isWallSliding()) state = State.WALLSLIDE;
		else if (!jumpTimer.timeUp()) state = State.JUMP;
		else state = State.FALL;
	}

	private boolean isRun(){
		boolean turningAround = (Math.signum(velocity.x) != Math.signum(stickX));
		boolean enterRun = (state == State.DASH && dashTimer.timeUp());
		return (state == State.RUN || enterRun) && !turningAround;
	}

	private void activateBlock(){
		if (state != State.DODGE) {
			state = State.DODGE;
			dodgeTimer.restart();
		}
	}

	private boolean tryDash(){
		if (!isGrounded() || !inGroundedState() || isRun()) return false;
		dashTimer.restart();
		velocity.x = Math.signum(stickX) * getDashStrength();
		state = State.DASH;
		MapHandler.addEntity(new Graphic.DustCloud(this, position.x, position.y));
		return true;
	}

	public boolean tryJump(){
		if (isGrounded()) {
			return true;
		}
		else if (isWallSliding()) {
			wallJump(); return true;
		}
		else if (isAboveEnemy() != null && footStoolTimer.timeUp()){
			Fighter f = isAboveEnemy();
			footStool(f); return true;
		}
		else if (!doubleJumped && state != State.JUMP){
			doubleJump(); return true;
		}
		return false;
	}

	public boolean tryNormal(){
		if (state == State.FALLEN){
			state = State.STAND;
			startAttack(new IDMove(moveList.getUpAttack(), MoveList.noStaleMove));
		}
		else if (canAttack()) {
			startAttack(moveList.selectNormalMove());
			return true;
		}
		return false;
	}

	public boolean trySpecial(){
		startAttack(moveList.selectSpecialMove());
		return true;
	}

	public boolean tryDodge(){
		Move block = moveList.selectBlock().move;
		if (null != block && canAttackDodge()){
			startAttack(moveList.selectBlock());
			return true;
		}
		else return false;
	}

	public boolean tryStickForward(){
		if (state == State.DODGE) {
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
		}
		if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
		}
		else if (canAttack()) tryDash();
		return true;
	}

	public boolean tryStickBack(){ 
		if (state == State.DODGE) {
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
		}
		if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
		}
		else if (canAttack()) tryDash();
		return true;
	}

	public boolean tryStickUp(){
		upDownStick();
		return true; 
	}

	public boolean tryStickDown(){
		upDownStick();
		if (isGrounded() && canAct()) fallThroughThinFloor();
		return true; 
	}
	
	private void fallThroughThinFloor(){
		for (Rectangle r: tempRectangleList){
			Rectangle checker = new Rectangle(getCenter().x, position.y - 1, 1, 1);
			if (checker.overlaps(r) && r.getHeight() <= 1) {
				endAttack();
				position.y -= 2;
			}
		}
	}

	private void upDownStick(){
		if (state == State.DODGE) spotDodge();
		else if (state == State.FALLEN){
			state = State.STAND;
			spotDodge();
		}
	}

	private void spotDodge(){
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		startAttack(new IDMove(moveList.dodge(), MoveList_Advanced.noStaleMove));
	}

	public boolean tryCStickUp(){
		startAttack(moveList.selectCStickUp()); 
		return true; 
	}

	public boolean tryCStickDown(){
		startAttack(moveList.selectCStickDown()); 
		return true; 
	}

	public boolean tryCStickForward(){
		startAttack(moveList.selectCStickForward()); 
		return true; 
	}

	public boolean tryCStickBack(){
		startAttack(moveList.selectCStickBack()); 
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

	private void startAttack(IDMove im){
		if (im.id == MoveList_Advanced.noMove) return;
		Move m = im.move;
		setActiveMove(im);
		attackTimer.setEndTime(m.getDuration() + 1);
		attackTimer.restart();
		tumbling = false;
		if (isGrounded()) state = State.WALK;
	}

	public void endAttack(){
		setActiveMove(null);
		attackTimer.end();
	}

	public void handleJumpHeld(boolean button) {
		if (isGrounded() && button && state != State.JUMPSQUAT && jumpSquatTimer.timeUp() && canAct()) jumpSquatTimer.restart();
		if (state == State.JUMP && !button) jumpTimer.end();
	}

	public void handleBlockHeld(boolean block) {
		blockHeld = block;
	}

	private void jump(){
		jumpTimer.restart();
		endAttack();
		if (stickX < 0) MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		else MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		if (velocity.y < 0) velocity.y = 0;
		velocity.x += 2 * stickX;
		getVelocity().y += getJumpStrength();
	}

	protected final int wallSlideDistance = 1;
	protected boolean isWallSliding() {
		if (isGrounded() || velocity.y > 4 || !canAct()) return false;
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
		velocity.x = getWallJumpStrengthX() * direct();
		velocity.y = getWallJumpStrengthY();
		tumbling = false;
	}

	private void doubleJump(){
		getVelocity().y = getDoubleJumpStrength();
		MapHandler.addEntity(new Graphic.DoubleJumpRing(position.x, getCenter().y));
		if (prevStickX < -unregisteredInputMax && velocity.x > 0) velocity.x = 0; 
		if (prevStickX >  unregisteredInputMax && velocity.x < 0) velocity.x = 0; 
		velocity.x += 1 * stickX;
		doubleJumped = true;
		doubleJumpTimer.restart();
		tumbling = false;
	}

	private void footStool(Fighter footStoolee){
		boolean prevDJ = doubleJumped;
		doubleJump();
		doubleJumpTimer.end();
		doubleJumped = prevDJ;
		new SFX.FootStool().play();
		footStoolee.velocity.set(footStoolKB);
		footStoolee.tumbling = true;
		footStoolee.percentage += footStoolDamage;
		if (footStoolee.hitstunTimer.timeUp()){
			footStoolee.hitstunTimer.setEndTime(footStoolDuration);
			footStoolee.hitstunTimer.restart();
		}
		footStoolTimer.restart();
	}

	private Fighter isAboveEnemy(){
		if (team != GlobalRepo.GOODTEAM) return null;
		for (Entity en: MapHandler.getEntities()) {
			if (en instanceof Fighter){
				Fighter fi = (Fighter) en;
				if (fi != this && isAbove(fi) && fi.getTeam() != GlobalRepo.GOODTEAM) return fi;
			}
		}
		return null;
	}

	private boolean isAbove(Fighter en){
		boolean xCorrect = Math.abs(en.getCenter().x - getCenter().x) < en.getImage().getWidth()/1.5f;
		boolean yCorrect = Math.abs(en.getCenter().y - position.y) < en.getImage().getHeight()/1.5f;
		return xCorrect && yCorrect;
	}

	public Rectangle getHurtBox(){
		if (null == activeMove || activeMove.move.getHurtBox() == null) return getNormalHurtBox();
		else return activeMove.move.getHurtBox();
	}

	public Rectangle getNormalHurtBox(){
		return super.getHurtBox();
	}

	private void selectImage(float deltaTime){
		switch(state){
		case STAND: setImage(getStandFrame(deltaTime)); break;
		case WALK: setImage(getWalkFrame(deltaTime)); break;
		case RUN: setImage(getRunFrame(deltaTime)); break;
		case JUMP: setImage(getJumpFrame(deltaTime)); break;
		case FALL: {
			if (tumbling) setImage(getTumbleFrame(deltaTime));
			else if (velocity.y > 1) setImage(getJumpFrame(deltaTime));
			else if (velocity.y > 0) setImage(getAscendFrame(deltaTime));
			else setImage(getFallFrame(deltaTime)); 
			break;
		}
		case WALLSLIDE: setImage(getWallSlideFrame(deltaTime)); break;
		case CROUCH: setImage(getCrouchFrame(deltaTime)); break;
		case HELPLESS: setImage(getHelplessFrame(deltaTime)); break;
		case DASH: setImage(getDashFrame(deltaTime)); break;
		case DODGE: setImage(getDodgeFrame(deltaTime)); break;
		case JUMPSQUAT: setImage(getJumpSquatFrame(deltaTime)); break;
		case FALLEN: setImage(getFallenFrame(deltaTime)); break;
		default: break;
		}
	}

	private float prevX = 0, currX = 0, prevY = 0, currY = 0;
	void updateImage(float deltaTime){
		prevX = image.getWidth();
		prevY = image.getHeight();
		TextureRegion prevImage = image;
		selectImage(deltaTime);
		if (!jumpSquatTimer.timeUp()) setImage(getJumpSquatFrame(deltaTime));
		if (!attackTimer.timeUp() && null != getActiveMove() && null != getActiveMove().move.getAnimation()){
			setImage(getActiveMove().move.getAnimation().getKeyFrame(getActiveMove().move.getFrame()));
		}
		if (!caughtTimer.timeUp() || !stunTimer.timeUp()) setImage(getHitstunFrame(0));
		if (!hitstunTimer.timeUp()) {
			if (hitstunType == HitstunType.SUPER || hitstunType == HitstunType.ULTRA) setImage(getTumbleFrame(deltaTime));
			else setImage(getHitstunFrame(deltaTime));
		}
		if (!grabbingTimer.timeUp()) setImage(getGrabFrame(deltaTime));
		
		currX = image.getWidth();
		currY = image.getHeight();
		if (!prevImage.equals(image)) adjustImage(deltaTime, prevImage);
	}

	private void adjustImage(float deltaTime, TextureRegion prevImage){
		if (prevImage != getWallSlideFrame(deltaTime) && state == State.WALLSLIDE && direction == Direction.RIGHT) wallCling();
		float adjustedPosX = (image.getWidth() - prevImage.getRegionWidth())/2;
		if (!doesCollide(position.x - adjustedPosX, position.y) && state != State.WALLSLIDE) position.x -= adjustedPosX;
		float dispX = prevX - currX;
		float dispY = prevY - currY;
		
		if (doesCollide(position.x, position.y) && !doesCollide(position.x - dispX, position.y)) position.x -= dispX;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x + dispX, position.y)) position.x += dispX;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x, position.y - dispY)) position.y -= dispY;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x, position.y + dispY)) position.y += dispY;
		
		//if (doesCollide(position.x, position.y)) setImage(prevImage); // failsafe
	}

	private int maxAdjust = 50;
	void wallCling(){
		int adjust = 0;
		while (!doesCollide(position.x + 1, position.y)){
			position.x += 1;
			adjust++;
			if (adjust > maxAdjust) break;
		}
	}

	void resetStance(){
		int adjust = 0;
		while (doesCollide(position.x, position.y)){
			position.x -= 1;
			adjust++;
			if (adjust > maxAdjust) break;
		}
	}

	public boolean doesCollide(float x, float y){
		if (collision == Collision.GHOST) return false;
		for (Rectangle r : tempRectangleList){
			Rectangle thisR = getCollisionBox(x, y);
			boolean fallThrough = stickY > 0.95f && null == getActiveMove() && !inGroundedState() && hitstunTimer.timeUp();
			boolean upThroughThinPlatform = r.getHeight() <= 1 && (r.getY() - this.getPosition().y > 0 || fallThrough);
			if (!upThroughThinPlatform && Intersector.overlaps(thisR, r) && thisR != r) return true;
		}
		return false;
	}

	void bounceOff(){
		if (inputHandler.isTeching()) tech();
		else super.bounceOff();
	}

	private void tech(){
		setInvincible(30);
		new SFX.Tech().play();
		hitstunTimer.end();
		tumbling = false;
		velocity.x = 0;
		velocity.y = 0;
	}

	public void ground(){
		if (hitstunTimer.getCounter() > 1 && velocity.y < 0){	
			if (tumbling) {
				if (inputHandler.isTeching()) tech();
				else {
					SFX.proportionalHit(knockbackIntensity(velocity)).play();
					state = State.FALLEN;
				}
			}
			hitstunTimer.end();
		}
		tumbling = false;
		super.ground();
		boolean upThroughThinPlatform = velocity.y >= 0;
		if (null != getActiveMove() && !getActiveMove().move.continuesOnLanding() && !upThroughThinPlatform) endAttack();
		if (velocity.y < 0 && getActiveMove() == null && state != State.FALLEN){
			startAttack(new IDMove(moveList.land(), MoveList_Advanced.noStaleMove));
		}
		doubleJumped = false;
	}

	void handleGravity(){
		if (state == State.WALLSLIDE) velocity.y = getWallSlideSpeed() * MapHandler.getRoomGravity();
		else super.handleGravity();
	}

	void handleDirection(){
		float minTurn = 0.2f;
		int minFrameBReverse = 6;
		if (Math.abs(stickX) < unregisteredInputMax || !canMove() || cantTurnStates.contains(state)) return;
		if (null != getActiveMove()){
			boolean bReverse = 
					!attackTimer.timeUp() && attackTimer.getCounter() < minFrameBReverse && !getActiveMove().move.isNoTurn()
					&& getActiveMove().id >= MoveList_Advanced.specialRange[0] && getActiveMove().id <= MoveList_Advanced.specialRange[1];
					if (!bReverse) return;
		}
		else if (!isGrounded()) return;
		boolean turnLeft = stickX < -minTurn && (prevStickX > -minTurn) && getDirection() == Direction.RIGHT;
		boolean turnRight = stickX > minTurn && (prevStickX < minTurn)  && getDirection() == Direction.LEFT;
		if (turnLeft || turnRight) flip();
	}
	private final List<State> cantTurnStates = new ArrayList<State>(Arrays.asList(State.CROUCH, State.DODGE, State.JUMPSQUAT, State.FALLEN));

	protected boolean activeMoveIsSpecial(){
		return activeMoveIsWhatever(MoveList_Advanced.specialRange);
	}
	
	protected boolean activeMoveIsCharge(){
		return activeMoveIsWhatever(MoveList_Advanced.chargeRange);
	}
	
	private boolean activeMoveIsWhatever(int[] range){
		if (null == getActiveMove()) return false;
		else return getActiveMove().id >= range[0] && getActiveMove().id <= range[1];
	}

	void handleMovement(){
		boolean groundedAttacking = !attackTimer.timeUp() && isGrounded();
		if (groundedAttacking || !canMove() || !hitstunTimer.timeUp() || state == State.DODGE) return;
		switch (state){
		case WALK: addSpeed(getWalkSpeed(), getWalkAcc()); break;
		case DASH:
		case RUN: addSpeed(getRunSpeed(), getRunAcc()); break;
		case JUMP: velocity.y += getJumpAcc(); break;
		default: break;
		}
		if (!isGrounded() && wallJumpTimer.timeUp() && Math.abs(stickX) > unregisteredInputMax) {
			if (Math.abs(velocity.x) > getAirSpeed()) addSpeed(getRunSpeed(), getAirAcc());
			else addSpeed(getAirSpeed(), getAirAcc());
		}
	}

	private void addSpeed(float speed, float acc){ 
		if (Math.abs(velocity.x) < Math.abs(speed)) velocity.x += Math.signum(stickX) * acc; 
	}

	protected float directionalInfluenceAngle(Vector2 knockback){
		float diStrength = 8;
		if (team == GlobalRepo.GOODTEAM) diStrength = 40;
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
	
	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		super.takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		if (null != caughtFighter) dropTarget();
	}
	
	public void isNowGrabbing(Hittable target, int caughtTime){
		caughtFighter = target;
		grabbingTimer.setEndTime(caughtTime);
		grabbingTimer.restart();
	}

	public void respawn() {
		lives -= 1;
		changeSpecial(SPECIALMETERMAX);
		percentage = 0;
		re();
	}
	
	public void refresh(){
		re();
		inputHandler.refresh();
	}
	
	private void re(){
		position.set(spawnPoint);
		velocity.x = 0;
		velocity.y = 0;
		state = State.FALL;
		doubleJumped = false;
		tumbling = false;
		if (direction == Direction.LEFT) flip();
		for (Timer t: timerList) t.end();
		if (team == GlobalRepo.GOODTEAM) setInvincible(120);
		staleMoveQueue.clear();
	}

	public void setRespawnPoint(Vector2 startPosition) { spawnPoint.set(startPosition); }
	public float getStickX() { return stickX; }
	public float getStickY() { return stickY; }
	public InputHandler getInputHandler() { return inputHandler; }
	public boolean canAttack() { return canAttackDodge() && state != State.DODGE; }
	public boolean canAttackDodge() { return canAct() && state != State.WALLSLIDE; }
	public boolean canAct(){ 
		return hitstunTimer.timeUp() && attackTimer.timeUp() && state != State.FALLEN && state != State.HELPLESS && canMove(); 
	}
	public boolean canMove(){ return super.canMove() && grabbingTimer.timeUp(); } 
	public boolean isRunning() { return state == State.RUN || state == State.DASH; }
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0 || !invincibleTimer.timeUp(); }
	public boolean isInHitstun() { return !hitstunTimer.timeUp(); }
	public boolean isCharging() {
		if (null == getInputHandler()) return false;
		else return activeMoveIsCharge() && getInputHandler().isCharging();
	}
	public boolean inputQueueTimeUp(){ return inputQueueTimer.timeUp(); }
	public boolean noGroundBelow(){
		Rectangle r = new Rectangle (getCenter().x, position.y, 1, 1234);
		for (Rectangle tr: tempRectangleList) if (r.overlaps(tr)) return false;
		return true;
	}
	public void restartInputQueue() { inputQueueTimer.restart(); }
	public boolean attackTimeUp(){ return attackTimer.timeUp(); }
	public void countDownAttackTimer(){ attackTimer.countDown(); }
	public InputPackage getInputPackage() { return new InputPackage(this); }
	public void setArmor(float armor) { this.armor = armor; }
	public float getArmor() { 
		float addedMoveArmor = 0;
		if (null != getActiveMove()) addedMoveArmor = getActiveMove().move.getArmor();
		return super.getArmor() + addedMoveArmor; 
	}
	public IDMove getActiveMove() { return activeMove; }
	public void setActiveMove(IDMove activeMove) { this.activeMove = activeMove; }
	public void setToFall() { state = State.FALL; }
	public void setInputHandler(InputHandler inputHandler) { this.inputHandler = inputHandler; }
	public int getTeam() { return team; }
	public State getState() { return state; } 
	protected TextureRegion getFrame(Animation anim, float deltaTime) { return anim.getKeyFrame(deltaTime + randomAnimationDisplacement); }
	public int getLives() { return lives; }
	public void setLives(int i) { lives = i; }
	public List<IDMove> getMoveQueue() { return staleMoveQueue; }
	public float getSpecialMeter() { return specialMeter; }
	public ShaderProgram getPalette() { return palette; }
	public void setPalette(ShaderProgram pal) { palette = pal; }
	public TextureRegion getIcon(){ return defaultIcon; }
	public void changeSpecial(float change) { specialMeter = MathUtils.clamp(specialMeter + change, 0, SPECIALMETERMAX); }
	public void setInvincible(int i) { 
		invincibleTimer.restart();
		invincibleTimer.setEndTime(i);
	}

	private final float minDirect = 0.85f;
	public boolean isHoldUp() 		{ return -stickY > minDirect; }
	public boolean isHoldDown()		{ return stickY > minDirect; }
	public boolean isHoldForward() 	{ return Math.signum(stickX) == direct() && Math.abs(stickX) > minDirect; }
	public boolean isHoldBack() 	{ return Math.signum(stickX) != direct() && Math.abs(stickX) > minDirect; }
	
	abstract TextureRegion getWalkFrame(float deltaTime);
	abstract TextureRegion getRunFrame(float deltaTime);
	abstract TextureRegion getJumpFrame(float deltaTime);
	abstract TextureRegion getWallSlideFrame(float deltaTime);
	abstract TextureRegion getHelplessFrame(float deltaTime);
	abstract TextureRegion getGrabFrame(float deltaTime);
	abstract TextureRegion getFallFrame(float deltaTime);
	abstract TextureRegion getAscendFrame(float deltaTime);
	abstract TextureRegion getCrouchFrame(float deltaTime);
	abstract TextureRegion getDashFrame(float deltaTime);
	abstract TextureRegion getDodgeFrame(float deltaTime);
	abstract TextureRegion getJumpSquatFrame(float deltaTime);
	abstract TextureRegion getHitstunFrame(float deltaTime);
	abstract TextureRegion getFallenFrame(float deltaTime);

}
