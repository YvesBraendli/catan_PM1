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
	private int numberOfWinningpoints;
	private int numberOfResourcesForPayout;
	private String faction;
	
	/**
	 * Constructs a new settlement for the specified owner.
	 * @param owner The faction of the owner, who created the settlement.
	 */
	public Settlement(Faction owner) {
		faction = owner.toString();
		numberOfResourcesForPayout = 1;
		numberOfWinningpoints = 1;
	}

	/**
	 * Returns the faction of the owner of this settlement.
	 * @return The faction of the settlement-owner.
	 */
	public String toString() {
		return faction;
	}

	/**
	 * Returns a number, which stands for the winning points, which the settlement gives to the owner.
	 * @return The number of winning points for a settlement.
	 */
	public int getNumberOfWinningpoints() {
		return numberOfWinningpoints;
	}
	
	/**
	 * Returns a number, which stands for the number of resource-cards, the owner gets with
	 * every settlement per resource-field.
	 * @return The number of resource-cards, the owner gets for a settlement.
	 */
	public int getNumberOfResourcesForPayout() {
		return numberOfResourcesForPayout;
	}
}

