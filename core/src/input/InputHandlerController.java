package input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;

public class InputHandlerController extends InputHandler implements ControllerListener {

	public InputHandlerController(Fighter player) {
		super(player);
		this.player = player;
		for (int i = 0; i < lastXSize; ++i) lastXPositions.add((float) 0);
	}

	Controller controller;
	final int lastXSize = 2;
	final List<Float> lastXPositions = new ArrayList<Float>(lastXSize);
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_SHOULDER = 4;

	public boolean setupController(int index){
		Controllers.addListener(this);
		if (Controllers.getControllers().size == 0) return false;
		controller = Controllers.getControllers().get(index);
		return true;
	}
	
	private final float flick = 0.80f;
	private final float pushed = 0.95f;
	public void update() {
		super.update();
		lastXPositions.add(controller.getAxis(AXIS_LEFT_X));
		boolean jump = controller.getButton(commandJump); // TODO: move these to InputHandler
		player.handleJumpCommand(jump);
		if ( Math.abs(controller.getAxis(AXIS_SHOULDER)) > pushed) { 
			// && lastShoulderInput < pushed) || controller.getAxis(AXIS_SHOULDER) < -pushed && lastShoulderInput > -pushed) 
			player.handleBlockCommand(true);
			player.tryBlock();
		}
		else player.handleBlockCommand(false);
		float prevX = lastXPositions.remove(0);
		float curX = lastXPositions.get(lastXSize - 1);
		if (Math.abs(curX) < pushed) curX = 0;
		if (Math.abs(curX - prevX) > flick) {
			if (Math.signum(curX) == 1) handleCommand(commandStickRight);
			if (Math.signum(curX) == -1) handleCommand(commandStickLeft);
		}
		if (controller.getPov(0) != PovDirection.center) handleCommand(commandTaunt);
		controller.getAxis(AXIS_SHOULDER);
	}

	public boolean isCharging() {
		return controller.getButton(commandCharge);
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		handleCommand(buttonCode);
		return true;
	}

	public float getXInput() {
		return controller.getAxis(AXIS_LEFT_X);
	}

	public float getYInput() {
		return controller.getAxis(AXIS_LEFT_Y);
	}

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
