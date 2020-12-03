package ch.zhaw.catan;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.catan.Config.Resource;

/**
 * This class holds a datafield for the amount of cards, the bank current has
 * for every resource of a catan game. It has the following functions: Check, if
 * a player can make a trade with the bank and carries out a trade with the
 * bank.
 * 
 * @author Meier Robin, Moser Nadine, Brändli Yves
 * @version 1.0
 *
 */

public class Bank {

	private Map<Resource, Integer> amountOfResources;

	public Bank() {
		amountOfResources = new HashMap<>();
		for (Map.Entry<Resource, Integer> resource: Config.INITIAL_RESOURCE_CARDS_BANK.entrySet()) {
			amountOfResources.put(resource.getKey(), resource.getValue());
		}
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
	
	/**
	 * Increases the number of resource-cards of the bank by the given int-value.
	 * @param resource The resource, the player is returning to the bank.
	 * @param numberOfCardsGiven The number of Cards, the player is returning to the bank.
	 */
	public void payCardsToBankStock (Map<Resource, Long> resourcesForStructure) {
		if (resourcesForStructure.containsKey(Config.Resource.CLAY)) {
			amountOfResources.put(Config.Resource.CLAY, amountOfResources.get(Config.Resource.CLAY) + resourcesForStructure.get(Config.Resource.CLAY).intValue());
		} if (resourcesForStructure.containsKey(Config.Resource.GRAIN)) {
			amountOfResources.put(Config.Resource.GRAIN, amountOfResources.get(Config.Resource.GRAIN) + resourcesForStructure.get(Config.Resource.GRAIN).intValue());
		} if (resourcesForStructure.containsKey(Config.Resource.STONE)) {
			amountOfResources.put(Config.Resource.STONE, amountOfResources.get(Config.Resource.STONE) + resourcesForStructure.get(Config.Resource.STONE).intValue());
		} if (resourcesForStructure.containsKey(Config.Resource.WOOD)) {
			amountOfResources.put(Config.Resource.WOOD, amountOfResources.get(Config.Resource.WOOD) + resourcesForStructure.get(Config.Resource.WOOD).intValue());
		} if (resourcesForStructure.containsKey(Config.Resource.WOOL)) {
			amountOfResources.put(Config.Resource.WOOL, amountOfResources.get(Config.Resource.WOOL) + resourcesForStructure.get(Config.Resource.WOOL).intValue());
		}
	}

}
