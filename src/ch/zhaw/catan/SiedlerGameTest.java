package ch.zhaw.catan;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.catan.Config.Resource;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * This class test the public methods from SiedlerGame.java.
 * Following Equivalence Partitionings are tested:
 * 
 * Negative
 * 		1	Illegal placement position
 *		3	Build with not enough resources
 *		6	Trade where player does not have enough resources
 *		7	Trade where bank does not have enough resources
 *		8	No player has won game
 *		11	Robbery no player has more than 7 resources
 *		14	Thief placement at field with no settlements around
 *		16	Resources are payed out
 *		17	No Resources are payed out
 *		19	player has no structures left to build
 * 
 * Positive
 * 		2	Legal placement position
 * 		4	Build with enough resources
 * 		5	Trade where player and bank have enough resources
 * 		9	One player has won game
 * 		10	More than one player has won game
 * 		12	Robbery one player has more than 7 resources
 * 		13	Robbery more than one player has 7 resources
 * 		15	Thief placement at field with one settlement around
 * 		18	Player structures left to build
 * 		20	initialize board
 * 		21  player switch to next player
 * 		22  player switch to previous player
 * 
 * 
 * @author Moser Nadine, Meier Robin, Brändli Yves
 *
 */
public class SiedlerGameTest {
	private SiedlerGame model;
	private static final int WIN_POINTS_5 = 5;
	private static final int PLAYERS = 3;
	private static final int MAX_DICE_VALUE = 12;
	private static final int MIN_DICE_VALUE = 2;

	private static final Map<Faction, Tuple<Point, Point>> first = Map.of(Faction.values()[0],
			new Tuple<>(new Point(5, 7), new Point(6, 6)), Faction.values()[1],
			new Tuple<>(new Point(11, 13), new Point(12, 12)), Faction.values()[2],
			new Tuple<>(new Point(2, 12), new Point(2, 10)));
	private static final Map<Faction, Tuple<Point, Point>> second = Map.of(Faction.values()[0],
			new Tuple<>(new Point(10, 16), new Point(9, 15)), Faction.values()[1],
			new Tuple<>(new Point(5, 15), new Point(5, 13)), Faction.values()[2],
			new Tuple<>(new Point(7, 19), new Point(8, 18)));

	private Map<Integer, Map<Faction, List<Resource>>> expectedDiceThrowPayout = Map.of(2,
			Map.of(Faction.values()[0], List.of(Resource.GRAIN), Faction.values()[1], List.of(), Faction.values()[2],
					List.of()),
			3, Map.of(Faction.values()[0], List.of(), Faction.values()[1], List.of(), Faction.values()[2], List.of()),
			4,
			Map.of(Faction.values()[0], List.of(Resource.STONE), Faction.values()[1], List.of(), Faction.values()[2],
					List.of(Resource.CLAY)),
			5,
			Map.of(Faction.values()[0], List.of(), Faction.values()[1], List.of(), Faction.values()[2],
					List.of(Resource.WOOD)),
			6,
			Map.of(Faction.values()[0], List.of(Resource.WOOD), Faction.values()[1], List.of(), Faction.values()[2],
					List.of()),
			8,
			Map.of(Faction.values()[0], List.of(), Faction.values()[1], List.of(Resource.WOOL), Faction.values()[2],
					List.of()),
			9,
			Map.of(Faction.values()[0], List.of(), Faction.values()[1], List.of(Resource.GRAIN), Faction.values()[2],
					List.of()),
			10,
			Map.of(Faction.values()[0], List.of(), Faction.values()[1], List.of(Resource.GRAIN), Faction.values()[2],
					List.of()),
			11,
			Map.of(Faction.values()[0], List.of(Resource.CLAY), Faction.values()[1], List.of(Resource.STONE),
					Faction.values()[2], List.of()),
			12, Map.of(Faction.values()[0], List.of(Resource.WOOL), Faction.values()[1], List.of(Resource.WOOL),
					Faction.values()[2], List.of()));

	private Map<Faction, Map<Resource, Integer>> initialResourceStockThreePlayerBoardStandard = Map.of(
			Faction.values()[0], Map.of(Resource.WOOL, 1, Resource.CLAY, 1), Faction.values()[1],
			Map.of(Resource.GRAIN, 1, Resource.WOOL, 1, Resource.STONE, 1), Faction.values()[2],
			Map.of(Resource.CLAY, 1));

	/**
	 * Equivalence Partitioning	21,22
	 * Test method: switchToNextPlayer() and switchToPreviousPlayer()
	 * already given tests.
	 */
	@Test
	public void requirementPlayerSwitching() {
		for (int players : List.of(2, 3, 4)) {
			initializeSiedlerGame(WIN_POINTS_5, players);
			assertTrue(players == model.getPlayerFactions().size(), "Wrong number of players returned by getPlayers()");
			// Switching forward
			for (int i = 0; i < players; i++) {
				assertEquals(Config.Faction.values()[i], model.getCurrentPlayerFaction(),
						"Player order does not match order of Faction.values()");
				model.switchToNextPlayer();
			}
			assertEquals(Config.Faction.values()[0], model.getCurrentPlayerFaction(),
					"Player wrap-around from last player to first player did not work.");
			// Switching backward
			for (int i = players - 1; i >= 0; i--) {
				model.switchToPreviousPlayer();
				assertEquals(Config.Faction.values()[i], model.getCurrentPlayerFaction(),
						"Switching players in reverse order does not work as expected.");
			}
		}
	}

