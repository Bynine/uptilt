package challenges;

public class ChallengeAdventure extends Challenge{
	
	final int adventureLength = 5; 
	
	public ChallengeAdventure(int difficulty){
		for (int i = 0; i < adventureLength; ++i){
			int newDifficulty = difficulty;
			if		(i == 0)					newDifficulty -= 1; // first round is easy...
			else if (i == adventureLength - 2)	newDifficulty += 1; // pre boss fight difficulty spike
			
			roundList.add(RoundGenerator.generate(newDifficulty, i));

			stageList.add(getRoomByRound(i));
		}
		begin();
	}
	
}