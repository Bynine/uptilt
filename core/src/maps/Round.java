package maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.UptiltEngine;
import entities.*;
import input.Brain;

public class Round {

	Enemy mooks = new Enemy(F_Mook.class, Brain.MookBrain.class);
	Enemy guns = new Enemy(F_Gunmin.class, Brain.GunminBrain.class);
	Enemy alloy = new Enemy(F_AlloyMook.class, Brain.MookBrain.class);
	Enemy rockets = new Enemy(F_Rocketmin.class, Brain.GunminBrain.class);
	Enemy heavies = new Enemy(F_Heavy.class, Brain.Braindead.class);
	Enemy kickers = new Enemy(F_Kicker.class, Brain.KickerBrain.class);
	EnemySpawner esAliens = new EnemySpawner(Arrays.asList(heavies), 16, 4, 120, true);
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
