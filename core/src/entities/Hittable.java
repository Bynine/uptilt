package entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import moves.Hitbox;
import timers.Timer;

public abstract class Hittable extends Entity {
	
	protected TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));
	float percentage = 0, armor = 0, weight = 100;
	boolean tumbling = false, slowed = true;
	final Timer caughtTimer = new Timer(0), knockIntoTimer = new Timer(20), stunTimer = new Timer(0);
	float hitstunMod = 1, powerMod = 1, damageMod = 1, initialHitAngle = 0;
	int baseHitstun = 0;
	HitstunType hitstunType = HitstunType.NORMAL;

	public Hittable(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(defaultTexture);
		timerList.addAll(Arrays.asList(caughtTimer, knockIntoTimer, stunTimer));
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);
	}
	
	void updateImage(float deltaTime){
		if (!hitstunTimer.timeUp()) setImage(getTumbleFrame(deltaTime));
		else setImage(getStandFrame(deltaTime));
	}
	
	void limitSpeeds(){
		boolean notAMeteor = initialHitAngle > 0 && initialHitAngle < 180;
		float gravFallSpeed = fallSpeed * MapHandler.getRoomGravity();
		if ( (hitstunTimer.timeUp() || notAMeteor) && velocity.y < gravFallSpeed) velocity.y = gravFallSpeed;
	}
	
	void updatePosition(){
		if (canMove()) super.updatePosition();
	}
	
	protected float baseHurtleBK = 4;
	void handleTouchHelper(Entity e){
		if (e instanceof Hittable){
			Hittable hi = (Hittable) e;
			int pushDistance = 16 + 2 * ((int) image.getWidth() - defaultTexture.getRegionWidth());
			boolean toPush = isTouching(hi, pushDistance) && Math.abs(hi.velocity.x) < 1 && Math.abs(this.velocity.x) < 1 && e.isGrounded() && isGrounded();
			if (getTeam() == hi.getTeam()) toPush = isTouching(hi, 0);
			if (toPush) pushAway(hi);
			boolean fighterGoingFastEnough = knockbackIntensity(hi.velocity) > baseHurtleBK;
			if (hi.hitstunType != HitstunType.NORMAL) fighterGoingFastEnough = true;
			boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && getTeam() == hi.getTeam() && !hi.hitstunTimer.timeUp();
			if (knockInto && isTouching(hi, 8) && knockbackIntensity(hi.velocity) > knockbackIntensity(velocity)) getHitByHurtlingObject(hi);
		}
	}
	
	private final float pushForce = 0.04f;
	protected void pushAway(Entity e){
		float dirPush = Math.signum(e.position.x - this.position.x);
		velocity.x -= dirPush * pushForce;
		e.velocity.x += dirPush * pushForce;
	}

	protected float hitSpeedMod = 0.6f;
	public void getHitByHurtlingObject(Hittable knocker){ // heheheh
		Vector2 knockIntoVector = new Vector2(knocker.velocity.x, knocker.velocity.y);
		float bkb = knockbackIntensity(knockIntoVector);
		float kbg = 0.5f;
		float dam = knockbackIntensity(knockIntoVector) * knocker.damageMod;
		Hitbox h;
		if (knocker.hitstunType != HitstunType.NORMAL) {
			if (knocker.hitstunType == HitstunType.ULTRA){ // ultra hitstun
				bkb *= 1.05f;
				h = new Hitbox(knocker, bkb, kbg, dam * 2, knockIntoVector.angle(), 0, 0, 0, null);
			}
			else{ // super hitstun
				bkb *= .7f;
				h = new Hitbox(knocker, bkb, kbg, dam * 3, knockIntoVector.angle(), 0, 0, 0, null);
			}
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			float newAngle = h.getAngle();
			knockIntoVector.setAngle(newAngle);
		}
		else { // normal hitstun
			bkb *= .3f;
			h = new Hitbox(knocker, bkb, kbg, dam, knockIntoVector.angle(), 0, 0, 0, null);
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
		}
		SFX.proportionalHit(h.getDamage()).play();
		takeKnockIntoKnockback(knockIntoVector, h.getDamage() / 2, (int) h.getDamage() + baseHitstun );
		knocker.knockIntoTimer.restart();
		knockIntoTimer.restart();
		knocker.velocity.set(knocker.velocity.x * knocker.hitSpeedMod, knocker.velocity.y * knocker.hitSpeedMod);
	}
	
	private void takeKnockIntoKnockback(Vector2 knockback, float DAM, int hitstun){
		knockbackHelper(knockback, DAM, hitstun, knockbackIntensity(velocity) < knockbackIntensity(knockback), HitstunType.NORMAL);
	}

	public void takeDamagingKnockback(Vector2 knockback, float DAM, int hitstun, HitstunType hitboxhitstunType, Hittable user) {
		if (null != user && this instanceof Fighter) user.dealDamage(DAM);
		knockbackHelper(knockback, DAM, hitstun, true, hitboxhitstunType);
	}

	public void takeRecoil(Vector2 knockback){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		velocity.add(knockback);
	}

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean tryy, HitstunType ht){
		takeDamage(DAM);
		if (knockbackIntensity(knockback) > 0) takeKnockback(knockback, hitstun, tryy, ht);
		if (knockbackIntensity(knockback) > tumbleBK) tumbling = true;
	}
	
	protected void takeDamage(float DAM){
		percentage += DAM;
	}
	
	protected void dealDamage(float DAM){
		/* */
	}
	
	protected void takeKnockback(Vector2 knockback, int hitstun, boolean tryy, HitstunType ht){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		if (tryy) velocity.set(knockback);
		initialHitAngle = knockback.angle();
		if (state == State.HELPLESS) state = State.FALL;
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.restart();
		endAttack();
		hitstunType = ht;
		stunTimer.end();
	}

	protected float directionalInfluenceAngle(Vector2 knockback){
		return knockback.angle();
	}

	public void getGrabbed(Fighter user, Hittable target, int caughtTime) {
		float heldDistance = 24;
		user.isNowGrabbing(target, caughtTime);
		float newPosX = user.position.x + (heldDistance * user.direct());
		float newPosY = user.position.y + image.getHeight()/4;
		if (!doesCollide(newPosX, newPosY)) position.set(newPosX, newPosY);
		else if (!doesCollide(position.x, newPosY)) position.set(position.x, newPosY);
		caughtTimer.setEndTime(caughtTime);
		caughtTimer.restart();
		endAttack();
	}
	
	public void stun(int duration) {
		stunTimer.setEndTime(duration);
		stunTimer.restart();
	}
	
	public void endAttack(){
		/**/
	}
	
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0; }
	
	void handleWind(){
		velocity.x += MapHandler.getRoomWind() * (weight/100);
	}

	public float getArmor() { 
		return armor;
	}
	
	public void setPercentage(float perc){
		percentage = perc;
	}

	public float getPercentage() { 
		return percentage; 
	}
	
	public float getWeight() { 
		return weight;
	}
	
	public float getHitstunMod(){
		return hitstunMod;
	}
	
	public float getPowerMod(){
		return powerMod;
	}
	
	public int getTeam() {
		return GlobalRepo.BADTEAM; 
	}
	
	public boolean canMove(){
		return stunTimer.timeUp() && caughtTimer.timeUp();
	}
	
	abstract TextureRegion getStandFrame(float deltaTime);
	abstract TextureRegion getTumbleFrame(float deltaTime);
	
	public enum HitstunType{ NORMAL, SUPER, ULTRA }

}
