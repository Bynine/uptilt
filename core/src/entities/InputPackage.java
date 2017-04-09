package entities;

import main.MapHandler;
import main.UptiltEngine;
import entities.Entity.State;

public class InputPackage {

	public final State state;
	public final boolean isOffStage, isBelowStage, hasDoubleJumped;
	public final float distanceFromCenter, distanceXFromPlayer, distanceYFromPlayer;
	public final int direct;
	
	InputPackage(Fighter fighter){
		state = fighter.state;
		isOffStage = (!fighter.isGrounded() && (fighter.position.x < MapHandler.getStageSides()[0] || fighter.position.x > MapHandler.getStageSides()[1]));
		isBelowStage = (fighter.position.y < MapHandler.getStageFloor());
		hasDoubleJumped = fighter.doubleJumped;
		distanceFromCenter = fighter.position.x - (MapHandler.getStageSides()[0] + MapHandler.getStageSides()[1])/2;
		distanceXFromPlayer = fighter.position.x - UptiltEngine.getPlayer().position.x;
		distanceYFromPlayer = fighter.position.y - UptiltEngine.getPlayer().position.y;
		direct = fighter.direct();
	}
}
