package ch.zhaw.catan;

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
		amountOfResources = Config.INITIAL_RESOURCE_CARDS_BANK;
	}

	/**
	 * Checks, if there were 4 given cards and enough cards in the bank stock. If
	 * so, returns true and deducts number of resource-cards from the bank.
	 * 
	 * @param resourcegiven  The resource, the player wants to trade.
	 * @param givencards     The number of the cards, the player is giving.
	 * @param resourcewanted The resource, the player wants to have.
	 * @return true, if the trade for one card was successful.
	 */
	public boolean trade(Resource resourcegiven, int givencards, Resource resourcewanted) {
		boolean tradesuccessful = false;
		if (givencards == 4 && amountOfResources.get(resourcewanted) >= 1) {
			tradesuccessful = true;
			amountOfResources.put(resourcewanted, amountOfResources.get(resourcewanted) - 1);
		}
		return tradesuccessful;
	}

}
