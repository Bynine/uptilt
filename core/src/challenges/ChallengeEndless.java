package challenges;

import main.GlobalRepo;
import maps.Stage_Standard;

public class ChallengeEndless extends Challenge {

	public ChallengeEndless(int difficulty){
		super(difficulty);
		numLives = 1;
		stageList.add(new Stage_Standard());
		combatPosition.set(GlobalRepo.TILE * 22, GlobalRepo.TILE * 6);
		begin();
		startCombatHelper(null, combatPosition);
		activeCombat = CombatGenerator.generateEndless(difficulty);
	}
	
	@Override
	public String getEnemyCounter() {
		return "KOs: " + activeCombat.getNumKilled();
	}
	
}
