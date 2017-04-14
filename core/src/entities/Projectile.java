package entities;

import java.util.List;

import timers.DurationTimer;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import main.MapHandler;
import main.SFX;
import moves.ActionCircle;
import moves.Hitbox;
import moves.ProjectileHitbox;

public abstract class Projectile extends Entity{

	ActionCircle ac;
	Fighter owner;
	float velX, velY = 0;
	public final Timer life = new DurationTimer(1);
	private TextureRegion texture;

	public Projectile(float posX, float posY, Fighter owner) {
		super(posX, posY);
		this.owner = owner;
		timerList.add(life);
	}

	public void reverse() {
		velX *= -1;
		velY *= -1;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		velocity.x = velX;
		velocity.y = velY;
		if (life.timeUp()) setRemove();
		setupRectangles(rectangleList, entityList);
		checkWalls();
		checkFloor();
		if (deltaTime > 1) handleTouch(entityList);
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);
	}

	/** texture string, lifetime, velocity x, velocity y **/
	void setup(String texString, int lifeTime, float velX, float velY){
		texture = new TextureRegion(new Texture(Gdx.files.internal(texString)));
		setImage(texture);
		if (owner.direct() == -1) flip();
		life.setEndTime(lifeTime);
		this.velX = owner.direct() * velX;
		this.velY = velY;
	}

	public boolean doesCollide(float x, float y){
		boolean hitWall = super.doesCollide(x, y);
		if (hitWall) life.end();
		return hitWall;
	}

	public static class Spiker extends Projectile{
		private final int lifeTime = 60;

		public Spiker(float posX, float posY, Fighter owner) {
			super(posX, posY + 30, owner);
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/projectile.png", lifeTime, 12, 0);
			ac = new ProjectileHitbox(owner, 1.2f, 0.02f, 4, 90, 0, 0, 11, new SFX.LightHit(), this, lifeTime);
			ac.setRefresh(8);
			MapHandler.addActionCircle(ac);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) velX *= 0.3f;
		}

	}

	public static class Rocket extends Projectile{

		private final int lifeTime = 60;
		boolean exploded = false;

		public Rocket(float posX, float posY, Fighter owner) {
			super(posX, posY + 24, owner);
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/rocket.png", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 5, Hitbox.SAMURAIANGLE, 0, 0, 16, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
			velX = owner.direct() * 2;
			if (owner.holdUp()) velX = 1;
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if ( (ac.hitAnybody() || life.timeUp()) && !exploded) explode();
			if (Math.abs(velX) < 10) velX *= 1.05f;
			if (deltaTime % 10 == 0) MapHandler.addEntity(new Graphic.SmokeTrail(this, 8));
		}

		public  boolean doesCollide(float x, float y){
			if (super.doesCollide(x, y)) explode();
			return super.doesCollide(x, y);
		}

		void explode(){
			MapHandler.addEntity(new Explosion(position.x, position.y, owner, this));
			exploded = true;
			life.end();
		}
	}

	private static class Explosion extends Projectile{

		private final int lifeTime = 4;
		private final int displacement = 18;

		public Explosion(float posX, float posY, Fighter owner, Projectile rocket) {
			super(0, 0, owner);
			new SFX.Explode().play();
			position.x = rocket.position.x - displacement;
			position.y = rocket.position.y - displacement;
			setup("sprites/entities/explosion.png", lifeTime, 0, 0);
			ac = new ProjectileHitbox(null, 8, 2, 30, Hitbox.SAMURAIANGLE, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
	}

	public static class ShotgunBlast extends Projectile{

		private final int lifeTime = 2;

		public ShotgunBlast(float posX, float posY, Fighter owner) {
			super(posX, posY - 24, owner);
			new SFX.Explode().play();
			setup("sprites/entities/blast.png", lifeTime, 0, 0);
			ac = new ProjectileHitbox(owner, 8, 2, 16, 270, 0, 0, 20, new SFX.HeavyHit(), this, lifeTime);
			ActionCircle ac2 = new ProjectileHitbox(owner, 5, 2, 16, 270, 5, -20, 10, new SFX.HeavyHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}
	}

	public static class Laser extends Projectile{

		final int lifeTime = 30;
		final int laserSpeed = 12;

		public Laser(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			setup("sprites/entities/projectile.png", lifeTime, laserSpeed, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 4, 270, 0, 0, 6, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) life.end();
		}

	}
	
	public static class LaserUp extends Laser{
		
		public LaserUp(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			setup("sprites/entities/projectile.png", lifeTime, 0, laserSpeed);
		}
		
	}

	public static class ChargeLaser extends Projectile{

		private final int lifeTime = 60;

		public ChargeLaser(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			setup("sprites/entities/projectile.png", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 1, 2, 8, Hitbox.SAMURAIANGLE, 0, 0, 12, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) life.end();
		}

	}

}
