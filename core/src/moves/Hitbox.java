package moves;

import main.PlatformerEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import entities.Fighter;

public class Hitbox extends ActionCircle{
	private final float BKB, KBG, DAM, ANG;
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
	public Hitbox(Fighter user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY, int size){
		super(user, dispX, dispY, size);
		this.BKB = BKB;
		this.KBG = KBG;
		this.DAM = DAM;
		this.ANG = ANG;
	}

	public void setProperty(Property property) { this.property = property; }

	private final int meteorAngleSize = 50;
	private final int downAngle = 270;
	private final float meteorHitstunMod = 1.25f;
	private final float meteorGroundMod = -0.75f;
	public void hitTarget(Fighter target){
		if (!didHitTarget(target)) return;
		
		Vector2 knockback = new Vector2();
		knockback.set(knockbackFormula(target), knockbackFormula(target));
		if (ANG == SAMURAIANGLE) setSamuraiAngle(target, knockback);
		else knockback.setAngle(ANG);
		if (user.getPosition().x > target.getPosition().x) knockback.x *= -1;
		int hitstun = hitstunFormula( knockbackFormula(target) );
		if (target.isGrounded() && ((downAngle + meteorAngleSize) > knockback.angle() && knockback.angle() > (downAngle - meteorAngleSize)) ){
			knockback.y *= meteorGroundMod;
			hitstun *= meteorHitstunMod;
		}
		target.takeKnockback(knockback, DAM, hitstun);
		
		PlatformerEngine.causeHitlag( hitlagFormula(knockbackFormula(target)) );
		// TODO: create graphic
		hit = true;
	}
	
	private final float minSamuraiKnockback = 4;
	private final float samuraiKnockbackAngle = 45;
	private void setSamuraiAngle(Fighter target, Vector2 knockback){
		if (knockbackFormula(target) < minSamuraiKnockback && target.isGrounded()) knockback.setAngle(0);
		else knockback.setAngle(samuraiKnockbackAngle);
	}

	private final int hitlagCap = 12;
	private final float hitlagRatio = 0.5f;
	private final float electricHitlagMultiplier = 1.5f;
	private int hitlagFormula(float knockback) {
		int hitlag = (int) (knockback * hitlagRatio);
		if (hitlag > hitlagCap) hitlag = hitlagCap;
		if (property == Property.ELECTRIC) hitlag *= electricHitlagMultiplier;
		return hitlag;
	}

	private final float hitstunRatio = 3.5f;
	private int hitstunFormula(float knockback){
		return  2 + (int) (knockback * hitstunRatio);
	}

	private final float kbgMod = 0.04f;
	private final float weightMod = 0.01f;
	private float knockbackFormula(Fighter target){
		float knockback = BKB + ( (KBG * target.getPercentage() * kbgMod) / (target.getWeight() * weightMod) );
		if (knockback < 0.25f) return 0;
		else return knockback;
	}
	
	@Override
	public Color getColor() {
		return new Color(1, 0.2f, 0.2f, 0.75f);
	}

	public enum Property { NORMAL, ELECTRIC }
}
