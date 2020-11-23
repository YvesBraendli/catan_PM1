package ch.zhaw.catan;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

public class SiedlerBoard extends HexBoard<Land, String, String, String> {

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
	public HashMap<Config.Faction, String[]> searchFieldSettlement(Point field) {
		// HashMap, key: faction, value: String (zu Beginn) oder List(Settlement/City)

		HashMap<Config.Faction, String[]> settlementsAroundField = new HashMap<>();
		if (hasField(field)) {
			List<String> buildingsAroundField = getCornersOfField(field);
			Config.Faction[] factions = { Config.Faction.BLUE, Config.Faction.GREEN, Config.Faction.RED,
					Config.Faction.YELLOW };
			// String faction = Config.Faction.
			for (int z = 0; z < factions.length; z++) {
				for (int i = 0; i < buildingsAroundField.size(); i++) {
					String[] buildingsForFaction = new String[3];
					if (buildingsAroundField.get(i).substring(0, 2).equals(factions[z].toString())) {
						buildingsForFaction[0] = buildingsAroundField.get(i);
					}
					settlementsAroundField.put(factions[z], buildingsForFaction);
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
		if (hasCorner(buildingGround)) {
			System.out.print(getNeighboursOfCorner(buildingGround));
			if (getNeighboursOfCorner(buildingGround).isEmpty() && getCorner(buildingGround) == null) {
				setCorner(buildingGround, faction.toString() + "S");
				successful = true;
			}
		}
		return successful;
	}

	/**
	 * Builds a new street between the specified point for the actual player.
	 * 
	 * @param start   The point, where the Player wants to start his road.
	 * @param end     The point, where the Player wants to end his road.
	 * @param faction The faction of the current player.
	 */
	public boolean createStreet(Point start, Point end, Config.Faction faction) {
		boolean successful = false;
		if (hasEdge(start, end)) {
			List<String> startRoads = getAdjacentEdges(start);
			List<String> endRoads = getAdjacentEdges(end);
			if (((startRoads != null && startRoads.size() < 3) && endRoads != null && endRoads.size() < 3)
					&& getEdge(start, end) == null) {
				for (int i = 0; i < startRoads.size(); i++) {
					boolean alreadyBuiltStreet = false;
					if ((startRoads.get(i).substring(0, 2).equals(faction.toString())) && !alreadyBuiltStreet) {
						setEdge(start, end, faction.toString());
						alreadyBuiltStreet = true;
						successful = true;
					}
				}
				for (int i = 0; i < endRoads.size(); i++) {
					boolean alreadyBuiltStreet = false;
					if ((endRoads.get(i).substring(0, 2).equals(faction.toString())) && !alreadyBuiltStreet) {
						setEdge(start, end, faction.toString());
						alreadyBuiltStreet = true;
						successful = true;
					}
				}
			}
		}
		return successful;
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
