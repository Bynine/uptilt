package main;

import entities.Fighter;

public class InputHandlerCPU extends InputHandler {

	float xInput = 0;
	float yInput = getRandomAxis();

	public void update(Fighter player) {
		// TODO: get "input package" class from fighter. This has all the relevant information that determines the AI's actions, like
		// player proximity, active state, etc
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
