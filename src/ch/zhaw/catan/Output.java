package ch.zhaw.catan;

import java.util.List;
import java.util.Map;

import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

public class Output {
	public void requestSettlementCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where do you want to build your initial settlement?");
		} else {
			textTerminal.println("Where do you want to build your settlement/city?");
		}
	}

	public void requestRoadStartCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where should your initial road start?");
		} else {
			textTerminal.println("Where should your road start?");
		}
	}

	public void requestRoadEndCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if (isInitial) {
			textTerminal.println("Where should your initial road end?");
		} else {
			textTerminal.println("Where should your road end?");
		}
	}

	public void requestResourceSell(TextTerminal<?> textTerminal) {
		textTerminal.println("\nWhich resource do you want to sell?");
	}

	public void requestResourceBuy(TextTerminal<?> textTerminal) {
		textTerminal.println("\nWhich resource do you want to buy?");
	}

	public void errorSettlementNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No settlement could be built at this location.");
	}

	public void errorRoadNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No road could be built at this location.");
	}

	public void errorTradeFailed(TextTerminal<?> textTerminal) {
		textTerminal.println("This trade failed, please check your resources.");
	}

	public void printPlayerStart(TextTerminal<?> textTerminal, Config.Faction currentPlayer) {
		textTerminal.println("\n-----------Player " + currentPlayer + "----------");
	}

	public void printPlayerResources(TextTerminal<?> textTerminal, int amountOfWood, int amountOfStone,
			int amountOfWool, int amountOfClay, int amountOfGrain) {
		textTerminal.println("Resources Owned:\n" + amountOfGrain + " Grain, " + amountOfWool + " Wool, " + amountOfWood
				+ " Wood " + amountOfStone + " Stone, " + amountOfClay + " Clay");
	}
	public void printDiceNumber(TextTerminal<?> textTerminal, int diceNumber) {
		textTerminal.println("The number "+diceNumber+" was rolled.");
	}
	public void printPayedOutResources(TextTerminal<?> textTerminal, Map<Faction, List<Resource>> payedOutResources) {
		for(Map.Entry<Faction, List<Resource>> entry : payedOutResources.entrySet()) {
			
		}
	}
}
