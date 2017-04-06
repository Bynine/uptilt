package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.InputHandler;

public class Kicker extends Fighter {

	private TextureRegion standImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/stand.png")));
	private TextureRegion walkImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/walk.png")));
	private TextureRegion runImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/run.png")));
	private TextureRegion jumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/jump.png")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/fall.png")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/ascend.png")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/slide.png")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/crouch.png")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/helpless.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/dash.png")));

	public Kicker(float posX, float posY, InputHandler inputHandler) {
		super(posX, posY, inputHandler);
		runAcc = 1.6f;
		runSpeed = 10f;
		walkAcc = 0.9f;
		walkSpeed = 4.8f;
		friction = 0.82f;
		gravity = -0.6f;
		jumpAcc = 1.16f;
		dashStrength = 1f;
		doubleJumpStrength = 12.8f;
		fallSpeed = -8f;
	}

	@Override
	void updateImage(float deltaTime){
		TextureRegion prevImage = image;
		switch(state){
		case STAND: setImage(standImage); break;
		case WALK: setImage(walkImage); break;
		case RUN: setImage(runImage); break;
		case JUMP: setImage(jumpImage); break;
		case FALL: {
			if (velocity.y > 0) setImage(ascendImage);
			else setImage(fallImage); 
			break;
		}
		case WALLSLIDE: setImage(slideImage); break;
		case CROUCH: setImage(crouchImage); break;
		case HELPLESS: setImage(helplessImage); break;
		case DASH: setImage(dashImage); break;
		default: setImage(standImage); break;
		}

		if (!attackTimer.timeUp() && null != activeMove && null != activeMove.getAnimation()){
			setImage(activeMove.getAnimation().getKeyFrame(activeMove.getFrame()));
		}
		
		if (prevImage != slideImage && state == State.WALLSLIDE && direction == Direction.RIGHT) wallCling();
		float adjustedPosX = (image.getWidth() - prevImage.getRegionWidth())/2;
		if (!doesCollide(position.x - adjustedPosX, position.y) && state != State.WALLSLIDE) position.x -= adjustedPosX;
		if (direction == Direction.RIGHT && doesCollide(position.x, position.y)) resetStance();

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
