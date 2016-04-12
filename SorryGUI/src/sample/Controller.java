package sample;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class Controller implements Initializable,EventHandler<MouseEvent>{

    @FXML
    protected BorderPane borderPane;

    @FXML
    protected HBox topPane,bottomPane;

    @FXML
    protected VBox rightPane,leftPane;

    @FXML
    protected Pane discardCard, blueHome,yellowHome,greenHome,redHome;

    @FXML
    protected Button pickCard;

    @FXML
    protected TextField playerName;

    @FXML
    protected Text textOutput,playerTurn;

    protected Deck deck;
    protected ArrayList<StackPane> tiles;
    protected Pawn[] bluePawns;
    protected Pawn[] yellowPawns;
    protected Pawn[] redPawns;
    protected Pawn[] greenPawns;
    protected Card currentCard;
    protected int startGameClicks;
    boolean playerOneTurn = true;
    boolean playerTwoTurn = false;
    String nameOfPlayer;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        deck = new Deck();
        tiles = new ArrayList(64);//Create a list that holds all stack panes for top row
        ArrayList<StackPane> leftTiles = new ArrayList(16);
        ArrayList<StackPane> bottomTiles = new ArrayList(16);

        //Create all 64 tiles
        tiles = createStackPanes(tiles, topPane, 16);
        tiles = createStackPanes(tiles, rightPane, 14);
        leftTiles = createStackPanes(leftTiles, leftPane, 14);
        bottomTiles = createStackPanes(bottomTiles, bottomPane, 16);
        Collections.reverse(bottomTiles);
        Collections.reverse(leftTiles);
        tiles.addAll(bottomTiles);
        tiles.addAll(leftTiles);

        //Display Back of Sorry Card
        File f = new File("style.css"); // open style sheet
        pickCard.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));


        playerTurn.setText("Enter your user name then press 'Start Game'");


    }
    /*
    Start Game
     */
    public void startGame(){
        if(textEntered()==false){

        }
        else {
            startGameClicks++;
            if (startGameClicks < 2) {
                //Spawn all pawns in Home base
                this.bluePawns = spawnPawns(blueHome, Color.BLUE);
                this.yellowPawns = spawnPawns(yellowHome, Color.YELLOW);
                this.greenPawns = spawnPawns(greenHome, Color.GREEN);
                this.redPawns = spawnPawns(redHome, Color.RED);


                for (int i = 0; i < 4; i++) {
                    bluePawns[i].getCircle().setOnMouseClicked(this);
                    yellowPawns[i].getCircle().setOnMouseClicked(this);
                    greenPawns[i].getCircle().setOnMouseClicked(this);
                    redPawns[i].getCircle().setOnMouseClicked(this);
                }
                playerName.setDisable(true);
                nameOfPlayer = playerName.getText();
                playGame();

            }
        }
    }

    /*
    Start Game Over
     */
    public void startOver(){
        if(startGameClicks>=1){
        startGameClicks = 0;
        for (int i = 0; i < 58; i++) {
            for(int j=0;j<4;j++){
                tiles.get(i).getChildren().remove(bluePawns[j].getCircle());
                blueHome.getChildren().remove(bluePawns[j].getCircle());
                tiles.get(i).getChildren().remove(yellowPawns[j].getCircle());
                yellowHome.getChildren().remove(yellowPawns[j].getCircle());
                tiles.get(i).getChildren().remove(redPawns[j].getCircle());
                redHome.getChildren().remove(redPawns[j].getCircle());
                tiles.get(i).getChildren().remove(greenPawns[j].getCircle());
                greenHome.getChildren().remove(greenPawns[j].getCircle());
            }
        }
            discardCard.getChildren().clear();
            playerName.setDisable(false);
            playerName.setText("");
            startGame();
            playerTurn.setText("Enter your username then press 'Start Game'");
            textOutput.setText("");
            deck.freshDeck();
            currentCard = null;
            playerOneTurn = true;

        }
    }
    /*
    Checks if a radio button is selected
     */
    public boolean textEntered(){
        if(playerName.getText().trim().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    /*
    Creates 16 stack panes that represent one side of tiles. Returns an ArrayList with the 16 stack panes.
     */
    public ArrayList<StackPane> createStackPanes(ArrayList<StackPane> tiles,Pane pane,int numStacks){
        for (int i=0;i<numStacks;i++){
            StackPane stacks = new StackPane();
            stacks.setPrefSize(39,39);
            pane.getChildren().addAll(stacks);
            tiles.add(stacks);
        }
        return tiles;
    }

    /*
    Spawns 5 Circles in each Home base saves pawns in array
     */
    public Pawn[] spawnPawns(Pane pane,Color color){
        double xLocation =0, yLocation = 0;
        Pawn[] pawns = new Pawn[4];
        for(int i=0;i<4;i++){
            Pawn p = new Pawn(color,Integer.toString(i));
            pane.getChildren().add(p.getCircle());
            p.getCircle().relocate(xLocation,yLocation);
            xLocation = xLocation+20;
            if(i==1 || i==3) {
                yLocation = yLocation + 20;
                xLocation= 0;
            }
            pawns[i] = p;
        }
    return pawns;
    }

    /*
    When Card Pile is clicked, this method displays a card.
     */
    public void newCard(){
        if(startGameClicks>=1 && playerOneTurn){
        Card currentCard = deck.drawCard();
        String cardNumber = Integer.toString(currentCard.getNumber());
        File file = new File("cards/"+cardNumber+".png");
        Image card = new Image(file.toURI().toString());
        ImageView showCard = new ImageView();
        showCard.setImage(card);
        discardCard.getChildren().add(showCard);
        textOutput.setText(currentCard.toString());
        this.currentCard = currentCard;
        enablePawns(bluePawns);
            playerOneTurn = false;

            //check for moves, if no moves, then it is computers turn
            if(!checkForMoves() && !playerTwoTurn){
                playerTurn.setText("No Moves");
                //Pause 2 Seconds
                Timeline waitTwo = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {

                        computerTurn();
                    }
                }));
                waitTwo.setCycleCount(1);
                waitTwo.play();
            }

            }
        }




    @Override
    public void handle(MouseEvent event){
            boolean home = false;
            //If pawns is in home and the card is a 1 or a 2
            if (blueHome.getChildren().contains(event.getSource()) && currentCard.getNumber() <=2) {
                Circle c = (Circle) event.getSource();
                movePawnFromStart(bluePawns[Integer.parseInt(c.getId())],Color.BLUE);
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

    /*
    Moves the pawn from home base out 1 position
     */
    public void movePawnFromStart(Pawn p, Color color){
        if(color == Color.BLUE){
            p.setLocation(34);
            disableUsersPawns(bluePawns,p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
            }

        else if(color == Color.YELLOW){
            p.setLocation(49);
            disableUsersPawns(yellowPawns,p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        }
        else if(color == Color.GREEN){
           p.setLocation(4);
            disableUsersPawns(greenPawns,p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        }
        else if(color == Color.RED){
            p.setLocation(19);
            disableUsersPawns(redPawns,p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        }

    }

    /*
    Moves pawn 1 space forward
     */
    public void movePawn(Pawn p){

            if (p.getLocation() == 59) {
                tiles.get(0).getChildren().add(p.getCircle());
                p.setLocation(0);

            } else {
                tiles.get(p.getLocation() + 1).getChildren().add(p.getCircle());
                p.setLocation(p.getLocation() + 1);

                }
            }

    /*
    Animates pawn forward a certain number of spaces
     */
    public void animateUserPawn(int spaces, Pawn p){
        Timeline animate = new Timeline(new KeyFrame(Duration.seconds(.5), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                movePawn(p);
            }
        }));
        animate.setCycleCount(spaces);
        animate.play();
        //If users turn, wait for animation to finish
        animate.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            computerTurn();
            }
        });

    }



    /*
    Disable all pawns except players pawns
     */
    public void disablePawns(Pawn[] p){
        for (int i = 0; i < 4; i++) {
            bluePawns[i].getCircle().setDisable(true);
            yellowPawns[i].getCircle().setDisable(true);
            greenPawns[i].getCircle().setDisable(true);
            redPawns[i].getCircle().setDisable(true);

        }

    }

    /*
    Disable all pawns except selected pawn
     */
    public void disableUsersPawns(Pawn[] pawns, Pawn p){
        for(int i=0;i<4;i++){
           pawns[i].getCircle().setDisable(true);
        }
        p.getCircle().setDisable(false);
    }

    /*
    Enables all pawns again
     */
    public void enablePawns(Pawn[] p){
        for (int i = 0; i < 4; i++) {
            p[i].getCircle().setDisable(false);

        }
    }

    /*
    Computers turn. Gets card and makes move
     */
    public void computerTurn(){

        playerTurn.setText("Computers Turn");

        //Get Card, check if 1 or two
        playerOneTurn = true;
        playerTwoTurn = true;
        newCard();
        playerTwoTurn = false;
        if(currentCard.getNumber()<=2){
            movePawnFromStart(redPawns[1],Color.RED);
        }
        playerOneTurn = true;
        disablePawns(bluePawns);
        disablePawns(redPawns);

        //Animate Computer pawn, when animation done, print that it is users turn. If no moves, print No moves

        //Pause 2 Seconds
        Timeline waitTwo = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                playerTurn.setText(nameOfPlayer+ "'s Turn. Click deck to get card. Your pawn color is blue");
            }
        }));
        waitTwo.setCycleCount(1);
        waitTwo.play();
    }

    /*
    Check's to see if player has any moves.  Returns true if has moves, false if no moves
     */
    public boolean checkForMoves(){
        //If card is not 1 or 2 and no pawns are outside, then no moves exist
        if(currentCard.getNumber() >2 && blueHome.getChildren().contains(bluePawns[0].getCircle())
            &&blueHome.getChildren().contains(bluePawns[1].getCircle())
    && blueHome.getChildren().contains(bluePawns[2].getCircle()) && blueHome.getChildren().contains(bluePawns[3].getCircle())){
     return false;
    }
        else{
        return true;
        }
    }


    /*
    Play Game
     */
    public void playGame() {

        //game Logic here
        playerTurn.setText(nameOfPlayer+ "'s Turn. Click deck to get card. Your pawn color is blue");
        disablePawns(bluePawns);

        }
    }








