package moves;

import main.MapHandler;
import main.SFX;
import main.UptiltEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import entities.Entity.State;
import entities.Fighter;
import entities.Graphic;

public class Hitbox extends ActionCircle{
	public static final int SAMURAI = 361;
	
	protected float BKB, KBG;
	protected float DAM;
	protected float ANG;
	protected float heldCharge = 1;
	protected final Effect.Charge charge;
	protected final SFX sfx;

	/**
	 * @param User (this)
	 * @param BaseKnockback
	 * @param KnockbackGrowth
	 * @param Damage
	 * @param Angle
	 * @param HDisplacement
	 * @param VDisplacement
	 * @param Size
	 */
	public Hitbox(Fighter user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY, int size, SFX sfx){
		super(user, dispX, dispY, size);
		this.BKB = BKB;
		this.KBG = KBG;
		this.DAM = DAM;
		this.ANG = ANG;
		this.sfx = sfx;
		charge = null;
	}

	/**
	 * @param User (this)
	 * @param BaseKnockback
	 * @param KnockbackGrowth
	 * @param Damage
	 * @param Angle
	 * @param HDisplacement
	 * @param VDisplacement
	 * @param Size
	 * @param Charge
	 */
	public Hitbox(Fighter user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY, int size, SFX sfx, Effect.Charge charge){
		super(user, dispX, dispY, size);
		this.BKB = BKB;
		this.KBG = KBG;
		this.DAM = DAM;
		this.ANG = ANG;
		this.sfx = sfx;
		this.charge = charge;
	}

	private final int meteorAngleSize = 50;
	private final int downAngle = 270;
	private final float meteorHitstunMod = 1.25f;
	private final float meteorGroundMod = -0.75f;
	public void hitTarget(Fighter target){
		if (!didHitTarget(target)) return;

		refreshTimer.restart();
		float staleness = 1;
		if (null != user) {
			staleness = getStaleness(user);
			DAM *= user.getPowerMod();
			BKB *= user.getPowerMod();
			KBG *= user.getPowerMod();
		}
		Vector2 knockback = new Vector2();
		if (null != charge) heldCharge = charge.getHeldCharge();
		knockback.set(knockbackFormula(target) * staleness, knockbackFormula(target) * staleness);
		if (ANG == SAMURAI) setSamuraiAngle(target, knockback);
		else knockback.setAngle(ANG);
		knockback.x *= applyReverseHitbox(target);
		if (knockbackFormula(target) > 8 && null != user) user.takeRecoil(recoilFormula(knockback, target));
		int hitstun = hitstunFormula( target, knockbackFormula(target) );
		boolean groundedMeteor = target.isGrounded() && ((downAngle + meteorAngleSize) > knockback.angle() && knockback.angle() > (downAngle - meteorAngleSize));
		if (groundedMeteor){
			knockback.y *= meteorGroundMod;
			hitstun *= meteorHitstunMod;
		}
		target.takeDamagingKnockback(knockback, heldCharge * DAM * staleness, hitstun, hitstunType);
		if (property == Property.STUN){
			target.stun((int) (DAM * 6));
		}
		startHitlag(target);
		MapHandler.addEntity(new Graphic.HitGraphic(area.x + area.radius/2, area.y + area.radius/2, hitlagFormula(knockbackFormula(target))));
		sfx.play();
		hitFighterList.add(target);
	}
	
	float staleMod = 0.9f;
	private float getStaleness(Fighter user) {
		IDMove currMove = user.getActiveMove();
		if (null == currMove) return 1;
		float staleness = 1/staleMod;
		for (IDMove im: user.getMoveQueue()){
			if (im.id == currMove.id) staleness *= staleMod;
		}
		return staleness;
	}

	private Vector2 recoilFormula(Vector2 knockback, Fighter target) {
		float recoil = -knockback.x/4;
		recoil *= (target.getWeight()/100);
		Vector2 recoilVector = new Vector2(recoil, 0);
		return recoilVector;
	}

	void handlePerfectBlockingKnockback(){
		/* NOTHING! */
	}

	private final float minSamuraiKnockback = 4;
	private final float samuraiKnockbackAngle = 45;
	protected void setSamuraiAngle(Fighter target, Vector2 knockback){
		if (knockbackFormula(target) < minSamuraiKnockback && target.isGrounded()) knockback.setAngle(0);
		else knockback.setAngle(samuraiKnockbackAngle);
	}

	void startHitlag(Fighter target){
		if (!UptiltEngine.getPlayers().contains(target) && !UptiltEngine.getPlayers().contains(user)) return;
		float hit = knockbackFormula(target);
		if (target.getArmor() > 0) hit += target.getArmor() * 2;
		int hitlag = hitlagFormula(hit);
		UptiltEngine.causeHitlag(hitlag);
	}

	private final int hitlagCap = 20;
	private final float hitlagRatio = 0.5f;
	private final float electricHitlagMultiplier = 1.5f;
	protected int hitlagFormula(float knockback) {
		int hitlag = (int) (knockback * hitlagRatio);
		if (hitlag > hitlagCap) hitlag = hitlagCap;
		if (property == Property.ELECTRIC) hitlag *= electricHitlagMultiplier;
		return hitlag;
	}

	private static final float hitstunRatio = 4f;
	public int hitstunFormula(Fighter target, float knockback){
		if (BKB + KBG == 0) return 0;
		return  2 + (int) (knockback * hitstunRatio * target.getHitstunMod());
	}

	private float crouchCancelMod = .75f;
	private final float kbgMod = 0.032f;
	private final float weightMod = 0.01f;
	private final float minKnockback = 0.25f;
	public float knockbackFormula(Fighter target){
		if (BKB + KBG == 0) return 0;
		float knockback = heldCharge * (BKB + ( (KBG * target.getPercentage() * kbgMod) / (target.getWeight() * weightMod) ));
		knockback -= target.getArmor();
		if (target.getState() == State.CROUCH) knockback *= crouchCancelMod;
		if (knockback < minKnockback) return 0;
		else return knockback;
	}

	public void update(int deltaTime){
		super.update(deltaTime);
		refreshTimer.countUp();
		if (doesRefresh && refreshTimer.timeUp()){
			reset();
			if (group != null) for (ActionCircle ac: group.connectedCircles) ac.reset();
			refreshTimer.restart();
		}
	}

	protected float applyReverseHitbox(Fighter target){
		if (null == user) {
			if (area.x - area.radius/2 > target.getPosition().x) return -1;
			else return 1;
		}
		else if (reverse){
			if (user.getPosition().x > target.getPosition().x) return -1;
			else return 1;
		}
		else return user.direct();
	}

	public enum Property { NORMAL, ELECTRIC, STUN  }
	public float getDamage() { return DAM; }
	public float getAngle() { return ANG; }
	public void setProperty(Property property) { this.property = property; }
	public void setHitstunType(Fighter.HitstunType ht) { hitstunType = ht; }
	public Color getColor() {
		return new Color(1, 0.2f, 0.2f, 0.75f);
	}
	
}
