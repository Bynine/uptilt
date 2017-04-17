package entities;

import com.badlogic.gdx.graphics.Color;

public class MetalMook extends Mook {

	public MetalMook(float posX, float posY, int team) {
		super(posX, posY, team);
		gravity = -0.63f;
		weight = 140;
		armor = 4;
		walkSpeed = 2f;
		runSpeed = 4f;
		jumpSquatTimer.setEndTime(8);
	}
	
	public Color getColor() { return new Color(0.3f, 0.5f, 0.6f, 1); }

}
