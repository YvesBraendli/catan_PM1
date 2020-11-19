package ch.zhaw.catan;

import org.beryx.textio.TextTerminal;

public class Output {
	public void requestSettlementCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if(isInitial) {
			textTerminal.println("Where do you want to build your initial settlement?");
		}
		else {
			textTerminal.println("Where do you want to build your settlement/city?");
		}
	}
	public void requestRoadStartCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if(isInitial) {
			textTerminal.println("Where should your initial road start?");
		}
		else {
			textTerminal.println("Where should your road start?");
		}
	}
	public void requestRoadEndCoordinates(TextTerminal<?> textTerminal, boolean isInitial) {
		if(isInitial) {
			textTerminal.println("Where should your initial road end?");
		}
		else {
			textTerminal.println("Where should your road end?");
		}
	}
	public void requestResourceSell(TextTerminal<?> textTerminal) {
		textTerminal.println("Which resource do you want to sell?");
	}
	public void requestResourceBuy(TextTerminal<?> textTerminal) {
		textTerminal.println("Which resource do you want to buy?");
	}
	public void errorSettlementNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No settlement could be built at this location.");
	}
	public void errorRoadNotBuilt(TextTerminal<?> textTerminal) {
		textTerminal.println("No road could be built at this location.");
	}
	public void printPlayerStart(TextTerminal<?> textTerminal, Config.Faction currentPlayer) {
		textTerminal.println("\n-----------Player "+currentPlayer+"----------");
	}
}
