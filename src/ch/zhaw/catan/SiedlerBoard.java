package ch.zhaw.catan;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

public class SiedlerBoard extends HexBoard<Land, Settlement, String, String> {
	
private Settlement settlement;
private City city;

	public SiedlerBoard() {
		super();
		createFields();
		
	}

	/**
	 * Checks if there are settlements around a resource field. If so returns a List
	 * with all the settlements for this field.
	 * 
	 * @param field The resource field, that needs to be checked, if there are
	 *              settlements around it.
	 * @return A HashMap with the key for the Factions a list with String values,
	 *         which represents the settlement.
	 */
	public HashMap<Faction, Integer> searchFieldSettlement(Point field) {
		// HashMap, key: faction, value: String (zu Beginn) oder List(Settlement/City)

		HashMap<Faction, Integer> settlementsAroundField = new HashMap<>();
		if (hasField(field)) {
			List<Settlement> buildingsAroundField = getCornersOfField(field);
			Faction[] factions = { Faction.BLUE, Faction.GREEN, Faction.RED, Faction.YELLOW };
			// String faction = Config.Faction.
			for (int z = 0; z < factions.length; z++) {
				for (int i = 0; i < buildingsAroundField.size(); i++) {
					int buildingsForFaction = 0;
					if (buildingsAroundField.get(i).toString().substring(0, 2).equals(factions[z].toString())) {
						buildingsForFaction += 1;
						Integer currentValue = settlementsAroundField.get(factions[z]);
						if(currentValue == null) currentValue = 0;
						settlementsAroundField.put(factions[z], currentValue + buildingsForFaction);
					}
				}
			}
		}
		return settlementsAroundField;
	}

	/**
	 * Builds a new settlement at the specified point for the actual player. The new
	 * settlement is build with the String "faction" and "S" for Settlement or "C"
	 * for City.
	 * 
	 * @param buildingGround The point, where the player wants to build his new
	 *                       settlement.
	 * @param faction        The faction of the current player.
	 */
	public boolean createSettlement(Point buildingGround, Config.Faction faction) {
		boolean successful = false;
		if (hasCorner(buildingGround) && hasLandAsFieldAround(buildingGround)) {
			if (getNeighboursOfCorner(buildingGround).isEmpty() && getCorner(buildingGround) == null) {
				setCorner(buildingGround, settlement = new Settlement(faction));
				successful = true;
			}
		}
		return successful;
	}

	private boolean hasLandAsFieldAround(Point buildingGround) {
		if ((buildingGround.x <= 12 && buildingGround.x >= 2 && buildingGround.y <= 19 && buildingGround.y >= 3)) {
			if (((buildingGround.x == 12 || buildingGround.x == 2)
					&& (buildingGround.y == 4 || buildingGround.y == 6 || buildingGround.y == 16 || buildingGround.y == 8))
					|| ((buildingGround.y == 3 || buildingGround.y == 19)
							&& (buildingGround.x == 3 || buildingGround.x == 11))) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Builds a new street between the specified point for the actual player.
	 * 
	 * @param start   The point, where the Player wants to start his road.
	 * @param end     The point, where the Player wants to end his road.
	 * @param faction The faction of the current player.
	 */
	public boolean createStreet(Point start, Point end, Faction faction) {
		if (hasEdge(start, end)) {
			List<String> startRoads = getAdjacentEdges(start);
			List<String> endRoads = getAdjacentEdges(end);
			if (startRoads != null && startRoads.size() > 0 && startRoads.size() < 3 && getEdge(start, end) == null) {
				for (int i = 0; i < startRoads.size(); i++) {
					if ((startRoads.get(i).substring(0, 2).equals(faction.toString()))) {
						setEdge(start, end, faction.toString());
						return true;
					}
				}
			}
			if (endRoads != null && endRoads.size() > 0 && endRoads.size() < 3 && getEdge(start, end) == null) {
				for (int i = 0; i < endRoads.size(); i++) {
					if ((endRoads.get(i).substring(0, 2).equals(faction.toString()))) {
						setEdge(start, end, faction.toString());
						return true;
					}
				}
			}
			if (hasSettlementAtStartOrEnd(start, end, faction)) {
				setEdge(start, end, faction.toString());
				return true;
			}

		}
		return false;
	}

	private boolean hasSettlementAtStartOrEnd(Point start, Point end, Faction faction) {
		String startCorner = null;
		String endCorner = null;
		if (getCorner(start) != null) {
			startCorner = getCorner(start).toString();
		}
		if (getCorner(end) != null) {
			endCorner = getCorner(end).toString();
		} 
		boolean hisOwnHouse = false;
		if (startCorner != null) {
			hisOwnHouse = (startCorner.substring(0, 2).equals(faction.toString()));
		}
		if (endCorner != null) {
			hisOwnHouse = (hisOwnHouse || endCorner.substring(0, 2).equals(faction.toString()));
		}
		return hisOwnHouse;
	}

	private void createFields() {
		int maxFieldCoordinateX = 10;
		int minFieldCoordinateX = 5;
		int rowCounter = 0;
		for (int fieldCoordinateY = 2; fieldCoordinateY < 23; fieldCoordinateY += 3) {
			if (rowCounter <= 3) {
				maxFieldCoordinateX += 1;
				minFieldCoordinateX -= 1;
			} else {
				maxFieldCoordinateX -= 1;
				minFieldCoordinateX += 1;
			}
			for (int fieldCoordinateX = minFieldCoordinateX; fieldCoordinateX < maxFieldCoordinateX; fieldCoordinateX += 2) {
				addField(new Point(fieldCoordinateX, fieldCoordinateY),
						Config.getStandardLandPlacement().get(new Point(fieldCoordinateX, fieldCoordinateY)));
			}
			rowCounter++;
		}
	}
}
