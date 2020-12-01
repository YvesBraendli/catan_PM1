package ch.zhaw.catan;

import java.awt.Point;
import java.util.Map;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.hexboard.Label;

/**
 * This class is used to print the gameboard and set the lowerfiel labels
 * to either the dice number or if the thief is on that specific field the Text "TH"
 * @author Moser Nadine, Meier Robin, Br�ndli Yves
 *
 */
public class SiedlerBoardTextView extends HexBoardTextView<Land, Settlement, String, String> {

  /**
   * Constructor of the SiedlerBoardTextView class
   * Sets the lowerfieldlabel to a dicenumber or the abbreviation "TH"
   * @param board the game board
   */
  public SiedlerBoardTextView(SiedlerBoard board) {
    super(board);
    Map<Point, Integer> lowerFieldLabel = Config.getStandardDiceNumberPlacement();
    for (Map.Entry<Point, Integer> e : lowerFieldLabel.entrySet()) {
    	if(board.getThief().equals(e.getKey())) {
    		setLowerFieldLabel(e.getKey(), new Label('T','H'));  
    	}
    	else if(e.getValue() < 10) {
    		char digit = Character.forDigit(e.getValue(), 16);
    		setLowerFieldLabel(e.getKey(), new Label('0',digit));
    	}
    	else {
    		char firstDigit = Character.forDigit(Integer.parseInt(Integer.toString(e.getValue()).substring(0,1)), 16);
    		char secondDigit = Character.forDigit(Integer.parseInt(Integer.toString(e.getValue()).substring(1)), 16);
    		setLowerFieldLabel(e.getKey(), new Label(firstDigit, secondDigit));
    	}
     }
  }
}
