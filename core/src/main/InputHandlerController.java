package main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;

public class InputHandlerController extends InputHandler implements ControllerListener {

	Controller controller;
	final int lastXSize = 2;
	final List<Float> lastXPositions = new ArrayList<Float>(lastXSize);
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
	public static final int AXIS_SHOULDER = 4;

	void begin(Fighter player){
		super.begin(player);
		this.player = player;
		for (int i = 0; i < lastXSize; ++i) lastXPositions.add((float) 0);
	}

	public boolean setupController(int index){
		Controllers.addListener(this);
		if (Controllers.getControllers().size == 0) return false;
		controller = Controllers.getControllers().get(index);
		return true;
	}
	
	private final float flick = 0.6f;
	private final float pushed = 0.95f;
	private float lastCX = 0;
	private float lastCY = 0;
	private float lastShoulder = 0;
	public void update() {
		super.update();
		lastXPositions.add(controller.getAxis(AXIS_LEFT_X));
		boolean jump = controller.getButton(commandJumpX);
		if (!jump) jump = controller.getButton(commandJumpY);
		if (lastCX < pushed && controller.getAxis(AXIS_RIGHT_X) > pushed) handleCommand(commandCRight);
		if (lastCX > -pushed &&controller.getAxis(AXIS_RIGHT_X) < -pushed) handleCommand(commandCLeft);
		if (lastCY < pushed && controller.getAxis(AXIS_RIGHT_Y) > pushed) handleCommand(commandCDown);
		if (lastCY > -pushed &&controller.getAxis(AXIS_RIGHT_Y) < -pushed) handleCommand(commandCUp);
		if ( (controller.getAxis(AXIS_SHOULDER) > pushed && lastShoulder < pushed) || 
				controller.getAxis(AXIS_SHOULDER) < -pushed && lastShoulder > -pushed) player.tryBoost();
		
		float prevX = lastXPositions.remove(0);
		float curX = lastXPositions.get(lastXSize - 1);
		if (Math.abs(curX) < pushed) curX = 0;
		if (Math.abs(curX - prevX) > flick) {
			if (Math.signum(curX) == 1) handleCommand(commandStickRight);
			if (Math.signum(curX) == -1) handleCommand(commandStickLeft);
		}
		lastCX = controller.getAxis(AXIS_RIGHT_X);
		lastCY = controller.getAxis(AXIS_RIGHT_Y);
		lastShoulder = controller.getAxis(AXIS_SHOULDER);
		player.handleJumpCommand(jump);
	}

	public boolean isCharging() {
		return Math.abs(controller.getAxis(AXIS_RIGHT_X)) > pushed || Math.abs(controller.getAxis(AXIS_RIGHT_Y)) > pushed;
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
