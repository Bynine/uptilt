package entities;

import movelists.Heavy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class F_Heavy extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/stand.png")));

	public F_Heavy(float posX, float posY, int team) {
		super(posX, posY, team);
		weight = 200;
		gravity = -0.4f;
		friction = 0.7f;
		armor = 2.5f;
		jumpSquatTimer.setEndTime(10);
		moveList = new Heavy(this);
	}

	TextureRegion getJumpFrame(float deltaTime) { return texture; }
	TextureRegion getStandFrame(float deltaTime) { return texture; }
	TextureRegion getWalkFrame(float deltaTime) { return texture; }
	TextureRegion getRunFrame(float deltaTime) { return texture; }
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
