package maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import input.Brain;
import input.InputHandlerCPU;
import main.MapHandler;
import entities.Fighter;

public class FighterSpawner<T extends Fighter, B extends Brain> {
	final Class<T> type;
	final Class<B> brain;
	final int initAmount;
	int amount, capacity;
	float frequency;
	final List<Fighter> spawnedEntities = new ArrayList<Fighter>();

	FighterSpawner (Class<T> type, Class<B> brain, int amount, int capacity, float frequency){
		this.type = type;
		this.brain = brain;
		this.amount = amount;
		initAmount = amount;
		this.capacity = capacity;
		this.frequency = frequency;
	}
	
	void update(float deltaTime){
		if (deltaTime % frequency == 0 && amount > 0 && spawnedEntities.size() < capacity) spawnNewFighter();
		
		Iterator<Fighter> spawnIter = spawnedEntities.iterator();
		while (spawnIter.hasNext()){
			Fighter spawn = spawnIter.next();
			if (spawn.getStocks() == 0) spawnIter.remove();
		}
		
	}
	
	void spawnNewFighter(){
		Fighter fi = null;
		Vector2 spawnPoint = MapHandler.getSpawnPoint();
		try {
			fi = type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(spawnPoint.x, spawnPoint.y, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fi.setInputHandler(new InputHandlerCPU(fi, brain));
		MapHandler.addEntity(fi);
		spawnedEntities.add(fi);
		amount--;
	}

	public void restart() {
		amount = initAmount;
		spawnedEntities.clear();
	}
}
