package entities;

import main.MapHandler;
import timers.Timer;

public abstract class Hittable extends Entity {

	float percentage = 0, armor = 0, weight = 100;
	final Timer caughtTimer = new Timer(0), knockIntoTimer = new Timer(20);
	float hitstunMod = 1;
	HitstunType hitstunType = HitstunType.NORMAL;

	public Hittable(float posX, float posY) {
		super(posX, posY);
	}
	
	void handleWind(){
		velocity.x += MapHandler.getRoomWind() * (weight/100);
	}

	public float getArmor() { 
		return armor;
	}
	
	public void setPercentage(float perc){
		percentage = perc;
	}

	public float getPercentage() { 
		return percentage; 
	}
	
	public float getWeight() { 
		return weight;
	}
	
	public enum HitstunType{ NORMAL, SUPER, ULTRA }

}
