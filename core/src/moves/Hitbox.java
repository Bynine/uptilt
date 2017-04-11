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
	private final float BKB, KBG, DAM, ANG;
	private float heldCharge = 1;
	private final Effect.Charge charge;
	private final SFX sfx;
	public static final int SAMURAIANGLE = 361;

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

	public void setProperty(Property property) { this.property = property; }

	@Override
	public Color getColor() {
		return new Color(1, 0.2f, 0.2f, 0.75f);
	}

	private final int meteorAngleSize = 50;
	private final int downAngle = 270;
	private final float meteorHitstunMod = 1.25f;
	private final float meteorGroundMod = -0.75f;
	public void hitTarget(Fighter target){
		if (!didHitTarget(target)) return;

		refreshTimer.restart();
		Vector2 knockback = new Vector2();
		if (null != charge) heldCharge = charge.getHeldCharge();
		knockback.set(knockbackFormula(target), knockbackFormula(target));
		if (ANG == SAMURAIANGLE) setSamuraiAngle(target, knockback);
		else knockback.setAngle(ANG);
		knockback.x *= applyReverseHitbox(target);
		int hitstun = hitstunFormula( knockbackFormula(target) );
		boolean groundedMeteor = target.isGrounded() && ((downAngle + meteorAngleSize) > knockback.angle() && knockback.angle() > (downAngle - meteorAngleSize));
		if (groundedMeteor){
			knockback.y *= meteorGroundMod;
			hitstun *= meteorHitstunMod;
		}
		if (target.isBlocking()) target.takeKnockback(knockback, heldCharge * DAM /2, hitstun);
		if (target.isPerfectBlocking()) handlePerfectBlockingKnockback();
		else target.takeKnockback(knockback, heldCharge * DAM, hitstun);
		startHitlag(target);
		MapHandler.addEntity(new Graphic.HitGraphic(area.x + area.radius/2, area.y + area.radius/2, hitlagFormula(knockbackFormula(target))));
		sfx.play();
		hitFighterList.add(target);
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
		int hitlag = hitlagFormula(knockbackFormula(target));
		UptiltEngine.causeHitlag(hitlag);
	}

	private final int hitlagCap = 16;
	private final float hitlagRatio = 0.5f;
	private final float electricHitlagMultiplier = 1.5f;
	protected int hitlagFormula(float knockback) {
		int hitlag = (int) (knockback * hitlagRatio);
		if (hitlag > hitlagCap) hitlag = hitlagCap;
		if (property == Property.ELECTRIC) hitlag *= electricHitlagMultiplier;
		return hitlag;
	}

	private static final float hitstunRatio = 4.2f;
	public static int hitstunFormula(float knockback){
		return  2 + (int) (knockback * hitstunRatio);
	}

	private float crouchCancelMod = .75f;
	private float blockMod = 0.5f;
	private float blockDecrement = 3;
	private final float kbgMod = 0.04f;
	private final float weightMod = 0.01f;
	private final float minKnockback = 0.25f;
	protected float knockbackFormula(Fighter target){
		float knockback = heldCharge * (BKB + ( (KBG * target.getPercentage() * kbgMod) / (target.getWeight() * weightMod) ));
		knockback -= target.getArmor();
		if (target.getState() == State.CROUCH) knockback *= crouchCancelMod;
		if (target.isBlocking()) {
			knockback = (knockback * blockMod) - blockDecrement;
		}
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
		else if (user.getPosition().x > target.getPosition().x) return -1;
		else return 1;
	}

	public enum Property { 
		NORMAL, ELECTRIC 
	}
	
}
