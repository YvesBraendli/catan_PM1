package ch.zhaw.catan;

import java.util.List;
import java.util.Map;

import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

/**
 * This class is used for all the outputs to the console which do not require
 * player input.
 * 
 * @author Moser Nadine, Meier Robin, Brändli Yves
 *
 */
public class Output {
	private SiedlerBoardTextView gameBoardView;

	/**
	 * String that asks where a settlement should be built.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 * @param isInitial    true if the structure is built during phase 2.
	 */
	public void requestSettlementCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where do you want to build your initial settlement?");
		} else {
			textTerminal.println("Where do you want to build your settlement/city?");
		}
	}

	/**
	 * String that asks where a road should start.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 * @param isInitial    true if the structure is built during phase 2.
	 */
	public void requestRoadStartCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where should your initial road start?");
		} else {
			textTerminal.println("Where should your road start?");
		}
	}

	/**
	 * String that asks where a road should end.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 * @param isInitial    true if the structure is built during phase 2.
	 */
	public void requestRoadEndCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where should your initial road end?");
		} else {
			textTerminal.println("Where should your road end?");
		}
	}

	/**
	 * String that asks which resource should be sold.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void requestResourceSell(TextTerminal<?> textTerminal) {
		textTerminal.println("\nWhich resource do you want to sell?");
	}

	/**
	 * String that asks which resource should be bought.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void requestResourceBuy(TextTerminal<?> textTerminal) {
		textTerminal.println("\nWhich resource do you want to buy?");
	}

	/**
	 * Error if a settlement could not be built because either 1 the player did not
	 * have the required resources. 2 the settlements location does not meet the
	 * requirements.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void errorSettlementNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No settlement could be built at this location.");
	}

	/**
	 * Error if a road could not be built because either 1 the player did not have
	 * the required resources. 2 the roads location does not meet the requirements.
	 * 
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void errorRoadNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No road could be built at this location.");
	}

	/**
	 * Error if a trade could not be made, because either 1 the player did not have
	 * the required resources. 2 the bank is out of the resource.
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void errorTradeFailed(TextTerminal<?> textTerminal) {
		textTerminal.println("This trade failed, please check your resources.");
	}

	/**
	 * Prints a turn start message for the player.
	 * 
	 * @param textTerminal  the terminal used for the outputs
	 * @param currentPlayer the current player faction
	 */
	public void printPlayerStart(TextTerminal<?> textTerminal, Config.Faction currentPlayer) {
		textTerminal.println("\n-----------Player " + currentPlayer + "----------");
	}

	/**
	 * Prints the resources the player owns
	 * 
	 * @param textTerminal  the terminal used for the outputs
	 * @param amountOfWood  amount of Wood the player owns
	 * @param amountOfStone amount of Stone the player owns
	 * @param amountOfWool  amount of Wool the player owns
	 * @param amountOfClay  amount of Clay the player owns
	 * @param amountOfGrain amount of Grain the player owns
	 */
	public void printPlayerResources(TextTerminal<?> textTerminal, int amountOfWood, int amountOfStone,
			int amountOfWool, int amountOfClay, int amountOfGrain) {
		textTerminal.println("Resources Owned:\n" + amountOfGrain + " Grain, " + amountOfWool + " Wool, " + amountOfWood
				+ " Wood " + amountOfStone + " Stone, " + amountOfClay + " Clay");
	}

	/**
	 * Prints the resources the player got from the dice roll
	 * 
	 * @param textTerminal      the terminal used for the outputs
	 * @param payedOutResources the payed out resources
	 * @param rolledNumber      the rolled dice number
	 */
	public void printPreTurnInfo(TextTerminal<?> textTerminal, Map<Faction, List<Resource>> payedOutResources,
			int rolledNumber) {
		textTerminal.println("\n-----------------------------------");
		textTerminal.println("The number " + rolledNumber + " was rolled!");
		for (Map.Entry<Faction, List<Resource>> entry : payedOutResources.entrySet()) {
			String resourcesPerPlayers = "Player " + entry.getKey() + ": ";
			int amountOfGrain = 0;
			int amountOfWool = 0;
			int amountOfWood = 0;
			int amountOfStone = 0;
			int amountOfClay = 0;

			for (Resource resource : entry.getValue()) {
				switch (resource) {
				case GRAIN:
					amountOfGrain += 1;
					break;
				case WOOL:
					amountOfWool += 1;
					break;
				case WOOD:
					amountOfWood += 1;
					break;
				case STONE:
					amountOfStone += 1;
					break;
				case CLAY:
					amountOfClay += 1;
					break;
				}
			}
			if (amountOfGrain >= 1) {
				resourcesPerPlayers += "+" + amountOfGrain + " Grain, ";
			}
			if (amountOfWool >= 1) {
				resourcesPerPlayers += "+" + amountOfWool + " Wool, ";
			}
			if (amountOfWood >= 1) {
				resourcesPerPlayers += "+" + amountOfWood + " Wood, ";
			}
			if (amountOfStone >= 1) {
				resourcesPerPlayers += "+" + amountOfStone + " Stone, ";
			}
			if (amountOfClay >= 1) {
				resourcesPerPlayers += "+" + amountOfClay + " Clay, ";
			}

			if (!resourcesPerPlayers.equals("Player " + entry.getKey() + ": ")) {
				textTerminal.println(resourcesPerPlayers);
			}
		}
	}

	/**
	 * Prints a delimiter
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void printBuildMenuDelimiter(TextTerminal<?> textTerminal) {
		textTerminal.println("\n-----------Build---------------");
	}

	/**
	 * Prints a delimiter
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void printTradeMenuDelimiter(TextTerminal<?> textTerminal) {
		textTerminal.println("\n-----------Trade--------------");
	}

	/**
	 * Prints a delimiter
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void printMapMenuDelimiter(TextTerminal<?> textTerminal) {
		textTerminal.println("\n-----------Map-----------------");
	}

	/**
	 * Prints the board
	 * 
	 * @param textTerminal the terminal used for the outputs
	 */
	public void printBoard(TextTerminal<?> textTerminal, SiedlerBoard board) {
		gameBoardView = new SiedlerBoardTextView(board);
		textTerminal.println(gameBoardView.toString());
	}
}
