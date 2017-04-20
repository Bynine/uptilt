package entities;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class F_Speedy extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/speedy/stand.png")));
	private Animation run = GlobalRepo.makeAnimation("sprites/fighters/speedy/run.png", 3, 1, 4, PlayMode.LOOP);

	public F_Speedy(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 1.8f;
		runSpeed = 8.6f;
		walkAcc = 0.82f;
		walkSpeed = 4.9f;
		friction = 0.82f;
		gravity = -0.52f;
		jumpAcc = 0.92f;
		dashStrength = 4f;
		doubleJumpStrength = 9.2f;
		fallSpeed = -8f;
	}
	
	TextureRegion getJumpFrame(float deltaTime) { return texture; }
	TextureRegion getStandFrame(float deltaTime) { return texture; }
	TextureRegion getWalkFrame(float deltaTime) { return run.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return run.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return texture; }
	TextureRegion getHelplessFrame(float deltaTime) { return texture; }
	TextureRegion getGrabFrame(float deltaTime) { return texture; }
	TextureRegion getFallFrame(float deltaTime) { return texture; }
	TextureRegion getAscendFrame(float deltaTime) { return texture; }
	TextureRegion getCrouchFrame(float deltaTime) { return texture; }
	TextureRegion getDashFrame(float deltaTime) { return texture; }
	TextureRegion getDodgeFrame(float deltaTime) { return texture; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return texture; }
	TextureRegion getTumbleFrame(float deltaTime) { return texture; }
	TextureRegion getHitstunFrame(float deltaTime) { return texture; }
	TextureRegion getFallenFrame(float deltaTime) { return texture; }

}
