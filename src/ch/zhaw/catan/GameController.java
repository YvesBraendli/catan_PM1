package ch.zhaw.catan;

import java.awt.Point;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class GameController {
	private GameController gameController;
	private SiedlerGame siedlerGame;
	private InputParser inputParser;
	private Output output;
    private TextIO textIO;
    private TextTerminal<?> textTerminal;
	
	private static final int WINPOINTS_NEEDED = 5; // Winpoints needed without City Implementation
	private int numberOfPlayers;
	private boolean isRunning;
	private boolean settlementBuilt;
	private boolean roadBuilt;
	
	public GameController() {
		inputParser = new InputParser();
		output = new Output();
		isRunning = true;
        textIO = TextIoFactory.getTextIO();
        textTerminal = textIO.getTextTerminal();
	}
	
	public void runGame() {

		
		numberOfPlayers = inputParser.requestNumberOfPlayers(textIO);
		siedlerGame = new SiedlerGame(WINPOINTS_NEEDED, numberOfPlayers);
		
		//Phase 2
		for(int i = 1; i <= numberOfPlayers; i++) {
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(false);
			if(i != numberOfPlayers) {
				siedlerGame.switchToNextPlayer();
			}
		}
		for(int i = numberOfPlayers; i >= 1; i--) {
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(true);
			if(i != 1) {
				siedlerGame.switchToPreviousPlayer();
			}
		}
		
		//Phase 3
		//while(isRunning) {}
		
		
	}
	
	private void buildInitialStructures(boolean payout) {
		settlementBuilt = false;
		roadBuilt = false;
		
		while(!settlementBuilt) {
			output.requestSettlementCoordinates(textTerminal, true);
			Point position = inputParser.requestXYCoordinates(textIO);
			settlementBuilt = siedlerGame.placeInitialSettlement(position, payout);
			if(!settlementBuilt) {
				output.errorSettlementNotBuilt(textTerminal);
			}
		}
		
		while(!roadBuilt) {
			output.requestRoadStartCoordinates(textTerminal, true);
			Point roadStart = inputParser.requestXYCoordinates(textIO);
			output.requestRoadEndCoordinates(textTerminal, true);
			Point roadEnd = inputParser.requestXYCoordinates(textIO);
			roadBuilt = siedlerGame.placeInitialRoad(roadStart, roadEnd);
			if(!roadBuilt) {
				output.errorRoadNotBuilt(textTerminal);
			}
		}
	}
	
	private void rollDice() {}
	
	
	public static void main(String[] args) {
		GameController gameController = new GameController();
		gameController.runGame();
	} 
}
