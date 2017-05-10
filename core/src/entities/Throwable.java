package entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public abstract class Throwable extends Hittable {
	
	float staticPercent = 50;

	public Throwable(float posX, float posY) {
		super(posX, posY);
		hitstunMod = 8;
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);
		if (isGrounded() && !inGroundedState()) ground();
		else if (!isGrounded()) state = State.FALL;
	}
	
	public void ground(){
		super.ground();
		state = State.STAND;
		hitstunTimer.end();
	}
}
