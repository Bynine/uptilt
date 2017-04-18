package entities;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Kicker extends Fighter {

	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/kicker/stand.png", 7, 1, 6, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/kicker/walk.png", 2, 1, 12, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/kicker/run.png", 11, 1, 4, PlayMode.LOOP);
	private Animation tumbleImage = GlobalRepo.makeAnimation("sprites/fighters/kicker/tumble.png", 4, 1, 8, PlayMode.LOOP);
	private TextureRegion fJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/fjump.png")));
	private TextureRegion nJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/njump.png")));
	private TextureRegion bJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/bjump.png")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/fall.png")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/ascend.png")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/crouch.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/dash.png")));
	private TextureRegion dodgeImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/dodgebegin.png")));
	private TextureRegion jumpSquatImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/crouch.png")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/slide.png")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/helpless.png")));
	private TextureRegion grabImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/grab.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/fallen.png")));

	public Kicker(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 1.7f;
		runSpeed = 8.3f;
		walkAcc = 0.75f;
		walkSpeed = 3.9f;
		friction = 0.82f;
		gravity = -0.5f;
		jumpAcc = 1.02f;
		dashStrength = 0f;
		doubleJumpStrength = 10.2f;
		fallSpeed = -7f;
	}
	
	private final float hurtBoxReduction = 0.9f;
	public Rectangle getHurtBox(){
		Rectangle r = image.getBoundingRectangle();
		float widthDiff = (36 - r.width)/2;
		r.width *= hurtBoxReduction;
		r.height *= hurtBoxReduction;
		r.y += 6;
		if (direction == Direction.LEFT) r.x -= widthDiff;
		return r;
	}
	
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
