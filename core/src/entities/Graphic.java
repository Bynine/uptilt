package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import timers.Timer;

public class Graphic extends Entity{
	
	private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hit.png")));
	private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hitsmall.png")));

	private final Timer duration;
	private int dur;
	
	public Graphic(float posX, float posY, int dur){
		super(posX, posY);
		this.dur = dur;
		duration = new Timer(dur, true);
		timerList.add(duration);
		image = new Sprite(fullSize);
		collision = Collision.GHOST;
		position.x -= image.getWidth()/2;
		position.y -= image.getHeight()/2;
	}
	
	@Override
	void updatePosition(){
		if (duration.getCounter() < dur/2) setImage(halfSize);
		if (duration.timeUp()) setRemove();
	}
}
