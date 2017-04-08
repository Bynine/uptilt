package entities;

import input.Brain;
import input.InputHandlerCPU;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Dummy extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.PNG")));

	public Dummy(float posX, float posY) {
		super(posX, posY);
		team = 1;
		setInputHandler(new InputHandlerCPU(this, Brain.Recover.class));
		image = new Sprite(texture);
		gravity = -0.5f;
		weight = 100;
	}
	
	@Override
	void updateImage(float deltaTime){
		setImage(texture);
	}
	
	@Override
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
	}

}
