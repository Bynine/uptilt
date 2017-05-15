package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import main.UptiltEngine;

public class CombatStarter extends ImmobileEntity{
	
	private boolean started = false;

	public CombatStarter(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/combatstarter.png"))));
	}
	
	void handleTouchHelper(Entity e){
		if (UptiltEngine.getPlayers().contains(e) && isTouching(e, 0) && !started){
			Vector2 startPosition = new Vector2(position);
			startPosition.x = position.x + image.getWidth() / 2;
			UptiltEngine.getChallenge().startCombat(this, startPosition);
			started = true;
		}
	}
	
	public static class EndCombatStarter extends CombatStarter{

		public EndCombatStarter(float posX, float posY) {
			super(posX, posY);
		}
		
	}
	
}
