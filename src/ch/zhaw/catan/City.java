package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;


/**
 * This class creates a new city for the current player.
 * It holds the information about how many additional winning points
 * and resource-cards a player can get for this building.
 * @author Meier Robin, Moser Nadine, Braendli Yves
 * 
 *
 */
public class City extends Settlement {
	
	private int additionalCityResourceCardsForPayout;

	
	/**
	 * Constructor for the city class.
	 * @param owner The Faction of the current player, which is the owner of the city.
	 */
	public City(Faction owner) {
		super(owner);
		additionalCityResourceCardsForPayout = 1;
	}
	
	/**
	 * Returns the number of resource-cards, a player gets for the city.
	 * @return The number of resource-cards.
	 */
	public int getNumberOfResourcesForPayout() {
		return super.getNumberOfResourcesForPayout() + additionalCityResourceCardsForPayout;
	}
	
	/**
	 * Returns the faction of the owner of this settlement.
	 * @return The faction of the settlement-owner.
	 */
	@Override
	public String toString() {
		return super.toString().toUpperCase();
	}
	

}
