package sample;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import sample.Controller;

/**
 * Created by Jack on 4/11/16.
 */
public class Eventhandler extends Controller implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event){
        boolean home = false;
        //If pawns is in home and the card is a 1 or a 2
        if (blueHome.getChildren().contains(event.getSource()) && currentCard.getNumber() <=2) {
            Circle c = (Circle) event.getSource();
            movePawnFromStart(bluePawns[Integer.parseInt(c.getId())], Color.BLUE);
            if(currentCard.getNumber() ==2){
                animateUserPawn(1,bluePawns[Integer.parseInt(c.getId())]);
            }
            disablePawns(bluePawns);//disable pawns again
            home = true;
            computerTurn();
            //COMPUTER TURN

        }

        if(yellowHome.getChildren().contains(event.getSource())) {
            Circle c = (Circle) event.getSource();
            movePawnFromStart(yellowPawns[Integer.parseInt(c.getId())],Color.YELLOW);
            home = true;
        }

        if (redHome.getChildren().contains(event.getSource())){
            Circle c = (Circle) event.getSource();
            movePawnFromStart(redPawns[Integer.parseInt(c.getId())],Color.RED);
            home = true;
        }

        if (greenHome.getChildren().contains(event.getSource())){
            Circle c = (Circle) event.getSource();
            movePawnFromStart(greenPawns[Integer.parseInt(c.getId())],Color.GREEN);
            home = true;
        }

        //if pawn isnt in home, move a certain amount of spaces
        else if(!home && !blueHome.getChildren().contains(event.getSource())){
            Circle c = (Circle) event.getSource();
            Paint color = c.getFill();
            if(color == Color.BLUE){
                disableUsersPawns(bluePawns,bluePawns[Integer.parseInt(c.getId())]);
                animateUserPawn(currentCard.getNumber(),bluePawns[Integer.parseInt(c.getId())]);
                disablePawns(bluePawns);//disable pawns again
                //computerTurn();
                //COMPUTER TURN, re enable card button, make move
            }
            if(color == Color.RED){
                animateUserPawn(currentCard.getNumber(),redPawns[Integer.parseInt(c.getId())]);
                disableUsersPawns(redPawns,redPawns[Integer.parseInt(c.getId())]);
            }
            if(color == Color.YELLOW){
                animateUserPawn(currentCard.getNumber(),yellowPawns[Integer.parseInt(c.getId())]);
                disableUsersPawns(yellowPawns,yellowPawns[Integer.parseInt(c.getId())]);
            }
            if(color == Color.GREEN){
                animateUserPawn(currentCard.getNumber(),greenPawns[Integer.parseInt(c.getId())]);
                disableUsersPawns(greenPawns,greenPawns[Integer.parseInt(c.getId())]);
            }
        }
    }
}
