package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPopup implements Initializable{
    @FXML
    private RadioButton button1,button2,button3,button4;
    @FXML
    private Button closeButton;
    private RadioButton selectedButton;
    private ToggleGroup group;
    Controller c;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        group = new ToggleGroup();
        button1.setToggleGroup(group);
        button2.setToggleGroup(group);
        button3.setToggleGroup(group);
        button4.setToggleGroup(group);
    }

    public void handleTen(){
        c.disablePawns(c.bluePawns);
        button3.setDisable(true);
        button4.setDisable(true);
        if(!checkMoves(10)){
            button2.setSelected(true);
            button1.setText("Move forward 10- No valid Moves");
            button2.setText("Move backwards 1");
            button1.setDisable(true);
        }
        else{
        button1.setSelected(true);
        button1.setText("Move forward 10");
        button2.setText("Move backwards 1");
        }

    }

    public void handleEleven(){
        c.disablePawns(c.bluePawns);
        button3.setDisable(true);
        button4.setDisable(true);
        if(!checkMoves(11)){
            button1.setSelected(true);
            button1.setText("Swap with another pawn");
            button2.setText("Move forward 11 - No valid moves");
            button2.setDisable(true);
        }
        else if(!c.checkSwaps()){
            button2.setSelected(true);
            button1.setText("Swap with another pawn- No valid moves");
            button2.setText("Move forward 11");
            button1.setDisable(true);
        }
        else{
        button1.setSelected(true);
        button1.setText("Swap with another pawn");
        button2.setText("Move forward 11");
        button3.setDisable(true);
        button4.setDisable(true);
        }
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        selectedButton = (RadioButton) group.getSelectedToggle();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        c.enablePawns(c.bluePawns);
        if(c.currentCard.getMoves() == 11 && getSelectedButton().getId().equals("button1")){
            c.playerTurn.setText("Click your pawn then the computers pawn to swap");
        }


    }

    public RadioButton getSelectedButton(){
        return selectedButton;
    }

    public void setController(Controller c){
        this.c = c;
    }

    public boolean checkMoves(int moves){
        int checkMove = 0;
        for(int i = 0;i<4;i++){
            int location = c.bluePawns[i].getLocation();
            if(!c.checkMoves(c.bluePawns[i],moves)){
                checkMove++;
            }

        }
        return checkMove <4;
    }

}
