package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import entities.Fighter;

public class InputHandlerKeyboard extends InputHandlerPlayer implements InputProcessor{

	public InputHandlerKeyboard(Fighter player) {
		super(player);
	}
	
	public float getXInput() {
		if (Gdx.input.isKeyPressed(Keys.D)) return  1;
		if (Gdx.input.isKeyPressed(Keys.A)) return -1;
		return 0;
	}

	public float getYInput() {
		if (Gdx.input.isKeyPressed(Keys.S)) return  1;
		if (Gdx.input.isKeyPressed(Keys.W)) return -1;
		return 0;
	}
	
	boolean attack(){
		return Gdx.input.isKeyJustPressed(Keys.M);
	}
	
	boolean special(){
		return Gdx.input.isKeyJustPressed(Keys.N);
	}
	
	private int chargeInput = Keys.K;
	boolean charge(){
		return Gdx.input.isKeyJustPressed(chargeInput);
	}
	
	boolean chargeHold(){
		return Gdx.input.isKeyPressed(chargeInput);
	}
	
	private int jumpInput = Keys.J;
	boolean jump(){
		return Gdx.input.isKeyJustPressed(jumpInput);
	}
	
	boolean jumpHold(){
		return Gdx.input.isKeyPressed(jumpInput);
	}
	
	boolean grab(){
		return Gdx.input.isKeyJustPressed(Keys.I);
	}
	
	private int blockInput = Keys.O;
	boolean block(){
		return Gdx.input.isKeyJustPressed(blockInput);
	}
	
	boolean blockHold(){
		return Gdx.input.isKeyPressed(blockInput);
	}
	
	boolean taunt(){
		return Gdx.input.isKeyJustPressed(Keys.L);
	}
	
	boolean flickLeft(){
		return Gdx.input.isKeyJustPressed(Keys.A);
	}
	
	boolean flickRight(){
		return Gdx.input.isKeyJustPressed(Keys.D);
	}
	
	boolean flickUp(){
		return Gdx.input.isKeyJustPressed(Keys.W);
	}
	
	boolean flickDown(){
		return Gdx.input.isKeyJustPressed(Keys.S);
	}
	
	boolean pause(){
		return Gdx.input.isKeyJustPressed(Keys.ENTER);
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
