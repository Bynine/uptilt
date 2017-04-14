package entities;

import main.GlobalRepo;
import moves.*;
import input.InputHandlerCPU;
import input.Brain.MookBrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Mook extends Fighter {
	
	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/mook/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/mook/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/mook/run.png", 2, 1, 8, PlayMode.LOOP);
	private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/mook/jump.png", 1, 1, 1, PlayMode.LOOP);
	private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/mook/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/mook/nair.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/mook/fall.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/mook/dash.png")));
	private TextureRegion wallSlideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/mook/wallslide.png")));

	public Mook(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.4f;
		weight = 70;
		jumpAcc = 0.52f;
		airSpeed = 2f;
		friction = 0.86f;
		wallJumpStrengthX = 7f;
		wallJumpStrengthY = 6.2f;
		doubleJumpStrength = 7.5f;
		moveList = new MoveList_Mook(this);
		jumpSquatTimer.setEndTime(6);
	}

	TextureRegion getJumpFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return wallSlideImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getGrabFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }

}
