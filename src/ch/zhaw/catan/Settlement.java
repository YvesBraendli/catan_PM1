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
	private static final int NUMBER_OF_WINNINGPOINTS = 2;
	private Config.Faction owner;
	
	/**
	 * Constructs a new settlement for the specified owner.
	 * @param owner The faction of the owner, who created the settlement.
	 */
	public Settlement(Faction owner) {
		this.owner = owner;
	}

	/**
	 * Returns the faction of the owner of this settlement.
	 * @return The faction of the settlement-owner.
	 */
	public Config.Faction getOwner() {
		return owner;
	}

	/**
	 * Returns a number, which stands for the winning points, which the settlement gives to the owner.
	 * @return The number of winning points for a settlement.
	 */
	public static int getNumberOfWinningpoints() {
		return NUMBER_OF_WINNINGPOINTS;
	}
	
	

}