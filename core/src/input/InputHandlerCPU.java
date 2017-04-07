package input;

import entities.Fighter;

public class InputHandlerCPU extends InputHandler {

	float xInput = 0;
	float yInput = 0;
	Brain cpuBrain = new Brain.Braindead(this);

	public <T extends Brain> InputHandlerCPU(Fighter cpu, Class<T> brain){
		super(cpu);
		try {
			this.cpuBrain = brain.getDeclaredConstructor(InputHandlerCPU.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Declare a brain dead CPU **/
	public InputHandlerCPU(Fighter cpu){ 
		super(cpu);
	}

	public void update() {
		cpuBrain.update(player.getInputPackage());
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

}
