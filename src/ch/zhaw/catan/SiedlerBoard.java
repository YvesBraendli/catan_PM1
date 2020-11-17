package ch.zhaw.catan;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

public class SiedlerBoard extends HexBoard<Land, String, String, String> {
	
	/**
	 * Creates a new fix board, with fix resource 
	 * fields and dice numbers for the fields for a siedler game.
	 */
	public void initializeBoard() {
		
	}
	
	/**
	 * Checks if there are settlements around a resource field.
	 * If so returns a Map with all the settlements for this field and their faction.
	 * @param field The resource field, that needs to be checked, if there are settlements around it.
	 * @return A Map with the current settlements and their faction for the specified resource field.
	 */
	public Map<Config.Faction, Integer> searchFieldSettlement (Point field) {
		Map<Config.Faction, Integer> settlementsAroundField = new HashMap<>();
		
		
		return settlementsAroundField;
	}

	/**
	 * Checks, if a building ground for a new settlement is vaLid.
	 * @param bulidingGround The point, where the Player wants to build a settlement.
	 * @return true, if the building ground for the new settlement is valid.
	 */
	public boolean checkIfValidSettlementBuildingGround(Point bulidingGround) {
		boolean buildingGroundValid = false;
		
		return buildingGroundValid;
	}
	
	/**
	 * Checks, if a building ground for a new settlement is vaLid.
	 * @param start The point, where the Player wants to start his road.
	 * @param end The point, where the Player wants to end his road.
	 * @return true, if the building ground for the new street is valid.
	 */
	public boolean checkIfValidStreeBuildingGround(Point start, Point end) {
		boolean buildingGroundValid = false;
		
		return buildingGroundValid;
	}
	
	/**
	 * Builds a new settlement at the specified point for the actual player.
	 * @param buildingGround The point, where the player wants to build his new settlement.
	 * @param faction The faction of the current player.
	 */
	public void buildSettlement (Point buildingGround, Config.Faction faction) {
		
	}
	
	/**
	 * Builds a new street between the specified point for the actual player.
	 * @param start The point, where the Player wants to start his road.
	 * @param end The point, where the Player wants to end his road.
	 * @param faction The faction of the current player.
	 */
	public void buildStreet (Point start, Point end, Config.Faction faction) {
		
	}

	
}
