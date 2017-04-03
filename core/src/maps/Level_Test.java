package maps;

import java.util.Arrays;

public class Level_Test extends Level {

	public Level_Test(){
		rooms.addAll(Arrays.asList(
				new Room_Test(this)//,	// 0
				//new Room_Test2(this)	// 1
				));
	}
	
}
