package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;

public class InputHandler implements InputProcessor, ControllerListener {
	
	static Controller controller;
	static CtlType ctlType;
	Fighter player;
	
	void begin(Fighter player){
		this.player = player;
		if (!setupController()) ctlType = CtlType.KEYBOARD;
	}
	
	private boolean setupController(){
		int numControllers = Controllers.getControllers().size;
		if (numControllers < 1) return false;
		Controllers.addListener(this);
		controller = Controllers.getControllers().first();
		ctlType = CtlType.CONTROLLER;
		return true;
	}
	
	void updatePlayer(Fighter player) {
		boolean jump;
		if (ctlType == CtlType.CONTROLLER) {
			jump = controller.getButton(Fighter.commandJumpX);
			if (!jump) jump = controller.getButton(Fighter.commandJumpY);
		}
		else {
			handleKeyboard(player);
			jump = (Gdx.input.isKeyPressed(Keys.W));
		}
		player.handleJumpCommand(jump);
	}	
	
	private void handleKeyboard(Fighter player){
		if (Gdx.input.isKeyJustPressed(Keys.W)) player.handleCommand(Fighter.commandJumpX);
	}
	
	public float getXInput(){ return getInput(1, Gdx.input.isKeyPressed(Keys.D), Gdx.input.isKeyPressed(Keys.A)); }
	
	public float getYInput(){ return getInput(0, Gdx.input.isKeyPressed(Keys.S), Gdx.input.isKeyPressed(Keys.W)); }
	
	private float getInput(int axis, boolean inputA, boolean inputB){
		if (ctlType == CtlType.CONTROLLER) return controller.getAxis(axis);
		if (inputA) return 1;
		else if (inputB) return -1;
		else return 0;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		player.handleCommand(buttonCode);
		return true;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) { 
		return false; 
	}
	
	public enum CtlType { CONTROLLER, KEYBOARD }
	
	/* USELESS METHODS */

	@Override public void connected(Controller controller) { }
	@Override public void disconnected(Controller controller) { }
	@Override public boolean buttonUp(Controller controller, int buttonCode) { return false; }
	@Override public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
	@Override public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	@Override public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	@Override public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }
	@Override public boolean keyDown(int keycode) { return false; }
	@Override public boolean keyUp(int keycode) { return false; }
	@Override public boolean keyTyped(char character) { return false; }
	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	@Override public boolean mouseMoved(int screenX, int screenY) { return false; }
	@Override public boolean scrolled(int amount) { return false; }

}
