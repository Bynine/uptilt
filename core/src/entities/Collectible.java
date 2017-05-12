package entities;

import main.GlobalRepo;

public abstract class Collectible extends Entity {

	public Collectible(float posX, float posY) {
		super(posX, posY);
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			if (isTouching(fi, 0) && fi.getTeam() == GlobalRepo.GOODTEAM) collect(fi);
		}
	}

	abstract void collect(Fighter fi);

}
