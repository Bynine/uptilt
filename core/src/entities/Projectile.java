package entities;

import java.util.List;

import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import main.MapHandler;
import moves.ActionCircle;
import moves.Hitbox;
import moves.ProjectileHitbox;

public abstract class Projectile extends Entity{

	ActionCircle ac;
	Fighter owner;
	float velX, velY = 0;
	public final Timer life = new Timer(1, true);
	private TextureRegion texture;

	public Projectile(float posX, float posY, Fighter owner) {
		super(posX, posY);
		this.owner = owner;
		timerList.add(life);
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		velocity.x = velX;
		velocity.y = velY;
		if (life.timeUp()) setRemove();
		if (deltaTime > 1) handleTouch(entityList);
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);
	}
	
	void setup(String texString, int lifeTime, float velX, float velY){
		texture = new TextureRegion(new Texture(Gdx.files.internal(texString)));
		if (owner.direct() == -1) texture.flip(true, false);
		setImage(texture);
		life.setEndTime(lifeTime);
		this.velX = owner.direct() * velX;
		this.velY = velY;
	}
	
	protected boolean doesCollide(float x, float y){
		boolean hitWall = super.doesCollide(x, y);
		if (hitWall) life.end();
		return hitWall;
	}

	public static class Spiker extends Projectile{
		private final int lifeTime = 30;

		public Spiker(float posX, float posY, Fighter owner) {
			super(posX, posY + 16, owner);
			setup("sprites/entities/projectile.PNG", lifeTime, 12, 0);
			ac = new ProjectileHitbox(owner, 0.8f, 0.4f, 1, 90, 12, 6, 11, this, lifeTime);
			ac.setRefresh(4);
			MapHandler.addActionCircle(ac);
		}

		void handleTouchHelper(Entity e){
			if (e != owner && isTouching(e, 8)) velX = 0;  // sticks into enemies
		}

	}

	public static class Rocket extends Projectile{

		private final int lifeTime = 60;

		public Rocket(float posX, float posY, Fighter owner) {
			super(posX, posY + 8, owner);
			setup("sprites/entities/rocket.PNG", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 5, Hitbox.SAMURAIANGLE, 12, 6, 12, this, lifeTime);
			MapHandler.addActionCircle(ac);
			velX = owner.direct() * 8;
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.isInitialHit()) explode();
		}

		void explode(){
			MapHandler.addEntity(new Explosion(position.x, position.y, owner));
			life.end();
		}
	}
	
	private static class Explosion extends Projectile{

		private final int lifeTime = 4;

		public Explosion(float posX, float posY, Fighter owner) {
			super(posX - 32, posY - 32, owner);
			setup("sprites/entities/explosion.PNG", lifeTime, 0, 0);
			ac = new ProjectileHitbox(null, 4, 3, 8, Hitbox.SAMURAIANGLE, 12, 6, 40, this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
	}

}
