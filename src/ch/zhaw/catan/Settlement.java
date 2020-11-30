package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;

/**
 * This class creates a new settlement for the current player.
 * It holds the information about how many winning points the settlement gives
 * and the information about the owner of the settlement.
 * @author Braendli Yves, Meier Robin, Moser Nadine
 *
 */

public class Settlement {
	private int numberOfResourcesForPayout;
	private String owner;
	private Faction faction;
	
	/**
	 * Constructs a new settlement for the specified owner.
	 * @param owner The faction of the owner, who created the settlement.
	 */
	public Settlement(Faction faction) {
		this.faction = faction;
		owner = faction.toString();
		numberOfResourcesForPayout = 1;
	}

	/**
	 * Returns the faction of the owner of this settlement.
	 * @return The faction of the settlement-owner.
	 */
	@Override
	public String toString() {
		return owner;
	}
	
	/**
	 * Returns a number, which stands for the number of resource-cards, the owner gets with
	 * every settlement per resource-field.
	 * @return The number of resource-cards, the owner gets for a settlement.
	 */
	public int getNumberOfResourcesForPayout() {
		return numberOfResourcesForPayout;
	}
	
	/**
	 * Returns the faction value for the owner of the building.
	 * @return The faction of the owner.
	 */
	public Faction getFaction() {
		return faction;
	}
}

