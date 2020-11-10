package ch.zhaw.catan;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

  private Map<Integer, Map<Faction, List<Resource>>> expectedDiceThrowPayout = Map.of(
      2, Map.of(
          Faction.values()[0], List.of(Resource.GRAIN), 
          Faction.values()[1], List.of(),
          Faction.values()[2], List.of()),
      3, Map.of(
          Faction.values()[0], List.of(),
          Faction.values()[1], List.of(),
          Faction.values()[2], List.of()),
      4, Map.of(
          Faction.values()[0], List.of(Resource.STONE),
          Faction.values()[1], List.of(),
          Faction.values()[2], List.of(Resource.CLAY)),
      5, Map.of(
          Faction.values()[0], List.of(),
          Faction.values()[1], List.of(),
          Faction.values()[2], List.of(Resource.WOOD)),
      6, Map.of(
          Faction.values()[0], List.of(Resource.WOOD), 
          Faction.values()[1], List.of(), 
          Faction.values()[2], List.of()),
      8, Map.of(
          Faction.values()[0], List.of(), 
          Faction.values()[1], List.of(Resource.WOOL), 
          Faction.values()[2], List.of()),
      9, Map.of(
          Faction.values()[0], List.of(),
          Faction.values()[1], List.of(Resource.GRAIN), 
          Faction.values()[2], List.of()),
      10, Map.of(
          Faction.values()[0], List.of(),
          Faction.values()[1], List.of(Resource.GRAIN), 
          Faction.values()[2], List.of()),
      11, Map.of(
          Faction.values()[0], List.of(Resource.CLAY), 
          Faction.values()[1], List.of(Resource.STONE), 
          Faction.values()[2], List.of()),
      12, Map.of(
          Faction.values()[0], List.of(Resource.WOOL), 
          Faction.values()[1], List.of(Resource.WOOL), 
          Faction.values()[2], List.of()));

  private Map<Faction, Map<Resource, Integer>> 
      initialResourceStockThreePlayerBoardStandard = Map.of(
      Faction.values()[0], Map.of(Resource.WOOL, 1, Resource.CLAY, 1), 
      Faction.values()[1], Map.of(Resource.GRAIN, 1, Resource.WOOL, 1, Resource.STONE, 1), 
      Faction.values()[2], Map.of(Resource.CLAY, 1));
  
  @Test  
  public void requirementPlayerSwitching() {
    for (int players : List.of(2, 3, 4)) {
      initializeSiedlerGame(WIN_POINTS_5, players);
      assertTrue(players == model.getPlayerFactions().size(), 
          "Wrong number of players returned by getPlayers()");
      //Switching forward
      for (int i = 0; i < players; i++) {
        assertEquals(Config.Faction.values()[i], model.getCurrentPlayerFaction(), 
            "Player order does not match order of Faction.values()"); 
        model.switchToNextPlayer();
      }
      assertEquals(Config.Faction.values()[0], model.getCurrentPlayerFaction(), 
          "Player wrap-around from last player to first player did not work."); 
      //Switching backward
      for (int i = players - 1; i >= 0; i--) {
        model.switchToPreviousPlayer();
        assertEquals(Config.Faction.values()[i], model.getCurrentPlayerFaction(), 
            "Switching players in reverse order does not work as expected."); 
      }
    }
  }

  @Test
  public void requirementSetupTestBoardUsedWithTheTests() { 
    requirementPlayerSwitching();
    bootstrapTestBoardForThreePlayersStandard(WIN_POINTS_5);

    //Land placement ok?
    assertTrue(Config.getStandardLandPlacement().size() == model.getBoard().getFields().size(),
        "Check if explicit init must be done (violates spec): "
            + "modify initializeSiedlerGame accordingly.");
    for (Map.Entry<Point, Land> e : Config.getStandardLandPlacement().entrySet()) {
      assertEquals(e.getValue(), model.getBoard().getField(e.getKey()), 
          "Land placement does not match default placement.");
    }

    //Initial settlements/roads placed?
    assertTrue(model.getPlayerFactions().size() == PLAYERS);
    for (Faction f : model.getPlayerFactions()) {
      assertTrue(model.getBoard().getCorner(first.get(f).first) != null);
      assertTrue(model.getBoard().getEdge(first.get(f).first, first.get(f).second) != null);
      assertTrue(model.getBoard().getCorner(second.get(f).first) != null);
      assertTrue(model.getBoard().getEdge(second.get(f).first, second.get(f).second) != null);
    }
  }

  @Test
  public void requirementResourcePayoutAndReturnValueForDiceThrow() {
    requirementSetupTestBoardUsedWithTheTests();
    //Return value
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

    //Resource payout
    Map<Faction, Map<Resource, Integer>> expected = Map.of(
        Faction.values()[0], Map.of(Resource.GRAIN, 1, Resource.WOOL, 1, 
            Resource.CLAY, 1, Resource.STONE, 1, Resource.WOOD, 1),
        Faction.values()[1],
        Map.of(Resource.GRAIN, 2, Resource.WOOL, 2, Resource.CLAY, 0,
            Resource.STONE, 1, Resource.WOOD, 0),
        Faction.values()[2],
        Map.of(Resource.GRAIN, 0, Resource.WOOL, 0, Resource.CLAY, 1,
            Resource.STONE, 0, Resource.WOOD, 1));

    for (int i = 0; i < model.getPlayerFactions().size(); i++) {
      Faction f = model.getCurrentPlayerFaction();
      for (Resource r : Resource.values()) {
        int hasAlready = initialResourceStockThreePlayerBoardStandard.get(f).getOrDefault(r, 0);
        assertEquals(expected.get(f).get(r) +  hasAlready,
            model.getCurrentPlayerResourceStock(r), 
            "Payout not equal for player " + i + " and resource " + r);
      }
      model.switchToNextPlayer();
    }
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
    if (list1 == list2 
        || list1 == null && list2.isEmpty()
        || list2 == null && list1.isEmpty()
        ) {
      return true;
    } else if (
        list1 == null 
        || list2 == null
        || list1.size() != list2.size()) {
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
}
