package entities;

import java.util.Arrays;
import java.util.List;

import timers.DurationTimer;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import entities.Hittable.HitstunType;
import main.MapHandler;
import main.SFX;
import moves.ActionCircleGroup;
import moves.Hitbox;
import moves.Hitbox.Property;
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
		updateVelocity(rectangleList, entityList);
		if (life.timeUp()) setRemove();
		setupRectangles(rectangleList, entityList);
		checkWalls();
		checkFloor();
		if (deltaTime > 1) handleTouch(entityList);
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);
	}

	void updateVelocity(List<Rectangle> rectangleList, List<Entity> entityList){
		velocity.x = velX;
		velocity.y = velY;
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

	public void reflect(Fighter reflector){
		reverse();
		if (null != owner) owner = reflector;
		life.restart();
		velX *= 1.6f;
	}


	public Fighter getUser() {
		return owner;
	}

	abstract void touchOtherProjectile(Projectile p);

	public abstract static class Explosive extends Projectile{
		boolean exploded = false;

		public Explosive(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if ( (ac.hitAnybody() || 
					life.timeUp()) && !exploded) explode();
		}

		void explode(){
			MapHandler.addEntity(getExplosion());
			exploded = true;
			life.end();
		}

		void touchOtherProjectile(Projectile p){
			if (!p.trans) explode();
		}

		protected abstract Projectile getExplosion();

	}

	public static class Stunner extends Projectile{
		private final int lifeTime = 60;

		public Stunner(float posX, float posY, Fighter owner) {
			super(posX, posY + 30, owner);
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/ripwheel.png", lifeTime, 12, 0);
			ac = new ProjectileHitbox(owner, 1, 0, 20, 0, 0, 0, 12, new SFX.SharpHit(), this, lifeTime);
			ac.setProperty(Property.STUN);
			MapHandler.addActionCircle(ac);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (ac.hitAnybody()) life.end();
		}

		void touchOtherProjectile(Projectile p){
			if (!p.trans) life.moveCounterForward(20);
		}

	}

	public static class Rocket extends Explosive{

		private final int lifeTime = 60;
		private final int ownerDirect;

		public Rocket(float posX, float posY, Fighter owner) {
			super(posX, posY + 24, owner);
			ownerDirect = owner.direct();
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/rocket.png", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 0, 0, 10, Hitbox.SAMURAI, 0, 0, 16, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
			velX = owner.direct() * 10;
			if (owner.isHoldUp()) velX = 1;
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			velX *= 0.985f;
			if (deltaTime % 10 == 0) MapHandler.addEntity(new Graphic.SmokeTrail(this, 8));
		}

		public boolean doesCollide(float x, float y){
			if (super.doesCollide(x, y)) explode();
			return super.doesCollide(x, y);
		}

		protected Projectile getExplosion() {
			return new RocketExplosion(owner, this, ownerDirect);
		}
	}

	private static class RocketExplosion extends Projectile{

		private final int lifeTime = 12;
		private final int displacement = 40;

		public RocketExplosion(Fighter owner, Projectile rocket, int ownerDirect) {
			super(0, 0, owner);
			new SFX.Explode().play();
			int facingOffset = 40 * ownerDirect;
			position.x = rocket.position.x - displacement;
			position.y = rocket.position.y - displacement;
			setup("sprites/entities/explosion.png", lifeTime, 0, 0);
			ac = 		 new ProjectileHitbox(null, 11.0f, 4.5f, 20, Hitbox.SAMURAI, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ProjectileHitbox(null,  8.0f, 3.5f, 12, Hitbox.SAMURAI, facingOffset, 0, 60, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac3 = new ProjectileHitbox(null,  5.0f, 1.5f, 0, 10, facingOffset*2, 0, 120, new SFX.LightHit(), this, lifeTime);
			ac.setRefresh(2);
			ac2.setRefresh(4);
			ac.setHitstunType(HitstunType.SUPER);
			new ActionCircleGroup(Arrays.asList(ac, ac2, ac3));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
			MapHandler.addActionCircle(ac3);
		}

		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}

	public static class Grenade extends Projectile{
		private final int lifeTime = 90;
		boolean exploded = false;
		private final Timer bounceTimer = new Timer(20);

		public Grenade(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			setup("sprites/entities/grenade.png", lifeTime, 0, 0);
			ac = new ProjectileHitbox(owner, 2.0f, 0.1f, 6, Hitbox.SAMURAI, 0, 0, 12, new SFX.LightHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
			float additionalSpeed = 0;
			if (Math.signum(owner.getStickX()) == owner.direct()) additionalSpeed = (owner.getStickX() * 4);
			velocity.x = owner.direct() * 6 + additionalSpeed;
			velocity.y = 6 - owner.getStickY() * 4;
			airFriction = 0.993f;
			friction = 0.97f;
			gravity = -0.42f;
			fallSpeed = -10f;
			timerList.add(bounceTimer);
		}

		private final int hitSetTimer = 8;
		void updateVelocity(List<Rectangle> rectangleList, List<Entity> entityList){
			for (Entity en: entityList){
				if (en != this && en != owner && life.getCounter() > 10){
					if (Intersector.overlaps(getImage().getBoundingRectangle(), en.getHurtBox()) && bounceTimer.timeUp() ) {
						velocity.x *= 0.5;
						velocity.y += 3;
						if ((life.getEndTime() - life.getCounter()) > hitSetTimer) {
							life.setEndTime(hitSetTimer);
							life.restart();
						}
						bounceTimer.restart();
					}
				}
			}
			super.limitingForces(rectangleList, entityList);
		}

		void checkWalls(){
			double horizontalBounceMod = -0.9;
			if (doesCollide(position.x + velocity.x, position.y)) velocity.x *= horizontalBounceMod;
		}

		protected double verticalBounceMod = -0.6;
		void checkFloor(){
			if (doesCollide(position.x, position.y + velocity.y)) velocity.y *= verticalBounceMod;
		}

		void touchOtherProjectile(Projectile p){
			if (!p.trans) reverse();
		}

		public boolean doesCollide(float x, float y){
			if (collision == Collision.GHOST) return false;
			for (Rectangle r : tempRectangleList){
				Rectangle thisR = getCollisionBox(x, y);
				boolean upThroughThinPlatform = r.getHeight() <= 1 && r.getY() - this.getPosition().y > 0;
				if (!upThroughThinPlatform && Intersector.overlaps(thisR, r) && thisR != r) return true;
			}
			return false;
		}

		protected Projectile getExplosion() {
			return new GrenadeExplosion(owner, this);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			if (life.timeUp() && !exploded){
				MapHandler.addEntity(getExplosion());
				exploded = true;
				life.end();
			}
			super.update(rectangleList, entityList, deltaTime);
		}

	}
	
	public static class DownwardGrenade extends Grenade{

		public DownwardGrenade(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
			velocity.x = 1;
			velocity.y = -6;
			verticalBounceMod = -0.1;
		}
		
	}

	private static class GrenadeExplosion extends Projectile{

		private final int lifeTime = 12;
		private final int displacement = 20;

		public GrenadeExplosion(Fighter owner, Projectile grenade) {
			super(0, 0, owner);
			new SFX.Explode().play();
			position.x = grenade.position.x - displacement;
			position.y = grenade.position.y - displacement;
			setup("sprites/entities/grenadeexplosion.png", lifeTime, 0, 0);
			ac =  new ProjectileHitbox(null, 8, 5, 8, 71, 0, 0, 36, new SFX.HeavyHit(), this, lifeTime);
			ac.setRefresh(1);
			Hitbox 
			ac2 = new ProjectileHitbox(null, 7, 4, 24, Hitbox.SAMURAI, 0, 0, 48, new SFX.HeavyHit(), this, lifeTime);
			new ActionCircleGroup(Arrays.asList(ac, ac2));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}

		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}

	public static class Dumpling extends Projectile{
		private final int lifeTime = 160;

		public Dumpling(float posX, float posY, Fighter owner) {
			super(posX, posY + 30, owner);
			if (owner.direction == Entity.Direction.RIGHT) position.x += owner.getImage().getWidth();
			setup("sprites/entities/dumpling.png", lifeTime, 6, 0);
			ac = new ProjectileHitbox(owner, 7, 2, 16, 100, 0, 0, 30, new SFX.HeavyHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
		}

		void touchOtherProjectile(Projectile p){
			if (!p.trans) p.life.end();
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
			ac = new ProjectileHitbox(owner, 2.4f, 1.8f, 8, Hitbox.SAMURAI, 0, 0, 12, new SFX.MidHit(), this, lifeTime);
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

		public boolean doesCollide(float x, float y){
			if (super.doesCollide(x, y)) explode();
			return super.doesCollide(x, y);
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
			ac = new ProjectileHitbox(null, 6, 2, 20, Hitbox.SAMURAI, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ProjectileHitbox(null, 5, 1, 10, Hitbox.SAMURAI, 0, 0, 80, new SFX.HeavyHit(), this, lifeTime);
			ac.setProperty(Hitbox.Property.ELECTRIC);
			ac2.setProperty(Hitbox.Property.ELECTRIC);
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}

		void touchOtherProjectile(Projectile p){
			/* nothing */
		}
	}
	
	public static class AcidSpit extends Projectile{

		final int lifeTime = 120;
		float gravity = -0.16f;

		public AcidSpit(float posX, float posY, Fighter owner) {
			super(posX, posY + 16, owner);
			new SFX.LaserFire().play();
			set();
		}
		
		protected void set(){
			setup("sprites/entities/acid.png", lifeTime, 7, 4);
			ac = new ProjectileHitbox(owner, 0.1f, 0.1f, 16, 70, 0, 0, 8, new SFX.MidHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			velY += gravity;
			if (ac.hitAnybody()) life.end();
		}

		void touchOtherProjectile(Projectile p){
			if (!p.trans) life.end();
		}
	}
	
	public static class HyperAcidSpit extends AcidSpit{

		public HyperAcidSpit(float posX, float posY, Fighter owner) {
			super(posX, posY, owner);
		}
		
		protected void set(){
			gravity = -0.12f;
			setup("sprites/entities/hyperacid.png", lifeTime, 11, 2);
			ac = new ProjectileHitbox(owner, 0.5f, 0.5f, 24, 70, 0, 0, 10, new SFX.MidHit(), this, lifeTime);
			MapHandler.addActionCircle(ac);
		}

	}

}
