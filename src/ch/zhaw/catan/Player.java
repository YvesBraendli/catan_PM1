package ch.zhaw.catan;

import java.util.HashMap;
import java.util.Random;
import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import java.util.ArrayList;

/**
 * This class creates a player and holds his specific parameters for the siedler
 * game. It has the following functions: - Creating a player and adding a
 * faction to the player. - Get the amount of stock Roads, Settlements, Cities
 * and Resource-Cards of the player. - Decrease the amount of stock Roads,
 * Settlements and Cities of the player. - Add or remove Resource-Cards from the
 * player. - Get the faction of the player.
 * 
 * @author Moser Nadine, Meier Robin, Bràndli Yves
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
		amountOfResources = new HashMap<Config.Resource, Integer>();
		currentNumberOfSettlements = Config.Structure.SETTLEMENT.getStockPerPlayer();
		currentNumberOfRoads = Config.Structure.ROAD.getStockPerPlayer();
		currentNumberOfCities = Config.Structure.CITY.getStockPerPlayer();
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
	public HashMap<Resource, Integer> getAmountOfResources() {
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
	public void setAmountOfResources(Resource resource, int numberOfCards, boolean getsCards) {
		Integer currentValue = amountOfResources.get(resource);
		if (currentValue == null) {
			currentValue = 0;
		}

		int newValue = currentValue;
		if (getsCards) {
			newValue = currentValue.intValue() + numberOfCards;
		} else {
			newValue = currentValue.intValue() - numberOfCards;
		}
		amountOfResources.put(resource, newValue);
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
		currentNumberOfRoads -= numberOfRoads;
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
		currentNumberOfSettlements -= numberOfSettlements;
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
		currentNumberOfCities -= numberOfCities;
	}

	/**
	 * Returns the faction of the player.
	 * 
	 * @return The faction of the current player
	 */
	public Faction getFaction() {
		return faction;
	}

	/**
	 * Selects a random resource, that the player has in his hands
	 * and returns this resource-value.
	 * 
	 * @return An random resource-value, which the player owns cards to. Returns
	 * null if he owns no cards in his hands.
	 */
	public Resource selectRandomResource() {
		ArrayList<Resource> availableResources = selectAvailableResources();
		if (availableResources.isEmpty()) return null;
		Random random = new Random();
		int index = random.nextInt(availableResources.size());
		Resource resource = availableResources.get(index);
		return resource;

	}

	private ArrayList<Resource> selectAvailableResources() {
		ArrayList<Resource> availableResources = new ArrayList<>();
		if (amountOfResources.containsKey(Config.Resource.CLAY) && amountOfResources.get(Config.Resource.CLAY) >= 1) {
			availableResources.add(Resource.CLAY);
		}
		if (amountOfResources.containsKey(Config.Resource.GRAIN) && amountOfResources.get(Config.Resource.GRAIN) >= 1) {
			availableResources.add(Resource.GRAIN);
		}
		if (amountOfResources.containsKey(Config.Resource.STONE) && amountOfResources.get(Config.Resource.STONE) >= 1) {
			availableResources.add(Resource.STONE);
		}
		if (amountOfResources.containsKey(Config.Resource.WOOD) && amountOfResources.get(Config.Resource.WOOD) >= 1) {
			availableResources.add(Resource.WOOD);
		}
		if (amountOfResources.containsKey(Config.Resource.WOOL) && amountOfResources.get(Config.Resource.WOOL) >= 1) {
			availableResources.add(Resource.WOOL);
		}
		return availableResources;
	}

}
