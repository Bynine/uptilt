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
	private Animation wallSlide = GlobalRepo.makeAnimation("sprites/fighters/speedy/wallslide.png", 3, 1, 18, PlayMode.LOOP);

	public Speedy(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 3f;
		runSpeed = 10.5f;
		walkAcc = 1.4f;
		walkSpeed = 6.3f;
		airAcc = 0.4f;
		airSpeed = 6f;
		friction = 0.82f;
		gravity = -0.52f;
		jumpAcc = 0.92f;
		dashStrength = 4f;
		doubleJumpStrength = 9.2f;
		fallSpeed = -8f;
		weight = 75;
		wallSlideSpeed = 4;
		moveList = new M_Speedy(this);
		hitstunMod = GlobalRepo.ENEMYHITSTUNMOD;
	}
	
	protected boolean isWallSliding() {
		if (inGroundedState() || velocity.y > 4 || !(canAct() || state == State.HELPLESS)) return false;
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
