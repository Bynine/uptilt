package tests;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

public class KnockbackTest {
	
	float heldCharge = 1;
	float BKB = 2;
	float KBG = 6;
	float percent = 200;
	float weight = 700;
	float kbgMod = 0.04f;
	float weightMod = 0.01f;
	float angle = 0;

	@Test
	public void test() {
		float knockback = heldCharge * (BKB + ( (KBG * percent * kbgMod) / (weight * weightMod) ));
		Vector2 vec = new Vector2(knockback, knockback);
		vec.setAngle(angle);
		if (Math.abs(vec.x) < 0.01) vec.x = 0;
		if (Math.abs(vec.y) < 0.01) vec.y = 0;
		System.out.println(vec);
	}

}
