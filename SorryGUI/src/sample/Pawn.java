package sample;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;


/**
 * Created by Jack on 3/23/16.
 */
public class Pawn extends Circle{
private int location;
private Circle circle;
private int id;

    public Pawn(Color c, String id){
        Circle circle = new Circle(10);
        circle.setFill(c);
        circle.setId(id);
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
}
