package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.UptiltEngine;

public class CombatStarter extends ImmobileEntity{
	
	private boolean started = false;

	public CombatStarter(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/combatstarter.png"))));
	}
	
	void handleTouchHelper(Entity e){
		if (UptiltEngine.getPlayers().contains(e) && isTouching(e, 0) && !started){
			UptiltEngine.getChallenge().startCombat(position);
			started = true;
		}
	}
	
}
