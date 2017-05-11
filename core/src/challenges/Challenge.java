package challenges;

import java.util.ArrayList;
import java.util.List;

import entities.Fighter;
import main.MapHandler;
import main.SFX;
import main.UptiltEngine;
import maps.Stage;
import maps.Room_Standard;

public class Challenge {

	private final List<Round> roundList = new ArrayList<Round>();
	private final List<Stage> stageList = new ArrayList<Stage>();
	private Round activeRound = null;
	int place = 0;

	/**
	 * 
	 * @param size Number of rounds
	 * @param difficulty Challenge level. Set w/ static ints
	 */
	public Challenge(int size, int difficulty){
		for (int i = 0; i < size; ++i){
			int newDifficulty = difficulty;
			if (size != 1){
				if		(i == 0)		newDifficulty -= 1; // first round is easy...
				else if (i == size - 1) newDifficulty += 1; // pre boss fight difficulty spike
			}
			roundList.add(RoundGenerator.generate(newDifficulty, i));
			
			stageList.add(getRoomByRound());
		}
		begin();
	}

	int waitBetween = 60;
	private void begin(){
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			player.setLives(5);
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
	}

	public void goToNextRound(){
		place++;
		if (place >= roundList.size()) win();
		else startRound();
	}
	
	private void startRound(){
		MapHandler.updateRoomMap(stageList.get(place));
		activeRound = roundList.get(place);
		ChallengeGraphicsHandler.readyGo();
		UptiltEngine.wait(waitBetween);
	}

	void win(){
		new SFX.Victory().play();
		restart();
	}

	public Round getActiveRound(){
		return activeRound;
	}

	public void restart(){
		place = 0;
		MapHandler.resetRoom();
		for (Round r: roundList) r.restart();
		begin();
	}
	
	private Stage getRoomByRound(){
		return new Room_Standard();
	}

}
