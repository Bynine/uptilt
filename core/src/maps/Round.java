package maps;

import main.MapHandler;
import main.UptiltEngine;
import entities.*;

public class Round {
	
	FighterSpawner<Mook> es;
	Fighter player;
	boolean restarted = false;

	public Round(Fighter player){
		this.player = player;
		setup();
	}
	
	public void update(float deltaTime){
		es.update(deltaTime);
		if (UptiltEngine.getPlayer().getStocks() == 0) restart();
	}
	
	private void restart(){
		MapHandler.resetRoom();
		setup();
	}
	
	private void setup(){
		player.setStocks(3);
		es = new FighterSpawner<Mook>(Mook.class, 24, 6, 90);
	}
	
}
