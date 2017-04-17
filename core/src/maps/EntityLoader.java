package maps;

import input.Brain;
import input.InputHandlerCPU;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import entities.*;

public class EntityLoader {

	Entity loadEntity(MapObject m){
		MapProperties mp = m.getProperties();
		float x = mp.get("x", Float.class);
		float y = mp.get("y", Float.class);

		switch(m.getName().toLowerCase()){
		case "kicker": {
			Kicker kicker = new Kicker(x, y, 1);
			kicker.setInputHandler(new InputHandlerCPU(kicker, Brain.MookBrain.class));
			return kicker;
		}
		default: {
			Dummy dummy = new Dummy(x, y, 1);
			dummy.setInputHandler(new InputHandlerCPU(dummy, Brain.Braindead.class));
			dummy.setStocks(999);
			return dummy;
		}
		}
	}
}
