package input;

import entities.Fighter;

public abstract class InputHandler {

	public static final int commandNone		=-1, commandSpecial	= 1, commandAttack	= 0;
	public static final int commandJump	= 2, commandCharge	= 3, commandBlock	= 4, commandGrab = 5;
	public static final int commandStickUp 	=20, commandStickLeft	= 21, commandStickRight	= 22,commandStickDown	= 23;

	Fighter player;
	public InputHandler(Fighter player){
		this.player = player;
	}

	public void update(){
		try{
			if (player.inputQueueTimer.timeUp()) player.queuedCommand = commandNone;
			if (!player.inputQueueTimer.timeUp()) handleCommand(player.queuedCommand);
		}
		catch(Exception e) {};
	}

	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 

	public void handleCommand(int command){
		boolean wasCommandAccepted = false;

		if (player.canAct()){
			switch (command){
			case commandJump: wasCommandAccepted = player.tryJump(); break;
			}
		}
		if (!wasCommandAccepted && player.canAttack()){
			switch (command){
			case commandAttack:			wasCommandAccepted = player.tryNormal(); break;
			case commandSpecial:		wasCommandAccepted = player.trySpecial(); break;
			case commandCharge: 		wasCommandAccepted = player.tryCharge(); break;
			case commandGrab:			wasCommandAccepted = player.tryGrab(); break;
			case commandStickUp:		wasCommandAccepted = player.tryStickUp(); break;
			case commandStickRight:		wasCommandAccepted = player.tryStickForward(); break;
			case commandStickLeft:		wasCommandAccepted = player.tryStickBack(); break;
			case commandStickDown:		wasCommandAccepted = player.tryStickDown(); break;
			default:					wasCommandAccepted = true; break;
			}
		}

		if (!wasCommandAccepted && player.inputQueueTimer.timeUp()) {
			player.queuedCommand = command;
			player.inputQueueTimer.restart();
		}
		else if (wasCommandAccepted) player.queuedCommand = commandNone;
	}

}
