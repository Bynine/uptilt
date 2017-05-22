package challenges;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.CombatStarter;
import entities.Fighter;
import entities.TreasureChest;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.UptiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Stage> stageList = new ArrayList<Stage>();
	protected Combat activeCombat = null;
	protected CombatStarter activeCombatStarter = null;
	protected int place = 0;
	protected long score = 0;
	protected int numLives = 5;
	protected Mode mode = Mode.ADVENTURE;
	protected final Vector2 combatPosition = new Vector2(0, 0);
	private boolean endCombat = false, combatNotEnded = false;
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
		if (activeCombat.getNumEnemies() == 0 && combatNotEnded) endCombat();
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
		if (!UptiltEngine.debugToggle){
			ChallengeGraphicsHandler.readyGo();
			UptiltEngine.wait(waitBetween);
		}
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

	public void startCombat(CombatStarter cs, Vector2 position) {
		startCombatHelper(cs, position);
		for (Fighter player: UptiltEngine.getPlayers()) player.setRespawnPoint(position);
		if (UptiltEngine.getPlayers().size() > 1){
			for (Fighter player: UptiltEngine.getPlayers().subList(1,  UptiltEngine.getPlayers().size())){
				player.refresh();
			}
		}
		if (cs instanceof CombatStarter.EndCombatStarter){
			activeCombat = CombatGenerator.generate(difficulty + 1);
			endCombat = true;
		}
		else activeCombat = CombatGenerator.generate(difficulty);
	}

	protected void startCombatHelper(CombatStarter cs, Vector2 position){
		activeCombatStarter = cs;
		combatPosition.set(position);
		mode = Mode.COMBAT;
		combatNotEnded = true;
	}

	public void endCombat(){
		mode = Mode.ADVENTURE;
		MapHandler.addEntity(new TreasureChest(combatPosition.x, combatPosition.y + GlobalRepo.TILE));
		combatNotEnded = false;
		if (endCombat) goToNextStage();
	}

	public void restart(){
		mode = Mode.ADVENTURE;
		place = 0;
		MapHandler.resetRoom();
		begin();
	}

	protected Stage getRoomByRound(int position){
		if (position == 0) return new Stage_Adventure();
		return new Stage_Adventure2();
	}

	public long getScore(){
		return score;
	}

	protected static enum Mode{
		COMBAT, ADVENTURE
	}

	public void score(int i) {
		score += i;
	}
	
	public abstract String getEnemyCounter();

}
