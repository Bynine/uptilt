package entities;

import moves.MoveList_Gunmin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gunmin extends Fighter {
	
	private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/stand.png")));

	public Gunmin(float posX, float posY, int team) {
		super(posX, posY, team);
		walkAcc = 0.1f;
		runAcc = 0.15f;
		gravity = -0.32f;
		weight = 70;
		moveList = new MoveList_Gunmin(this);
		jumpSquatTimer.setEndTime(7);
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

}
