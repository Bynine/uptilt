package challenges;

import java.util.ArrayList;
import java.util.List;

public class Combat {

	private List<EnemySpawner> fSList = new ArrayList<EnemySpawner>();
	private int numEnemies = 0;
	SpecialEffect specialEffect;

	public Combat(EnemySpawner es){
		fSList.add(es);
		setup();
	}

	public void update(float deltaTime){
		numEnemies = 0;
		for (EnemySpawner fs: fSList) {
			fs.update(deltaTime);
			numEnemies += fs.getAmount() + fs.getNumActiveEnemies();
		}
	}

	void restart(){
		setup();
	}

	private void setup(){
		for (EnemySpawner fs: fSList) fs.restart();
	}
	
	public int getNumEnemies(){
		return numEnemies;
	}
	
	public int getNumKilled(){
		int killed = 0;
		for (EnemySpawner fs: fSList) killed += fs.getKilled();
		return killed;
	}
	
	enum SpecialEffect{
		NONE
	}

}
