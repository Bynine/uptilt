package entities;

import main.GlobalRepo;
import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

public class TreasureChest extends Throwable {

	private TextureRegion closed = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/chestclosed.png")));
	private TextureRegion open = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/chestopen.png")));
	private Animation spin = GlobalRepo.makeAnimation("sprites/entities/chestspin.png", 4, 1, 8, PlayMode.LOOP);
	boolean opened = false;

	public TreasureChest(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(closed);
	}

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean tryy, HitstunType ht){
		if (knockbackIntensity(knockback) > 0 && !opened) open();
		super.knockbackHelper(knockback, DAM, hitstun, tryy, ht);
	}

	private void open(){
		final float collectibleSpeed = 7;
		int numberOfCollectibles = 2;
		for (int i = 0; i < numberOfCollectibles; ++i){
			Collectible co = spawnCollectible();
			co.velocity.set(collectibleSpeed, collectibleSpeed);
			co.velocity.setAngle((float) (110 - (Math.random() * 40)));
			MapHandler.addEntity(co);
		}
		opened = true;
	}

	private Collectible spawnCollectible(){
		if (Math.random() < 1.0/3.0) return new Treasure(position.x, position.y);
		else if (Math.random() < 1.0/2.0) return new Drink(position.x, position.y);
		else return new Food(position.x, position.y);
	}

	TextureRegion getStandFrame(float deltaTime) {
		if (opened) return open;
		else return closed;
	}

	TextureRegion getTumbleFrame(float deltaTime) {
		return spin.getKeyFrame(deltaTime);
	}

}
