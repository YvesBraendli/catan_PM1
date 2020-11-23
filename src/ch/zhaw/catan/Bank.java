package ch.zhaw.catan;

import java.util.Map;

import ch.zhaw.catan.Config.Resource;

/**
 * This class holds a datafield for the amount of cards, the bank current has
 * for every resource of a catan game. It has the following functions: Check, if
 * a player can make a trade with the bank and carries out a trade with the
 * bank.
 * 
 * @author Meier Robin, Moser Nadine, Br�ndli Yves
 * @version 1.0
 *
 */

public class Bank {

	private Map<Resource, Integer> amountOfResources;

	public Bank() {
		amountOfResources = Config.INITIAL_RESOURCE_CARDS_BANK;
	}

	/**
	 * Checks, if there were 4 given cards and enough cards in the bank stock. If
	 * so, returns true and deducts number of resource-cards from the bank.
	 * 
	 * @param resourcegiven  The resource, the player wants to trade.
	 * @param resourcewanted The resource, the player wants to have.
	 * @return true, if the trade for one card was successful.
	 */
	public boolean trade(Resource resourcegiven, Resource resourcewanted) {
		boolean tradesuccessful = false;
		if (amountOfResources.get(resourcewanted) >= 1) {
			tradesuccessful = true;
			amountOfResources.put(resourcewanted, amountOfResources.get(resourcewanted) - 1);
		}
		return tradesuccessful;
	}
	
	/**
	 * Subtracts resources from the stock of the bank, if the bank has enough resource-cards.
	 * If there is any card, where the bank hasn't enough on stock, all the resource-cards are not
	 * Subtracted from the stock.
	 * @param resourcesWanted A Map with the specified resource and an int-value for the number of the cards, that needs to be
	 * 							paid out to the player.
	 * @return A boolean which tells true, if the subtraction was successful.
	 */
	public boolean payoutToDiceThrows (Map<Resource, Integer> resourcesWanted) {
		boolean successful = false;
		if ((resourcesWanted.containsKey(Config.Resource.CLAY) && 
				amountOfResources.get(Config.Resource.CLAY) >= resourcesWanted.get(Config.Resource.CLAY))
						|| (resourcesWanted.containsKey(Config.Resource.GRAIN) && 
						amountOfResources.get(Config.Resource.GRAIN) >= resourcesWanted.get(Config.Resource.GRAIN))
						|| (resourcesWanted.containsKey(Config.Resource.STONE) && 
								amountOfResources.get(Config.Resource.STONE) >= resourcesWanted.get(Config.Resource.STONE))
						|| (resourcesWanted.containsKey(Config.Resource.WOOD) && 
								amountOfResources.get(Config.Resource.WOOD) >= resourcesWanted.get(Config.Resource.WOOD))
						|| (resourcesWanted.containsKey(Config.Resource.WOOL) && 
								amountOfResources.get(Config.Resource.WOOL) >= resourcesWanted.get(Config.Resource.WOOL))) {
			successful = true;
			if (resourcesWanted.containsKey(Config.Resource.CLAY)) {
				amountOfResources.put(Config.Resource.CLAY, amountOfResources.get(Config.Resource.CLAY) - resourcesWanted.get(Config.Resource.CLAY));
			} else if (resourcesWanted.containsKey(Config.Resource.GRAIN)) {
				amountOfResources.put(Config.Resource.GRAIN, amountOfResources.get(Config.Resource.GRAIN) - resourcesWanted.get(Config.Resource.GRAIN));
			} else if (resourcesWanted.containsKey(Config.Resource.CLAY)) {
				amountOfResources.put(Config.Resource.CLAY, amountOfResources.get(Config.Resource.CLAY) - resourcesWanted.get(Config.Resource.CLAY));
			} else if (resourcesWanted.containsKey(Config.Resource.STONE)) {
				amountOfResources.put(Config.Resource.STONE, amountOfResources.get(Config.Resource.STONE) - resourcesWanted.get(Config.Resource.STONE));
			} else if (resourcesWanted.containsKey(Config.Resource.WOOD)) {
				amountOfResources.put(Config.Resource.WOOD, amountOfResources.get(Config.Resource.WOOD) - resourcesWanted.get(Config.Resource.WOOD));
			} else if (resourcesWanted.containsKey(Config.Resource.WOOL)) {
				amountOfResources.put(Config.Resource.WOOL, amountOfResources.get(Config.Resource.WOOL) - resourcesWanted.get(Config.Resource.WOOL));
			}
			
		}
		return successful;
	}

}
