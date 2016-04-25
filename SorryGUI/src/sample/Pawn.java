package sample;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;


/**
 * Created by Jack on 3/23/16.
 */
public class Pawn extends Circle{
private int location;
private Circle circle;
private int safeZoneLocation;
private boolean inSafeZone;

    public Pawn(Color c, String id){
        Circle circle = new Circle(10);
        circle.setFill(c);
        circle.setId(id);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1.5);
        this.circle = circle;
        location = -1;
        inSafeZone = false;
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

    public void setSafeZoneLocation(int location){
        safeZoneLocation = location;
    }

    public int getSafeZoneLocation(){
        return safeZoneLocation;
    }

    public boolean inSafeZone(){
        return inSafeZone;
    }

    public void setInSafeZone(boolean b){
        inSafeZone = b;
    }
}
