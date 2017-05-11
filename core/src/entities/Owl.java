package entities;

public class Owl extends Wasp {

	public Owl(float posX, float posY, int team) {
		super(posX, posY, team);
		friction = 0.96f;
		airFriction = 0.92f;
		walkAcc = 0.8f;
		walkSpeed = 2f;
		runAcc = 0.56f;
		runSpeed = 6.2f;
		airAcc = 0.7f;
		airSpeed = 5.5f;
	}

}
