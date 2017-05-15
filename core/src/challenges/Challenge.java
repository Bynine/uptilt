package challenges;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.CombatStarter;
import entities.Fighter;
import main.MapHandler;
import main.SFX;
import main.UptiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Stage> stageList = new ArrayList<Stage>();
	protected Combat activeCombat = null;
	protected int place = 0;
	protected int numLives = 5;
	private Mode mode = Mode.ADVENTURE;
	private final Vector2 combatPosition = new Vector2(0, 0);
	private boolean endCombat = false;
	final int difficulty;

	Challenge(int difficulty){
		this.difficulty = difficulty;
	}

	int waitBetween = 60;
	protected void begin(){
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			player.setLives(numLives);
		}
		startStage();
	}

	public void update(){
		if (isInCombat()) activeCombat.update(UptiltEngine.getDeltaTime());
		if (activeCombat.getNumEnemies() == 0) endCombat();
		boolean shouldRestart = true;
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		if (shouldRestart) restart();
	}

	public void goToNextStage(){
		place++;
		if (place >= stageList.size()) win();
		else startStage();
	}

	protected void startStage(){
		endCombat = false;
		UptiltEngine.changeRoom(stageList.get(place));
		activeCombat = CombatGenerator.generate(difficulty);
		ChallengeGraphicsHandler.readyGo();
		UptiltEngine.wait(waitBetween);
		for (Fighter player: UptiltEngine.getPlayers()) player.refresh();
	}

	void win(){
		new SFX.Victory().play();
		UptiltEngine.returnToMenu();
		restart();
	}

	public Combat getActiveCombat(){
		return activeCombat;
	}

	public boolean isInCombat(){
		return mode == Mode.COMBAT;
	}

	public Vector2 getCombatPosition(){
		return combatPosition;
	}

	public void startCombat(CombatStarter cs, Vector2 startPosition) {
		for (Fighter player: UptiltEngine.getPlayers()) player.setRespawnPoint(startPosition);
		if (UptiltEngine.getPlayers().size() > 1){
			for (Fighter player: UptiltEngine.getPlayers().subList(1,  UptiltEngine.getPlayers().size())){
				player.refresh();
			}
		}
		combatPosition.set(startPosition);
		mode = Mode.COMBAT;
		if (cs instanceof CombatStarter.EndCombatStarter){
			activeCombat = CombatGenerator.generate(difficulty + 1);
			endCombat = true;
		}
		else activeCombat = CombatGenerator.generate(difficulty);
	}

	public void startEndlessCombat(Vector2 position) {
		combatPosition.set(position);
		mode = Mode.COMBAT;
		activeCombat = CombatGenerator.generate(difficulty);
	}

	public void endCombat(){
		mode = Mode.ADVENTURE;
		if (endCombat) goToNextStage();
	}

	public void restart(){
		mode = Mode.ADVENTURE;
		place = 0;
		MapHandler.resetRoom();
		begin();
	}

	protected Stage getRoomByRound(int position){
		return new Stage_Adventure();
	}

	private static enum Mode{
		COMBAT, ADVENTURE
	}

}
