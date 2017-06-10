package entities;

import java.util.List;

import main.GlobalRepo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class FallingEnemy extends Entity {
	
	private Animation anim = GlobalRepo.makeAnimation("sprites/graphics/enemyfall.png", 4, 1, 6, PlayMode.LOOP);

	public FallingEnemy(float posX, float posY) {
		super(posX, posY);
		layer = Layer.BACKGROUND;
		collision = Collision.GHOST;
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		updatePosition();
		velocity.y = -3;
		setImage(anim.getKeyFrame(deltaTime));
	}

}
