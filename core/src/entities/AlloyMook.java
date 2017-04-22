package entities;

import com.badlogic.gdx.graphics.Color;

public class AlloyMook extends Mook {

	public AlloyMook(float posX, float posY, int team) {
		super(posX, posY, team);
		gravity = -0.75f;
		weight = 140;
		doubleJumpStrength = 11f;
		jumpAcc = 0.82f;
	}
	
	public Color getColor() { return new Color(0.3f, 0.5f, 0.6f, 1); }

}