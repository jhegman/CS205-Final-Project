package sample;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;


/**
 * Created by Jack on 3/23/16.
 */
public class Pawn extends Circle{
private int location;
private Circle circle;
private int moves;

    public Pawn(Color c, String id){
        Circle circle = new Circle(10);
        circle.setFill(c);
        circle.setId(id);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1.5);
        this.circle = circle;
        location = 0;
    }

    public Circle getCircle(){

        return circle;
    }


    public int getLocation(){

        return location;
    }

    public void setLocation(int location){

        this.location = location;
    }

    public void setMoves(int moves){
        this.moves = moves;
    }

    public void move(){
        moves--;
    }

    public int getMoves() {
        return moves;
    }

    public void resetMoves(){
        moves = 0;
    }
}
