package challenges;

public class ChallengeAdventure extends Challenge{
	
	final int adventureLength = 2; 
	
	public ChallengeAdventure(int difficulty){
		super(difficulty);
		for (int i = 0; i < adventureLength; ++i){
//			int newDifficulty = difficulty;
//			if		(i == 0)					newDifficulty -= 1; // first round is easy...
//			else if (i == adventureLength - 2)	newDifficulty += 1; // pre boss fight difficulty spike
//			
//			roundList.add(CombatGenerator.generate(newDifficulty, i));

			stageList.add(getRoomByRound(i));
		}
		begin();
	}

	@Override
	public String getEnemyCounter() {
		if (null == activeCombat || activeCombat.getNumEnemies() <= 0) return "";
		else return "Remaining: " + activeCombat.getNumEnemies();
	}
	
}