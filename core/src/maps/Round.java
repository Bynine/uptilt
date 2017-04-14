package maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.MapHandler;
import main.UptiltEngine;
import entities.*;

public class Round {
	
	FighterSpawner<? extends Fighter> es1 = new FighterSpawner<Mook>(Mook.class, 24, 4, 120);
	FighterSpawner<? extends Fighter> es2 = new FighterSpawner<Kicker>(Kicker.class, 3, 1, 270);
	List<FighterSpawner<? extends Fighter>> fSList = new ArrayList<FighterSpawner<? extends Fighter>>(Arrays.asList(es1));
	Fighter player;
	boolean restarted = false;

	public Round(Fighter player){
		this.player = player;
		setup();
	}
	
	public void update(float deltaTime){
		for (FighterSpawner<? extends Fighter> fs: fSList) fs.update(deltaTime);
		if (UptiltEngine.getPlayer().getStocks() == 0) restart();
	}
	
	private void restart(){
		MapHandler.resetRoom();
		setup();
	}
	
	private void setup(){
		player.setStocks(3);
		for (FighterSpawner<? extends Fighter> fs: fSList) fs.restart();
	}
	
}
