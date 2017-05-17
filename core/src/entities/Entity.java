package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.SFX;
import main.UptiltEngine;
import timers.Timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	final Vector2 position = new Vector2();
	final Vector2 velocity = new Vector2();
	State state, prevState, preJumpSquatState;
	Direction direction = Direction.RIGHT;
	protected Sprite image;
	Collision collision;

	protected float gravity = -0.35f, friction = 0.85f, airFriction = 0.95f, fallSpeed = -7f;
	final int tumbleBK = 10;

	boolean toRemove = false;
	protected final List<Rectangle> tempRectangleList = new ArrayList<Rectangle>();
	public final Timer hitstunTimer = new Timer(10);
	final Timer jumpTimer = new Timer(8);
	final Timer inActionTimer = new Timer(0);
	final Timer jumpSquatTimer = new Timer(4);
	final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitstunTimer, jumpTimer, inActionTimer, jumpSquatTimer));

	public Entity(float posX, float posY){
		position.x = posX;
		position.y = posY;
		prevState = state;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		updateState();
		handleDirection();
		handleMovement();
		if (deltaTime > 1) handleTouch(entityList);
		limitingForces(rectangleList, entityList);
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);

		if (state == State.RUN && deltaTime % 8 == 0 && Math.abs(velocity.x) > 3) {
			if (direction == Direction.LEFT) MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
			else MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		}
		int knockbackPower = (int) (Math.abs(velocity.x) + Math.abs(velocity.y));
		if (!hitstunTimer.timeUp() && deltaTime % 4 == 0 && knockbackPower > tumbleBK) {
			MapHandler.addEntity(new Graphic.SmokeTrail(this, knockbackPower));
		}
	}

	void updateState() {
		/* */
	}

	void handleDirection(){
		/* */
	}

	void handleMovement(){
		/* */
	}

	void handleTouch(List<Entity> entityList){
		for (Entity e: entityList) if (e != this) handleTouchHelper(e);
	}

	void handleTouchHelper(Entity e){
		/* */
	}

	void updatePosition(){
		position.x += velocity.x;
		position.y += velocity.y;
	}

	final void updateImagePosition(int deltaTime){
		image.setX(position.x);
		image.setY(position.y);
	}

	void updateTimers(){
		for (Timer t: timerList) t.countUp();
	}

	private final float lowerLimit = 0.01f;
	void limitingForces(List<Rectangle> mapRectangleList, List<Entity> entityList){
		handleWind();
		handleGravity();
		handleFriction();
		limitSpeeds();

		setupRectangles(mapRectangleList, entityList);
		checkWalls();
		checkFloor();
		if (Math.abs(velocity.x) < lowerLimit) velocity.x = 0;
		if (Math.abs(velocity.y) < lowerLimit) velocity.y = 0;
	}

	void handleWind(){
		velocity.x += MapHandler.getRoomWind();
	}

	void handleGravity(){
		velocity.y += getGravity() * MapHandler.getRoomGravity();
	}

	void handleFriction(){
		if (!isGrounded() && !hitstunTimer.timeUp() || state == State.JUMPSQUAT) {}
		else if (!isGrounded()) velocity.x *= getAirFriction();
		else velocity.x *= getFriction();
	}

	void limitSpeeds(){
		float gravFallSpeed = getFallSpeed() * MapHandler.getRoomGravity();
		if (hitstunTimer.timeUp() && velocity.y < gravFallSpeed) velocity.y = gravFallSpeed;
	}

	void setupRectangles(List<Rectangle> mapRectangleList, List<Entity> entityList){
		tempRectangleList.clear();
		tempRectangleList.addAll(mapRectangleList);
		for (Entity en: entityList){
			if (en.getCollision() == Collision.SOLID) tempRectangleList.add(en.getImage().getBoundingRectangle());
		}
	}

	private final int collisionCheck = 4;
	private final float softening = .8f;
	private final float bounce = -0.75f;
	void checkWalls(){
		for (int i = 0; i < collisionCheck; ++i)
			if (doesCollide(position.x + velocity.x, position.y)) {
				if (!hitstunTimer.timeUp()) {
					bounceOffWall();
					return;
				}
				else velocity.x *= softening;
			}
		if (doesCollide(position.x + velocity.x, position.y)) {
			velocity.x = 0;
		}
	}

	void bounceOffWall(){
		velocity.x *= bounce;
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth()/2, position.y));
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth()/2, position.y + image.getHeight()));
		bounceOff();
	}

	void bounceOffCeiling(){
		velocity.y *= bounce;
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + image.getHeight()/2));
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + image.getHeight()/2));
		bounceOff();
	}

	void bounceOff(){
		UptiltEngine.causeHitlag((int)(knockbackIntensity(velocity) / 4));
		if (knockbackIntensity(velocity) > 14) new SFX.MeatyHit().play();
		else if (knockbackIntensity(velocity) > 9) new SFX.MidHit().play();
		else new SFX.LightHit().play();
	}

	void checkFloor(){
		for (int i = 0; i < collisionCheck; ++i)
			if (doesCollide(position.x, position.y + velocity.y)) {
				if (!hitstunTimer.timeUp() && velocity.y > 0) {
					bounceOffCeiling();
					return;
				}
				velocity.y *= softening;
			}
		if (doesCollide(position.x, position.y + velocity.y)) velocity.y = 0;
		if (doesCollide(position.x + velocity.x, position.y + velocity.y)) velocity.y = 0; // checks for diagonal floor
	}

	public boolean doesCollide(float x, float y){
		if (collision == Collision.GHOST) return false;
		for (Rectangle r : tempRectangleList){
			Rectangle thisR = getCollisionBox(x, y);
			boolean upThroughThinPlatform = r.getHeight() <= 1 && r.getY() - this.getPosition().y > 0;
			if (!upThroughThinPlatform && Intersector.overlaps(thisR, r) && thisR != r) return true;
		}
		return false;
	}

	Rectangle getCollisionBox(float x, float y){
		Rectangle r = image.getBoundingRectangle();
		r.setX(x); r.setY(y);
		return r;
	}

	public Rectangle getHurtBox(){
		return image.getBoundingRectangle();
	}

	public void flip(){
		if (direction == Direction.LEFT){
			setDirection(Direction.RIGHT);
			image.setFlip(false, false);
		}
		else{
			setDirection(Direction.LEFT);
			image.setFlip(true, false);
		}
	}

	public int direct(){
		if (direction == Direction.RIGHT) return 1;
		else return -1;
	}

	public boolean isTouching(Entity en, int decrement){
		Rectangle hitboxRect = en.getImage().getBoundingRectangle();
		hitboxRect.setWidth(hitboxRect.getWidth() - decrement);
		hitboxRect.setHeight(hitboxRect.getHeight() - decrement);
		hitboxRect.setX(hitboxRect.getX() + decrement/2);
		hitboxRect.setY(hitboxRect.getY() + decrement/2);
		return Intersector.overlaps(image.getBoundingRectangle(), hitboxRect);
	}

	public void setPosition(Vector2 startPosition) {
		position.x = startPosition.x;
		position.y = startPosition.y;
		velocity.x = 0;
		velocity.y = 0;
	}

	void setAnimation(Animation ani, int deltaTime){
		image.setRegion(ani.getKeyFrame(deltaTime));
		if (direction == Direction.LEFT) image.flip(true, false);
	}

	void setImage(TextureRegion tr){
		float x = 0;
		float y = 0;
		boolean flipped = false;
		if (image != null){
			flipped = image.isFlipX();
			x = image.getX();
			y = image.getY();
		}
		image = new Sprite(tr);
		image.setFlip(flipped, false);
		image.setX(x);
		image.setY(y);
	}

	private final float aboveGround = 2;
	public boolean isGrounded(){ 
		return doesCollide(position.x, position.y - aboveGround); 
	}

	public void ground(){ 
		if (velocity.y < -1 && !inGroundedState()){
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
			new SFX.Ground().play();
		}
	}

	public void setRemove() { toRemove = true; }
	public boolean toRemove() { 
		return toRemove; 
	} 
	public boolean isOOB(Rectangle boundary) {
		int OOBGrace = 2;
		boolean highNotHitstun = (position.y > (boundary.y + + boundary.height + image.getHeight()*OOBGrace)) && !hitstunTimer.timeUp();
		if (
				(position.x < (boundary.x - image.getWidth()*OOBGrace)) ||
				(position.x > (boundary.x + boundary.width + image.getWidth()*OOBGrace))  ||
				(position.y < (boundary.y - image.getHeight()*OOBGrace))  ||
				(highNotHitstun))
			return true;
		return false;
	}
	public Vector2 getPosition() { return position; }
	public Vector2 getVelocity() { return velocity; }
	public Direction getDirection() { return direction; }
	public void setDirection (Direction d) { direction = d; }
	public Collision getCollision() { return collision; }
	public Sprite getImage() { return image; }
	protected boolean inGroundedState() { return groundedStates.contains(state);}
	protected boolean inGroundedState(State prevState) { return groundedStates.contains(prevState); }
	public Vector2 getCenter() {
		Vector2 center = new Vector2();
		center.x = position.x +  image.getWidth()/2;
		center.y = position.y + image.getHeight()/2;
		return center;
	}
	public static float knockbackIntensity(Vector2 knockback) { 
		float intensity = (float) Math.sqrt(Math.pow(Math.abs(knockback.x), 2) + Math.pow(Math.abs(knockback.y), 2)); 
		return intensity; 
	}
	public Color getColor() { return new Color(1, 1, 1, 1); }
	private final List<State> groundedStates = new ArrayList<State>(Arrays.asList(State.STAND, State.WALK, State.RUN, State.DASH, State.CROUCH, State.DODGE));

	public static enum Direction{ LEFT, RIGHT }
	public static enum State{ STAND, WALK, DASH, RUN, CROUCH, DODGE, JUMPSQUAT, FALLEN, JUMP, FALL, WALLSLIDE, HELPLESS }
	public static enum Collision{ SOLID, CREATURE, GHOST }
	public float getGravity() { return gravity; }
	public float getFallSpeed() { return fallSpeed; }
	public float getFriction() { return friction; }
	public float getAirFriction() { return airFriction; }

}
