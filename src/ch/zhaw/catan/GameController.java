package ch.zhaw.catan;

import java.awt.Point;
import java.util.Random;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.Config.Structure;

/**
 * This class is used to play a game of catan with up to 4 players. The game is
 * segmented into the following three parts: Phase 1 - setup the game map with
 * fields with different regions and number values the setup is fixed. Phase 2 -
 * Each player is allowed to place one settlement and one road, then every
 * player in reverse order can do it again. Phase 3 - Two dice get rolled and
 * the resources get payed out with the corresponding field numbers then the
 * player gets the option to either build or trade resources. This goes on until
 * a player has amassed 5-7 winpoints by building settlements.
 * 
 * @author Moser Nadine, Meier Robin, Brändli Yves
 *
 */
public class GameController {
	private SiedlerGame siedlerGame;
	private InputParser inputParser;
	private Output output;
	private TextIO textIO;
	private TextTerminal<?> textTerminal;
	private Random random;
	private static final int DICEROLL_ADDITION = 2;
	private static final int NUMBER_OF_DICESIDES = 6;
	private static final int WINPOINTS_NEEDED = 5; // Winpoints needed without City Implementation
	private int numberOfPlayers;
	private boolean isRunning;
	private boolean isSettlementBuilt;
	private boolean isRoadBuilt;
	private Point position;

	/**
	 * constructor of the GameController class.
	 */
	public GameController() {
		
		
		textIO = TextIoFactory.getTextIO();
		textTerminal = textIO.getTextTerminal();
		textTerminal.getProperties().setPaneWidth(1000);
		textTerminal.getProperties().setPaneHeight(740);
		output = new Output(textTerminal);
		inputParser = new InputParser(textIO);
		isRunning = true;
		random = new Random();
	}

