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
		for (int i = 0; i < lastSize; ++i) {
			lastXPositions.add((float) 0);
			lastYPositions.add((float) 0);
		}
	}

	Controller controller;
	final int lastSize = 2;
	final List<Float> lastXPositions = new ArrayList<Float>(lastSize), lastYPositions = new ArrayList<Float>(lastSize);
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_SHOULDER = 4;

	public boolean setupController(int index){
		Controllers.addListener(this);
		if (Controllers.getControllers().size == 0) return false;
		controller = Controllers.getControllers().get(index);
		return true;
	}
	
	private final float depressed = 0.1f;
	private float prevShoulder = 0;
	public void update() {
		super.update();
		player.handleJumpHeld(controller.getButton(commandJump));
		float currShoulder = controller.getAxis(AXIS_SHOULDER);
		player.handleBlockHeld( (Math.abs(currShoulder) > depressed) );
		if ( Math.abs(currShoulder) > depressed && (Math.abs(currShoulder) - Math.abs(prevShoulder)) > depressed) player.tryDodge();
		prevShoulder = Math.abs(controller.getAxis(AXIS_SHOULDER));
		stickFlick(lastXPositions, AXIS_LEFT_X, commandStickRight, commandStickLeft);
		stickFlick(lastYPositions, AXIS_LEFT_Y, commandStickDown, commandStickUp);
		if (controller.getPov(0) != PovDirection.center) handleCommand(commandTaunt);
	}
	
	private final float flick = 0.80f;
	private final float pushed = 0.85f;
	private void stickFlick(List<Float> lastPositions, int axis, int command1, int command2){
		lastPositions.add(controller.getAxis(axis));
		float prevY = lastPositions.remove(0);
		float curY = lastPositions.get(lastSize - 1);
		if (Math.abs(curY) < pushed) curY = 0;
		if (Math.abs(curY - prevY) > flick) {
			if (Math.signum(curY) == 1) handleCommand(command1);
			if (Math.signum(curY) == -1) handleCommand(command2);
		}
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
