package entities;

import input.InputHandler;
import input.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import movelists.MoveList;
import movelists.M_Mook;
import moves.Hitbox;
import moves.IDMove;
import moves.Move;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Entity{

	final float unregisteredInputMax = 0.2f;
	float wallJumpStrengthX = 8f;
	float wallJumpStrengthY = 7.2f;
	float wallSlideSpeed = -1f;
	float doubleJumpStrength = 8.5f;

	boolean doubleJumped, blockHeld, tumbling = false;
	public int queuedCommand = InputHandler.commandNone;
	public final Timer inputQueueTimer = new Timer(8), wallJumpTimer = new Timer(10), attackTimer = new Timer(0);
	final Timer caughtTimer = new Timer(0), grabbingTimer = new Timer(0), dashTimer = new Timer(20);
	private final Timer knockIntoTimer = new Timer(20), invincibleTimer = new Timer(0), dodgeTimer = new Timer(1);
	float prevStickX = 0, stickX = 0, prevStickY = 0, stickY = 0;

	float fallSpeed = -7f, walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	float jumpStrength = 5f, dashStrength = 5f;
	float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;

	private TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));
	private Fighter caughtFighter = null;
	private IDMove activeMove = null; 
	float percentage = 0, armor = 0, weight = 100;
	private InputHandler inputHandler = new InputHandlerKeyboard(this);
	private float initialHitAngle = 0;
	private final int randomAnimationDisplacement;
	final Vector2 spawnPoint;
	private int team = 0, stocks = 1;
	MoveList moveList = new M_Mook(this);
	private final List<IDMove> staleMoveQueue = new ArrayList<IDMove>();
	private HitstunType hitstunType = HitstunType.NORMAL;

	public Fighter(float posX, float posY, int team) {
		super(posX, posY);
		this.team = team;
		spawnPoint = new Vector2(posX, posY);
		this.setInputHandler(inputHandler);
		timerList.addAll(Arrays.asList(inputQueueTimer, wallJumpTimer, attackTimer, caughtTimer,
				grabbingTimer, dashTimer, knockIntoTimer, invincibleTimer, dodgeTimer));
		image = new Sprite(defaultTexture);
		state = State.STAND;
		randomAnimationDisplacement = (int) (8 * Math.random());
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		stickX = getInputHandler().getXInput();
		stickY = getInputHandler().getYInput();

		if (!grabbingTimer.timeUp()) handleThrow();
		if (null != getActiveMove()) {
			if (getActiveMove().id != MoveList.noStaleMove && getActiveMove().move.connected() && !staleMoveQueue.contains(getActiveMove())){
				staleMoveQueue.add(getActiveMove());
				if (staleMoveQueue.size() > staleMoveQueueSize) staleMoveQueue.remove(0);
			}
			handleMove();
		}

		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);

		prevStickX = stickX;
		prevStickY = stickY;
	}

	private void handleThrow(){
		setActiveMove(moveList.selectThrow());
		if (null != getActiveMove()) {
			startAttack(getActiveMove());
			grabbingTimer.end();
			caughtFighter.caughtTimer.end();
			caughtFighter.hitstunTimer.setEndTime(5);
			caughtFighter.hitstunTimer.restart();
			caughtFighter = null;
		}
	}

	private void handleMove(){
		getActiveMove().move.update();
		if (getActiveMove().move.stopsInAir() && !isGrounded()) getActiveMove().move.setDone();
		if (getActiveMove().move.done()) {
			if (getActiveMove().move.causesHelpless()) state = State.HELPLESS;
			setActiveMove(null);
		}
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			int pushDistance = 16 + 2 * ((int) image.getWidth() - defaultTexture.getRegionWidth());
			boolean toPush = isTouching(fi, pushDistance) && Math.abs(fi.velocity.x) < 1 && Math.abs(this.velocity.x) < 1 && e.isGrounded() && isGrounded();
			if (team == fi.team) toPush = isTouching(fi, 0);
			if (toPush){
				pushAway(fi);
				((Fighter) fi).pushAway(this);
			}
			boolean fighterGoingFastEnough = knockbackIntensity(fi.velocity) > tumbleBK;
			if (fi.hitstunType == HitstunType.SUPER) fighterGoingFastEnough = true;
			boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && getTeam() == fi.getTeam() && !fi.hitstunTimer.timeUp();
			if (knockInto && isTouching(fi, 8) && knockbackIntensity(fi.velocity) > knockbackIntensity(velocity)) knockBack(fi);
		}

	}

	private final float pushForce = 0.04f;
	private void pushAway(Entity e){
		float dirPush = Math.signum(e.position.x - this.position.x);
		velocity.x -= dirPush * pushForce;
	}

	public void knockBack(Fighter fi){
		Vector2 knockIntoVector = new Vector2(fi.velocity.x, fi.velocity.y);
		Hitbox h;
		if (fi.hitstunType != HitstunType.NORMAL) {
			if (fi.hitstunType == HitstunType.ULTRA){ // ultra hitstun
				float bkb = ((fi.weight/100.0f) * 4) + knockbackIntensity(knockIntoVector)/5;
				h = new Hitbox(fi, bkb, 2f, knockbackIntensity(knockIntoVector) * 2, knockIntoVector.angle(), 0, 0, 0, null);
				new SFX.MeatyHit().play();
			}
			else{ // super hitstun
				float bkb = ((fi.weight/100.0f) * 6) + knockbackIntensity(knockIntoVector)/5;
				h = new Hitbox(fi, bkb, 2f, knockbackIntensity(knockIntoVector) * 3, knockIntoVector.angle(), 0, 0, 0, null);
				new SFX.MidHit().play();
			}
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			float newAngle = h.getAngle();
			knockIntoVector.setAngle(newAngle);
		}
		else { // ultra hitstun
			h = new Hitbox(fi, (fi.weight/100.0f) * 3, 1.5f, knockbackIntensity(knockIntoVector), knockIntoVector.angle(), 0, 0, 0, null);
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
			new SFX.LightHit().play();
		}
		takeKnockIntoKnockback(knockIntoVector, h.getDamage() / 2, (int) h.getDamage() );
		fi.knockIntoTimer.restart();
		knockIntoTimer.restart();
	}

	void updateState(){
		if (isGrounded()) updateGroundedState();
		else updateAerialState();
		prevState = state;
	}

	private float minCrouchHold = 0.9f;
	private float minRunHold = 0.5f;
	private float minRunFromAirHold = 0.9f;
	private void updateGroundedState(){
		if (jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) {
			state = State.JUMP;
			jump();
		}
		else if (!inGroundedState()) ground();

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
		if (canAttack() && prevState == State.RUN && state != State.RUN) startAttack(new IDMove(moveList.skid(), MoveList.noStaleMove));
	}

	private boolean isRun(){
		boolean turningAround = (Math.signum(velocity.x) != Math.signum(stickX));
		boolean enterRun = (state == State.DASH && dashTimer.timeUp());
		return (state == State.RUN || enterRun) && !turningAround;
	}

	protected void updateAerialState(){
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
			dodgeTimer.restart();
		}
	}

	public Rectangle getHurtBox(){
		if (null == activeMove || activeMove.move.getHurtBox() == null) return getNormalHurtBox();
		else return activeMove.move.getHurtBox();
	}

	public Rectangle getNormalHurtBox(){
		return image.getBoundingRectangle();
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

	void updateImage(float deltaTime){
		TextureRegion prevImage = image;
		selectImage(deltaTime);
		if (!jumpSquatTimer.timeUp()) setImage(getJumpSquatFrame(deltaTime));
		if (!attackTimer.timeUp() && null != getActiveMove() && null != getActiveMove().move.getAnimation()){
			setImage(getActiveMove().move.getAnimation().getKeyFrame(getActiveMove().move.getFrame()));
		}
		if (!caughtTimer.timeUp()) setImage(getHelplessFrame(deltaTime));
		if (!hitstunTimer.timeUp()) {
			if (hitstunType == HitstunType.SUPER) setImage(getTumbleFrame(deltaTime));
			else setImage(getHitstunFrame(deltaTime));
		}
		if (!grabbingTimer.timeUp()) setImage(getGrabFrame(deltaTime));
		adjustImage(deltaTime, prevImage);
	}

	private void adjustImage(float deltaTime, TextureRegion prevImage){
		if (prevImage != getWallSlideFrame(deltaTime) && state == State.WALLSLIDE && direction == Direction.RIGHT) wallCling();
		float adjustedPosX = (image.getWidth() - prevImage.getRegionWidth())/2;
		if (!doesCollide(position.x - adjustedPosX, position.y) && state != State.WALLSLIDE) position.x -= adjustedPosX;
		if (doesCollide(position.x, position.y)) resetStance();

		if (doesCollide(position.x, position.y)) {
			setImage(prevImage);
		}
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

	void bounceOffWall(){
		if (inputHandler.isTeching()) setInvincible(20);
		else super.bounceOffWall();
	}

	void handleGravity(){
		if (state == State.WALLSLIDE) velocity.y = wallSlideSpeed;
		else velocity.y += gravity;
	}

	private final float minTurn = 0.2f;
	private final int minFrameTurn = 3;
	void handleDirection(){
		if (Math.abs(stickX) < unregisteredInputMax || !canMove() || !isGrounded() || cantTurnStates.contains(state)) return;
		if (null != getActiveMove()){
			boolean startAttackTurn = !attackTimer.timeUp() && attackTimer.getCounter() < minFrameTurn && !getActiveMove().move.isNoTurn();
			if (!canAct() && !startAttackTurn) return;
		}
		boolean turnLeft = stickX < -minTurn && (prevStickX > -minTurn) && getDirection() == Direction.RIGHT;
		boolean turnRight = stickX > minTurn && (prevStickX < minTurn)  && getDirection() == Direction.LEFT;
		if (turnLeft || turnRight) flip();
	}
	private final List<State> cantTurnStates = new ArrayList<State>(Arrays.asList(State.CROUCH, State.DODGE, State.JUMPSQUAT, State.FALLEN));

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
		else if (isAboveEnemy() != null){
			Fighter f = isAboveEnemy();
			footStool(f); return true;
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
		velocity.x = wallJumpStrengthX * direct();
		velocity.y = wallJumpStrengthY;
		tumbling = false;
	}

	private void doubleJump(){
		getVelocity().y = doubleJumpStrength;
		if (prevStickX < -unregisteredInputMax && velocity.x > 0) velocity.x = 0; 
		if (prevStickX >  unregisteredInputMax && velocity.x < 0) velocity.x = 0; 
		velocity.x += 1 * stickX;
		doubleJumped = true;
		tumbling = false;
	}

	protected Vector2 footStoolKB = new Vector2(0, 0);
	protected int footStoolDuration = 30;
	protected int footStoolDamage = 0;
	private void footStool(Fighter footStoolee){
		boolean prevDJ = doubleJumped;
		doubleJump();
		doubleJumped = prevDJ;
		new SFX.FootStool().play();
		footStoolee.velocity.set(footStoolKB);
		footStoolee.tumbling = true;
		footStoolee.percentage += footStoolDamage;
		if (footStoolee.hitstunTimer.timeUp()){
			footStoolee.hitstunTimer.setEndTime(footStoolDuration);
			footStoolee.hitstunTimer.restart();
		}
	}

	private Fighter isAboveEnemy(){
		if (team != GlobalRepo.GOODTEAM) return null;
		for (Entity en: MapHandler.getEntities()) {
			if (en instanceof Fighter){
				Fighter fi = (Fighter) en;
				if (fi != this && isAbove(fi)) return fi;
			}
		}
		return null;
	}

	private boolean isAbove(Fighter en){
		boolean xCorrect = Math.abs(en.getCenter().x - getCenter().x) < en.getImage().getWidth()/1.5f;
		boolean yCorrect = Math.abs(en.getCenter().y - position.y) < en.getImage().getHeight()/1.5f;
		return xCorrect && yCorrect;
	}

	public boolean tryNormal(){
		if (state == State.FALLEN){
			state = State.STAND;
			startAttack(new IDMove(moveList.getUpAttack(), -1));
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
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList.noStaleMove));
		}
		if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList.noStaleMove));
		}
		else if (canAttack()) tryDash();
		return true;
	}

	public boolean tryStickBack(){ 
		if (state == State.DODGE) {
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList.noStaleMove));
		}
		if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList.noStaleMove));
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
		if (isGrounded()){
			for (Rectangle r: tempRectangleList){
				if (Math.abs(position.y - r.y) < 2 && r.getHeight() <= 1) position.y -= 2;
			}
		}
		return true; 
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
		startAttack(new IDMove(moveList.dodge(), MoveList.noStaleMove));
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

	private final int staleMoveQueueSize = 5;
	private void startAttack(IDMove im){
		if (im.id == MoveList.noMove) return;
		Move m = im.move;
		setActiveMove(im);
		attackTimer.setEndTime(m.getDuration() + 1);
		attackTimer.restart();
		tumbling = false;
		if (isGrounded()) state = State.WALK;
	}

	private void takeKnockIntoKnockback(Vector2 knockback, float DAM, int hitstun){
		knockbackHelper(knockback, DAM, hitstun, knockbackIntensity(velocity) < knockbackIntensity(knockback), HitstunType.NORMAL);
	}

	public void takeDamagingKnockback(Vector2 knockback, float DAM, int hitstun, HitstunType hitboxhitstunType) {
		knockbackHelper(knockback, DAM, hitstun, true, hitboxhitstunType);
	}

	public void takeRecoil(Vector2 knockback){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		velocity.add(knockback);
	}

	private void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean tryy, HitstunType ht){
		percentage += DAM;
		if (knockbackIntensity(knockback) > 0){
			knockback.setAngle(directionalInfluenceAngle(knockback));
			if (tryy) velocity.set(knockback);
			initialHitAngle = knockback.angle();
			if (state == State.HELPLESS) state = State.FALL;
			hitstunTimer.setEndTime(hitstun);
			hitstunTimer.restart();
			setActiveMove(null);
			attackTimer.end();
			hitstunType = ht;
		}
		if (knockbackIntensity(knockback) > tumbleBK) tumbling = true;
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
	public void getGrabbed(Fighter user, Fighter target, int caughtTime) {
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
		if (hitstunTimer.getCounter() > 1 && velocity.y < 0){	
			if (tumbling) {
				if (inputHandler.isTeching()) setInvincible(20);
				else state = State.FALLEN;
			}
			hitstunTimer.end();
		}
		tumbling = false;
		super.ground();
		if (null != getActiveMove() && !getActiveMove().move.continuesOnLanding()) endAttack();
		if (velocity.y < 0 && getActiveMove() == null && state != State.FALLEN) startAttack(new IDMove(moveList.land(), MoveList.noStaleMove));
		doubleJumped = false;
	}

	private void endAttack(){
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

	public void respawn() {
		stocks -= 1;
		position.set(spawnPoint);
		percentage = 0;
		velocity.x = 0;
		velocity.y = 0;
		state = State.FALL;
		doubleJumped = false;
		tumbling = false;
		for (Timer t: timerList) t.end();
		setInvincible(120);
		staleMoveQueue.clear();
	}

	public void setRespawnPoint(Vector2 startPosition) {
		spawnPoint.set(startPosition);
	}

	public static float knockbackIntensity(Vector2 knockback) { 
		float intensity = (float) Math.sqrt(Math.pow(Math.abs(knockback.x), 2) + Math.pow(Math.abs(knockback.y), 2)); 
		return intensity; 
	}

	public float getPercentage() { return percentage; }
	public float getWeight() { return weight; }
	public float getStickX() { return stickX; }
	public float getStickY() { return stickY; }
	public InputHandler getInputHandler() { return inputHandler; }
	public boolean canAttack() { return canAttackDodge() && state != State.DODGE; }
	public boolean canAttackDodge() { return canAct() && state != State.WALLSLIDE; }
	public boolean canAct(){ 
		return hitstunTimer.timeUp() && attackTimer.timeUp() && state != State.FALLEN && state != State.HELPLESS && canMove(); 
	}
	public boolean canMove(){ return caughtTimer.timeUp() && grabbingTimer.timeUp(); } 
	public boolean isRunning() { return state == State.RUN || state == State.DASH; }
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
		if (null != getActiveMove()) addedMoveArmor = getActiveMove().move.getArmor();
		return armor + addedMoveArmor; 
	}
	public IDMove getActiveMove() { return activeMove; }
	public void setActiveMove(IDMove activeMove) { this.activeMove = activeMove; }
	public void setToFall() { state = State.FALL; }
	public void setInputHandler(InputHandler inputHandler) { this.inputHandler = inputHandler; }
	public int getTeam() { return team; }
	public State getState() { return state; } 
	protected TextureRegion getFrame(Animation anim, float deltaTime) { return anim.getKeyFrame(deltaTime + randomAnimationDisplacement); }
	public int getStocks() { return stocks; }
	public void setStocks(int i) { stocks = i; }
	public Color getColor() { return new Color(1, 1, 1, 1); }
	public List<IDMove> getMoveQueue() { return staleMoveQueue; }
	public void setInvincible(int i) { 
		invincibleTimer.restart();
		invincibleTimer.setEndTime(i);
	}

	public enum HitstunType{
		NORMAL, SUPER, ULTRA
	}

	abstract TextureRegion getStandFrame(float deltaTime);
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
	abstract TextureRegion getTumbleFrame(float deltaTime);
	abstract TextureRegion getHitstunFrame(float deltaTime);
	abstract TextureRegion getFallenFrame(float deltaTime);

}
