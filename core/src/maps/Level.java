package maps;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {
	final List<Room> rooms = new ArrayList<>();
	
	public Room getRoom(int i){
		return rooms.get(i);
	}
	
	public int numberRooms(){
		return rooms.size();
	}
	
}
