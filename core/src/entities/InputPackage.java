package entities;

import main.MapHandler;
import entities.Entity.State;

public class InputPackage {

	public final State state;
	public final boolean isOffStage;
	public final boolean isBelowStage;
	public final boolean hasDoubleJumped;
	public final float distanceFromCenter;
	
	InputPackage(Fighter fighter){
		this.state = fighter.state;
		isOffStage = (!fighter.isGrounded() && (fighter.position.x < MapHandler.getStageSides()[0] || fighter.position.x > MapHandler.getStageSides()[1]));
		isBelowStage = (fighter.position.y < MapHandler.getStageFloor());
		hasDoubleJumped = fighter.doubleJumped;
		distanceFromCenter = fighter.position.x - (MapHandler.getStageSides()[0] + MapHandler.getStageSides()[1])/2;
	}
}
