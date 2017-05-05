package maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.UptiltEngine;
import entities.*;
import input.Brain;

public class Round {

	Enemy mooks = new Enemy(Mook.class, Brain.MookBrain.class);
	Enemy guns = new Enemy(Gunmin.class, Brain.GunminBrain.class);
	Enemy alloys = new Enemy(AlloyMook.class, Brain.MookBrain.class);
	Enemy rockets = new Enemy(Rocketmin.class, Brain.GunminBrain.class);
	Enemy heavies = new Enemy(Heavy.class, Brain.MookBrain.class);
	Enemy speedies = new Enemy(Speedy.class, Brain.SpeedyBrain.class);
	Enemy hypers = new Enemy(HyperSpeedy.class, Brain.SpeedyBrain.class);
	Enemy dummies = new Enemy(Mook.class, Brain.Recover.class);
	Enemy kickers = new Enemy(Kicker.class, Brain.KickerBrain.class);
	EnemySpawner esAliensEasy = new EnemySpawner(Arrays.asList(mooks, guns), 16, 2, 120, true);
	EnemySpawner esAliensHard = new EnemySpawner(Arrays.asList(mooks, alloys, guns, rockets, speedies, hypers), 16, 4, 80, true);
	EnemySpawner esClones = new EnemySpawner(Arrays.asList(kickers), 3, 1, 100, true);
	EnemySpawner esDummies = new EnemySpawner(Arrays.asList(dummies), 24, 4, 10, true);
	EnemySpawner esTest = new EnemySpawner(Arrays.asList(mooks), 100, 1, 10, true);
	List<EnemySpawner> fSList = new ArrayList<EnemySpawner>(Arrays.asList(esAliensHard));
	boolean restarted = false;

	public Round(){
		setup();
	}

	public void update(float deltaTime){
		for (EnemySpawner fs: fSList) fs.update(deltaTime);
		boolean shouldRestart = true;
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		if (shouldRestart) restart();
	}

	private void restart(){
		MapHandler.resetRoom();
		setup();
	}

	private void setup(){
		for (Fighter player: (UptiltEngine.getPlayers() ) ){
			player.setLives(3);
		}
		for (EnemySpawner fs: fSList) fs.restart();
	}

}
