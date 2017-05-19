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
import moves.Equipment;
import moves.Hitbox;
import timers.Timer;

public abstract class Hittable extends Entity {
	
	protected TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));
	protected float percentage = 0;
	protected boolean tumbling = false, slowed = true;
	protected final Timer caughtTimer = new Timer(0), knockIntoTimer = new Timer(20), stunTimer = new Timer(0);
	private float initialHitAngle = 0;
	protected HitstunType hitstunType = HitstunType.NORMAL;
	
	protected float baseHurtleBK = 4;
	protected float baseHitSpeed = 0.6f;
	protected float baseHitstun = 1, basePower = 1, baseKnockIntoDamage = 1, armor = 0, baseWeight = 100;
	protected float walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	protected float jumpStrength = 5f, doubleJumpStrength = 8.5f, dashStrength = 5f;
	protected float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;
	protected float wallJumpStrengthX = 8f, wallJumpStrengthY = 7.2f;
	protected float wallSlideSpeed = -1f;
	
	protected float powerMod = 1, defenseMod = 1, speedMod = 1, airMod = 1, armorAdd = 0;
	protected int hitstunDealtBonus = 0;
	private Equipment equipment = new Equipment.Default();

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
		float gravFallSpeed = getFallSpeed() * MapHandler.getRoomGravity();
		if ( (hitstunTimer.timeUp() || notAMeteor) && velocity.y < gravFallSpeed) velocity.y = gravFallSpeed;
	}
	
	void updatePosition(){
		if (canMove()) super.updatePosition();
	}
	
	void handleTouchHelper(Entity e){
		if (e instanceof Hittable){
			checkPushAway((Hittable) e);
			checkHitByHurtlingObject((Hittable) e);
		}
	}
	
	private void checkPushAway(Hittable hi){
		int pushDistance = 16 + 2 * ((int) image.getWidth() - defaultTexture.getRegionWidth());
		boolean toPush = isTouching(hi, pushDistance) && Math.abs(hi.velocity.x) < 1 && Math.abs(this.velocity.x) < 1;
		if (getTeam() == hi.getTeam()) toPush = isTouching(hi, 0);
		if (toPush) pushAway(hi);
	}
	
	protected void pushAway(Entity e){
		float pushForce = 0.04f;
		float dirPush = Math.signum(e.position.x - this.position.x);
		velocity.x -= dirPush * pushForce;
		e.velocity.x += dirPush * pushForce;
	}
	
	private void checkHitByHurtlingObject(Hittable hi){
		boolean fighterGoingFastEnough = knockbackIntensity(hi.velocity) > baseHurtleBK;
		if (hi.hitstunType != HitstunType.NORMAL) fighterGoingFastEnough = true;
		boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && getTeam() == hi.getTeam() && !hi.hitstunTimer.timeUp();
		if (knockInto && isTouching(hi, 8) && knockbackIntensity(hi.velocity) > knockbackIntensity(velocity)) getHitByHurtlingObject(hi);
	}

	public void getHitByHurtlingObject(Hittable hurtler){ // heheheh
		Vector2 knockIntoVector = new Vector2(hurtler.velocity.x, hurtler.velocity.y);
		float bkb = knockbackIntensity(knockIntoVector);
		float kbg = 0.5f;
		float dam = knockbackIntensity(knockIntoVector) * hurtler.baseKnockIntoDamage;
		Hitbox h;
		if (hurtler.hitstunType != HitstunType.NORMAL) {
			if (hurtler.hitstunType == HitstunType.ULTRA){ // ultra hitstun
				bkb *= 1.05f;
				h = new Hitbox(hurtler, bkb, kbg, dam * 3, knockIntoVector.angle(), 0, 0, 0, null);
			}
			else { // super hitstun
				bkb *= .7f;
				h = new Hitbox(hurtler, bkb, kbg, dam * 2, knockIntoVector.angle(), 0, 0, 0, null);
			}
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			float newAngle = h.getAngle();
			knockIntoVector.setAngle(newAngle);
		}
		else { // normal hitstun
			bkb *= .3f;
			h = new Hitbox(hurtler, bkb, kbg, dam, knockIntoVector.angle(), 0, 0, 0, null);
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
		}
		SFX.proportionalHit(h.getDamage()).play();
		takeKnockIntoKnockback(knockIntoVector, h.getDamage() / 2, (int) h.getDamage() + hitstunDealtBonus );
		hurtler.knockIntoTimer.restart();
		knockIntoTimer.restart();
		hurtler.velocity.set(hurtler.velocity.x * hurtler.baseHitSpeed, hurtler.velocity.y * hurtler.baseHitSpeed);
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

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		takeDamage(DAM);
		if (knockbackIntensity(knockback) > 0) takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		if (knockbackIntensity(knockback) > tumbleBK) tumbling = true;
	}
	
	protected void takeDamage(float DAM){
		percentage += DAM;
	}
	
	protected void dealDamage(float DAM){
		/* */
	}
	
	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		if (shouldChangeKnockback) velocity.set(knockback);
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
	
	public float getPower(){ return basePower * powerMod * equipment.getPowerMod(); }
	public float getWeight() { return baseWeight * defenseMod * equipment.getWeightMod(); }
	public float getArmor() { return armor + defenseMod + equipment.getArmorMod(); }
	public float getDashStrength() { return dashStrength * getSpeedMod(); }
	public float getWalkSpeed() { return walkSpeed * getSpeedMod() * equipment.getSpeedMod() * equipment.getWalkSpeedMod(); }
	public float getWalkAcc() { return walkAcc * getSpeedMod() * equipment.getSpeedMod() * equipment.getWalkAccMod(); }
	public float getRunSpeed() { return runSpeed * getSpeedMod() * equipment.getSpeedMod() * equipment.getRunSpeedMod(); }
	public float getRunAcc() { return runAcc * getSpeedMod() * equipment.getSpeedMod() * equipment.getRunAccMod(); }
	public float getAirSpeed() { return airSpeed * airMod * equipment.getAirSpeedMod(); }
	public float getAirAcc() { return airAcc * airMod * equipment.getAirAccMod(); }
	public float getJumpStrength() { return jumpStrength; }
	public float getJumpAcc() { return jumpAcc * airMod * equipment.getJumpAccMod(); }
	public float getFallSpeed() { return fallSpeed * equipment.getFallSpeedMod(); }
	public float getGravity() { return gravity * equipment.getGravityMod(); }
	public float getFriction() { return (float) Math.pow(friction, equipment.getFrictionMod()); }
	public float getAirFriction() { return (float) Math.pow(airFriction, equipment.getAirFrictionMod()); }
	public float getDoubleJumpStrength() { return doubleJumpStrength * airMod; }
	public float getWallJumpStrengthX() { return wallJumpStrengthX; }
	public float getWallJumpStrengthY() { return wallJumpStrengthY; }
	public float getWallSlideSpeed() { return wallSlideSpeed * equipment.getWallSlideMod(); }
	
	public void addPower(float add){ powerMod += add; }
	public void addDefense(float add){ defenseMod += add; }
	public void addArmor(float add){ armorAdd += add; }
	public void addSpeed(float add){ speedMod += add; }
	public void addAir(float add){ airMod += add; }
	
	private float getSpeedMod(){ return speedMod * equipment.getSpeedMod(); }
	
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0; }
	
	void handleWind(){
		velocity.x += MapHandler.getRoomWind() * (baseWeight/100);
	}
	
	public void setPercentage(float perc){
		percentage = perc;
	}

	public float getPercentage() { 
		return percentage; 
	}
	
	public float getHitstun(){
		return baseHitstun;
	}
	
	public int getTeam() {
		return GlobalRepo.BADTEAM; 
	}
	
	public boolean canMove(){
		return stunTimer.timeUp() && caughtTimer.timeUp();
	}
	
	public void setEquipment(Equipment e){
		equipment = e;
	}
	
	abstract TextureRegion getStandFrame(float deltaTime);
	abstract TextureRegion getTumbleFrame(float deltaTime);
	
	public enum HitstunType{ NORMAL, SUPER, ULTRA }

}
