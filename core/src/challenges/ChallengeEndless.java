package challenges;

public class ChallengeEndless extends Challenge {

	public ChallengeEndless(int difficulty){
		numLives = 1;
		roundList.add(RoundGenerator.generateEndless(difficulty));
		stageList.add(getRoomByRound(difficulty));
		begin();
	}
	
}
