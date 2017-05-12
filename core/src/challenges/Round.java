package challenges;

import java.util.ArrayList;
import java.util.List;

public class Round {

	protected List<EnemySpawner> fSList = new ArrayList<EnemySpawner>();
	protected boolean restarted = false;
	protected int numEnemies = 0;
	SpecialEffect specialEffect;

	public Round(EnemySpawner es){
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
	
	enum SpecialEffect{
		NONE
	}

}
