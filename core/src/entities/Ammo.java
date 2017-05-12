package entities;

import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ammo extends Collectible {

	public Ammo(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ammo.png"))));
	}
	
	void collect(Fighter fi){
		if (fi instanceof Wasp && ((Wasp) fi).getSpecialMeter() < Fighter.SPECIALMETERMAX){
			new SFX.Collect().play();
			((Wasp) fi).changeSpecial(4);
			setRemove();
		}
	}

}
