package ch.zhaw.catan;

import java.util.HashMap;

import ch.zhaw.catan.Config.Faction;

/**
 * This class creates a player and holds his specific parameters for the siedler
 * game. It has the following functions: - Creating a player and adding a
 * faction to the player. - Get the amount of stock Roads, Settlements, Cities
 * and Resource-Cards of the player. - Decrease the amount of stock Roads,
 * Settlements and Cities of the player. - Add or remove Resource-Cards from the
 * player. - Get the faction of the player.
 * 
 * @author yvesb
 *
 */

public class Player {
	private int numberOfWinningpoints;
	private HashMap<Config.Resource, Integer> amountOfResources;
	private int currentNumberOfRoads;
	private int currentNumberOfSettlements;
	private int currentNumberOfCities;
	private Config.Faction faction;

	/**
	 * Creates a new player and sets the corresponding faction for him.
	 * 
	 * @param faction The corresponding faction for the actual player.
	 */
	public Player(Faction faction) {
		this.faction = faction;
	}

	/**
	 * Returns a a number which stands for the current amount of winning points for
	 * the current player.
	 * 
	 * @return The number of winning points of the player.
	 */
	public int getNumberOfWinningpoints() {
		return numberOfWinningpoints;
	}

	/**
	 * Increases the number of winning points for the current player.
	 * 
	 * @param numberOfWinningpoints additional winning points for the player.
	 */
	public void setNumberOfWinningpoints(int numberOfWinningpoints) {
		this.numberOfWinningpoints = numberOfWinningpoints;
	}

	/**
	 * Returns a map with the current amount of stock cards for the current player.
	 * 
	 * @return The map with the current amount of stock cards per resource for the
	 *         player.
	 */
	public HashMap<Config.Resource, Integer> getAmountOfResources() {
		return amountOfResources;
	}

	/**
	 * Increases or decreases the number of stock cards for the current player.
	 * 
	 * @param resource      The current resource, that needs to be adapted.
	 * @param numberOfCards The number of the resource cards, that need to be added
	 *                      or taken away.
	 * @param getsCard      True, if the player gets cards and false if the cards
	 *                      are taken away.
	 */
	public void setAmountOfResources(Config.Resource resource, int numberOfCards, boolean getsCards) {

	}

	/**
	 * Returns a number which stands for the current number of stock streets for the
	 * current player.
	 * 
	 * @return The number of stock streets of the player.
	 */
	public int getCurrentNumberOfRoads() {
		return currentNumberOfRoads;
	}

	/**
	 * Decreases the number of stock roads per player.
	 * 
	 * @param numberOfRoads The number of roads, which has to be taken away from the
	 *                      stock of the player.
	 */
	public void setCurrentNumberOfRoads(int numberOfRoads) {

	}

	/**
	 * Returns a number which stands for the current number of stock settlements for
	 * the current player.
	 * 
	 * @return The number of stock settlements of the player.
	 */
	public int getCurrentNumberOfSettlements() {
		return currentNumberOfSettlements;
	}

	/**
	 * Decreases the number of stock settlements per player.
	 * 
	 * @param numberOfSettlements The number of settlements, which has to be taken
	 *                            away from the player's stock.
	 */
	public void setCurrentNumberOfSettlements(int numberOfSettlements) {

	}

	/**
	 * Returns a number which stands for the current number of stock cities for the
	 * current player.
	 * 
	 * @return The number of stock cities of the player.
	 */
	public int getCurrentNumberOfCities() {
		return currentNumberOfCities;
	}

	/**
	 * Decreases the number of stock cities per player.
	 * 
	 * @param numberOfCities The number of cities, which has to be taken away from
	 *                       the player's stock.
	 */
	public void setCurrentNumberOfCities(int numberOfCities) {

	}

	/**
	 * Returns the faction of the player.
	 * 
	 * @return The faction of the current player
	 */
	public Config.Faction getFaction() {
		return faction;
	}

}
