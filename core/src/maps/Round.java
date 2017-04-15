package maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.UptiltEngine;
import entities.*;
import input.Brain;

public class Round {

	FighterSpawner<? extends Fighter, ? extends Brain> esMook = new FighterSpawner<Mook, Brain.MookBrain>(Mook.class, Brain.MookBrain.class, 12, 2, 120f);
	FighterSpawner<? extends Fighter, ? extends Brain> esGunmin = new FighterSpawner<Gunmin, Brain.GunminBrain>(Gunmin.class, Brain.GunminBrain.class, 12, 2, 120f);
	FighterSpawner<? extends Fighter, ? extends Brain> esKicker = new FighterSpawner<Kicker, Brain.MookBrain>(Kicker.class, Brain.MookBrain.class, 12, 2, 120f);
	List<FighterSpawner<? extends Fighter, ? extends Brain>> fSList = new ArrayList<FighterSpawner<? extends Fighter, ? extends Brain>>
	(Arrays.asList(esMook, esGunmin));
	Fighter player;
	boolean restarted = false;

	public Round(Fighter player){
		this.player = player;
		setup();
	}

	public void update(float deltaTime){
		for (FighterSpawner<? extends Fighter, ? extends Brain> fs: fSList) fs.update(deltaTime);
		if (UptiltEngine.getPlayer().getStocks() == 0) restart();
	}

	private void restart(){
		MapHandler.resetRoom();
		setup();
	}

	private void setup(){
		player.setStocks(3);
		for (FighterSpawner<? extends Fighter, ? extends Brain> fs: fSList) fs.restart();
	}

}
