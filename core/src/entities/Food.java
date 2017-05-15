package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Food extends Collectible {

	public Food(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/food.png"))));
		bonus = 30;
	}

	void collectHelper(Fighter fi) {
		fi.setPercentage(MathUtils.clamp(fi.getPercentage() - restoredHealth(), 0, 999));
	}
	
	private float restoredHealth(){
		return (float) ( 20 + (Math.random() * 15) );
	}

}
