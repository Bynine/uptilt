package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import timers.DurationTimer;

public abstract class Graphic extends Entity{
	
	final DurationTimer duration;
	int dur;

	Graphic(float posX, float posY, int dur){
		super(posX, posY);
		this.dur = dur;
		duration = new DurationTimer(dur);
		duration.restart();
		timerList.add(duration);
		collision = Collision.GHOST;
	}
	
	public static class HitGraphic extends Graphic{
		private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hit.png")));
		private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hitsmall.png")));
		
		public HitGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			image = new Sprite(fullSize);
			position.x -= image.getWidth()/2;
			position.y -= image.getHeight()/2;
		}
		
		void updatePosition(){
			if (duration.getCounter() > dur/2) setImage(halfSize);
			if (duration.timeUp()) setRemove();
		}
		
	}
	
	public static class SmokeTrail extends Graphic{
		private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/poff.png")));
		private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/poffsmall.png")));
		
		public SmokeTrail(float posX, float posY){
			super(posX, posY, 6);
			image = new Sprite(fullSize);
			position.x -= image.getWidth()/2;
			position.y -= image.getHeight()/2;
		}
		
		public SmokeTrail(float posX, float posY, int dur){
			this(posX, posY);
			duration.setEndTime(dur);
		}
		
		void updatePosition(){
			if (duration.getCounter() > dur/2) setImage(halfSize);
			if (duration.timeUp()) setRemove();
		}
		
	}
	
	public static class DustCloud extends Graphic{
		private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/dustcloud.png")));
		
		public DustCloud(Entity e, float posX, float posY){
			super(posX, posY, 4);
			image = new Sprite(texture);
			position.x -= image.getWidth()/2;
			if (e.direction == Direction.LEFT){
				image.flip(true, false);
				position.x += e.getImage().getWidth();
			}
		}
		
		void updatePosition(){
			if (duration.timeUp()) setRemove();
		}
		
	}
}
