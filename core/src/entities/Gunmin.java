package entities;

import main.GlobalRepo;
import moves.MoveList_Gunmin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Gunmin extends Fighter {
	
	private TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/stand.png")));
	private TextureRegion walk = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/walk.png")));
	private TextureRegion run = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/run.png")));
	private TextureRegion crouch = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/crouch.png")));
	private TextureRegion jump = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/jump.png")));
	private TextureRegion ascend = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/ascend.png")));
	private TextureRegion fall = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/fall.png")));
	private TextureRegion hitstun = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/hitstun.png")));
	private TextureRegion fallen = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/fallen.png")));
	private Animation tumble = GlobalRepo.makeAnimation("sprites/fighters/laser/tumble.png", 4, 1, 6, PlayMode.LOOP);

	public Gunmin(float posX, float posY, int team) {
		super(posX, posY, team);
		walkAcc = 0.08f;
		runAcc = 0.12f;
		airAcc = 0.13f;
		gravity = -0.38f;
		weight = 64;
		doubleJumpStrength = 8;
		friction = 0.94f;
		jumpAcc = 0.6f;
		moveList = new MoveList_Gunmin(this);
		jumpSquatTimer.setEndTime(6);
	}

	TextureRegion getJumpFrame(float deltaTime) { return jump; }
	TextureRegion getStandFrame(float deltaTime) { return stand; }
	TextureRegion getWalkFrame(float deltaTime) { return walk; }
	TextureRegion getRunFrame(float deltaTime) { return run; }
	TextureRegion getWallSlideFrame(float deltaTime) { return jump; }
	TextureRegion getHelplessFrame(float deltaTime) { return jump; }
	TextureRegion getGrabFrame(float deltaTime) { return walk; }
	TextureRegion getFallFrame(float deltaTime) { return fall; }
	TextureRegion getAscendFrame(float deltaTime) { return ascend; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouch; }
	TextureRegion getDashFrame(float deltaTime) { return run; }
	TextureRegion getDodgeFrame(float deltaTime) { return run; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return crouch; }
	TextureRegion getTumbleFrame(float deltaTime) { return tumble.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return hitstun; }
	TextureRegion getFallenFrame(float deltaTime) { return fallen; }

}
