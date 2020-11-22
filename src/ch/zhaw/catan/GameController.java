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
	private GameController gameController;
	private SiedlerGame siedlerGame;
	private InputParser inputParser;
	private Output output;
	private TextIO textIO;
	private TextTerminal<?> textTerminal;
	private Random random;

	private static final int WINPOINTS_NEEDED = 5; // Winpoints needed without City Implementation
	private int numberOfPlayers;
	private boolean isRunning;
	private boolean settlementBuilt;
	private boolean roadBuilt;
	private Point position;

	/**
	 * constructor of the GameController class.
	 */
	public GameController() {
		inputParser = new InputParser();
		output = new Output();
		textIO = TextIoFactory.getTextIO();
		textTerminal = textIO.getTextTerminal();
		textTerminal.getProperties().setPaneWidth(1000);
		textTerminal.getProperties().setPaneHeight(740);
		isRunning = true;
		random = new Random();
	}

	/**
	 * runs the game in order of the three phases
	 */
	public void runGame() {
		// Phase 1
		numberOfPlayers = inputParser.requestNumberOfPlayers(textIO);
		siedlerGame = new SiedlerGame(WINPOINTS_NEEDED, numberOfPlayers);

		// Phase 2
		for (int i = 1; i <= numberOfPlayers; i++) {
			output.printBoard(textTerminal, siedlerGame.getBoard());
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(false);
			if (i != numberOfPlayers) {
				siedlerGame.switchToNextPlayer();
			}
		}
		for (int i = numberOfPlayers; i >= 1; i--) {
			output.printBoard(textTerminal, siedlerGame.getBoard());
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(true);
			if (i != 1) {
				siedlerGame.switchToPreviousPlayer();
			}
		}

		// Phase 3
		while (isRunning) {

			boolean isUsersTurn = true;
			int rolledNumber = rollDice();
			output.printPreTurnInfo(textTerminal, siedlerGame.throwDice(rolledNumber), rolledNumber);
			while (isUsersTurn) {
				output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
				outputPrintPlayerResources();
				switch (inputParser.showMainMenuAction(textIO)) {
				case SHOW:
					output.printMapMenuDelimiter(textTerminal);
					output.printBoard(textTerminal, siedlerGame.getBoard());
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
		output.printTradeMenuDelimiter(textTerminal);
		output.requestResourceSell(textTerminal);
		Config.Resource resourceToSell = inputParser.showTradeAction(textIO);
		output.requestResourceBuy(textTerminal);
		Config.Resource resourceToBuy = inputParser.showTradeAction(textIO);
		if (inputParser.askBuyResource(textIO, resourceToBuy)) {
			if (siedlerGame.tradeWithBankFourToOne(resourceToSell, resourceToBuy)) {
				output.errorTradeFailed(textTerminal);
			}
		}
	}

	/**
	 * Lets the player choose what he wants to build and starts the corresponding
	 * action.
	 */
	private void buildAction() {
		output.printBuildMenuDelimiter(textTerminal);
		outputPrintPlayerResources();
		switch (inputParser.showBuildAction(textIO)) {
		case SETTLEMENT:
			output.printBoard(textTerminal, siedlerGame.getBoard());
			output.requestSettlementCoordinates(textTerminal, false);
			position = inputParser.requestXYCoordinates(textIO);
			if (inputParser.askBuildStructure(textIO, Structure.SETTLEMENT)) {
				settlementBuilt = siedlerGame.buildSettlement(position);
				if (!settlementBuilt) {
					output.errorSettlementNotBuilt(textTerminal);
				}
				else {
					isRunning = checkForWinner();
				}
			}
			break;
		case CITY:
			output.printBoard(textTerminal, siedlerGame.getBoard());
			output.requestSettlementCoordinates(textTerminal, false);
			position = inputParser.requestXYCoordinates(textIO);
			if (inputParser.askBuildStructure(textIO, Structure.CITY)) {
				settlementBuilt = siedlerGame.buildCity(position);
				if (!settlementBuilt) {
					output.errorSettlementNotBuilt(textTerminal);
				}
				else {
					isRunning = checkForWinner();
				}
			}
			break;
		case ROAD:
			output.printBoard(textTerminal, siedlerGame.getBoard());
			output.requestRoadStartCoordinates(textTerminal, false);
			Point roadStart = inputParser.requestXYCoordinates(textIO);
			output.requestRoadEndCoordinates(textTerminal, false);
			Point roadEnd = inputParser.requestXYCoordinates(textIO);
			if (inputParser.askBuildStructure(textIO, Structure.ROAD)) {
				roadBuilt = siedlerGame.buildRoad(roadStart, roadEnd);
				if (!roadBuilt) {
					output.errorRoadNotBuilt(textTerminal);
				}
			}
			break;
		}
	}

	/**
	 * Prints out the current players resources
	 */
	private void outputPrintPlayerResources() {
		output.printPlayerResources(textTerminal, siedlerGame.getCurrentPlayerResourceStock(Resource.WOOD),
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
		settlementBuilt = false;
		roadBuilt = false;

		while (!settlementBuilt) {
			output.requestSettlementCoordinates(textTerminal, true);
			Point position = inputParser.requestXYCoordinates(textIO);
			settlementBuilt = siedlerGame.placeInitialSettlement(position, payout);
			if (!settlementBuilt) {
				output.errorSettlementNotBuilt(textTerminal);
			}
		}

		while (!roadBuilt) {
			output.requestRoadStartCoordinates(textTerminal, true);
			Point roadStart = inputParser.requestXYCoordinates(textIO);
			output.requestRoadEndCoordinates(textTerminal, true);
			Point roadEnd = inputParser.requestXYCoordinates(textIO);
			roadBuilt = siedlerGame.placeInitialRoad(roadStart, roadEnd);
			if (!roadBuilt) {
				output.errorRoadNotBuilt(textTerminal);
			}
		}
	}

	/**
	 * Rolls two six sided dices and adds the result together.
	 * 
	 * @return the combined rolled numbers.
	 */
	private int rollDice() {
		return random.nextInt(6) + random.nextInt(6) + 2;
	}
	
	/**
	 * Checks if a player won the game and prints out a message
	 * 
	 * @return true if the game needs to continue running.
	 */
	private boolean checkForWinner() {
		Config.Faction winner = siedlerGame.getWinner();
		if(winner != null) {
			output.printWinner(textTerminal, winner);
			return false;
		}
		return true;
	}

	/**
	 * The man method of the program used to start the runGame method.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		GameController gameController = new GameController();
		gameController.runGame();
	}
}
