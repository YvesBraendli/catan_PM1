package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.Config.Structure;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	private static final int MIN_AMOUNT_CARDS = 8;
	private static final int MIN_AMOUNT_FOR_OFFER = 4;
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
		if (winPoints > 2) {
			this.winPoints = winPoints;
		} else {
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
		if (resources == null) return 0;
		Integer resourceStock = resources.get(resource);
		if (resourceStock == null) return 0;
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
		if (currentPlayer.getCurrentNumberOfSettlements() <= 0) return false;

		boolean hasBuildSettlement = siedlerBoard.createInitialSettlement(position, getCurrentPlayerFaction());
		if (!hasBuildSettlement) return false;

		currentPlayer.setCurrentNumberOfSettlements(1);
		increaseWinningPoints(currentPlayer, 1);

		if (payout) {
			List<Land> landPlacements = siedlerBoard.getFields(position);
			for (Land currentLand : landPlacements) {
				if (currentLand.getResource() == null) continue;
				currentPlayer.setAmountOfResources(currentLand.getResource(), 1, true);
			}

			return true;
		}
		return true;
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
		if (currentPlayer.getCurrentNumberOfRoads() <= 0) return false;
		boolean hasBuildRoad = siedlerBoard.createStreet(roadStart, roadEnd, getCurrentPlayerFaction());
		if (hasBuildRoad) {
			currentPlayer.setCurrentNumberOfRoads(1);
		}
		return hasBuildRoad;
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
		Map<Faction, List<Resource>> payout = new HashMap<Config.Faction, List<Resource>>();
		Map<Point, Integer> diceNumberPlacements = Config.getStandardDiceNumberPlacement();

		Set<Point> placementToGetResources = new HashSet<Point>();
		for (Map.Entry<Point, Integer> entry : diceNumberPlacements.entrySet()) {
			if (entry.getValue() != null && entry.getValue() == dicethrow) {
				placementToGetResources.add(entry.getKey());
			}
		}

		for (Player player : players) {
			payout.put(player.getFaction(), new ArrayList<Resource>());
		}

		Point thiefPosition = siedlerBoard.getThief();
		Map<Point, Land> landPlacements = Config.getStandardLandPlacement();

		for (Point currentField : placementToGetResources) {
			if (currentField.x == thiefPosition.x 
					&& currentField.y == thiefPosition.y) continue;

			Land currentLand = landPlacements.get(currentField);
			Resource currentResource = currentLand.getResource();

			HashMap<Faction, ArrayList<Settlement>> factionsWithSettlementAroundField = siedlerBoard
					.searchFieldBuildings(currentField);

			for (Player player : players) {
				List<Resource> addedResources = payout.get(player.getFaction());
				for (Map.Entry<Faction, ArrayList<Settlement>> faction : factionsWithSettlementAroundField.entrySet()) {
					if (player.getFaction() == faction.getKey()) {
						Map<Resource, Integer> payoutResources = new HashMap<>();
						int amountOfCurrentResource = getAmountOfResourcesForPayout(faction.getValue());
						payoutResources.put(currentResource, amountOfCurrentResource);
						boolean isPayoutSuccessful = bank.payoutToDiceThrows(payoutResources);
						if (isPayoutSuccessful) {
							player.setAmountOfResources(currentResource, amountOfCurrentResource, true);
							for (int i = 0; i < amountOfCurrentResource; i++) {
								addedResources.add(currentResource);
							}
						}
					}
				}
				payout.put(player.getFaction(), addedResources);
			}
		}
		return payout;
	}

	private int getAmountOfResourcesForPayout(List<Settlement> buildings) {
		int sum = 0;
		for (Settlement building : buildings) {
			sum = sum + building.getNumberOfResourcesForPayout();
		}
		return sum;
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
		if (currentPlayer.getCurrentNumberOfSettlements() <= 0) return false;
		boolean canPayForSettlement = canPlayerPayForStructure(Structure.SETTLEMENT);
		if (!canPayForSettlement) return false;

		boolean hasBuildSettlement = siedlerBoard.createSettlement(position, getCurrentPlayerFaction());

		if (hasBuildSettlement) {
			payForConstruct(Structure.SETTLEMENT);
			currentPlayer.setCurrentNumberOfSettlements(1);
			increaseWinningPoints(currentPlayer, 1);
			return true;
		}
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
		if (currentPlayer.getCurrentNumberOfCities() <= 0) return false;
		boolean canPayForCity = canPlayerPayForStructure(Structure.CITY);
		if (!canPayForCity) return false;

		boolean hasBuildCity = siedlerBoard.createCity(position, getCurrentPlayerFaction());

		if (hasBuildCity) {
			payForConstruct(Structure.CITY);
			currentPlayer.setCurrentNumberOfCities(1);
			increaseWinningPoints(currentPlayer, 2);
			return true;
		}
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
		if (currentPlayer.getCurrentNumberOfRoads() <= 0) return false;
		boolean canPayForRoad = canPlayerPayForStructure(Structure.ROAD);
		if (!canPayForRoad) return false;

		boolean hasBuildRoad = siedlerBoard.createStreet(roadStart, roadEnd, getCurrentPlayerFaction());

		if (hasBuildRoad) {
			payForConstruct(Structure.ROAD);
			currentPlayer.setCurrentNumberOfRoads(1);
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
		if (currentPlayer.getAmountOfResources() == null) return false;
		Integer resourcesToOffer = currentPlayer.getAmountOfResources().get(offer);
		if (resourcesToOffer == null) {
			resourcesToOffer = 0;
		}

		boolean hasResourcesToTradeWith = resourcesToOffer >= MIN_AMOUNT_FOR_OFFER;
		if (hasResourcesToTradeWith) {
			boolean isTradingSuccessful = bank.trade(offer, want);
			if (isTradingSuccessful) {
				currentPlayer.setAmountOfResources(offer, MIN_AMOUNT_FOR_OFFER, false);
				currentPlayer.setAmountOfResources(want, 1, true);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the winner of the game, if any.
	 * 
	 * @return the winner of the game or null, if there is no winner (yet)
	 */
	public Faction getWinner() {
		for (Player player : players) {
			if (player.getNumberOfWinningpoints() >= winPoints) {
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
		boolean isValidThiefField = siedlerBoard.isValidThiefField(field);
		if (!isValidThiefField) return false;

		stealResourcesFromPlayers();

		Faction factionWithThief = siedlerBoard.setThief(field, getCurrentPlayerFaction());

		// if faction is null, thief isn't adjacent to a settlement or city
		if (factionWithThief == null) return true;

		for (Player player : players) {
			if (player.getFaction() == factionWithThief) {
				Resource resource = player.selectRandomResource();
				if(resource == null) break;
				player.setAmountOfResources(resource, 1, false);
				currentPlayer.setAmountOfResources(resource, 1, true);
				break;
			}
		}
		return true;
	}

	private void stealResourcesFromPlayers() {
		HashMap<Resource, Long> resourcesForBank = new HashMap<Config.Resource, Long>();
		for (Player player : players) {
			int amountOfResources = getAmountOfResources(player);
			if (amountOfResources < MIN_AMOUNT_CARDS) continue;
			int amountLeft = amountOfResources / 2;
			int amountToRemove = amountOfResources - amountLeft;

			for (int i = 0; i < amountToRemove; i++) {
				Resource selectedRandomResource = player.selectRandomResource();
				Long currentResourceForBank = resourcesForBank.get(selectedRandomResource);
				if (currentResourceForBank == null) {
					currentResourceForBank = (long) 0;
				}
				player.setAmountOfResources(selectedRandomResource, 1, false);
				resourcesForBank.put(selectedRandomResource, currentResourceForBank + 1);
			}
		}
		bank.payCardsToBankStock(resourcesForBank);
	}

	private int getAmountOfResources(Player player) {
		HashMap<Config.Resource, Integer> resources = player.getAmountOfResources();
		int amountOfAllResources = 0;
		for (Entry<Resource, Integer> resource : resources.entrySet()) {
			if (resource.getValue() != null) {
				amountOfAllResources = amountOfAllResources + resource.getValue();
			}
		}
		return amountOfAllResources;
	}

	private void generatePlayers(int numberOfPlayers) {

		if (numberOfPlayers < 2 || numberOfPlayers > 4) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < numberOfPlayers; i++) {
			this.players.add(new Player(Faction.values()[i]));
		}
	}

	private void increaseWinningPoints(Player player, int increment) {
		int currentPoints = player.getNumberOfWinningpoints();
		player.setNumberOfWinningpoints(currentPoints + increment);
	}

	private void payForConstruct(Structure structure) {
		List<Resource> costs = structure.getCosts();
		for (Resource resource : costs) {
			currentPlayer.setAmountOfResources(resource, 1, false);
		}
		bank.payCardsToBankStock(structure.getCostsAsMap());
	}

	private boolean canPlayerPayForStructure(Structure structure) {
		List<Resource> costs = structure.getCosts();

		Map<Resource, Integer> currentResources = new HashMap<Config.Resource, Integer>();
		for(Entry<Resource, Integer> resources : currentPlayer.getAmountOfResources().entrySet()) {
			currentResources.put(resources.getKey(), resources.getValue());
		}
		
		Integer currentAmountOfResource;
		for (Resource resource : costs) {
			currentAmountOfResource = currentResources.get(resource);
			if (currentAmountOfResource == null) return false;
			currentAmountOfResource--;
			if (currentAmountOfResource < 0) return false;
			currentResources.put(resource, currentAmountOfResource);
		}
		return true;
	}

}
