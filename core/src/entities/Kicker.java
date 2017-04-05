package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.InputHandler;

public class Kicker extends Fighter {
	
	private TextureRegion standImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/stand.PNG")));
	private TextureRegion walkImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/walk.PNG")));
	private TextureRegion runImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/run.PNG")));
	private TextureRegion jumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/jump.PNG")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/fall.PNG")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/ascend.PNG")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/slide.PNG")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/crouch.PNG")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/helpless.PNG")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/dash.PNG")));

	public Kicker(float posX, float posY, InputHandler inputHandler) {
		super(posX, posY, inputHandler);
		runAcc = 1.6f;
		runSpeed = 10f;
		walkAcc = 0.9f;
		walkSpeed = 4.8f;
		friction = 0.82f;
		gravity = -0.6f;
		jumpAcc = 0.92f;
		doubleJumpStrength = 10;
		fastFallSpeed = -10;
	}
	
	@Override
	void updateImage(){
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
		if (prevImage != slideImage && state == State.WALLSLIDE && direction == Direction.RIGHT) {
			while (!doesCollide(position.x + 1, position.y)){
				position.x += 1;
			}
		}
		if (doesCollide(position.x, position.y)) {
			setImage(prevImage);
		}
	}

}
