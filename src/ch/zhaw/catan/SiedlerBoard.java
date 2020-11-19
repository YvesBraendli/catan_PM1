package ch.zhaw.catan;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;
import ch.zhaw.hexboard.Label;

public class SiedlerBoard extends HexBoard<Land, String, String, String> {
	Map<Point, Label> diceNumberForFields;

	public SiedlerBoard() {
		super();
		diceNumberForFields = new HashMap<>();
		initializeBoard();
	}

	/**
	 * Returns a map with the specified numbers for each resource-Field.
	 * @return
	 */
	public Map<Point, Label> getDiceNumberForFields() {
		return diceNumberForFields;
	}

	/**
	 * Checks if there are settlements around a resource field. If so returns a List
	 * with all the settlements for this field.
	 * 
	 * @param field The resource field, that needs to be checked, if there are
	 *              settlements around it.
	 * @return A List with all settlement for the specified resource Field.
	 * 			 Returns an empty list if there are no settlements.
	 */
	public List<String> searchFieldSettlement(Point field) {
		List<String> settlementsAroundField = new LinkedList<>();
		if (hasField(field)) {
			settlementsAroundField = getCornersOfField(field);
		}
		return settlementsAroundField;
	}

	/**
	 * Builds a new settlement at the specified point for the actual player.
	 * The new settlement is build with the String "faction" and "S" for Settlement
	 * or "C" for City.
	 * 
	 * @param buildingGround The point, where the player wants to build his new
	 *                       settlement.
	 * @param faction        The faction of the current player.
	 */
	public void createSettlement(Point buildingGround, Config.Faction faction) {
		if (getNeighboursOfCorner(buildingGround) != null || getCorner(buildingGround) != null) {
			System.err.println("Es ist nicht möglich, auf diesem Feld zu bauen. "
					+ "Bitte wählen sie ein anderes Feld aus.");
		} else {
			setCorner(buildingGround, faction.toString() + "S");
		}
	}

	/**
	 * Builds a new street between the specified point for the actual player.
	 * 
	 * @param start   The point, where the Player wants to start his road.
	 * @param end     The point, where the Player wants to end his road.
	 * @param faction The faction of the current player.
	 */
	public void createStreet(Point start, Point end, Config.Faction faction) {
		List<String> startRoads = getAdjacentEdges(start);
		List<String> endRoads = getAdjacentEdges(end);
		if (((startRoads != null && startRoads.size() < 3) 
				&& endRoads != null && endRoads.size() < 3) && getEdge(start, end) == null) {
			for (int i = 0; i < startRoads.size(); i++) {
				boolean alreadyBuiltStreet = false;
				// Richtig mit Cast von Faction zu String?
				if (((startRoads.get(i).substring(0, 2).equals(faction.toString()))
					|| (endRoads.get(i).substring(0, 2).equals(faction.toString()))) && !alreadyBuiltStreet) {
					setEdge(start, end, faction.toString());
				}
			}
		} else {
			System.err.println("Es ist nicht möglich, auf diesem Feld zu bauen. "
					+ "Bitte wählen sie ein anderes Feld aus.");
		}
	}
	
	/**
	 * Creates a new fix board, with fix resource fields and dice numbers for the
	 * fields for a siedler game.
	 */
	private void initializeBoard() {
		createFields();
		createFieldNumbers();
	}

	private void createFields() {
		addField(new Point(4, 2), Land.WATER);
		addField(new Point(6, 2), Land.WATER);
		addField(new Point(8, 2), Land.WATER);
		addField(new Point(10, 2), Land.WATER);

		addField(new Point(3, 5), Land.WATER);
		addField(new Point(5, 5), Land.FOREST);
		addField(new Point(7, 5), Land.MEADOW);
		addField(new Point(9, 5), Land.MEADOW);
		addField(new Point(11, 5), Land.WATER);

		addField(new Point(2, 8), Land.WATER);
		addField(new Point(4, 8), Land.GRAINFIELD);
		addField(new Point(6, 8), Land.MOUNTAIN);
		addField(new Point(8, 8), Land.GRAINFIELD);
		addField(new Point(10, 8), Land.FOREST);
		addField(new Point(12, 8), Land.WATER);

		addField(new Point(1, 11), Land.WATER);
		addField(new Point(3, 11), Land.FOREST);
		addField(new Point(5, 11), Land.CLAYSOIL);
		addField(new Point(7, 11), Land.DESERT);
		addField(new Point(9, 11), Land.MOUNTAIN);
		addField(new Point(11, 11), Land.GRAINFIELD);
		addField(new Point(13, 11), Land.WATER);

		addField(new Point(2, 14), Land.WATER);
		addField(new Point(4, 14), Land.GRAINFIELD);
		addField(new Point(6, 14), Land.MOUNTAIN);
		addField(new Point(8, 14), Land.FOREST);
		addField(new Point(10, 14), Land.MEADOW);
		addField(new Point(12, 14), Land.WATER);

		addField(new Point(3, 17), Land.WATER);
		addField(new Point(5, 17), Land.CLAYSOIL);
		addField(new Point(7, 17), Land.MEADOW);
		addField(new Point(9, 17), Land.CLAYSOIL);
		addField(new Point(11, 17), Land.WATER);

		addField(new Point(4, 20), Land.WATER);
		addField(new Point(6, 20), Land.WATER);
		addField(new Point(8, 20), Land.WATER);
		addField(new Point(10, 20), Land.WATER);
	}

	private void createFieldNumbers() {
		diceNumberForFields.put(new Point(5, 5), new Label('0', '6'));
		diceNumberForFields.put(new Point(7, 5), new Label('0', '3'));
		diceNumberForFields.put(new Point(9, 5), new Label('0', '8'));
		
		diceNumberForFields.put(new Point(4, 8), new Label('0', '2'));
		diceNumberForFields.put(new Point(6, 8), new Label('0', '4'));
		diceNumberForFields.put(new Point(8, 8), new Label('0', '5'));
		diceNumberForFields.put(new Point(10, 8), new Label('1','0'));
		
		diceNumberForFields.put(new Point(3, 11), new Label('0', '5'));
		diceNumberForFields.put(new Point(5, 11), new Label('0', '9'));
		diceNumberForFields.put(new Point(9, 11), new Label('0', '6'));
		diceNumberForFields.put(new Point(11, 11), new Label('0', '9'));
		
		diceNumberForFields.put(new Point(4, 14), new Label('1', '0'));
		diceNumberForFields.put(new Point(6, 14), new Label('1', '1'));
		diceNumberForFields.put(new Point(8, 14), new Label('0', '3'));
		diceNumberForFields.put(new Point(10, 14), new Label('1', '2'));
		
		diceNumberForFields.put(new Point(5, 17), new Label('0', '8'));
		diceNumberForFields.put(new Point(7, 17), new Label('0', '4'));
		diceNumberForFields.put(new Point(9, 17), new Label('1', '1'));
	}

}
