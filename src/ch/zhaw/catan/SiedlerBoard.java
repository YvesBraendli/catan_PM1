package ch.zhaw.catan;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

public class SiedlerBoard extends HexBoard<Land, Settlement, String, String> {

	public SiedlerBoard() {
		super();
		createFields();

	}

	/**
	 * Creates a city, if the player already has a settlement at the specified
	 * corner.
	 * 
	 * @param buildingGround The point-value of the corner, where the player wants
	 *                       to build a city.
	 * @param faction        The faction of the current player.
	 * @return True, if the placement of the city was successful.
	 */
	public boolean createCity(Point buildingGround, Faction faction) {
		if (getCorner(buildingGround) != null && !(getCorner(buildingGround) instanceof City)) {
			Settlement settlement = getCorner(buildingGround);
			if (settlement.getFaction() == faction) {
				setCorner(buildingGround, new City(faction));
				return true;
			}
		}
		return false;
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
	public HashMap<Faction, ArrayList<Settlement>> searchFieldSettlement(Point field) {
		HashMap<Faction, ArrayList<Settlement>> buildings = new HashMap<>();
		if (hasField(field)) {
			List<Settlement> buildingsAroundField = getCornersOfField(field);
			Faction[] factions = { Faction.BLUE, Faction.GREEN, Faction.RED, Faction.YELLOW };
			for (Faction faction: factions) {
				ArrayList<Settlement> buildingsForFaction = new ArrayList<>();
				for (Settlement building: buildingsAroundField) {
					if (building.toString().equals(faction.toString())) {
						buildingsForFaction.add(building);
					}
				}
				buildings.put(faction, buildingsForFaction);
			}
		}
		return buildings;
	}

	/**
	 * Builds a new settlement at the specified point for the actual player. The new
	 * settlement is build with an instance of the class settlement.
	 * 
	 * @param buildingGround The point, where the player wants to build his new
	 *                       settlement.
	 * @param faction        The faction of the current player.
	 */
	public boolean createSettlement(Point buildingGround, Config.Faction faction) {
		boolean successful = false;
		if (hasCorner(buildingGround) && hasLandAsFieldAround(buildingGround) 
				&& !(getAdjacentEdges(buildingGround).isEmpty()) && getAdjacentEdges(buildingGround).contains(faction.toString())) {
			if (getNeighboursOfCorner(buildingGround).isEmpty() && getCorner(buildingGround) == null) {
				setCorner(buildingGround, new Settlement(faction));
				successful = true;
			}
		}
		return successful;
	}
	
	/**
	 * Builds a new initial settlement at the specified point for the actual player. The new
	 * settlement is build with an instance of the class settlement.
	 * 
	 * @param buildingGround The point, where the player wants to build his new
	 *                       settlement.
	 * @param faction        The faction of the current player.
	 */
	public boolean createInitialSettlement(Point buildingGround, Config.Faction faction) {
		boolean successful = false;
		if (hasCorner(buildingGround) && hasLandAsFieldAround(buildingGround)) {
			if (getNeighboursOfCorner(buildingGround).isEmpty() && getCorner(buildingGround) == null) {
				setCorner(buildingGround, new Settlement(faction));
				successful = true;
			}
		}
		return successful;
	}

	private boolean hasLandAsFieldAround(Point buildingGround) {
		if ((buildingGround.x <= 12 && buildingGround.x >= 2 && buildingGround.y <= 19 && buildingGround.y >= 3)) {
			if (((buildingGround.x == 12 || buildingGround.x == 2) && (buildingGround.y == 4 || buildingGround.y == 6
					|| buildingGround.y == 16 || buildingGround.y == 8))
					|| ((buildingGround.y == 3 || buildingGround.y == 19)
							&& (buildingGround.x == 3 || buildingGround.x == 11))) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the thief to a new corner, which has to has a settlement.
	 * 
	 * @param field The Point value of the field, in which the player wants to put the thief.
	 * @return The faction of the player from whom cards are stolen by the thief or null, if there is no
	 * building around the specified field.
	 */
	public Faction setThief(Point field) {
		List<Settlement> buildingsAroundField = getCornersOfField(field);
		if (buildingsAroundField.isEmpty()) return null;
		Random random = new Random();
		int index = random.nextInt(buildingsAroundField.size());
		Faction faction = buildingsAroundField.get(index).getFaction();
		return faction;
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
			if (startRoads != null && startRoads.size() > 0 && startRoads.size() < 3 && getEdge(start, end) == null 
					&& hasLandAsFieldAround(start) && hasLandAsFieldAround(end)) {
				for (int i = 0; i < startRoads.size(); i++) {
					if ((startRoads.get(i).substring(0, 2).equals(faction.toString()))) {
						setEdge(start, end, faction.toString());
						return true;
					}
				}
			}
			if (endRoads != null && endRoads.size() > 0 && endRoads.size() < 3 && getEdge(start, end) == null 
					&& hasLandAsFieldAround(start) && hasLandAsFieldAround(end)) {
				for (int i = 0; i < endRoads.size(); i++) {
					if ((endRoads.get(i).substring(0, 2).equals(faction.toString()))) {
						setEdge(start, end, faction.toString());
						return true;
					}
				}
			}
			if (hasSettlementAtStartOrEnd(start, end, faction) && hasLandAsFieldAround(start) 
					&& getEdge(start, end) == null && hasLandAsFieldAround(end)) {
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
