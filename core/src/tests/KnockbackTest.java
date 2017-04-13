package tests;

import moves.Hitbox;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

import entities.*;

public class KnockbackTest {
	
	float missileX = 1, missileY = 1;

	@Test
	public void test() {
		Vector2 knockIntoVector = new Vector2(missileX, missileY);
		Hitbox h = new Hitbox(null, 1 * 3, 1.5f, Fighter.knockbackIntensity(knockIntoVector), 
				knockIntoVector.angle(), 0, 0, 0, null);
		//knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
		knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
		System.out.println(knockIntoVector);
	}

}
