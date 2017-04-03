package moves;

import com.badlogic.gdx.graphics.Color;

import entities.Fighter;

public class Grabbox extends ActionCircle {

	public Grabbox(Fighter user, float dispX, float dispY, int size) {
		super(user, dispX, dispY, size);
	}

	@Override
	public void hitTarget(Fighter target) {
		if (!didHitTarget(target)) return;
		
		target.setCaught(user, target, caughtTimeFormula(target));
		hit = true;
	}
	
	private final int minGrabTime = 40;
	private int caughtTimeFormula(Fighter target){
		return (int) (minGrabTime + target.getPercentage());
	}

	@Override
	public Color getColor() {
		return new Color(1, 0, 0.8f, 0.75f);
	}

}
