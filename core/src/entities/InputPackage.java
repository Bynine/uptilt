package entities;

import main.MapHandler;
import main.UptiltEngine;
import moves.Move;
import entities.Entity.State;

public class InputPackage {

	public final State state;
	public final boolean isOffStage, isBelowStage, hasDoubleJumped;
	public final float distanceFromCenter, distanceXFromPlayer, distanceYFromPlayer;
	public final int direct;
	public final Move activeMove;
	
	InputPackage(Fighter fighter){
		state = fighter.state;
		isOffStage = (!fighter.isGrounded() && (fighter.position.x < MapHandler.getStageSides()[0] || fighter.position.x > MapHandler.getStageSides()[1]));
		isBelowStage = (fighter.position.y < MapHandler.getStageFloor());
		hasDoubleJumped = fighter.doubleJumped;
		distanceFromCenter = fighter.position.x - (MapHandler.getStageSides()[0] + MapHandler.getStageSides()[1])/2;
		distanceXFromPlayer = fighter.position.x + fighter.getHurtBox().getWidth()/2 - UptiltEngine.getPlayer().position.x;
		distanceYFromPlayer = fighter.position.y + fighter.getHurtBox().getHeight()/2 - UptiltEngine.getPlayer().position.y;
		direct = fighter.direct();
		activeMove = fighter.getActiveMove();
	}
}
