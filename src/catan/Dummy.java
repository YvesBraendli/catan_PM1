package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.Label;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class Dummy {

  public enum Actions {
    SHOW, QUIT
  }

  private void run() {
    TextIO textIO = TextIoFactory.getTextIO();
    TextTerminal<?> textTerminal = textIO.getTextTerminal();

    SiedlerBoard board = new SiedlerBoard();
    board.addField(new Point(2, 2), Land.FOREST);
    board.setCorner(new Point(3, 3), "RR");
    board.setEdge(new Point(2, 0), new Point(3, 1), "r");
    board.addFieldAnnotation(new Point(2, 2), new Point(3, 1), "AA");

    Map<Point, Label> lowerFieldLabel = new HashMap<>();
    lowerFieldLabel.put(new Point(2, 2), new Label('0', '9'));
    SiedlerBoardTextView view = new SiedlerBoardTextView(board);

    for (Map.Entry<Point, Label> e : lowerFieldLabel.entrySet()) {
      view.setLowerFieldLabel(e.getKey(), e.getValue());
    }

    boolean running = true;
    while (running) {
      switch (getEnumValue(textIO, Actions.class)) {
        case SHOW:
          textTerminal.println(view.toString());
          break;
        case QUIT:
          running = false;
          break;
        default:
          throw new IllegalStateException("Internal error found - Command not implemented.");
      }
    }
    textIO.dispose();
  }

  public static <T extends Enum<T>> T getEnumValue(TextIO textIO, Class<T> commands) {
    return textIO.newEnumInputReader(commands).read("What would you like to do?");
  }

  public static void main(String[] args) {
    new Dummy().run();
  }
}
