package input;

import java.util.ArrayList;
import java.util.List;

import timers.Timer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;

public class InputHandlerController extends InputHandlerPlayer implements ControllerListener {

	Controller controller;
	final int lastSize = 2;
	private float currShoulder, prevShoulder, currX, prevX, currY, prevY = 0;
	final List<Float> lastXPositions = new ArrayList<Float>(lastSize), lastYPositions = new ArrayList<Float>(lastSize);
	private final float depressed = 0.1f;
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_SHOULDER = 4;
	int prevButton = commandNone;
	Timer pauseSelectBuffer = new Timer(10);

	public InputHandlerController(Fighter player) {
		super(player);
		this.fighter = player;
		for (int i = 0; i < lastSize; ++i) {
			lastXPositions.add((float) 0);
			lastYPositions.add((float) 0);
		}
	}

	public boolean setupController(int index){
		if (Controllers.getControllers().size <= index) return false;
		Controllers.addListener(this);
		controller = Controllers.getControllers().get(index);
		return true;
	}

	private final float pushed = 0.85f;
	public void update() {
		pauseSelectBuffer.countUp();
		currShoulder = controller.getAxis(AXIS_SHOULDER);
		super.update();
		prevShoulder = currShoulder;

		lastXPositions.add(controller.getAxis(AXIS_LEFT_X));
		prevX = lastXPositions.remove(0);
		currX = lastXPositions.get(lastSize - 1);
		if (Math.abs(currX) < pushed) currX = 0;

		lastYPositions.add(controller.getAxis(AXIS_LEFT_Y));
		prevY = lastYPositions.remove(0);
		currY = lastYPositions.get(lastSize - 1);
		if (Math.abs(currY) < pushed) currY = 0; // mmm, curry
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		if (!controller.getButton(buttonCode)) return false;
		handleCommand(buttonCode);
		return true;
	}

	public float getXInput() {
		return controller.getAxis(AXIS_LEFT_X);
	}

	public float getYInput() {
		return controller.getAxis(AXIS_LEFT_Y);
	}

	public boolean dodge(){
		return blockHold() && (prevShoulder - currShoulder) > depressed;
	}

	public boolean blockHold(){
		return Math.abs(currShoulder) > depressed;
	}

	public boolean taunt(){
		return controller.getPov(0) != PovDirection.center;
	}

	public boolean chargeHold(){
		return controller.getButton(commandCharge);
	}

	public boolean jumpHold(){
		return controller.getButton(commandJump);
	}

	private final float flick = 0.80f;

	public boolean flickLeft(){
		return Math.abs(currX - prevX) > flick && Math.signum(currX) == -1;
	}

	public boolean flickRight(){
		return Math.abs(currX - prevX) > flick && Math.signum(currX) == 1;
	}

	public boolean flickUp(){
		return Math.abs(currY - prevY) > flick && Math.signum(currY) == -1;
	}

	public boolean flickDown(){
		return Math.abs(currY - prevY) > flick && Math.signum(currY) == 1;
	}
	
	public boolean pause(){ 
		boolean paused = pauseSelectBuffer.timeUp() && controller.getButton(commandPause);
		if (paused) pauseSelectBuffer.restart();
		return paused;
	}
	
	public boolean select(){ 
		boolean selected = pauseSelectBuffer.timeUp() && controller.getButton(commandSelect);
		if (selected) pauseSelectBuffer.restart();
		return selected;
	}

	/* handled by buttonDown */

	public boolean attack(){ return false; }
	public boolean special(){ return false; }
	public boolean charge(){ return false; }
	public boolean jump(){ return false; }
	public boolean grab(){ return false; }

	/* NOT USED */

	public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
	public void connected(Controller controller) { }
	public void disconnected(Controller controller) { }
	public boolean buttonUp(Controller controller, int buttonCode) { return false; }
	public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }

}
