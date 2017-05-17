package entities;

import main.GlobalRepo;
import movelists.M_Speedy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Speedy extends Fighter {
	
	private TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/stand.png")));
	private TextureRegion dash = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/dash.png")));
	private TextureRegion crouch = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/crouch.png")));
	private TextureRegion hitstun = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/hitstun.png")));
	private TextureRegion jump = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/jump.png")));
	private TextureRegion fallen = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/fallen.png")));
	private Animation run = GlobalRepo.makeAnimation("sprites/fighters/speedy/run.png", 3, 1, 4, PlayMode.LOOP);
	private Animation walk = GlobalRepo.makeAnimation("sprites/fighters/speedy/run.png", 3, 1, 8, PlayMode.LOOP);
	private Animation tumble = GlobalRepo.makeAnimation("sprites/fighters/speedy/dair.png", 2, 1, 6, PlayMode.LOOP_RANDOM);
	private Animation helpless = GlobalRepo.makeAnimation("sprites/fighters/speedy/fair.png", 2, 1, 6, PlayMode.LOOP_RANDOM);
	protected Animation wallSlide = GlobalRepo.makeAnimation("sprites/fighters/speedy/wallslide.png", 3, 1, 20, PlayMode.LOOP);

	public Speedy(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 2.5f;
		runSpeed = 7.5f;
		walkAcc = 1.1f;
		walkSpeed = 4.3f;
		airAcc = 0.3f;
		airSpeed = 5f;
		friction = 0.82f;
		gravity = -0.50f;
		jumpAcc = 0.91f;
		dashStrength = 2f;
		doubleJumpStrength = 9.1f;
		fallSpeed = -7.8f;
		baseWeight = 72;
		wallSlideSpeed = 3;
		wallJumpStrengthX = 1.2f;
		wallJumpStrengthY = 8.2f;
		moveList = new M_Speedy(this);
		basePower = 0.8f;
		baseHitstun = GlobalRepo.ENEMYHITSTUNMOD;
	}
	
	protected boolean isWallSliding() {
		if (inGroundedState() || velocity.y > wallSlideSpeed || !(canAct() || state == State.HELPLESS)) return false;
		boolean canWS = false;
		if (prevStickX < -unregisteredInputMax) {
			canWS = doesCollide(position.x - wallSlideDistance, position.y);
			if (direction == Direction.RIGHT && canWS) flip();
		}
		if (prevStickX > unregisteredInputMax) {
			canWS = doesCollide(position.x + wallSlideDistance, position.y);
			if (direction == Direction.LEFT && canWS) flip();
		}
		if (canWS) state = State.WALLSLIDE;
		return canWS;
	}
	
	protected void updateAerialState(){
		if (isWallSliding()) state = State.WALLSLIDE;
		else if (state == State.HELPLESS) return;
		else if (!jumpTimer.timeUp()) state = State.JUMP;
		else state = State.FALL;
	}
	
	TextureRegion getJumpFrame(float deltaTime) { return jump; }
	TextureRegion getStandFrame(float deltaTime) { return stand; }
	TextureRegion getWalkFrame(float deltaTime) { return walk.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return run.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return wallSlide.getKeyFrame(deltaTime); }
	TextureRegion getHelplessFrame(float deltaTime) { return helpless.getKeyFrame(deltaTime); }
	TextureRegion getGrabFrame(float deltaTime) { return stand; }
	TextureRegion getFallFrame(float deltaTime) { return dash; }
	TextureRegion getAscendFrame(float deltaTime) { return jump; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouch; }
	TextureRegion getDashFrame(float deltaTime) { return dash; }
	TextureRegion getDodgeFrame(float deltaTime) { return stand; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return crouch; }
	TextureRegion getTumbleFrame(float deltaTime) { return tumble.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return hitstun; }
	TextureRegion getFallenFrame(float deltaTime) { return fallen; }

}
