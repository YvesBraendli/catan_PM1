package ch.zhaw.catan;

import java.awt.Point;

import org.beryx.textio.TextIO;

/**
 * This class is used to get user inputs.
 * 
 * @author Moser Nadine, Meier Robin, Brändli Yves
 *
 */
public class InputParser {

	/**
	 * The actions used for the main menu.
	 */
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
    
    /**
     * Requests X and Y coordinates
     * 
     * @param textIO the reader for the inputs
     * @return a Point containing X and Y coordinates
     */
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
    
    /**
     * Makes sure, the player really wants to build a structure.
     * 
     * @param textIO the reader for the inputs
     * @param structureType the type of structure the player wants to build
     * @return true if the player wants to build the structure
     */
    public boolean askBuildStructure(TextIO textIO, Config.Structure structureType) {
    	  return textIO.newBooleanInputReader()
                  .withTrueInput("Yes")
                  .withFalseInput("No")
                  .withDefaultValue(true)
                  .read("Are you sure you want to build a "+structureType+"?");
    }
    
    /**
     * Makes sure, the player really wants to buy a resource.
     * 
     * @param textIO the reader for the inputs
     * @param resourceType the type of resource the player wants to buy
     * @return true if the player wants to buy the resource
     */
    public boolean askBuyResource(TextIO textIO, Config.Resource resourceType) {
  	  return textIO.newBooleanInputReader()
                .withTrueInput("Yes")
                .withFalseInput("No")
                .withDefaultValue(true)
                .read("Are you sure you want to buy "+resourceType+"?");
    }
    
    /**
     * Requests how many players want to play (between 2-4)
     * 
     * @param textIO the reader for the inputs
     * @return the number of players
     */
    public int requestNumberOfPlayers(TextIO textIO) {
    	return textIO.newIntInputReader()
    			.withMinVal(2)
    			.withMaxVal(4)
    			.read("How many players want to play?");
    }
    
    /**
     * Shows the main menu.
     * 
     * @param textIO the reader for the inputs
     * @return the action which the player wants to perform in the main menu
     */
    public BaseActions showMainMenuAction(TextIO textIO) {
    	return textIO.newEnumInputReader(BaseActions.class).read("\nWhat would you like to do?");
    }
    
    /**
     * Shows the build menu.
     * 
     * @param textIO the reader for the inputs
     * @return the structure a player wants to build
     */
    public Config.Structure  showBuildAction(TextIO textIO) {
    	return textIO.newEnumInputReader(Config.Structure.class).read("\nWhat do you want to build?");
    }
    
    /**
     * Shows the trade menu.
     * 
     * @param textIO the reader for the inputs
     * @return the resource a player wants to buy/sell
     */
    public Config.Resource showTradeAction(TextIO textIO) {
    	return textIO.newEnumInputReader(Config.Resource.class).read();
    }  
}
