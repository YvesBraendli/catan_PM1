package ch.zhaw.catan;

public class GameController {
	private GameController gameController;
	private SiedlerGame siedlerGame;
	private InputParser inputParser;
	private Output output;
	
	private static final int WINPOINTS_NEEDED = 5; // Winpoints needed without City Implementation
	private int numberOfPlayers;
	private boolean isRunning;
	private boolean settlementBuilt;
	private boolean roadBuilt;
	
	
	public void runGame() {
		inputParser = new InputParser();
		output = new Output();
		isRunning = true;
		
		// InputParser getNumberOfPlayers
		siedlerGame = new SiedlerGame(WINPOINTS_NEEDED, numberOfPlayers);
		
		//Phase 2
		for(int i = 1; i <= numberOfPlayers; i++) {
			buildInitialStructures(false);
			if(i != numberOfPlayers) {
				siedlerGame.switchToNextPlayer();
			}
		}
		for(int i = numberOfPlayers; i >= 1; i--) {
			buildInitialStructures(true);
			if(i != 1) {
				siedlerGame.switchToNextPlayer();
			}
		}
		
		//Phase 3
		while(isRunning) {}
		
		
	}
	private void buildInitialStructures(boolean payout) {
		settlementBuilt = false;
		roadBuilt = false;
		
		while(!settlementBuilt) {
			//InputParser getPointsForSettlement
			//settlementBuilt = siedlerGame.placeInitialSettlement(position, payout);
			if(!settlementBuilt) {
				//Output errorSettlementCouldNotBeBuiltAtPosition
			}
		}
		
		while(!roadBuilt) {
			//InputParser getPointsForStreetX
			//InputParser getPointsForStreetY
			//roadBuilt = siedlerGame.placeInitialRoad(roadStart, roadEnd);
			if(!roadBuilt) {
				//Output errorRoadCouldNotBeBuiltAtPosition
			}
		}
	}
	
	private void rollDice() {}
	
	
//	public static void main(String[] args) {
//		GameController gameController = new GameController();
//		gameController.runGame();
//	} 
}
