package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.UptiltEngine;
import timers.Timer;

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

	float gravity = -0.35f;
	float friction = 0.85f, airFriction = 0.95f;
	final int superHitstunBase = 10;

	boolean toRemove = false;
	private final List<Rectangle> tempRectangleList = new ArrayList<Rectangle>();
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
		//if (this instanceof Kicker) System.out.println(state);
		updateState();
		handleDirection();
		handleMovement();
		if (deltaTime > 1) handleTouch(entityList);
		limitingForces(rectangleList, entityList);
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);

		if (state == State.RUN && deltaTime % 8 == 0) {
			if (direction == Direction.LEFT) MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
			else MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		}
		int knockbackPower = (int) (Math.abs(velocity.x) + Math.abs(velocity.y));
		if (!hitstunTimer.timeUp() && deltaTime % 4 == 0 && knockbackPower > superHitstunBase) {
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
		handleGravity();
		handleFriction();
		limitSpeeds();

		setupRectangles(mapRectangleList, entityList);
		checkWalls();
		checkFloor();
		if (Math.abs(velocity.x) < lowerLimit) velocity.x = 0;
		if (Math.abs(velocity.y) < lowerLimit) velocity.y = 0;
	}

	void handleGravity(){
		velocity.y += gravity;
	}

	void handleFriction(){
		if (!isGrounded() && !hitstunTimer.timeUp() || state == State.JUMPSQUAT) {}
		else if (!isGrounded()) velocity.x *= airFriction;
		else velocity.x *= friction;
	}

	void limitSpeeds(){
		/* */
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
					// TODO: make actually hit wall. right now bounces off before it even touches
					velocity.x *= bounce;
					UptiltEngine.causeHitlag((int)(velocity.x / 3));
					return;
				}
				else velocity.x *= softening;
			}
		if (doesCollide(position.x + velocity.x, position.y)) {
			velocity.x = 0;
		}
	}

	void checkFloor(){
		for (int i = 0; i < collisionCheck; ++i)
			if (doesCollide(position.x, position.y + velocity.y)) {
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

	public boolean isOOB(int mapWidth, int mapHeight) {
		int OOBGrace = 2;
		if (position.x < (0 - image.getWidth()*OOBGrace) || (mapWidth + image.getWidth()*OOBGrace) < position.x) return true;
		if (position.y < (0 - image.getHeight()*OOBGrace) || (mapHeight + image.getHeight()*OOBGrace) < position.y) return true;
		return false;
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
		if (velocity.y < -0.1 && !inGroundedState()){
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
			MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		}
	}

	public void fallOffScreen() {
		collision = Collision.GHOST;
		gravity = -0.5f;
	}

	public void setRemove() { toRemove = true; }
	public boolean toRemove() { return toRemove; } 

	public Vector2 getPosition() { return position; }
	public Vector2 getVelocity() { return velocity; }
	public Direction getDirection() { return direction; }
	public void setDirection (Direction d) { direction = d; }
	public Collision getCollision() { return collision; }
	public Sprite getImage() { return image; }
	protected boolean inGroundedState() { return groundedStates.contains(state);}
	protected boolean inGroundedState(State prevState) { return groundedStates.contains(prevState); }
	private final List<State> groundedStates = new ArrayList<State>(Arrays.asList(State.STAND, State.WALK, State.RUN, State.DASH, State.CROUCH, State.DODGE));

	public static enum Direction{ LEFT, RIGHT }
	public static enum State{ STAND, WALK, DASH, RUN, CROUCH, DODGE, JUMPSQUAT, JUMP, FALL, WALLSLIDE, HELPLESS }
	public static enum Collision{ SOLID, CREATURE, GHOST }

}
