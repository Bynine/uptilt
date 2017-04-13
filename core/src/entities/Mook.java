package entities;

import moves.*;
import input.InputHandlerCPU;
import input.Brain.Basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Mook extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/mook/stand.png")));

	public Mook(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, Basic.class));
		image = new Sprite(texture);
		gravity = -0.4f;
		weight = 50;
		jumpAcc = 0.4f;
		moveList = new MoveList_Mook(this);
	}
	
	@Override
	void updateImage(float deltaTime){
		setImage(texture);
	}

}
