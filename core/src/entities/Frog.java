package entities;

import main.GlobalRepo;
import movelists.M_Frog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Frog extends Fighter {
	
	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/frog/stand.png", 7, 1, 6, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/frog/walk.png", 2, 1, 12, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/frog/run.png", 11, 1, 4, PlayMode.LOOP);
	private Animation tumbleImage = GlobalRepo.makeAnimation("sprites/fighters/frog/tumble.png", 4, 1, 8, PlayMode.LOOP);
	private TextureRegion fJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/fjump.png")));
	private TextureRegion nJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/njump.png")));
	private TextureRegion bJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/bjump.png")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/fall.png")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/ascend.png")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/crouch.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/dash.png")));
	private TextureRegion dodgeImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/dodgebegin.png")));
	private TextureRegion jumpSquatImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/crouch.png")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/slide.png")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/helpless.png")));
	private TextureRegion grabImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/grab.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/frog/fallen.png")));

	public Frog(float posX, float posY, int team) {
		super(posX, posY, team);
		weight = 120;
		runAcc = 2.2f;
		runSpeed = 9.2f;
		airSpeed = 3.4f;
		airAcc = 0.29f;
		gravity = -0.48f;
		jumpAcc = 1.1f;
		doubleJumpStrength = 10;
		wallJumpStrengthX = 7f;
		wallJumpStrengthY = 8.4f;
		dashStrength = -1f;
		friction = 0.83f;
		jumpSquatTimer.setEndTime(5);
		moveList = new M_Frog(this);
		footStoolKB.set(0, -1);
		footStoolDuration = 40;
		defaultIcon = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconfrog.png")));
	}
	
	public Rectangle getNormalHurtBox(){
		return GlobalRepo.makeHurtBoxDynamic(this, 0.9f, 0.9f);
	}
	
	public Color getColor() { return new Color(0.4f, 1.0f, 0.5f, 1); }
	
	TextureRegion getJumpFrame(float deltaTime){
		if (Math.abs(velocity.x) > 1 && Math.signum(velocity.x) != direct()) return bJumpImage; 
		else if (Math.abs(velocity.x) > airSpeed) return fJumpImage;
		else return nJumpImage;
	}
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return slideImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage; }
	TextureRegion getGrabFrame(float deltaTime) { return grabImage; }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return ascendImage; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage; }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return dodgeImage; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return jumpSquatImage; }
	TextureRegion getTumbleFrame(float deltaTime) { return tumbleImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return helplessImage; }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

}
