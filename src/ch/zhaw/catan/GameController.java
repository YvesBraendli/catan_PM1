package ch.zhaw.catan;

import java.awt.Point;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Resource;

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
		textIO = TextIoFactory.getTextIO();
		textTerminal = textIO.getTextTerminal();
		isRunning = true;
	}

	public void runGame() {

		numberOfPlayers = inputParser.requestNumberOfPlayers(textIO);
		siedlerGame = new SiedlerGame(WINPOINTS_NEEDED, numberOfPlayers);

		// Phase 2
		for (int i = 1; i <= numberOfPlayers; i++) {
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(false);
			if (i != numberOfPlayers) {
				siedlerGame.switchToNextPlayer();
			}
		}
		for (int i = numberOfPlayers; i >= 1; i--) {
			output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
			buildInitialStructures(true);
			if (i != 1) {
				siedlerGame.switchToPreviousPlayer();
			}
		}

		// Phase 3
		while (isRunning) {

			boolean isUsersTurn = true;

			while (isUsersTurn) {
				// TODO Eimal würlfelelelele
				output.printPlayerStart(textTerminal, siedlerGame.getCurrentPlayerFaction());
				outputPrintPlayerResources();
				switch (inputParser.showMainMenuAction(textIO)) {
				case SHOW:
					// TODO Feldli id konsole werfe
					break;

				case BUILD:
					outputPrintPlayerResources();
					switch (inputParser.showBuildAction(textIO)) {
					case SETTLEMENT:
						output.requestSettlementCoordinates(textTerminal, false);
						Point position = inputParser.requestXYCoordinates(textIO);
						settlementBuilt = siedlerGame.buildSettlement(position);
						if (!settlementBuilt) {
							output.errorSettlementNotBuilt(textTerminal);
						}
						break;
					case CITY:
						// TODO Stadt ghört genau da ane und niergens anders!
						break;
					case ROAD:
						output.requestRoadStartCoordinates(textTerminal, false);
						Point roadStart = inputParser.requestXYCoordinates(textIO);
						output.requestRoadEndCoordinates(textTerminal, false);
						Point roadEnd = inputParser.requestXYCoordinates(textIO);
						roadBuilt = siedlerGame.buildRoad(roadStart, roadEnd);
						if (!roadBuilt) {
							output.errorRoadNotBuilt(textTerminal);
						}
						break;
					}
					break;

				case TRADE:
					output.requestResourceSell(textTerminal);
					Config.Resource resourceToSell = inputParser.showTradeAction(textIO);
					output.requestResourceBuy(textTerminal);
					Config.Resource resourceToBuy = inputParser.showTradeAction(textIO);
					if (inputParser.askBuyResource(textIO, resourceToBuy)) {
						if (siedlerGame.tradeWithBankFourToOne(resourceToSell, resourceToBuy)) {
							output.errorTradeFailed(textTerminal);
						}
					}
					break;

				case END_TURN:
					siedlerGame.switchToNextPlayer();
					isUsersTurn = false;
					break;
				}
			}
		}

	}

	private void outputPrintPlayerResources() {
		output.printPlayerResources(textTerminal, siedlerGame.getCurrentPlayerResourceStock(Resource.WOOD),
				siedlerGame.getCurrentPlayerResourceStock(Resource.STONE),
				siedlerGame.getCurrentPlayerResourceStock(Resource.WOOL),
				siedlerGame.getCurrentPlayerResourceStock(Resource.CLAY),
				siedlerGame.getCurrentPlayerResourceStock(Resource.GRAIN));
	}

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

	private void rollDice() {
		//TODO Würfl implementememtem
	}

	public static void main(String[] args) {
		GameController gameController = new GameController();
		gameController.runGame();
	}
}
