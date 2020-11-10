package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import java.awt.Point;
import java.util.List;
import java.util.Map;


/**
 * This class performs all actions related to modifying the game state. 
 * 
 * <p>For example, calling the method {@link SiedlerGame#throwDice(int)}
 * will do the payout of the resources to the players according to 
 * the payout rules of the game which take into account factors like
 * the amount of resources requeste of a certain type, the number of players
 * requesting it, the amount of resources available in the bank and 
 * the settlement types.</p> 
 * 
 * @author TODO
 *
 */
public class SiedlerGame {

  
  /**
   * Constructs a SiedlerGame game state object.
   * 
   * @param winPoints the number of points required to win the game
   * @param players the number of players
   * 
   * @throws IllegalArgumentException if winPoints is lower than 
   *     three or players is not between two and four
   */
  public SiedlerGame(int winPoints, int players) {
    // TODO: Implement
  }

  /**
   * Switches to the next player in the defined sequence of players.
   */
  public void switchToNextPlayer() {
    // TODO: Implement
  }

  /**
   * Switches to the previous player in the defined sequence of players.
   */
  public void switchToPreviousPlayer() {
    // TODO: Implement
  }

  /**
   * Returns the {@link Faction}s of the active players.
   * 
   * <p>The order of the player's factions in the list must 
   * correspond to the oder in which they play. 
   * Hence, the player that sets the first settlement must be 
   * at position 0 in the list etc. 
   * 
   * <strong>Important note:</strong> The list must contain the 
   * factions of active players only.</p> 
   * 
   * @return the list with player's factions
   */
  public List<Faction> getPlayerFactions() {
    // TODO: Implement
    return null;
  }

  
  /**
   * Returns the game board. 
   * 
   * @return the game board
   */
  public SiedlerBoard getBoard() {
    // TODO: Implement
    return null;
  }

  /**
   * Returns the {@link Faction} of the current player.
   * 
   * @return the faction of the current player
   */
  public Faction getCurrentPlayerFaction() {
    // TODO: Implement
    return null;
  }

  /**
   * Gets the current amount of resources of the specified type
   * in the current players' stock (hand). 
   * 
   * @param resource the resource type
   * @return the amount of resources of this type
   */
  public int getCurrentPlayerResourceStock(Resource resource) {
    // TODO: Implement
    return 0;
  }

  /**
   * Places a settlement in the founder's phase (phase II) of the game.
   * 
   * <p>The placement does not cost any resources. If payout is
   * set to true, one resource per adjacent field is taken from the
   * bank and added to the players' stock of resources.</p>  
   * 
   * @param position the position of the settlement
   * @param payout true, if the player shall get the resources of the surrounding fields
   * @return true, if the placement was successful
   */
  public boolean placeInitialSettlement(Point position, boolean payout) {
    // TODO: Implement
    return false;
  }

  /**
   * Places a road in the founder's phase (phase II) of the game.
   * The placement does not cost any resources. 
   * 
   * @param roadStart position of the start of the road
   * @param roadEnd position of the end of the road
   * @return true, if the placement was successful
   */
  public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
    // TODO: Implement
    return false;
  }

  /**
   * This method takes care of the payout of the resources to the players
   * according to the payout rules of the game. If a player does not get resources, 
   * the list for this players' faction is empty.
   * 
   * <p>The payout rules of the game take into account factors like
   * the amount of resources of a certain type, the number of players
   * requesting resources of this type, the amount of resources available 
   * in the bank and the settlement types.</p>
   * 
   * @param dicethrow the result of the dice throw
   * @return the resources that have been paid to the players
   */
  public Map<Faction, List<Resource>> throwDice(int dicethrow) {
    // TODO: Implement
    return null;
  }

  /**
   * Builds a settlement at the specified position on the board.
   * 
   * <p>The settlement can be built if:
   * <ul>
   * <li> the player has the resource cards required</li>
   * <li> a settlement to place on the board</li>
   * <li> the specified position meets the build rules for settlements</li>
   * </ul> 
   * 
   * @param position the position of the settlement
   * @return true, if the placement was successful
   */
  public boolean buildSettlement(Point position) {
    // TODO: Implement
    return false;
  }

  /**
   * Builds a city at the specified position on the board.
   * 
   * <p>The city can be built if:
   * <ul>
   * <li> the player has the resource cards required</li>
   * <li> a city to place on the board</li>
   * <li> the specified position meets the build rules for cities</li>
   * </ul> 
   * 
   * @param position the position of the city
   * @return true, if the placement was successful
   */
  public boolean buildCity(Point position) {
    // TODO: OPTIONAL task - Implement
    return false;
  }

  /**
   * Builds a road at the specified position on the board.
   * 
   * <p>The road can be built if:
   * <ul>
   * <li> the player has the resource cards required</li>
   * <li> a road to place on the board</li>
   * <li> the specified position meets the build rules for roads</li>
   * </ul> 
   * 
   * @param roadStart the position of the start of the road
   * @param roadEnd the position of the end of the road
   * @return true, if the placement was successful
   */
  public boolean buildRoad(Point roadStart, Point roadEnd) {
    // TODO: Implement
    return false;
  }

  
  /**
   * Trades in {@value #FOUR_TO_ONE_TRADE_OFFER} resources of the 
   * offered type for {@value #FOUR_TO_ONE_TRADE_WANT} resource of the wanted type.
   *  
   * @param offer offered type
   * @param want wanted type
   * @return true, if player and bank had enough resources and the trade was successful
   */
  public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
    // TODO: Implement
    return false;
  }

  /**
   * Returns the winner of the game, if any. 
   * 
   * @return the winner of the game or null, if there is no winner (yet)
   */
  public Faction getWinner() {
    // TODO: Implement
    return null;
  }
  
  
  /**
   * Places the thief on the specified field and steals a random resource card (if
   * the player has such cards) from a random player with a settlement at that
   * field (if there is a settlement) and adds it to the resource cards of the
   * current player.
   * 
   * @param field the field on which to place the thief
   * @return false, if the specified field is not a field or the thief cannot be 
   *     placed there (e.g., on water) 
   */
  public boolean placeThiefAndStealCard(Point field) {
    //TODO: Implement (or longest road functionality)
    return false;
  }

}
