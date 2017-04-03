package entities;

import maps.Room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Portal extends Entity {
	private Room room;
	protected Vector2 destination = new Vector2();

	public Portal(float x, float y, Room room, float desX, float desY){
		super(x, y);
		image = new Sprite(new Texture(Gdx.files.internal("sprites/portal.PNG")));
		this.room = room;
		destination.x = desX;
		destination.y = desY;
		updateImagePosition(0);
	}

	@Override
	protected void updatePosition(){ /* doesn't move */ }
	
	public Room getRoom(){ 
		return room; 
	}

	public Vector2 getDestination(Fighter p){ 
		return destination; 
	}

}
