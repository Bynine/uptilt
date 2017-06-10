package entities;

import inputs.InputHandlerCPU;
import inputs.Brain.MookBrain;
import main.GlobalRepo;
import movelists.M_Basic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Basic extends Fighter {

	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/basic/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/basic/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/basic/run.png", 2, 1, 8, PlayMode.LOOP);
	private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/basic/jump.png", 1, 1, 1, PlayMode.LOOP);
	private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/basic/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/basic/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/basic/hitstun.png", 2, 1, 8, PlayMode.LOOP);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fall.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fallen.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/dash.png")));

	public Basic(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.45f;
		baseWeight = 90;
		jumpAcc = 0.49f;
		airSpeed = 1.8f;
		walkSpeed = 1.7f;
		runSpeed = 2.5f;
		friction = 0.9f;
		wallJumpStrengthX = 2f;
		wallJumpStrengthY = 7.2f;
		doubleJumpStrength = 7.5f;
		moveList = new M_Basic(this);
		jumpSquatTimer.setEndTime(5);
		baseHitstun = GlobalRepo.ENEMYHITSTUNMOD;
	}
		
	protected boolean isWallSliding(){
		return false;
	}

	TextureRegion getJumpFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return fallImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getGrabFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getTumbleFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return hitstunImage.getKeyFrame(deltaTime); }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

}