	/**
	 * runs the game in order of the three phases
	 */
	public void runGame() {
		// Phase 1
		numberOfPlayers = inputParser.requestNumberOfPlayers();
		siedlerGame = new SiedlerGame(WINPOINTS_NEEDED, numberOfPlayers);

		// Phase 2
		for (int i = 1; i <= numberOfPlayers; i++) {
			output.printBoard(siedlerGame.getBoard());
			output.printPlayerStart(siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(false);
			if (i != numberOfPlayers) {
				siedlerGame.switchToNextPlayer();
			}
		}
		for (int i = numberOfPlayers; i >= 1; i--) {
			output.printBoard(siedlerGame.getBoard());
			output.printPlayerStart(siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(true);
			if (i != 1) {
				siedlerGame.switchToPreviousPlayer();
			}
		}

		// Phase 3
		while (isRunning) {

			boolean isUsersTurn = true;
			int rolledNumber = rollDice();
			output.printPreTurnInfo(siedlerGame.throwDice(rolledNumber), rolledNumber);
			while (isUsersTurn) {
				output.printPlayerStart(siedlerGame.getCurrentPlayerFaction());
				outputPrintPlayerResources();
				switch (inputParser.showMainMenuAction()) {
					case SHOW:
						output.printMapMenuDelimiter();
						output.printBoard(siedlerGame.getBoard());
						break;
	
					case BUILD:
						buildAction();
						break;
	
					case TRADE:
						tradeAction();
						break;
	
					case END_TURN:
						siedlerGame.switchToNextPlayer();
						isUsersTurn = false;
						break;
					}
				}
			}

	}

	/**
	 * Shows the options which resource should be sold and which resource should be
	 * bought, then starts the exchange.
	 */
	private void tradeAction() {
		output.printTradeMenuDelimiter();
		output.requestResourceSell();
		Config.Resource resourceToSell = inputParser.showTradeAction();
		output.requestResourceBuy();
		Config.Resource resourceToBuy = inputParser.showTradeAction();
		if (inputParser.askBuyResource(resourceToBuy)) {
			if (!siedlerGame.tradeWithBankFourToOne(resourceToSell, resourceToBuy)) {
				output.errorTradeFailed();
			}
		}
	}

	/**
	 * Lets the player choose what he wants to build and starts the corresponding
	 * action.
	 */
	private void buildAction() {
		output.printBuildMenuDelimiter();
		outputPrintPlayerResources();
		switch (inputParser.showBuildAction()) {
			case SETTLEMENT:
				output.printBoard(siedlerGame.getBoard());
				output.requestSettlementCoordinates(false);
				position = inputParser.requestXYCoordinates();
				if (inputParser.askBuildStructure(Structure.SETTLEMENT)) {
					isSettlementBuilt = siedlerGame.buildSettlement(position);
					checkIfStructureIsBuilt();
				}
				break;
			case CITY:
				output.printBoard(siedlerGame.getBoard());
				output.requestSettlementCoordinates(false);
				position = inputParser.requestXYCoordinates();
				if (inputParser.askBuildStructure(Structure.CITY)) {
					isSettlementBuilt = siedlerGame.buildCity(position);
					checkIfStructureIsBuilt();
				}
				break;
			case ROAD:
				output.printBoard(siedlerGame.getBoard());
				output.requestRoadStartCoordinates(false);
				Point roadStart = inputParser.requestXYCoordinates();
				output.requestRoadEndCoordinates(false);
				Point roadEnd = inputParser.requestXYCoordinates();
				if (inputParser.askBuildStructure(Structure.ROAD)) {
					isRoadBuilt = siedlerGame.buildRoad(roadStart, roadEnd);
					if (!isRoadBuilt) {
						output.errorRoadNotBuilt();
					}
				}
				break;
			}
	}

	/**
	 * Checks if either a city or settlement was built
	 */
	private void checkIfStructureIsBuilt() {
		if (!isSettlementBuilt) {
			output.errorSettlementNotBuilt();
		}
		else {
			isRunning = isStillRunning();
		}
	}

	/**
	 * Prints out the current players resources
	 */
	private void outputPrintPlayerResources() {
		output.printPlayerResources(siedlerGame.getCurrentPlayerResourceStock(Resource.WOOD),
				siedlerGame.getCurrentPlayerResourceStock(Resource.STONE),
				siedlerGame.getCurrentPlayerResourceStock(Resource.WOOL),
				siedlerGame.getCurrentPlayerResourceStock(Resource.CLAY),
				siedlerGame.getCurrentPlayerResourceStock(Resource.GRAIN));
	}

	/**
	 * Used to build the initial structures.
	 * 
	 * @param payout true if a payout for the structure should be made.
	 */
	private void buildInitialStructures(boolean payout) {
		isSettlementBuilt = false;
		isRoadBuilt = false;

		while (!isSettlementBuilt) {
			output.requestSettlementCoordinates(true);
			Point position = inputParser.requestXYCoordinates();
			isSettlementBuilt = siedlerGame.placeInitialSettlement(position, payout);
			if (!isSettlementBuilt) {
				output.errorSettlementNotBuilt();
			}
		}

		while (!isRoadBuilt) {
			output.requestRoadStartCoordinates(true);
			Point roadStart = inputParser.requestXYCoordinates();
			output.requestRoadEndCoordinates(true);
			Point roadEnd = inputParser.requestXYCoordinates();
			isRoadBuilt = siedlerGame.placeInitialRoad(roadStart, roadEnd);
			if (!isRoadBuilt) {
				output.errorRoadNotBuilt();
			}
		}
	}

	/**
	 * Rolls two six sided dices and adds the result together.
	 * 
	 * @return the combined rolled numbers.
	 */
	private int rollDice() {
		return random.nextInt(NUMBER_OF_DICESIDES) + random.nextInt(NUMBER_OF_DICESIDES) + DICEROLL_ADDITION;
	}
	
	/**
	 * Checks if a player won the game and prints out a message
	 * 
	 * @return true if the game needs to continue running.
	 */
	private boolean isStillRunning() {
		Config.Faction winner = siedlerGame.getWinner();
		if(winner != null) {
			output.printWinner(winner);
			return false;
		}
		return true;
	}

	/**
	 * The main method of the program used to start the runGame method.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		new GameController().runGame();
	}
}
