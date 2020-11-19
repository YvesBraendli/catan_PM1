package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class performs all actions related to modifying the game state.
 * 
 * <p>
 * For example, calling the method {@link SiedlerGame#throwDice(int)} will do
 * the payout of the resources to the players according to the payout rules of
 * the game which take into account factors like the amount of resources
 * requeste of a certain type, the number of players requesting it, the amount
 * of resources available in the bank and the settlement types.
 * </p>
 * 
 * @author Moser Nadine, Meier Robin, Br�ndli Yves
 *
 */
public class SiedlerGame {

	private Player currentPlayer;
	private ArrayList<Player> players;
	private int winPoints;
	private SiedlerBoard siedlerBoard;
	private Bank bank;

	/**
	 * Constructs a SiedlerGame game state object.
	 * 
	 * @param winPoints the number of points required to win the game
	 * @param players   the number of players
	 * 
	 * @throws IllegalArgumentException if winPoints is lower than three or players
	 *                                  is not between two and four
	 */
	public SiedlerGame(int winPoints, int players) {
		if(winPoints > 2) {
			this.winPoints = winPoints;
		}
		else {
			throw new IllegalArgumentException();
		}
		this.players = new ArrayList<Player>();
		generatePlayers(players);
		currentPlayer = this.players.get(0);
		siedlerBoard = new SiedlerBoard();
		bank = new Bank();
	}

	/**
	 * Switches to the next player in the defined sequence of players.
	 */
	public void switchToNextPlayer() {
		int numberOfPlayers = players.size();
		int currentIndex = players.indexOf(currentPlayer);
		if ((currentIndex + 1) == numberOfPlayers) {
			currentPlayer = players.get(0);
		} else {
			currentPlayer = players.get(currentIndex + 1);
		}
	}

	/**
	 * Switches to the previous player in the defined sequence of players.
	 */
	public void switchToPreviousPlayer() {
		int currentIndex = players.indexOf(currentPlayer);
		if (currentIndex == 0) {
			currentPlayer = players.get(players.size() - 1);
		} else {
			currentPlayer = players.get(currentIndex - 1);
		}
	}

	/**
	 * Returns the {@link Faction}s of the active players.
	 * 
	 * <p>
	 * The order of the player's factions in the list must correspond to the oder in
	 * which they play. Hence, the player that sets the first settlement must be at
	 * position 0 in the list etc.
	 * 
	 * <strong>Important note:</strong> The list must contain the factions of active
	 * players only.
	 * </p>
	 * 
	 * @return the list with player's factions
	 */
	public List<Faction> getPlayerFactions() {
		// todo: auch oke, da in zweiterrunde in die andere richtung gesielt wird?
		List<Faction> factions = new ArrayList<Faction>();
		for (Player player : players) {
			factions.add(player.getFaction());
		}
		return factions;
	}

	/**
	 * Returns the game board.
	 * 
	 * @return the game board
	 */
	public SiedlerBoard getBoard() {
		return siedlerBoard;
	}

	/**
	 * Returns the {@link Faction} of the current player.
	 * 
	 * @return the faction of the current player
	 */
	public Faction getCurrentPlayerFaction() {
		return currentPlayer.getFaction();
	}

	/**
	 * Gets the current amount of resources of the specified type in the current
	 * players' stock (hand).
	 * 
	 * @param resource the resource type
	 * @return the amount of resources of this type
	 */
	public int getCurrentPlayerResourceStock(Resource resource) {
		HashMap<Resource, Integer> resources = currentPlayer.getAmountOfResources();	
		Integer resourceStock = resources.get(resource);
		if(resourceStock == null) return 0;
		return resourceStock;
	}

	/**
	 * Places a settlement in the founder's phase (phase II) of the game.
	 * 
	 * <p>
	 * The placement does not cost any resources. If payout is set to true, one
	 * resource per adjacent field is taken from the bank and added to the players'
	 * stock of resources.
	 * </p>
	 * 
	 * @param position the position of the settlement
	 * @param payout   true, if the player shall get the resources of the
	 *                 surrounding fields
	 * @return true, if the placement was successful
	 */
	public boolean placeInitialSettlement(Point position, boolean payout) {
		// TODO: Implement

		// if(sidlerBoard.placeSettlement(position)){
	// do everything} else return false;
		if(payout) {
			increaseWinningPoints(currentPlayer, 1);
		}
		// + winpoints erh�hen
		// + resourcen auszahlen
		// + m�gliche h�user eins abziehen?
		
		return false;

	}

	/**
	 * Places a road in the founder's phase (phase II) of the game. The placement
	 * does not cost any resources.
	 * 
	 * @param roadStart position of the start of the road
	 * @param roadEnd   position of the end of the road
	 * @return true, if the placement was successful
	 */
	public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
		// TODO: Implement
		return true;
	}

	/**
	 * This method takes care of the payout of the resources to the players
	 * according to the payout rules of the game. If a player does not get
	 * resources, the list for this players' faction is empty.
	 * 
	 * <p>
	 * The payout rules of the game take into account factors like the amount of
	 * resources of a certain type, the number of players requesting resources of
	 * this type, the amount of resources available in the bank and the settlement
	 * types.
	 * </p>
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
	 * <p>
	 * The settlement can be built if:
	 * <ul>
	 * <li>the player has the resource cards required</li>
	 * <li>a settlement to place on the board</li>
	 * <li>the specified position meets the build rules for settlements</li>
	 * </ul>
	 * 
	 * @param position the position of the settlement
	 * @return true, if the placement was successful
	 */
	public boolean buildSettlement(Point position) {
		// TODO: Implement
		//TODO: if �berpr�fen: konnte es gebaut werden?
		siedlerBoard.createSettlement(position, getCurrentPlayerFaction());
		increaseWinningPoints(currentPlayer, 1);
		// resourcen auszahlen!!
		// resourcen abzeihen!! bezahlung
		return false;
	}

	/**
	 * Builds a city at the specified position on the board.
	 * 
	 * <p>
	 * The city can be built if:
	 * <ul>
	 * <li>the player has the resource cards required</li>
	 * <li>a city to place on the board</li>
	 * <li>the specified position meets the build rules for cities</li>
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
	 * <p>
	 * The road can be built if:
	 * <ul>
	 * <li>the player has the resource cards required</li>
	 * <li>a road to place on the board</li>
	 * <li>the specified position meets the build rules for roads</li>
	 * </ul>
	 * 
	 * @param roadStart the position of the start of the road
	 * @param roadEnd   the position of the end of the road
	 * @return true, if the placement was successful
	 */
	public boolean buildRoad(Point roadStart, Point roadEnd) {
		// TODO: Implement
		//TODO: if �berpr�fen: konnte es gebaut werden?
		// beuahlung!!
		// if player has bezahlung
		List<Resource> costs = Config.Structure.ROAD.getCosts();
		
		boolean hasBuildRoad = siedlerBoard.createStreet(roadStart, roadEnd, getCurrentPlayerFaction());
		if(hasBuildRoad) {
			// abziehen.
			return true;
		}
		return false;
	}

	/**
	 * Trades in {@value #FOUR_TO_ONE_TRADE_OFFER} resources of the offered type for
	 * {@value #FOUR_TO_ONE_TRADE_WANT} resource of the wanted type.
	 * 
	 * @param offer offered type
	 * @param want  wanted type
	 * @return true, if player and bank had enough resources and the trade was
	 *         successful
	 */
	public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
		boolean hasResourcesToTradeWith = currentPlayer.getAmountOfResources().get(offer) >= 4;
		boolean isTradingSuccessful = false;
		if(hasResourcesToTradeWith) {
			isTradingSuccessful = bank.trade(offer, want);
		}		
		return isTradingSuccessful;
	}

	/**
	 * Returns the winner of the game, if any.
	 * 
	 * @return the winner of the game or null, if there is no winner (yet)
	 */
	public Faction getWinner() {
		for(Player player : players) {
			if(player.getNumberOfWinningpoints() >= winPoints) {
				return player.getFaction();
			}
		}
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
	 *         placed there (e.g., on water)
	 */
	public boolean placeThiefAndStealCard(Point field) {
		// TODO: Implement (or longest road functionality)
		return false;
	}

	private void generatePlayers(int numberOfPlayers) {
		switch (numberOfPlayers) {
		case 4:
			this.players.add(new Player(Faction.BLUE));
		case 3:
			this.players.add(new Player(Faction.GREEN));
		case 2:
			this.players.add(new Player(Faction.RED));
			this.players.add(new Player(Faction.YELLOW));
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void increaseWinningPoints(Player player, int increment) {
		int currentPoints = player.getNumberOfWinningpoints();
		player.setNumberOfWinningpoints(currentPoints + increment);
	}
}