	/**
	 * Equivalence Partitioning	20
	 * tests if the board is correctly set up at the beginning of the game.
	 * already given tests.
	 */
	@Test
	public void requirementSetupTestBoardUsedWithTheTests() {
		requirementPlayerSwitching();
		bootstrapTestBoardForThreePlayersStandard(WIN_POINTS_5);

		// Land placement ok?
		assertTrue(Config.getStandardLandPlacement().size() == model.getBoard().getFields().size(),
				"Check if explicit init must be done (violates spec): " + "modify initializeSiedlerGame accordingly.");
		for (Map.Entry<Point, Land> e : Config.getStandardLandPlacement().entrySet()) {
			assertEquals(e.getValue(), model.getBoard().getField(e.getKey()),
					"Land placement does not match default placement.");
		}

		// Initial settlements/roads placed?
		assertTrue(model.getPlayerFactions().size() == PLAYERS);
		for (Faction f : model.getPlayerFactions()) {
			assertTrue(model.getBoard().getCorner(first.get(f).first) != null);
			assertTrue(model.getBoard().getEdge(first.get(f).first, first.get(f).second) != null);
			assertTrue(model.getBoard().getCorner(second.get(f).first) != null);
			assertTrue(model.getBoard().getEdge(second.get(f).first, second.get(f).second) != null);
		}
	}

	/**
	 * Equivalence Partitioning	16
	 * Test method: throwDice()
	 * tests if logik for every dice throw is correct inkl. payout.
	 * already given tests.
	 */
	@Test
	public void requirementResourcePayoutAndReturnValueForDiceThrow() {
		requirementSetupTestBoardUsedWithTheTests();
		// Return value
		for (int i = MIN_DICE_VALUE; i <= MAX_DICE_VALUE; i++) {
			Map<Faction, List<Resource>> result = model.throwDice(i);
			Map<Faction, List<Resource>> expected = expectedDiceThrowPayout.get(i);
			if (expected == null) {
				for (Map.Entry<Faction, List<Resource>> e : result.entrySet()) {
					assertTrue(e.getValue() == null || e.getValue().isEmpty());
				}
			} else {
				for (Map.Entry<Faction, List<Resource>> e : expected.entrySet()) {
					if (result.get(e.getKey()) != null) {
						assertTrue(compareLists(e.getValue(), result.get(e.getKey())),
								"Expected resources: " + e.getValue() + ". Got: " + result.get(e.getKey()));
					} else {
						assertTrue(e.getValue() == null || e.getValue().isEmpty());
					}
				}
			}
		}

		// Resource payout
		Map<Faction, Map<Resource, Integer>> expected = Map.of(Faction.values()[0],
				Map.of(Resource.GRAIN, 1, Resource.WOOL, 1, Resource.CLAY, 1, Resource.STONE, 1, Resource.WOOD, 1),
				Faction.values()[1],
				Map.of(Resource.GRAIN, 2, Resource.WOOL, 2, Resource.CLAY, 0, Resource.STONE, 1, Resource.WOOD, 0),
				Faction.values()[2],
				Map.of(Resource.GRAIN, 0, Resource.WOOL, 0, Resource.CLAY, 1, Resource.STONE, 0, Resource.WOOD, 1));

		for (int i = 0; i < model.getPlayerFactions().size(); i++) {
			Faction f = model.getCurrentPlayerFaction();
			for (Resource r : Resource.values()) {
				int hasAlready = initialResourceStockThreePlayerBoardStandard.get(f).getOrDefault(r, 0);
				assertEquals(expected.get(f).get(r) + hasAlready, model.getCurrentPlayerResourceStock(r),
						"Payout not equal for player " + i + " and resource " + r);
			}
			model.switchToNextPlayer();
		}
	}

	/**
	 * Equivalence Partitioning	2,17
	 * Test method: placeInitialSettlement() Tests if initial Settlement has been
	 * build and no resource were added.
	 */
	@Test
	public void requirementPlaceInitialSettlementInCountrySideNoPayout() {
		// Arrange
		initializeSiedlerGame(4, 3);
		int stockBefore = getAmountOfResources();

		// Act
		boolean hasBuildSettlement = model.placeInitialSettlement(new Point(6, 6), false);
		int stockAfter = getAmountOfResources();

		// Assert
		assertTrue(hasBuildSettlement);
		assertEquals(stockBefore, stockAfter);
	}

	/**
	 * Equivalence Partitioning	1,17
	 * Test method: placeInitialSettlement() Tests if initial Settlement is not
	 * build, when position is invalid. (Position is in water)
	 */
	@Test
	public void requirementPlaceInitialSettlementInWaterNoPayout() {
		// Arrange
		initializeSiedlerGame(4, 3);
		int stockBefore = getAmountOfResources();

		// Act
		boolean hasBuildSettlement = model.placeInitialSettlement(new Point(0, 10), false);
		int stockAfter = getAmountOfResources();

		// Assert
		assertFalse(hasBuildSettlement);
		assertEquals(stockBefore, stockAfter);
	}

