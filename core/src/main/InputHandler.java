package main;

import entities.Fighter;

public abstract class InputHandler {

	public static final int commandNone		=-1, commandSpecial	= 0, commandAttack	= 1;
	public static final int commandJumpX	= 2, commandJumpY	= 3, commandBlock	= 4, commandGrab = 5;
	public static final int commandStickUp 	=20, commandStickLeft	= 21, commandStickRight	= 22,commandStickDown	= 23;
	public static final int commandCUp		=30, commandCLeft 		= 31, commandCRight		= 32,commandCDown 		= 33;

	Fighter player;
	void begin(Fighter player){
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
			case commandJumpX: 
			case commandJumpY:			wasCommandAccepted = player.tryJump(); break;
			}
		}
		if (!wasCommandAccepted && player.canAttack()){
			switch (command){
			case commandAttack:			wasCommandAccepted = player.tryNormal(); break;
			case commandSpecial:		wasCommandAccepted = player.trySpecial(); break;
			case commandGrab:			wasCommandAccepted = player.tryGrab(); break;
			case commandStickUp:		wasCommandAccepted = player.tryStickUp(); break;
			case commandStickRight:		wasCommandAccepted = player.tryStickForward(); break;
			case commandStickLeft:		wasCommandAccepted = player.tryStickBack(); break;
			case commandStickDown:		wasCommandAccepted = player.tryStickDown(); break;
			case commandCUp:			wasCommandAccepted = player.tryCUp(); break;
			case commandCRight:			wasCommandAccepted = player.tryCForward(); break;
			case commandCLeft:			wasCommandAccepted = player.tryCBack(); break;
			case commandCDown:			wasCommandAccepted = player.tryCDown(); break;
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
