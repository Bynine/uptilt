package main;

import entities.Fighter;

public abstract class InputHandler {
	
	Fighter player;
	void begin(Fighter player){
		this.player = player;
	}
	
	public abstract void update(Fighter player); 
	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 

}
