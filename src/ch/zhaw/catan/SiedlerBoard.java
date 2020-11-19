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

	public SiedlerBoard() {
		super();
		initializeBoard();
	}

	/**
	 * Checks if there are settlements around a resource field. If so returns a List
	 * with all the settlements for this field.
	 * 
	 * @param field The resource field, that needs to be checked, if there are
	 *              settlements around it.
	 * @return A List with all settlement for the specified resource Field. Returns
	 *         an empty list if there are no settlements.
	 */
	public List<String> searchFieldSettlement(Point field) {
		List<String> settlementsAroundField = new LinkedList<>();
		if (hasField(field)) {
			settlementsAroundField = getCornersOfField(field);
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
		if (getNeighboursOfCorner(buildingGround) != null || getCorner(buildingGround) != null) {
			System.err.println(
					"Es ist nicht möglich, auf diesem Feld zu bauen. " + "Bitte wählen sie ein anderes Feld aus.");
			successful = true;
		} else {
			setCorner(buildingGround, faction.toString() + "S");
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
		List<String> startRoads = getAdjacentEdges(start);
		List<String> endRoads = getAdjacentEdges(end);
		if (((startRoads != null && startRoads.size() < 3) && endRoads != null && endRoads.size() < 3)
				&& getEdge(start, end) == null) {
			for (int i = 0; i < startRoads.size(); i++) {
				boolean alreadyBuiltStreet = false;
				// Richtig mit Cast von Faction zu String?
				if (((startRoads.get(i).substring(0, 2).equals(faction.toString()))
						|| (endRoads.get(i).substring(0, 2).equals(faction.toString()))) && !alreadyBuiltStreet) {
					setEdge(start, end, faction.toString());
					alreadyBuiltStreet = true;
					successful = true;
				}
			}
		} else {
			System.err.println(
					"Es ist nicht möglich, auf diesem Feld zu bauen. " + "Bitte wählen sie ein anderes Feld aus.");
		}
		return successful;
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
		int maxFieldCoordinateX = 11;
		int fieldCoordinateY = 2;
		for (int x = 4; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX += 1;
		fieldCoordinateY += 3;
		for (int x = 3; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX += 1;
		fieldCoordinateY += 3;
		for (int x = 2; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX += 1;
		fieldCoordinateY += 3;
		for (int x = 1; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX -= 1;
		fieldCoordinateY += 3;
		for (int x = 2; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX -= 1;
		fieldCoordinateY += 3;
		for (int x = 3; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

		maxFieldCoordinateX -= 1;
		fieldCoordinateY += 3;
		for (int x = 4; x < maxFieldCoordinateX; x += 2) {
			addField(new Point(x, fieldCoordinateY),
					Config.getStandardLandPlacement().get(new Point(x, fieldCoordinateY)));
		}

}
