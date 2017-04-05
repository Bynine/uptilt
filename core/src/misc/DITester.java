package misc;

import com.badlogic.gdx.math.Vector2;

public class DITester {

	public static void main(String[] args){
		System.out.println(directionalInfluenceAngle(new Vector2(0, 1), -0.5f, 1));
	}
	
	static float directionalInfluenceAngle(Vector2 knockback, float x, float y){
		Vector2 di = new Vector2(x, y);
		float parallelAngle = Math.round(knockback.angle() - di.angle());
		double sin = Math.sin(parallelAngle * Math.PI/180);
		int signMod = 1;
		if (knockback.angle() > 90 && knockback.angle() <= 270) signMod *= -1;
		if (di.x < 0) signMod *= -1;
		double diModifier = signMod * Math.signum(180 - di.angle()) * 18 * Math.pow(sin, 2);
		knockback.setAngle((float) (knockback.angle() + diModifier));
		return knockback.angle();
	}
}
