package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import main.SFX;

public class Food extends Collectible {

	public Food(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/food.png"))));
	}

	void collect(Fighter fi) {
		if (fi.getPercentage() > 0){
			new SFX.Collect().play();
			fi.setPercentage(MathUtils.clamp(fi.getPercentage() - restoredHealth(), 0, 999));
			setRemove();
		}
	}
	
	private float restoredHealth(){
		return (float) ( 20 + (Math.random() * 15) );
	}

}