	/**
	 * Equivalence Partitioning	1,17
	 * Test method: placeInitialSettlement() Tests if initial Settlement is build,
	 * when position is valid. no payout (Position next to water)
	 */
	@Test
	public void requirementPlaceInitialSettlementNextToWaterNoPayout() {
		// Arrange
		initializeSiedlerGame(4, 3);
		int stockBefore = getAmountOfResources();

		// Act
		boolean hasBuildSettlement = model.placeInitialSettlement(new Point(3, 7), false);
		int stockAfter = getAmountOfResources();

		// Assert
		assertTrue(hasBuildSettlement);
		assertEquals(stockBefore, stockAfter);
	}

	/**
	 * Equivalence Partitioning	2,16
	 * Test method: placeInitialSettlement() Tests if initial Settlement is build,
	 * when position is valid and check if payout is correct.
	 */
	@Test
	public void requirementPlaceInitialSettlementValidPositionWithPayout() {
		// Arrange
		initializeSiedlerGame(4, 3);
		int stockBefore = getAmountOfResources();
		int expectedStock = stockBefore + 3;

		// Act
		boolean hasBuildSettlement = model.placeInitialSettlement(new Point(6, 6), true);
		int stockAfter = getAmountOfResources();

		// Assert
		assertTrue(hasBuildSettlement);
		assertTrue(expectedStock == stockAfter);
	}

