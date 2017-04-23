package entities;

import main.MapHandler;
import main.UptiltEngine;
import moves.Move;
import entities.Entity.State;

public class InputPackage {

	public final State state;
	public final boolean isOffStage, isBelowStage, hasDoubleJumped, isGrounded, isRunning, awayFromWall;
	public final float distanceFromCenter, distanceXFromPlayer, distanceYFromPlayer;
	public final int direct;
	public final Move activeMove;
	
	/**
	 * 
	 * distance X : left of player is negative, right is positive
	 * distance Y: below player is negative, above is positive
	 */
	InputPackage(Fighter fighter){
		state = fighter.state;
		isOffStage = (!fighter.isGrounded() && distanceFromEdges(0, fighter) );
		isBelowStage = (fighter.position.y < MapHandler.getStageFloor());
		hasDoubleJumped = fighter.doubleJumped;
		isGrounded = fighter.isGrounded();
		isRunning = fighter.isRunning();
		awayFromWall = distanceFromEdges(-48, fighter);
		distanceFromCenter = fighter.position.x - (MapHandler.getStageSides()[0] + MapHandler.getStageSides()[1])/2;
		distanceXFromPlayer = fighter.position.x + fighter.getHurtBox().getWidth()/2 - UptiltEngine.getPlayer().position.x;
		distanceYFromPlayer = fighter.position.y + fighter.getHurtBox().getHeight()/2 - UptiltEngine.getPlayer().position.y;
		direct = fighter.direct();
		if (null != fighter.getActiveMove()) activeMove = fighter.getActiveMove().move;
		else activeMove = null;
	}
	
	private boolean distanceFromEdges(float dist, Fighter fighter){
		return (fighter.position.x - dist*2 <= MapHandler.getStageSides()[0] || fighter.position.x + dist*2 >= MapHandler.getStageSides()[1]);
	}
}
