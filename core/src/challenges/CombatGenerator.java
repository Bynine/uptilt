package challenges;

import inputs.Brain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import entities.*;

public class CombatGenerator {

	public static final int DIFF_TEST	=-1;
	public static final int DIFF_BABB	= 0;
	public static final int DIFF_EASY	= 1;
	public static final int DIFF_MIDD	= 2;
	public static final int DIFF_HARD	= 3;
	public static final int DIFF_OHNO	= 4;
	private static final int DIFF_FUCK	= 5;

	protected static Enemy mooks = new Enemy(Basic.class, Brain.MookBrain.class);
	protected static Enemy guns = new Enemy(Gunmin.class, Brain.MookBrain.class);
	protected static Enemy alloys = new Enemy(AlloyMook.class, Brain.MookBrain.class);
	protected static Enemy rockets = new Enemy(Rocketmin.class, Brain.MookBrain.class);
	protected static Enemy heavies = new Enemy(Heavy.class, Brain.MookBrain.class);
	protected static Enemy speedies = new Enemy(Speedy.class, Brain.MookBrain.class);
	protected static Enemy hypers = new Enemy(HyperSpeedy.class, Brain.MookBrain.class);
	protected static Enemy dummies = new Enemy(Mook.class, Brain.Recover.class);
	protected static Enemy kickers = new Enemy(Wasp.class, Brain.MookBrain.class);

	protected static List<EnemySpawner> babbList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(mooks), 3, 1, 120, true),
			new EnemySpawner(Arrays.asList(guns), 3, 1, 120, true)
			));
	protected static List<EnemySpawner> easyList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(mooks),	5, 2, 80, true)
//			new EnemySpawner(Arrays.asList(guns),	5, 2, 80, true),
//			new EnemySpawner(Arrays.asList(mooks, guns),	5, 2, 80, true)
			));
	protected static List<EnemySpawner> middList = new ArrayList<EnemySpawner>(Arrays.asList(			
			new EnemySpawner(Arrays.asList(mooks, speedies),			8,	3, 60, true),
			new EnemySpawner(Arrays.asList(guns, speedies),				8,	3, 60, true),
			new EnemySpawner(Arrays.asList(mooks, alloys),				8,	3, 60, true),
			new EnemySpawner(Arrays.asList(guns, rockets),				8,	3, 60, true),
			
			new EnemySpawner(Arrays.asList(alloys, speedies),			5,	2, 60, true),
			new EnemySpawner(Arrays.asList(rockets, speedies),			5,	2, 60, true),
			new EnemySpawner(Arrays.asList(alloys, rockets),			5,	2, 60, true),
			
			new EnemySpawner(Arrays.asList(alloys, rockets, speedies),	8,	3, 60, true),
			new EnemySpawner(Arrays.asList(mooks),		 				12, 16, 40, true)
			));
	protected static List<EnemySpawner> hardList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(alloys, rockets, speedies, hypers),	5, 4, 60, true),
			new EnemySpawner(Arrays.asList(alloys, rockets, speedies, hypers),	8, 3, 60, true),
			
			new EnemySpawner(Arrays.asList(hypers, speedies),					8,	3, 60, true),
			new EnemySpawner(Arrays.asList(rockets, speedies),					8,	3, 60, true),
			new EnemySpawner(Arrays.asList(hypers, alloys),						8,	3, 60, true),
			new EnemySpawner(Arrays.asList(hypers, rockets),					8,	3, 60, true),

			new EnemySpawner(Arrays.asList(mooks, alloys),		 				16, 32, 40, true)
			));
	protected static List<EnemySpawner> ohnoList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(alloys, rockets, speedies, hypers),	10, 8, 60, true),
			new EnemySpawner(Arrays.asList(alloys, rockets, speedies, hypers),	16, 6, 60, true),
			
			new EnemySpawner(Arrays.asList(alloys),		 				20, 40, 20, true)
			));
	protected static List<EnemySpawner> fuckList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(hypers), 12, 6, 60, true)
			));

	protected EnemySpawner esDummies = new EnemySpawner(Arrays.asList(dummies), 24, 4, 10, true);
	protected EnemySpawner esTest = new EnemySpawner(Arrays.asList(mooks, alloys), 1, 1, 10, true);

	static Combat generate(int difficulty){
		return new Combat(getDifficultyEnemySpawner(difficulty));
	}
	
	static Combat generateEndless(int difficulty) {
		EnemySpawner enemySpawner = getDifficultyEnemySpawner(difficulty);
		enemySpawner.setToEndless();
		return new Combat(enemySpawner);
	}
	
	private static EnemySpawner getDifficultyEnemySpawner(int difficulty){
		switch(difficulty){
		case DIFF_BABB: return newEnemySpawner(babbList);
		case DIFF_EASY: return newEnemySpawner(easyList);
		case DIFF_MIDD: return newEnemySpawner(middList);
		case DIFF_HARD: return newEnemySpawner(hardList);
		case DIFF_OHNO: return newEnemySpawner(ohnoList);
		case DIFF_FUCK: return newEnemySpawner(fuckList);
		default: return newEnemySpawner(babbList);
		}
	}

	private static EnemySpawner newEnemySpawner(List<EnemySpawner> enemyList){
		return new EnemySpawner(GlobalRepo.getRandomElementFromList(enemyList));
	}

}
