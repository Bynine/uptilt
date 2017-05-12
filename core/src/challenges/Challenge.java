package challenges;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.SupplyCrate;
import entities.Fighter;
import main.MapHandler;
import main.SFX;
import main.UptiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Round> roundList = new ArrayList<Round>();
	protected final List<Stage> stageList = new ArrayList<Stage>();
	protected Round activeRound = null;
	protected int place = 0;
	protected int numLives = 5;
	private Mode mode = Mode.ADVENTURE;
	private final Vector2 combatPosition = new Vector2(0, 0);
	
	int waitBetween = 60;
	protected void begin(){
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			player.setLives(numLives);
		}
		startRound();
	}

	public void update(){
		activeRound.update(UptiltEngine.getDeltaTime());
		if (activeRound.getNumEnemies() == 0) goToNextRound();
		boolean shouldRestart = true;
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		if (shouldRestart) restart();
		if (UptiltEngine.getDeltaTime() % 640 == 0) MapHandler.addEntity(new SupplyCrate(MapHandler.getSpawnPoint().x, MapHandler.getSpawnPoint().y));
	}

	public void goToNextRound(){
		place++;
		if (place >= roundList.size()) win();
		else startRound();
	}

	protected void startRound(){
		UptiltEngine.changeRoom(stageList.get(place));
		activeRound = roundList.get(place);
		ChallengeGraphicsHandler.readyGo();
		UptiltEngine.wait(waitBetween);
		for (Fighter player: UptiltEngine.getPlayers()) player.refresh();
	}

	void win(){
		new SFX.Victory().play();
		UptiltEngine.returnToMenu();
		restart();
	}

	public Round getActiveRound(){
		return activeRound;
	}
	
	public boolean isInCombat(){
		return mode == Mode.COMBAT;
	}
	
	public Vector2 getCombatPosition(){
		return combatPosition;
	}
	
	public void startCombat(Vector2 position) {
		combatPosition.set(position);
		mode = Mode.COMBAT;
	}

	public void restart(){
		place = 0;
		MapHandler.resetRoom();
		for (Round r: roundList) r.restart();
		begin();
	}

	protected Stage getRoomByRound(int position){
		switch(position){
		case 0: return new Stage_Standard();
		case 1: return new Stage_Walledin();
		case 2: return new Stage_Flat();
		case 3: return new Stage_Height();
		case 4: return new Stage_Ship();
		}
		return new Stage_Standard();
	}
	
	private static enum Mode{
		COMBAT, ADVENTURE
	}

}
