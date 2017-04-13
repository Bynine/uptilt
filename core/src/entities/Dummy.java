package entities;

import input.Brain.*;
import input.InputHandlerCPU;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Dummy extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));

	public Dummy(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, Basic.class));
		image = new Sprite(texture);
		gravity = -0.42f;
		weight = 100;
	}
	
	@Override
	void updateImage(float deltaTime){
		setImage(texture);
	}

}
