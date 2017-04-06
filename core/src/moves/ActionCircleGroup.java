package moves;

import java.util.ArrayList;
import java.util.List;

public class ActionCircleGroup {
	
	final List<ActionCircle> connectedCircles = new ArrayList<ActionCircle>();
	
	ActionCircleGroup(List<ActionCircle> list){
		connectedCircles.addAll(list);
		for (ActionCircle ac: connectedCircles){
			ac.group = this;
		}
	}
	
}
