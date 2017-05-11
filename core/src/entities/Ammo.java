package entities;

import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ammo extends Entity {

	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ammo.png")));

	public Ammo(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(texture);
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Wasp && isTouching(e, 0)){
			if (((Wasp) e).getSpecialMeter() < Fighter.SPECIALMETERMAX){
				new SFX.Collect().play();
				((Wasp) e).changeSpecial(4);
				setRemove();
			}
		}
	}

}
