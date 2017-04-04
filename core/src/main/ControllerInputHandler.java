package main;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;

public class ControllerInputHandler extends InputHandler implements ControllerListener {

	Controller controller;
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down

	@Override
	void begin(Fighter player){
		super.begin(player);
		this.player = player;
	}

	public void setupController(int index){
		Controllers.addListener(this);
		controller = Controllers.getControllers().get(index);
	}

	@Override
	public void update(Fighter player) {
		boolean jump = controller.getButton(Fighter.commandJumpX);
		if (!jump) jump = controller.getButton(Fighter.commandJumpY);
		if (controller.getAxis(AXIS_RIGHT_X) > minHoldForCharge) player.handleCommand(Fighter.commandCForward);
		if (controller.getAxis(AXIS_RIGHT_X) < -minHoldForCharge) player.handleCommand(Fighter.commandCBack);
		if (controller.getAxis(AXIS_RIGHT_Y) > minHoldForCharge) player.handleCommand(Fighter.commandCDown);
		if (controller.getAxis(AXIS_RIGHT_Y) < -minHoldForCharge) player.handleCommand(Fighter.commandCUp);
		player.handleJumpCommand(jump);
	}

	private final float minHoldForCharge = 0.9f;
	@Override
	public boolean isCharging() {
		return Math.abs(controller.getAxis(AXIS_RIGHT_X)) > minHoldForCharge || Math.abs(controller.getAxis(AXIS_RIGHT_X)) > minHoldForCharge;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		player.handleCommand(buttonCode);
		return true;
	}

	@Override
	public float getXInput() {
		return controller.getAxis(AXIS_LEFT_X);
	}

	@Override
	public float getYInput() {
		return controller.getAxis(AXIS_LEFT_Y);
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) { 
		return false; 
	}

	@Override public void connected(Controller controller) { }
	@Override public void disconnected(Controller controller) { }
	@Override public boolean buttonUp(Controller controller, int buttonCode) { return false; }
	@Override public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
	@Override public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	@Override public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	@Override public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }

}
