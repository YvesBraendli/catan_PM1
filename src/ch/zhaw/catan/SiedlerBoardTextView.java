package ch.zhaw.catan;

import java.awt.Point;
import java.util.Map;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.hexboard.Label;

public class SiedlerBoardTextView extends HexBoardTextView<Land, String, String, String> {

  public SiedlerBoardTextView(SiedlerBoard board) {
    super(board);
    Map<Point, Integer> lowerFieldLabel = Config.getStandardDiceNumberPlacement();
    for (Map.Entry<Point, Integer> e : lowerFieldLabel.entrySet()) {
    	if(e.getValue() < 10) {
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
