package maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import input.InputHandlerCPU;
import main.MapHandler;
import entities.Fighter;

public class EnemySpawner {
	final List<Enemy> enemyList = new ArrayList<Enemy>();
	final int initAmount;
	int amount, capacity;
	float frequency;
	final List<Fighter> spawnedEntities = new ArrayList<Fighter>();
	private final boolean random;

	EnemySpawner (List<Enemy> enemyList, int amount, int capacity, float frequency, boolean random){
		this.enemyList.addAll(enemyList);
		this.amount = amount;
		initAmount = amount;
		this.capacity = capacity;
		this.frequency = frequency;
		this.random = random;
	}

	void update(float deltaTime){
		if (deltaTime % frequency == 1 && amount > 0 && spawnedEntities.size() < capacity) spawnNewEnemy();

		Iterator<Fighter> spawnIter = spawnedEntities.iterator();
		while (spawnIter.hasNext()){
			Fighter spawn = spawnIter.next();
			if (spawn.getLives() == 0) spawnIter.remove();
		}
	}

	void spawnNewEnemy(){
		Fighter fi = null;
		Vector2 spawnPoint = MapHandler.getSpawnPoint();
		Enemy en = getEnemy();
		try {
			fi = en.type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(spawnPoint.x, spawnPoint.y, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fi.setInputHandler(new InputHandlerCPU(fi, en.brain));
		MapHandler.addEntity(fi);
		spawnedEntities.add(fi);
		amount--;
	}

	Enemy getEnemy(){
		Enemy en = null;
		if (random) en = enemyList.get( (int) (Math.random() * enemyList.size()) );
		else{
			en = enemyList.get(0);
			rotate(enemyList);
		}
		return en;
	}

	public <T> List<T> rotate(List<T> aL) {  // remove last element, add it to front of the ArrayList
		if (aL.size() == 0) return aL;
		T element = null;
		element = aL.remove( aL.size() - 1 );
		aL.add(0, element);
		return aL;
	}

	public void restart() {
		amount = initAmount;
		spawnedEntities.clear();
	}
}
