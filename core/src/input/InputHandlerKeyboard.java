package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import entities.Fighter;

public class InputHandlerKeyboard extends InputHandler implements InputProcessor{

	public InputHandlerKeyboard(Fighter player) {
		super(player);
	}

	@Override
	public void update() {
		super.update();
		if (Gdx.input.isKeyJustPressed(Keys.J)) handleCommand(commandJump);
		if (Gdx.input.isKeyJustPressed(Keys.M)) handleCommand(commandAttack);
		if (Gdx.input.isKeyJustPressed(Keys.A)) handleCommand(commandStickLeft);
		if (Gdx.input.isKeyJustPressed(Keys.D)) handleCommand(commandStickRight);
		if (Gdx.input.isKeyJustPressed(Keys.K)) handleCommand(commandCharge);
		if (Gdx.input.isKeyJustPressed(Keys.N)) handleCommand(commandSpecial);
		if (Gdx.input.isKeyJustPressed(Keys.I)) handleCommand(commandGrab);
		if (Gdx.input.isKeyJustPressed(Keys.O)) handleCommand(commandBlock);
		boolean jump = (Gdx.input.isKeyPressed(Keys.J));
		player.handleJumpCommand(jump);
		boolean block = (Gdx.input.isKeyPressed(Keys.O));
		player.handleBlockCommand(block);
	}

	@Override
	public boolean isCharging() {
		return Gdx.input.isKeyPressed(Keys.K);
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
