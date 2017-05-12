package entities;

import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SupplyCrate extends Hittable {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/supplycrate.png")));

	public SupplyCrate(float posX, float posY) {
		super(posX, posY);
		fallSpeed = -1f;
		image = new Sprite(texture);
	}
	
	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean tryy, HitstunType ht){
		MapHandler.addEntity(spawnCollectible());
		setRemove();
	}
	
	private final int crateDimensions = 48;
	public Rectangle getHurtBox(){
		Rectangle r = new Rectangle(position.x + (texture.getRegionWidth()-crateDimensions)/2, position.y, 
				crateDimensions, crateDimensions);
		return r;
	}
	
	private Collectible spawnCollectible(){
		if (Math.random() < 0.5) return new Ammo(position.x, position.y);
		else return new Food(position.x, position.y);
	}

	TextureRegion getStandFrame(float deltaTime) {
		return texture;
	}

	TextureRegion getTumbleFrame(float deltaTime) {
		return texture;
	}

}
