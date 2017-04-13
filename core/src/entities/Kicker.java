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
	private TextureRegion fJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/fjump.png")));
	private TextureRegion nJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/njump.png")));
	private TextureRegion bJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/bjump.png")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/fall.png")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/ascend.png")));
	private TextureRegion wallSlideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/slide.png")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/crouch.png")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/helpless.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/dash.png")));
	private TextureRegion dodgeImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/dodgebegin.png")));
	private TextureRegion grabImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/grab.png")));
	private TextureRegion jumpSquatImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/kicker/crouch.png")));

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
		float widthDiff = (standImage.getKeyFrame(0).getRegionWidth() - r.width)/2;
		r.width *= hurtBoxReduction;
		r.height *= hurtBoxReduction;
		r.y += 6;
		if (direction == Direction.LEFT) r.x -= widthDiff;
		return r;
	}
	
	private void setJumpImage(){
		if (Math.abs(velocity.x) > 1 && Math.signum(velocity.x) != direct()) setImage(bJumpImage); 
		else if (Math.abs(velocity.x) > airSpeed) setImage(fJumpImage);
		else setImage(nJumpImage);
	}

	void updateImage(float deltaTime){
		TextureRegion prevImage = image;
		switch(state){
		case STAND: setImage(getFrame(standImage, deltaTime)); break;
		case WALK: setImage(walkImage.getKeyFrame(deltaTime)); break;
		case RUN: setImage(runImage.getKeyFrame(deltaTime)); break;
		case JUMP: {
			setJumpImage();
			break;
		}
		case FALL: {
			if (velocity.y > 1) setJumpImage();
			else if (velocity.y > 0) setImage(ascendImage);
			else setImage(fallImage); 
			break;
		}
		case WALLSLIDE: setImage(wallSlideImage); break;
		case CROUCH: setImage(crouchImage); break;
		case HELPLESS: setImage(helplessImage); break;
		case DASH: setImage(dashImage); break;
		case DODGE: setImage(dodgeImage); break;
		case JUMPSQUAT: setImage(jumpSquatImage); break;
		default: break;
		}
		/* following should be generic */
		if (!attackTimer.timeUp() && null != activeMove && null != activeMove.getAnimation()){
			setImage(activeMove.getAnimation().getKeyFrame(activeMove.getFrame()));
		}
		if (!hitstunTimer.timeUp() || !caughtTimer.timeUp()) setImage(helplessImage);
		if (!grabbingTimer.timeUp()) setImage(grabImage);
		adjustImage(prevImage);
	}
	
	private void adjustImage(TextureRegion prevImage){
		if (prevImage != wallSlideImage && state == State.WALLSLIDE && direction == Direction.RIGHT) wallCling();
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

}
