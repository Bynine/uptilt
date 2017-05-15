package challenges;

import com.badlogic.gdx.math.Vector2;

public class ChallengeEndless extends Challenge {

	public ChallengeEndless(int difficulty){
		super(difficulty);
		numLives = 1;
		activeCombat = CombatGenerator.generateEndless(difficulty);
		stageList.add(getRoomByRound(difficulty));
		startEndlessCombat(new Vector2(200, 200));
		begin();
	}
	
}
