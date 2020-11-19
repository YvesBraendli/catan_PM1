package ch.zhaw.catan;

import java.awt.Point;

import org.beryx.textio.TextIO;

public class InputParser {

    public enum BaseActions {
        SHOW("Show Map"), TRADE("Trade with Bank"), BUILD("Build Structure"), END_TURN("End Turn");
    
    	private String name;
		private BaseActions(String name){
    		this.name = name;
    	}
		
	    @Override
	    public String toString() {
	      return name;
	    }
    }
    
    public Point requestXYCoordinates(TextIO textIO) {

    	int x = textIO.newIntInputReader()
    			.withMinVal(0)
    			.withMaxVal(14)
    			.read("X Coordinate");
    	int y = textIO.newIntInputReader()
    			.withMinVal(0)
    			.withMaxVal(22)
    			.read("Y Coordinate");
    	
    	return new Point(x, y); 	
    }
    
    public boolean askBuildStructure(TextIO textIO, Config.Structure structureType) {
    	  return textIO.newBooleanInputReader()
                  .withTrueInput("Yes")
                  .withFalseInput("No")
                  .withDefaultValue(true)
                  .read("Are you sure you want to build a "+structureType+"?");
    }
    
    public boolean askBuyResource(TextIO textIO, Config.Resource resourceType) {
  	  return textIO.newBooleanInputReader()
                .withTrueInput("Yes")
                .withFalseInput("No")
                .withDefaultValue(true)
                .read("Are you sure you want to buy "+resourceType+"?");
    }
    
    
    public int requestNumberOfPlayers(TextIO textIO) {
    	return textIO.newIntInputReader()
    			.withMinVal(2)
    			.withMaxVal(4)
    			.read("How many players want to play?");
    }
    
    public BaseActions showMainMenuAction(TextIO textIO) {
    	return textIO.newEnumInputReader(BaseActions.class).read("\nWhat would you like to do?");
    }
    
    public Config.Structure  showBuildAction(TextIO textIO) {
    	return textIO.newEnumInputReader(Config.Structure.class).read("\nWhat do you want to build?");
    }
    
    public Config.Resource showTradeAction(TextIO textIO) {
    	return textIO.newEnumInputReader(Config.Resource.class).read();
    }
    
    
}
