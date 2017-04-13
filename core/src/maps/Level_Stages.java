package maps;

import java.util.Arrays;

public class Level_Stages extends Level {

	public Level_Stages(){
		rooms.addAll(Arrays.asList(
				new Room_Standard(this),	//	0
				new Room_Flat(this),		//	1
				new Room_Platforming(this)	//  2
				));
	}
	
}
