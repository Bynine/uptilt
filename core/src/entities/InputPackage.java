package entities;

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
	 * distance X: left of player is positive, right is neg
	 * distance Y: below player is positive, above is neg
	 */
	InputPackage(Fighter fighter){
		Fighter target = getTarget(fighter);
		state = fighter.state;
		isOffStage = (!fighter.isGrounded() && fighter.noGroundBelow());
		isBelowStage = fighter.position.y < UptiltEngine.getChallenge().getCombatPosition().y;
		hasDoubleJumped = fighter.doubleJumped;
		isGrounded = fighter.isGrounded();
		isRunning = fighter.isRunning();
		awayFromWall = distanceFromEdges(-48, fighter);
		distanceFromCenter = UptiltEngine.getChallenge().getCombatPosition().x - fighter.position.x;
		distanceXFromPlayer = (target.position.x + target.getHurtBox().getWidth()/2) - fighter.position.x + fighter.getHurtBox().getWidth()/2;
		distanceYFromPlayer = target.position.y - (fighter.position.y + fighter.getHurtBox().getHeight()/2);
		direct = fighter.direct();
		if (null != fighter.getActiveMove()) activeMove = fighter.getActiveMove().move;
		else activeMove = null;
	}
	
	private boolean distanceFromEdges(float dist, Fighter fighter){
		return false;
	}
	
	private Fighter getTarget(Fighter fighter){
		Fighter target = UptiltEngine.getPlayers().get(0);
		for (Fighter player: UptiltEngine.getPlayers()){
			if (fighter.getPosition().dst(player.getPosition() ) < fighter.getPosition().dst(target.getPosition() )) target = player;
		}
		return target;
	}
}