	/**
	 * Equivalence Partitioning	2
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is valid and own house is adjacent
	 */
	@Test
	public void requirementPlaceInitialRoadCorrectPlacement() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), false);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 6), new Point(6, 4));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	2
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is valid and own house is adjacent. Checks if it doesn't matter if start or
	 * end point is first mentioned.
	 */
	@Test
	public void requirementPlaceInitialRoadCorrectPlacementStartWithEndPoint() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), false);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 4), new Point(6, 6));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is valid but no house is adjacent
	 */
	@Test
	public void requirementPlaceInitialRoadNoHouseAdjacent() {
		// Arrange
		initializeSiedlerGame(5, 2);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 4), new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is valid but only house from other players are adjacent.
	 * 
	 */
	@Test
	public void requirementPlaceInitialRoadNoOwnHouseAdjacent() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), false);
		model.switchToNextPlayer();

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 4), new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is already used from other road.
	 */
	@Test
	public void requirementPlaceInitialRoadPlaceAlreadyUsed() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), false);
		model.placeInitialRoad(new Point(6, 4), new Point(6, 6));
		model.switchToNextPlayer();

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 4), new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeInitialRoad() Tests if initial Road is build, when position
	 * is in the sea (not valid).
	 */
	@Test
	public void requirementPlaceInitialRoadPositionInWater() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(4, 4), false);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(4, 4), new Point(3, 3));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeInitialRoad() Tests if initial Road is build, when start and
	 * end point aren't possible for a road.
	 */
	@Test
	public void requirementPlaceInitialRoadInvalidStartAndEnd() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), false);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(6, 6), new Point(8, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	2,4,16,18
	 * Test method: buildSettlement() Test if Settlement can be build at a valid
	 * position and player has Resources.
	 */
	@Test
	public void requirementBuildSettlementValidPosition() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(6, 6), true);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));
		model.placeInitialRoad(new Point(6, 4), new Point(5, 3));

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(5, 3));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	3
	 * Test method: buildSettlement() Test if Settlement can be build at a valid
	 * position but player has no Resources.
	 */
	@Test
	public void requirementBuildSettlementNoResources() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(6, 6), false);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));
		model.placeInitialRoad(new Point(6, 4), new Point(5, 3));

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(5, 3));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	19
	 * Test method: buildSettlement() Test if Settlement can be build at a valid
	 * position but player has no settlements left to build.
	 */
	@Test
	public void requirementBuildSettlementNoSettlementsLeftToBuild() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(6, 6), true);
		model.placeInitialSettlement(new Point(7, 9), true);
		model.placeInitialSettlement(new Point(8, 12), true);

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(7, 15));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build at a valid
	 * position and player has Resources, but no adjacent road.
	 */
	@Test
	public void requirementBuildSettlementNoAdjacentRoad() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build at a invalid
	 * position and player has Resources, but all adjacentRoads are from other
	 * players.
	 */
	@Test
	public void requirementBuildSettlementAdjacentRoadFromOtherPlayers() {
		// Arrange
		initializeSiedlerGame(4, 2);

		model.placeInitialSettlement(new Point(5, 3), true);
		model.placeInitialRoad(new Point(6, 4), new Point(5, 3));
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));
		model.switchToNextPlayer();

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build at an invalid
	 * position.
	 */
	@Test
	public void requirementBuildSettlementInWater() {
		// Arrange
		initializeSiedlerGame(4, 2);
		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(1, 10));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build when opponent
	 * settlement is adjacent.
	 */
	@Test
	public void requirementBuildSettlementOponentSettlementAdjacent() {
		// Arrange
		initializeSiedlerGame(4, 2);

		model.placeInitialSettlement(new Point(8, 12), true);
		model.switchToNextPlayer();

		model.placeInitialSettlement(new Point(9, 9), false);
		model.placeInitialRoad(new Point(9, 9), new Point(8, 10));

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(8, 10));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build when own
	 * settlement is adjacent.
	 */
	@Test
	public void requirementBuildSettlementOwnSettlementAdjacent() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(8, 12), true);
		model.placeInitialSettlement(new Point(9, 9), false);
		model.placeInitialRoad(new Point(9, 9), new Point(8, 10));

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(8, 10));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildSettlement() Test if Settlement can be build when settlement
	 * is already build at chosen position.
	 */
	@Test
	public void requirementBuildSettlementPositionAllreadyUsed() {
		// Arrange
		initializeSiedlerGame(4, 2);

		model.placeInitialSettlement(new Point(8, 10), true);
		model.placeInitialRoad(new Point(8, 10), new Point(8, 12));
		model.switchToNextPlayer();
		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.buildSettlement(new Point(8, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	2,4,18
	 * Test method: buildCity() Test if City can be build at a valid position and
	 * player has Resources.
	 */
	@Test
	public void requirementBuildCityValidPosition() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));

		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);

		// Act
		boolean isSuccessful = model.buildCity(new Point(4, 12));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildCity() Test if City can be build at a invalid position
	 * because city is already there.
	 */
	@Test
	public void requirementBuildCityAlreadyBuildCityAtPosition() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));

		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);
		model.buildCity(new Point(4, 12));
		// Act
		boolean isSuccessful = model.buildCity(new Point(4, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	3
	 * Test method: buildCity() Test if City can be build at a valid position and
	 * player has not enough Resources.
	 */
	@Test
	public void requirementBuildCityNotEnoughResources() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));

		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);

		// Act
		boolean isSuccessful = model.buildCity(new Point(4, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildCity() Test if City can be build at invalid position. (Has
	 * no Settlement at position)
	 */
	@Test
	public void requirementBuildCityInvalidPosition() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);

		// Act
		boolean isSuccessful = model.buildCity(new Point(6, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	19
	 * Test method: buildCity() Test if City can be build at valid position but user
	 * has already build all cities possible.
	 */
	@Test
	public void requirementBuildCityNoCitiesLeftToBuild() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(8, 12), false);
		model.placeInitialSettlement(new Point(7, 9), false);
		model.placeInitialSettlement(new Point(9, 15), false);

		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);

		model.buildCity(new Point(8, 12));
		model.buildCity(new Point(7, 9));
		model.buildCity(new Point(9, 15));
		model.buildCity(new Point(4, 12));

		// Act
		boolean isSuccessful = model.buildCity(new Point(5, 9));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	2,4,18
	 * Test method: buildRoad() Test if Road can be build when position is valid and
	 * player has the resources.
	 */
	@Test
	public void requirementBuildRoadValidPositionAdjacentSettlement() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(8, 10), true);

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(8, 12));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	19
	 * Test method: buildSettlement() Test if Settlement can be build at a valid
	 * position but player has no Roads left to build.
	 */
	@Test
	public void requirementBuildRoadNoRoadsLeftToBuild() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(2, 10), true);
		model.placeInitialRoad(new Point(2, 10), new Point(3, 9));
		model.placeInitialRoad(new Point(3, 9), new Point(3, 7));
		model.placeInitialRoad(new Point(3, 7), new Point(4, 6));
		model.placeInitialRoad(new Point(4, 6), new Point(4, 4));
		model.placeInitialRoad(new Point(4, 4), new Point(5, 3));
		model.placeInitialRoad(new Point(5, 3), new Point(6, 4));
		model.placeInitialRoad(new Point(6, 4), new Point(6, 6));
		model.placeInitialRoad(new Point(6, 6), new Point(7, 7));
		model.placeInitialRoad(new Point(7, 7), new Point(7, 9));
		model.placeInitialRoad(new Point(7, 9), new Point(8, 10));
		model.placeInitialRoad(new Point(8, 10), new Point(8, 12));
		model.placeInitialRoad(new Point(8, 12), new Point(9, 13));
		model.placeInitialRoad(new Point(9, 13), new Point(10, 12));
		model.placeInitialRoad(new Point(10, 12), new Point(10, 10));
		model.placeInitialRoad(new Point(10, 10), new Point(11, 9));

		// place Settlement with payout to get necessary resources to build new
		// settlement
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 15), true);

		// Act
		boolean isSuccessful = model.placeInitialRoad(new Point(11, 7), new Point(11, 9));

		// Assert
		assertFalse(isSuccessful);
	}

	
	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build when position is valid and
	 * player has the resources.
	 */
	@Test
	public void requirementBuildRoadValidPositionAdjacentRoad() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(7, 9), false);
		model.placeInitialRoad(new Point(8, 10), new Point(7, 9));

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(8, 12));

		// Assert
		assertTrue(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build when position is invalid,
	 * because only adjacent road is from other player. and no own settlement is
	 * adjacent.
	 */
	@Test
	public void requirementBuildRoadValidPositionAdjacentRoadOnlyFromOtherPlayer() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(9, 9), false);
		model.placeInitialRoad(new Point(8, 10), new Point(9, 9));
		model.placeInitialRoad(new Point(8, 10), new Point(7, 9));

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(7, 9));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build when position is valid and
	 * player has the resources but no adjacent settlement or road.
	 */
	@Test
	public void requirementBuildRoadValidPositionNoAdjacentSettlementOrRoad() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(8, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	3
	 * Test method: buildRoad() Test if Road can be build when position is valid, has
	 * adjacent road but player has not the resources.
	 */
	@Test
	public void requirementBuildRoadValidPositionAdjacentRoadNoResources() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(7, 9), false);
		model.placeInitialRoad(new Point(8, 10), new Point(7, 9));

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(8, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	3
	 * Test method: buildRoad() Test if Road can be build when position is valid, has
	 * adjacent settlement but player has not the resources.
	 */
	@Test
	public void requirementBuildRoadValidPositionAdjacentSettlementNoResources() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(8, 10), false);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(8, 10), new Point(8, 12));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build in water, has adjacent
	 * settlement.
	 */
	@Test
	public void requirementBuildRoadInWaterHasAdjacentSettlement() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(3, 7), false);

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(3, 7), new Point(2, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build in water, has adjacent
	 * road.
	 */
	@Test
	public void requirementBuildRoadInWaterHasAdjacentRoad() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(3, 9), false);
		model.placeInitialRoad(new Point(3, 7), new Point(3, 9));

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(3, 7), new Point(2, 6));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: buildRoad() Test if Road can be build, another road is already
	 * build at position.
	 */
	@Test
	public void requirementBuildRoadOtherRoadAlreadyBuild() {
		// Arrange
		initializeSiedlerGame(4, 2);
		model.placeInitialSettlement(new Point(6, 6), false);
		model.placeInitialRoad(new Point(6, 6), new Point(6, 4));

		// place Settlement with payout to get necessary resources to build new Road
		model.placeInitialSettlement(new Point(4, 12), true);

		// Act
		boolean isSuccessful = model.buildRoad(new Point(6, 6), new Point(6, 4));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	5
	 * Test method: tradeWithBankFourToOne() Tests if trading with bank is well
	 * executed.
	 */
	@Test
	public void requirementTradeWithBankFourToOnePlayerAndBankHaveResources() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout and thorw dice to get necessary resources
		model.placeInitialSettlement(new Point(4, 12), true);
		model.throwDice(5);
		model.throwDice(5);
		model.throwDice(5);

		int amountOfWoodBefore = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int expectedWoodAfterTrading = amountOfWoodBefore - 4;
		int amountOfStoneBefore = model.getCurrentPlayerResourceStock(Resource.STONE);
		int expectedStoneAfterTrading = amountOfStoneBefore + 1;
		int amountOfClayBefore = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainBefore = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolBefore = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Act
		boolean isSuccessful = model.tradeWithBankFourToOne(Resource.WOOD, Resource.STONE);
		int AmountOfWoodAfterTrading = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int AmountOfStoneAfterTrading = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayAfter = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainAfter = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolAfter = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedWoodAfterTrading, AmountOfWoodAfterTrading);
		assertEquals(expectedStoneAfterTrading, AmountOfStoneAfterTrading);
		// make sure other resources haven't been changed.
		assertEquals(amountOfClayBefore, amountOfClayAfter);
		assertEquals(amountOfGrainBefore, amountOfGrainAfter);
		assertEquals(amountOfWoolBefore, amountOfWoolAfter);
	}

	/**
	 * Equivalence Partitioning	5
	 * Test method: tradeWithBankFourToOne() Tests if trading with bank is well
	 * executed. Trades 4 resources for one resource of same type.
	 */
	@Test
	public void requirementTradeWithBankFourToOnePlayerAndBankHaveResourcesSameResources() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout and throw dice to get necessary resources
		model.placeInitialSettlement(new Point(4, 12), true);
		model.throwDice(5);
		model.throwDice(5);
		model.throwDice(5);

		int AmountOfWoodBefore = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int expectedWoodAfterTrading = AmountOfWoodBefore - 4 + 1;

		int amountOfStoneBefore = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayBefore = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainBefore = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolBefore = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Act
		boolean isSuccessful = model.tradeWithBankFourToOne(Resource.WOOD, Resource.WOOD);
		int AmountOfWoodAfterTrading = model.getCurrentPlayerResourceStock(Resource.WOOD);

		int AmountOfStoneAfter = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayAfter = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainAfter = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolAfter = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedWoodAfterTrading, AmountOfWoodAfterTrading);
		// make sure other resources haven't been changed.
		assertEquals(amountOfStoneBefore, AmountOfStoneAfter);
		assertEquals(amountOfClayBefore, amountOfClayAfter);
		assertEquals(amountOfGrainBefore, amountOfGrainAfter);
		assertEquals(amountOfWoolBefore, amountOfWoolAfter);
	}

	/**
	 * Equivalence Partitioning	6
	 * Test method: tradeWithBankFourToOne() Tests if trading with bank is well
	 * executed. Bank has no resource from this type left.
	 */
	@Test
	public void requirementTradeWithBankFourToOneBankHasNotResource() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout and throw dice to get necessary resources
		model.placeInitialSettlement(new Point(4, 12), true);
		int availableNumber = Config.INITIAL_RESOURCE_CARDS_BANK.get(Resource.WOOD);
		for (int i = 0; i < availableNumber; i++) {
			model.throwDice(5);
		}

		int amountOfWoodBefore = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int amountOfStoneBefore = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayBefore = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainBefore = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolBefore = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Act
		boolean isSuccessful = model.tradeWithBankFourToOne(Resource.WOOD, Resource.WOOD);
		int amountOfWoodAfter = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int AmountOfStoneAfter = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayAfter = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainAfter = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolAfter = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Assert
		assertFalse(isSuccessful);
		// make sure no amount of resources has changed.
		assertEquals(amountOfWoodBefore, amountOfWoodAfter);
		assertEquals(amountOfStoneBefore, AmountOfStoneAfter);
		assertEquals(amountOfClayBefore, amountOfClayAfter);
		assertEquals(amountOfGrainBefore, amountOfGrainAfter);
		assertEquals(amountOfWoolBefore, amountOfWoolAfter);
	}

	/**
	 * Equivalence Partitioning	7
	 * Test method: tradeWithBankFourToOne() Tests if trading with bank is well
	 * executed. Player has not enough resource from this type.
	 */
	@Test
	public void requirementTradeWithBankFourToOnePlayerHasNotEnoughResource() {
		// Arrange
		initializeSiedlerGame(4, 2);

		// place Settlement with payout and throw dice to get necessary resources
		model.placeInitialSettlement(new Point(4, 12), true);

		int amountOfWoodBefore = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int amountOfStoneBefore = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayBefore = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainBefore = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolBefore = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Act
		boolean isSuccessful = model.tradeWithBankFourToOne(Resource.WOOD, Resource.WOOD);
		int amountOfWoodAfter = model.getCurrentPlayerResourceStock(Resource.WOOD);
		int AmountOfStoneAfter = model.getCurrentPlayerResourceStock(Resource.STONE);
		int amountOfClayAfter = model.getCurrentPlayerResourceStock(Resource.CLAY);
		int amountOfGrainAfter = model.getCurrentPlayerResourceStock(Resource.GRAIN);
		int amountOfWoolAfter = model.getCurrentPlayerResourceStock(Resource.WOOL);

		// Assert
		assertFalse(isSuccessful);
		// make sure no amount of resources has changed.
		assertEquals(amountOfWoodBefore, amountOfWoodAfter);
		assertEquals(amountOfStoneBefore, AmountOfStoneAfter);
		assertEquals(amountOfClayBefore, amountOfClayAfter);
		assertEquals(amountOfGrainBefore, amountOfGrainAfter);
		assertEquals(amountOfWoolBefore, amountOfWoolAfter);
	}

	/**
	 * Equivalence Partitioning	8
	 * Test method: GetWinner() Tests if there is a winner. Expected: No Faction
	 * returned, because there is no winner yet.
	 */
	@Test
	public void requirementGetWinnerWithNoWinner() {
		// Arrange
		initializeSiedlerGame(4, 3);
		boolean hasWinner = false;

		// Act
		Faction winnerFaction = model.getWinner();
		if (winnerFaction != null)
			hasWinner = true;

		// Assert
		assertFalse(hasWinner);
	}

	/**
	 * Equivalence Partitioning	9
	 * Test method: GetWinner() Tests if there is a winner. player has settlements
	 * and a city
	 */
	@Test
	public void requirementGetWinnerWithOneWinnerWithCities() {
		// Arrange
		initializeSiedlerGame(4, 2);
		// place Settlement with payout to get necessary resources to build new
		// city
		model.placeInitialSettlement(new Point(4, 12), true);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.throwDice(10);
		model.throwDice(4);
		model.throwDice(4);
		model.buildCity(new Point(4, 12));
		Faction currentFaction = model.getCurrentPlayerFaction();

		// Arrange
		Faction winnerFaction = model.getWinner();

		// Assert
		assertEquals(currentFaction, winnerFaction);
	}

	/**
	 * Equivalence Partitioning	9
	 * Test method: GetWinner() Tests if there is a winner. Expected: Winner Faction
	 * returned and is equals to faction from current Player.
	 */
	@Test
	public void requirementGetWinnerWithOneWinner() {
		// Arrange
		initializeSiedlerGame(5, 4);
		// set 5 settlements for current Player
		model.placeInitialSettlement(new Point(6, 6), false);
		model.placeInitialSettlement(new Point(8, 6), false);
		model.placeInitialSettlement(new Point(9, 9), false);
		model.placeInitialSettlement(new Point(6, 10), false);
		model.placeInitialSettlement(new Point(4, 12), false);
		Faction currentFaction = model.getCurrentPlayerFaction();

		// Arrange
		Faction winnerFaction = model.getWinner();

		// Assert
		assertEquals(currentFaction, winnerFaction);
	}

	/**
	 * Equivalence Partitioning	10
	 * Test method: GetWinner() Tests if there is a winner. Case: Multiple Winner,
	 * faction from first Player in row is returned. Expected: Winner Faction from
	 * first Player returned and is not equals to faction from current Player 
	 * (when current player is not the first winner).
	 */
	@Test
	public void requirementGetWinnerWithMultipleWinner() {
		// Arrange
		initializeSiedlerGame(5, 4);
		// set 5 settlements for current Player
		model.placeInitialSettlement(new Point(6, 6), false);
		model.placeInitialSettlement(new Point(8, 6), false);
		model.placeInitialSettlement(new Point(9, 9), false);
		model.placeInitialSettlement(new Point(6, 10), false);
		model.placeInitialSettlement(new Point(4, 12), false);
		Faction fristFaction = model.getCurrentPlayerFaction();
		model.switchToNextPlayer();
		model.placeInitialSettlement(new Point(5, 15), false);
		model.placeInitialSettlement(new Point(8, 12), false);
		model.placeInitialSettlement(new Point(7, 15), false);
		model.placeInitialSettlement(new Point(9, 15), false);
		model.placeInitialSettlement(new Point(6, 18), false);
		Faction currentFaction = model.getCurrentPlayerFaction();

		// Arrange
		Faction winnerFaction = model.getWinner();

		// Assert
		assertEquals(fristFaction, winnerFaction);
		assertNotEquals(currentFaction, winnerFaction);
	}

	/**
	 * Equivalence Partitioning	11
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed and added to bank, but no player has enough
	 * resources.
	 */
	@Test
	public void requirementPlaceThiefAndStealCardNoPlayerHasMoreThanSevenResources() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), true);

		int expectedAmountOfResources = getAmountOfResources();

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	12
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed and added to bank. (odd amount of
	 * resources)
	 */
	@Test
	public void requirementPlaceThiefAndStealCurrentPlayerHasMoreThanSevenResources() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), false);
		for (int i = 0; i < 11; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		int expectedAmountOfResources = currentAmountOfResources / 2;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	12
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed and added to bank. CurrentPlayer has
	 * exactly 8 resources.
	 */
	@Test
	public void requirementPlaceThiefAndStealCurrentPlayerHasEightResources() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), false);
		for (int i = 0; i < 8; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		int expectedAmountOfResources = currentAmountOfResources / 2;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	11
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed and added to bank. CurrentPlayer has
	 * exactly 7 resources. No Resources are taken away.
	 */
	@Test
	public void requirementPlaceThiefAndStealCurrentPlayerHasSevenResources() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), false);
		for (int i = 0; i < 7; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		int expectedAmountOfResources = currentAmountOfResources;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	13
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed. All two player have more than seven
	 * resources.
	 */
	@Test
	public void requirementPlaceThiefAndStealTwoHasMoreThanSevenResources() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(6, 6), true);
		model.switchToNextPlayer();
		model.placeInitialSettlement(new Point(6, 10), true);
		for (int i = 0; i < 10; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		int expectedAmountOfResources = currentAmountOfResources / 2;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResourcesPlayerOne = getAmountOfResources();
		int resultAmountOfResourcesPlayerTwo = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResourcesPlayerOne);
		assertEquals(expectedAmountOfResources, resultAmountOfResourcesPlayerTwo);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeThiefAndStealCard() Tests if thief is set, field is invalid.
	 */
	@Test
	public void requirementPlaceThiefAndStealInvalidThiefPosition() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), true);
		for (int i = 0; i < 10; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(2, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertFalse(isSuccessful);
		assertEquals(currentAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeThiefAndStealCard() Tests if thief is set, field is invalid.
	 * thief is set on water.
	 */
	@Test
	public void requirementPlaceThiefAndStealPositionOnWater() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(6, 6), true);
		for (int i = 0; i < 10; i++) {
			model.throwDice(4);
		}

		int currentAmountOfResources = getAmountOfResources();
		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(0, 10));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertFalse(isSuccessful);
		assertEquals(currentAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	15
	 * Test method: placeThiefAndStealCard() Tests if thief is set, and only own
	 * settlement is around field with thief.
	 * 
	 */
	@Test
	public void requirementPlaceThiefAndStealOneSettlementFromCurrentPlayer() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(5, 9), true);
		int expectedAmountOfResourcesCurrentPlayer = getAmountOfResources();

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResourcesCurrentPlayer = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResourcesCurrentPlayer, resultAmountOfResourcesCurrentPlayer);
	}

	/**
	 * Equivalence Partitioning	15
	 * Test method: placeThiefAndStealCard() Tests if thief is set, and resource is
	 * stolen from other player.
	 * 
	 */
	@Test
	public void requirementPlaceThiefAndStealOneSettlementFromOtherPlayer() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(5, 9), true);
		int expectedAmountOfResourcesOtherPlayer = getAmountOfResources() - 1;
		model.switchToNextPlayer();
		int expectedAmountOfResourcesCurrentPlayer = getAmountOfResources() + 1;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResourcesCurrentPlayer = getAmountOfResources();
		model.switchToNextPlayer();
		int resultAmountOfResourcesOtherPlayer = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResourcesCurrentPlayer, resultAmountOfResourcesCurrentPlayer);
		assertEquals(expectedAmountOfResourcesOtherPlayer, resultAmountOfResourcesOtherPlayer);
	}

	/**
	 * Equivalence Partitioning	15
	 * Test method: placeThiefAndStealCard() Tests if thief is set, and resource is
	 * stolen from other player. other player has no resources.
	 */
	@Test
	public void requirementPlaceThiefAndStealOneSettlementFromOtherPlayerWithNoResources() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(5, 9), false);
		int expectedAmountOfResourcesOtherPlayer = 0;
		model.switchToNextPlayer();
		int expectedAmountOfResourcesCurrentPlayer = getAmountOfResources() + 0;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResourcesCurrentPlayer = getAmountOfResources();
		model.switchToNextPlayer();
		int resultAmountOfResourcesOtherPlayer = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResourcesCurrentPlayer, resultAmountOfResourcesCurrentPlayer);
		assertEquals(expectedAmountOfResourcesOtherPlayer, resultAmountOfResourcesOtherPlayer);
	}

	/**
	 * Equivalence Partitioning	15
	 * Test method: placeThiefAndStealCard() Tests if thief is set, and resource is
	 * stolen from other player. other player has no more resources (they were
	 * already stolen).
	 */
	@Test
	public void requirementPlaceThiefAndStealOneSettlementFromOtherPlayerWithNoMoreResources() {
		// Arrange
		initializeSiedlerGame(5, 2);
		model.placeInitialSettlement(new Point(5, 9), true);
		int currentAmountOfOtherPlayer = getAmountOfResources();
		int expectedAmountOfResourcesOtherPlayer = 0;
		model.switchToNextPlayer();
		int expectedAmountOfResourcesCurrentPlayer = getAmountOfResources() + currentAmountOfOtherPlayer;

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(6, 8));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(5, 11));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(6, 8));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(5, 11));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(6, 8));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResourcesCurrentPlayer = getAmountOfResources();
		model.switchToNextPlayer();
		int resultAmountOfResourcesOtherPlayer = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResourcesCurrentPlayer, resultAmountOfResourcesCurrentPlayer);
		assertEquals(expectedAmountOfResourcesOtherPlayer, resultAmountOfResourcesOtherPlayer);
	}

	/**
	 * Equivalence Partitioning	14
	 * Test method: placeThiefAndStealCard() Tests if thief is set and player gets
	 * right amount of resources removed and added to bank. No Settlements are
	 * around thief field.
	 */
	@Test
	public void requirementPlaceThiefAndStealNoSettlementsAroundThiefField() {
		// Arrange
		initializeSiedlerGame(5, 4);
		int expectedAmountOfResources = getAmountOfResources();

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		int resultAmountOfResources = getAmountOfResources();

		// Assert
		assertTrue(isSuccessful);
		assertEquals(expectedAmountOfResources, resultAmountOfResources);
	}

	/**
	 * Equivalence Partitioning	1
	 * Test method: placeThiefAndStealCard() Set Thief twice on same land. (invalid)
	 */
	@Test
	public void requirementPlaceThiefAndStealSetThiefOnSameField() {
		// Arrange
		initializeSiedlerGame(5, 4);

		// Act
		boolean isSuccessful = model.placeThiefAndStealCard(new Point(5, 11));
		isSuccessful = isSuccessful && model.placeThiefAndStealCard(new Point(5, 11));

		// Assert
		assertFalse(isSuccessful);
	}

	/**
	 * Equivalence Partitioning	16
	 * Test method: throwDice() Thief is set, player does not get resources, because
	 * thief blocks field.
	 */
	@Test
	public void requirementThrowDiceThiefIsSetNoResourcesPayedOut() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.placeThiefAndStealCard(new Point(5, 11));

		// Act
		Map<Faction, List<Resource>> result = model.throwDice(9);
		List<Resource> resources = result.get(model.getCurrentPlayerFaction());

		// Assert
		assertEquals(0, resources.size());
	}

	/**
	 * Equivalence Partitioning	17
	 * Test method: throwDice() Thief is set, player gets resources, because thief
	 * blocks other field.
	 */
	@Test
	public void requirementThrowDiceThiefIsOnOtherFieldSetResourcesPayedOut() {
		// Arrange
		initializeSiedlerGame(5, 4);
		model.placeInitialSettlement(new Point(5, 9), true);
		model.placeThiefAndStealCard(new Point(9, 11));

		// Act
		Map<Faction, List<Resource>> result = model.throwDice(9);
		List<Resource> resources = result.get(model.getCurrentPlayerFaction());

		// Assert
		assertEquals(1, resources.size());
	}

	private void bootstrapTestBoardForThreePlayersStandard(int winpoints) {
		initializeSiedlerGame(winpoints, PLAYERS);

		for (int i = 0; i < model.getPlayerFactions().size(); i++) {
			Faction f = model.getCurrentPlayerFaction();
			assertTrue(model.placeInitialSettlement(first.get(f).first, false));
			assertTrue(model.placeInitialRoad(first.get(f).first, first.get(f).second));
			model.switchToNextPlayer();
		}
		for (int i = 0; i < model.getPlayerFactions().size(); i++) {
			model.switchToPreviousPlayer();
			Faction f = model.getCurrentPlayerFaction();
			assertTrue(model.placeInitialSettlement(second.get(f).first, true));
			assertTrue(model.placeInitialRoad(second.get(f).first, second.get(f).second));
		}
	}

	private void initializeSiedlerGame(int winpoints, int players) {
		model = new SiedlerGame(winpoints, players);
	}

	private <T> boolean compareLists(List<T> list1, List<T> list2) {
		if (list1 == list2 || list1 == null && list2.isEmpty() || list2 == null && list1.isEmpty()) {
			return true;
		} else if (list1 == null || list2 == null || list1.size() != list2.size()) {
			return false;
		}
		List<T> tmp = new LinkedList<>(list2);
		Iterator<T> iter = list1.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (tmp.contains(item)) {
				tmp.remove(item);
			} else {
				return false;
			}
		}
		return true;
	}

	private int getAmountOfResources() {
		int numberOfResources = Resource.values().length;
		int sum = 0;
		for (int i = 0; i < numberOfResources; i++) {
			sum += model.getCurrentPlayerResourceStock(Resource.values()[i]);
		}
		return sum;
	}

}
