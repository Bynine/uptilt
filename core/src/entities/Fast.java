package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.InputHandler;

public class Fast extends Fighter {
	
	private TextureRegion standImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/stand.PNG")));
	private TextureRegion runImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/run.PNG")));
	private TextureRegion jumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/jump.PNG")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/fall.PNG")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/ascend.PNG")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/slide.PNG")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/crouch.PNG")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/player/helpless.PNG")));

	public Fast(float posX, float posY, InputHandler inputHandler) {
		super(posX, posY, inputHandler);
	}
	
	@Override
	void updateImage(){
		TextureRegion prevImage = image;
		switch(state){
		case STAND: setImage(standImage); break;
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
		default: setImage(standImage); break;
		}
		if (checkCollision(position.x, position.y)) setImage(prevImage);
	}

}
