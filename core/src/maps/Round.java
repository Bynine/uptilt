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
	Enemy alloy = new Enemy(AlloyMook.class, Brain.MookBrain.class);
	Enemy rockets = new Enemy(Rocketmin.class, Brain.GunminBrain.class);
	Enemy kickers = new Enemy(Kicker.class, Brain.KickerBrain.class);
	EnemySpawner esAliens = new EnemySpawner(Arrays.asList(mooks, alloy, rockets, guns), 16, 4, 120, true);
	EnemySpawner esClones = new EnemySpawner(Arrays.asList(kickers), 3, 1, 100, true);
	List<EnemySpawner> fSList = new ArrayList<EnemySpawner>(Arrays.asList(esAliens));
	Fighter player;
	boolean restarted = false;

	public Round(Fighter player){
		this.player = player;
		setup();
	}

	public void update(float deltaTime){
		for (EnemySpawner fs: fSList) fs.update(deltaTime);
		if (UptiltEngine.getPlayer().getStocks() == 0) restart();
	}

	private void restart(){
		MapHandler.resetRoom();
		setup();
	}

	private void setup(){
		player.setStocks(3);
		for (EnemySpawner fs: fSList) fs.restart();
	}

}
