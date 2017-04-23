package entities;

import java.util.Arrays;
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
import moves.ActionCircleGroup;
import moves.Hitbox;
import moves.ProjectileHitbox;

public abstract class Projectile extends Entity{

	Hitbox ac;
	Fighter owner;
	float velX, velY = 0;
	public final Timer life = new DurationTimer(1);
	boolean trans = false;
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
	
	void handleTouchHelper(Entity e){
		if (isTouching(e, 0) && e instanceof Projectile){
			Projectile p = (Projectile) e;
			boolean collides = true;
			if (null != p.owner){
				if (p.owner.getTeam() == owner.getTeam()) collides = false;
			}
			if (collides) touchOtherProjectile(p);
		}
	}
	
	abstract void touchOtherProjectile(Projectile p);

	public static class Spiker extends Projectile{
		private final int lifeTime = 60;

		public Spiker(float posX, float posY, Fighter owner) {
			super(posX, posY + 30, owner);
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/ripwheel.png", lifeTime, 12, 0);
			ac = new ProjectileHitbox(owner, 1.2f, 0.02f, 4, 90, 0, 0, 11, new SFX.LightHit(), this, lifeTime);
			ac.setRefresh(8);
			MapHandler.addActionCircle(ac);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) velX *= 0.3f;
		}
		
		void touchOtherProjectile(Projectile p){
			if (!p.trans) life.moveCounterForward(20);
		}

	}
	
	public abstract static class Explosive extends Projectile{
		boolean exploded = false;
		
		public Explosive(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if ( (ac.hitAnybody() || life.timeUp()) && !exploded) explode();
		}
		
		void explode(){
			MapHandler.addEntity(getExplosion());
			exploded = true;
			life.end();
		}

		public boolean doesCollide(float x, float y){
			if (super.doesCollide(x, y)) explode();
			return super.doesCollide(x, y);
		}
		
		void touchOtherProjectile(Projectile p){
			if (!p.trans) explode();
		}
		
		protected abstract Projectile getExplosion();
		
	}

	public static class Rocket extends Explosive{

		private final int lifeTime = 60;

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
			if (Math.abs(velX) < 10) velX *= 1.06f;
			if (deltaTime % 10 == 0) MapHandler.addEntity(new Graphic.SmokeTrail(this, 8));
		}

		protected Projectile getExplosion() {
			return new Explosion(position.x, position.y, owner, this);
		}
	}

	private static class Explosion extends Projectile{

		private final int lifeTime = 4;
		private final int displacement = 40;

		public Explosion(float posX, float posY, Fighter owner, Projectile rocket) {
			super(0, 0, owner);
			new SFX.Explode().play();
			position.x = rocket.position.x - displacement;
			position.y = rocket.position.y - displacement;
			setup("sprites/entities/explosion.png", lifeTime, 0, 0);
			ac = new ProjectileHitbox(null, 8, 3, 30, Hitbox.SAMURAIANGLE, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ProjectileHitbox(null, 7, 2, 10, Hitbox.SAMURAIANGLE, 0, 0, 60, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac3 = new ProjectileHitbox(null, 6, 0, 0, 20, 0, 0, 120, new SFX.HeavyHit(), this, lifeTime);
			new ActionCircleGroup(Arrays.asList(ac, ac2, ac3));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
			MapHandler.addActionCircle(ac3);
		}
		
		void touchOtherProjectile(Projectile p){
			/* nothing */
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
		
		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}

	public static class Laser extends Projectile{

		final int lifeTime = 30;
		final int laserSpeed = 12;

		public Laser(float posX, float posY, Fighter owner) {
			super(posX, posY + 32, owner);
			trans = true;
			new SFX.LaserFire().play();
			setup("sprites/entities/laser.png", lifeTime, laserSpeed, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 4, 270, 0, 0, 6, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) life.end();
		}
		
		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}
	
	public static class LaserUp extends Laser{
		
		public LaserUp(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			position.x += owner.getImage().getWidth()/2;
			setup("sprites/entities/laserup.png", lifeTime, 0, laserSpeed);
		}
		
	}
	
	public static class LaserDiagonalF extends Laser{

		public LaserDiagonalF(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			fixLaser(owner.getDirection() == Direction.RIGHT);
			setup("sprites/entities/laserdiagonal.png", lifeTime, laserSpeed/3, laserSpeed);
		}

	}
	
	public static class LaserDiagonalB extends Laser{

		public LaserDiagonalB(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			fixLaser(owner.getDirection() == Direction.LEFT);
			setup("sprites/entities/laserdiagonal.png", lifeTime, -laserSpeed/3, laserSpeed);
		}

	}
	
	void fixLaser(boolean dir){
		if (dir) flip();
		else position.x += owner.getImage().getWidth()/2;
	}

	public static class ChargeLaser extends Projectile{

		private final int lifeTime = 60;

		public ChargeLaser(float posX, float posY, Fighter owner) {
			super(posX, posY + 32, owner);
			new SFX.ChargeLaserFire().play();
			setup("sprites/entities/chargelaser.png", lifeTime, 4, 0);
			ac = new ProjectileHitbox(owner, 1, 2, 8, Hitbox.SAMURAIANGLE, 0, 0, 12, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) life.end();
		}
		
		void touchOtherProjectile(Projectile p){
			if (!p.trans) life.end();
		}

	}
	
	public static class SuperLaser extends Explosive{

		private final int lifeTime = 60;

		public SuperLaser(float posX, float posY, Fighter owner) {
			super(posX, posY + 32, owner);
			new SFX.ChargeLaserFire().play();
			new SFX.LaserFire().play();
			setup("sprites/entities/superlaser.png", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 5, 0, 0, 0, 12, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}

		protected Projectile getExplosion() {
			return new LaserExplosion(position.x, position.y, owner, this);
		}

	}
	
	private static class LaserExplosion extends Projectile{

		private final int lifeTime = 4;
		private final int displacement = 18;

		public LaserExplosion(float posX, float posY, Fighter owner, Projectile superlaser) {
			super(0, 0, owner);
			new SFX.ChargeLaserFire().play();
			position.x = superlaser.position.x - displacement;
			position.y = superlaser.position.y - displacement;
			setup("sprites/entities/laserexplosion.png", lifeTime, 0, 0);
			ac = new ProjectileHitbox(null, 6, 2, 20, Hitbox.SAMURAIANGLE, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ProjectileHitbox(null, 5, 1, 10, Hitbox.SAMURAIANGLE, 0, 0, 80, new SFX.HeavyHit(), this, lifeTime);
			ac.setProperty(Hitbox.Property.ELECTRIC);
			ac2.setProperty(Hitbox.Property.ELECTRIC);
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}
		
		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}

}
