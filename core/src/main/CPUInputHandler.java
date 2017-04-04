package main;

import timers.Timer;
import entities.Fighter;

public class CPUInputHandler extends InputHandler {

	float xInput = 0;
	float yInput = 1;
	Timer changeInputs = new Timer(30, false);

	public void update(Fighter player) {
		boolean jump = 0.02 > Math.random();
		player.handleJumpCommand(jump);
//		if (changeInputs.timeUp()){
//			xInput = getRandomAxis();
//			yInput = getRandomAxis();
//			changeInputs.restart();
//		}
//		changeInputs.countUp();
	}

	public boolean isCharging() {
		return false;
	}

	public float getXInput() {
		return xInput;
	}

	public float getYInput() {
		return -yInput;
	}

	private float getRandomAxis(){
		return (float) (1 - (2 * Math.random()));
	}

}
