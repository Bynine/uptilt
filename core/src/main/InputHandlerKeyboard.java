package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class InputHandlerKeyboard extends InputHandler implements InputProcessor{

	@Override
	public void update() {
		super.update();
		if (Gdx.input.isKeyJustPressed(Keys.I)) handleCommand(commandJumpX);
		if (Gdx.input.isKeyJustPressed(Keys.J)) handleCommand(commandAttack);
		if (Gdx.input.isKeyJustPressed(Keys.K)) handleCommand(commandSpecial);
		if (Gdx.input.isKeyJustPressed(Keys.L)) handleCommand(commandGrab);
		if (Gdx.input.isKeyJustPressed(Keys.T)) handleCommand(commandCUp);
		if (Gdx.input.isKeyJustPressed(Keys.F)) handleCommand(commandCLeft);
		if (Gdx.input.isKeyJustPressed(Keys.G)) handleCommand(commandCDown);
		if (Gdx.input.isKeyJustPressed(Keys.H)) handleCommand(commandCRight);
		boolean jump = (Gdx.input.isKeyPressed(Keys.I));
		player.handleJumpCommand(jump);
	}

	@Override
	public boolean isCharging() {
		return Gdx.input.isKeyPressed(Keys.T) || Gdx.input.isKeyPressed(Keys.F) || Gdx.input.isKeyPressed(Keys.G) || Gdx.input.isKeyPressed(Keys.H);
	}
	
	@Override
	public float getXInput() {
		if (Gdx.input.isKeyPressed(Keys.D)) return  1;
		if (Gdx.input.isKeyPressed(Keys.A)) return -1;
		return 0;
	}

	@Override
	public float getYInput() {
		if (Gdx.input.isKeyPressed(Keys.S)) return  1;
		if (Gdx.input.isKeyPressed(Keys.W)) return -1;
		return 0;
	}
	
	/* NOT USED */

	@Override public boolean keyDown(int keycode) { return false; }
	@Override public boolean keyUp(int keycode) { return false; }
	@Override public boolean keyTyped(char character) { return false; }
	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	@Override public boolean mouseMoved(int screenX, int screenY) { return false; }
	@Override public boolean scrolled(int amount) { return false; }
	
}
