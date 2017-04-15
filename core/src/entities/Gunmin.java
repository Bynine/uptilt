package entities;

import moves.MoveList_Gunmin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gunmin extends Fighter {
	
	private TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/stand.png")));
	private TextureRegion walk = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/walk.png")));
	private TextureRegion run = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/run.png")));
	private TextureRegion crouch = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/crouch.png")));
	private TextureRegion jump = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/laser/jump.png")));

	public Gunmin(float posX, float posY, int team) {
		super(posX, posY, team);
		walkAcc = 0.1f;
		runAcc = 0.15f;
		airAcc = 0.1f;
		gravity = -0.32f;
		weight = 55;
		moveList = new MoveList_Gunmin(this);
		jumpSquatTimer.setEndTime(7);
	}

	TextureRegion getJumpFrame(float deltaTime) { return jump; }
	TextureRegion getStandFrame(float deltaTime) { return stand; }
	TextureRegion getWalkFrame(float deltaTime) { return walk; }
	TextureRegion getRunFrame(float deltaTime) { return run; }
	TextureRegion getWallSlideFrame(float deltaTime) { return jump; }
	TextureRegion getHelplessFrame(float deltaTime) { return jump; }
	TextureRegion getGrabFrame(float deltaTime) { return walk; }
	TextureRegion getFallFrame(float deltaTime) { return jump; }
	TextureRegion getAscendFrame(float deltaTime) { return jump; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouch; }
	TextureRegion getDashFrame(float deltaTime) { return run; }
	TextureRegion getDodgeFrame(float deltaTime) { return run; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return crouch; }

}
