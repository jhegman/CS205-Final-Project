package sample;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Controller implements Initializable,EventHandler<MouseEvent> {

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox topPane, bottomPane;

    @FXML
    private VBox rightPane, leftPane, blueSafe, greenSafe;

    @FXML
    private Pane discardCard, blueHome, yellowHome, greenHome, redHome, greenHomeCircle, blueHomeCircle;

    @FXML
    private Button pickCard, help;

    @FXML
    private TextField playerName;

    @FXML
    public Text textOutput, playerTurn, numGreenPawnsHome, numBluePawnsHome;

    private Deck deck;
    public ArrayList<StackPane> tiles;
    private ArrayList<StackPane> blueSafeZone;
    private ArrayList<StackPane> greenSafeZone;
    public Pawn[] bluePawns;
    private Pawn[] yellowPawns;
    private Pawn[] redPawns;
    private Pawn[] greenPawns;
    public Card currentCard;
    private int startGameClicks;
    boolean playerOneTurn = true;
    boolean playerTwoTurn = false;
    String nameOfPlayer;
    int occupied; //The space where the pawn will move to
    int sorryClicks = 0; //How many times sorry function has been called
    Pawn sorryPawn1;
    Pawn sorryPawn2;
    board gameBoard;
    computer nice;
    int[] positions; // Positions of Computer pawns
    int computerStart;
    ControllerPopup controller;
    int bluePawnsInHome;
    int greenPawnsInHome;
    int numberMoves;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        deck = new Deck();
        //noinspection unchecked
        tiles = new ArrayList(64);//Create a list that holds all stack panes for top row
        //noinspection unchecked
        blueSafeZone = new ArrayList(6);
        greenSafeZone = new ArrayList(6);
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

        //Create blue and green safe zones
        blueSafeZone = createStackPanes(blueSafeZone, blueSafe, 6);
        Collections.reverse(blueSafeZone);
        greenSafeZone = createStackPanes(greenSafeZone,greenSafe,6);

        //Display Back of Sorry Card
        File f = new File("style.css"); // open style sheet
        pickCard.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));


        playerTurn.setText("Enter your user name then press 'Start Game'");


    }

    /*
    Start Game
     */
    public void startGame() {
        if (textEntered() == false) {

        } else {
            startGameClicks++;
            if (startGameClicks < 2) {
                //Spawn all pawns in Home base
                this.bluePawns = spawnPawns(blueHome, Color.BLUE);
                this.yellowPawns = spawnPawns(yellowHome, Color.YELLOW);
                this.greenPawns = spawnPawns(greenHome, Color.LIMEGREEN);
                this.redPawns = spawnPawns(redHome, Color.RED);


                for (int i = 0; i < 4; i++) {
                    bluePawns[i].getCircle().setOnMouseClicked(this);
                    greenPawns[i].getCircle().setOnMouseClicked(this);
                }
                playerName.setDisable(true);
                nameOfPlayer = playerName.getText();
                gameBoard = new board();
                nice = new computer(true);
                //tiles.get(30).getChildren().add(bluePawns[0].getCircle());
                //bluePawns[0].setLocation(30);
                playGame();

            }
        }
    }

    /*
    Start Game Over
     */
    public void startOver() {
        if (startGameClicks >= 1) {
            startGameClicks = 0;
            for (int i = 0; i < 58; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles.get(i).getChildren().remove(bluePawns[j].getCircle());
                    blueHome.getChildren().remove(bluePawns[j].getCircle());
                    yellowHome.getChildren().remove(yellowPawns[j].getCircle());
                    redHome.getChildren().remove(redPawns[j].getCircle());
                    tiles.get(i).getChildren().remove(greenPawns[j].getCircle());
                    greenHome.getChildren().remove(greenPawns[j].getCircle());
                }
            }
            blueHomeCircle.getChildren().clear();
            greenHomeCircle.getChildren().clear();
            for(int i=0;i<6;i++){
                for(int j=0;j<4;j++){
                blueSafeZone.get(i).getChildren().remove(bluePawns[j].getCircle());
                    greenSafeZone.get(i).getChildren().remove(greenPawns[j].getCircle());
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
            gameBoard = new board();
            nice = new computer(true);
            greenPawnsInHome = 0;
            bluePawnsInHome =0;
            numGreenPawnsHome.setText("Pawn's Home: 0");
            numBluePawnsHome.setText("Pawn's Home: 0");

        }
    }

    /*
    Checks if a radio button is selected
     */
    public boolean textEntered() {
        if (playerName.getText().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /*
    Creates 16 stack panes that represent one side of tiles. Returns an ArrayList with the 16 stack panes.
     */
    public ArrayList<StackPane> createStackPanes(ArrayList<StackPane> tiles, Pane pane, int numStacks) {
        for (int i = 0; i < numStacks; i++) {
            StackPane stacks = new StackPane();
            stacks.setPrefSize(39, 39);
            pane.getChildren().addAll(stacks);
            tiles.add(stacks);
        }
        return tiles;
    }

    /*
    Spawns 5 Circles in each Home base saves pawns in array
     */
    public Pawn[] spawnPawns(Pane pane, Color color) {
        double xLocation = 0, yLocation = 0;
        Pawn[] pawns = new Pawn[4];
        for (int i = 0; i < 4; i++) {
            Pawn p = new Pawn(color, Integer.toString(i));
            pane.getChildren().add(p.getCircle());
            p.getCircle().relocate(xLocation, yLocation);
            xLocation = xLocation + 20;
            if (i == 1 || i == 3) {
                yLocation = yLocation + 20;
                xLocation = 0;
            }
            pawns[i] = p;
        }
        return pawns;
    }

    /*
    When Card Pile is clicked, this method displays a card.
     */
    public void newCard() {
        if (startGameClicks >= 1 && playerOneTurn) {
            Card currentCard = deck.drawCard();
            String cardNumber = Integer.toString(currentCard.getNumber());
            File file = new File("cards/" + cardNumber + ".png");
            Image card = new Image(file.toURI().toString());
            ImageView showCard = new ImageView();
            showCard.setImage(card);
            discardCard.getChildren().add(showCard);
            textOutput.setText(currentCard.toString());
            this.currentCard = currentCard;
            enablePawns(bluePawns);
            playerOneTurn = false;
            if (currentCard.getMoves() == 13 && !playerTwoTurn) {
                playerTurn.setText("Click one of your pawns in home and then click a green pawn outside home to swap with");
            }

            //check for moves, if no moves, then it is computers turn
            if (!checkForMoves() && !playerTwoTurn) {
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
            else if(currentCard.getMoves() == 10 || currentCard.getMoves() == 11 ){
                if(!playerTwoTurn){
                newWindow();
                }
            }

        }
    }


    @Override
    public void handle(MouseEvent event) {
        boolean home = false;
        boolean bump = false; //bump or not
        boolean backwards = false;
        Circle c = (Circle) event.getSource();
        Pawn b = bluePawns[Integer.parseInt(c.getId())];
        Pawn g = greenPawns[Integer.parseInt(c.getId())];
        //Sorry! Card
        if (currentCard.getMoves() == 13 && checkSorryMoves()) {
            if (sorryClicks == 0 && c.getFill() == Color.BLUE) {
                if (blueHome.getChildren().contains(b.getCircle())) {
                    sorryPawn1 = b;
                    enablePawns(greenPawns);
                    sorryClicks++;
                } else {
                    playerTurn.setText("Must click pawn in home");
                }
            } else if (sorryClicks == 1 && c.getFill() == Color.LIMEGREEN) {
                if (!greenHome.getChildren().contains(g.getCircle())) {
                    disablePawns(bluePawns);
                    sorryPawn2 = g;
                    handleSorry(sorryPawn1, sorryPawn2);
                    sorryClicks = 0;
                    computerTurn();
                } else {
                    playerTurn.setText("Must click green pawn outside of start");
                }
            }
        }
        else if(currentCard.getMoves() == 10 &&
                controller.getSelectedButton().getId().equals("button2")){
                int location1 = b.getLocation() -1;
                if(location1 <0){
                    location1 = 59;
                }
                if(checkIfOccupied(tiles.get(location1),b).equals("bump")){
                    bump(b.getLocation(),Color.BLUE);
                }
                else if(checkIfOccupied(tiles.get(location1),b).equals("true")){
                    bump(b.getLocation(),Color.LIMEGREEN);
                }
                movePawnBackwards(b);
                slide(b);
                computerTurn();

        }
        //SWAP PAWNS 11 CARD
        else if(currentCard.getMoves() == 11 &&
                controller.getSelectedButton().getId().equals("button1")){
            if (sorryClicks == 0 && c.getFill() == Color.BLUE) {
                if (!blueHome.getChildren().contains(b.getCircle()) && !b.inSafeZone()) {
                    sorryPawn1 = b;
                    enablePawns(greenPawns);
                    sorryClicks++;
                } else {
                    playerTurn.setText("Must click pawn not in home or safe zone");
                }
            } else if (sorryClicks == 1 && c.getFill() == Color.LIMEGREEN) {
                if (!greenHome.getChildren().contains(g.getCircle())) {
                    disablePawns(bluePawns);
                    sorryPawn2 = g;
                    handleSwap(sorryPawn1, sorryPawn2);
                    sorryClicks = 0;
                    computerTurn();
                } else {
                    playerTurn.setText("Must click green pawn outside of start");
                }
            }
        }

        //If pawns is in home,no pawn in the way and the card is a 1 or a 2
        else if (blueHome.getChildren().contains(event.getSource())
                && currentCard.getMoves() <= 2) {

            //check if a blue pawn is in the way
            if (checkIfOccupied(tiles.get(34), b).equals("true")) {
                playerTurn.setText("Not a valid move");
            } else {
                if (checkIfOccupied(tiles.get(34), b).equals("bump")) {
                    bump(34, Color.BLUE);
                }
                movePawnFromStart(b, Color.BLUE);
                disablePawns(bluePawns);//disable pawns again
                home = true;
                //Go again if card is 2
                if(currentCard.getMoves() ==2){
                    playerTurn.setText("Draw again and make another move");
                    playerOneTurn = true;

                }
                else{
                computerTurn();
                }
            }


        }

        //Check to see if user can move pawn into home
        else if (b.getLocation() < 33 && b.getLocation() + currentCard.getMoves() > 38) {
            playerTurn.setText("Not a valid move. Move a different pawn");
        }
        //Check to see if user can move into home from safe zone
        else if (b.inSafeZone() &&
                b.getSafeZoneLocation() + currentCard.getMoves() > 5 && currentCard.getMoves() != 4) {
            playerTurn.setText("Not a valid move. Move a different pawn");
        }

        //Check if own pawn is in the way in the safe zone
        else if (b.inSafeZone() && b.getSafeZoneLocation() + currentCard.getMoves() <= 5) {
            int position = b.getSafeZoneLocation() + currentCard.getMoves();

            //If pawn is in space you're trying to move to
            if (checkIfOccupied(blueSafeZone.get(position), b).equals("true")
                    && b.getSafeZoneLocation() + currentCard.getMoves() != 5) {
                playerTurn.setText("Not a valid move");
            }
            //Else, move in safe zone
            else {
                disableUsersPawns(bluePawns, b);
                animateUserPawn(currentCard.getMoves(), b, bump);
                disablePawns(bluePawns);//disable pawns again
            }
        }

        //if pawn isnt in home, move a certain amount of spaces
        else if (!home && !blueHome.getChildren().contains(event.getSource())) {
            int occupied = 0;
            if (currentCard.getMoves() == 4) {
                occupied = b.getLocation() - currentCard.getMoves();
                if (occupied < 0) {
                    occupied = 60 + occupied;
                    backwards = true;
                }
            } else {
                occupied = b.getLocation() + currentCard.getMoves();
                if (occupied > 59) {
                    occupied = occupied - 60;
                }
            }
            this.occupied = occupied;

            //Moving around board, not into home
            Paint color = c.getFill();
            if (color == Color.BLUE) {
                //check for open space when moving into home
                if (!backwards && b.getLocation() <= 32 && occupied > 32) {
                    int position = occupied - 33;
                    if (position!= 5 && checkIfOccupied(blueSafeZone.get(position), b).equals("true")) {
                        playerTurn.setText("Not a valid move");
                    } else {
                        disableUsersPawns(bluePawns, b);
                        animateUserPawn(currentCard.getMoves(), b, bump);
                        disablePawns(bluePawns);//disable pawns again
                    }

                } else if (checkIfOccupied(tiles.get(occupied), b).equals("true")) {
                    playerTurn.setText("Not a valid move");

                } else {
                    disableUsersPawns(bluePawns, b);
                    //Check for bump
                    if (checkIfOccupied(tiles.get(occupied), b).equals("bump")) {
                        bump = true;
                    }
                    animateUserPawn(currentCard.getMoves(), b, bump);
                    disablePawns(bluePawns);//disable pawns again
                }
            }

        }

    }


    /*
    Moves the pawn from home base out 1 position
     */
    public void movePawnFromStart(Pawn p, Color color) {
        if (color == Color.BLUE) {
            p.setLocation(34);
            disableUsersPawns(bluePawns, p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        } else if (color == Color.LIMEGREEN) {
            p.setLocation(4);
            disableUsersPawns(greenPawns, p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        }
        numberMoves++;

    }

    /*
    Moves pawn 1 space forward
     */
    public void movePawn(Pawn p) {

        if (p.getLocation() == 59) {
            tiles.get(0).getChildren().add(p.getCircle());
            p.setLocation(0);

        } else if (p.getLocation() == 32 && p.getCircle().getFill() == Color.BLUE && !p.inSafeZone()) {
            blueSafeZone.get(0).getChildren().add(p.getCircle());
            p.setSafeZoneLocation(0);
            p.setInSafeZone(true);
        } else if (p.getCircle().getFill() == Color.BLUE && p.inSafeZone()) {
            blueSafeZone.get(p.getSafeZoneLocation() + 1).getChildren().add(p.getCircle());
            p.setSafeZoneLocation(p.getSafeZoneLocation() + 1);
        } else if(p.getLocation() == 2 && p.getCircle().getFill() == Color.LIMEGREEN && !p.inSafeZone()){
             greenSafeZone.get(0).getChildren().add(p.getCircle());
               p.setSafeZoneLocation(0);
               p.setInSafeZone(true);
        } else if (p.getCircle().getFill() == Color.LIMEGREEN && p.inSafeZone()) {
            greenSafeZone.get(p.getSafeZoneLocation() + 1).getChildren().add(p.getCircle());
            p.setSafeZoneLocation(p.getSafeZoneLocation() + 1);
        }
        else {
            tiles.get(p.getLocation() + 1).getChildren().add(p.getCircle());
            p.setLocation(p.getLocation() + 1);

        }

    }

    /*
    Move pawn backwards one space
     */
    public void movePawnBackwards(Pawn p) {
        //If backwards 4 while in safe zone
        if (p.inSafeZone()) {
            if (p.getSafeZoneLocation() == 0 && p.getCircle().getFill() == Color.BLUE) {
                tiles.get(32).getChildren().add(p.getCircle());
                p.setLocation(32);
                p.setInSafeZone(false);
            } else if(p.getCircle().getFill() == Color.BLUE) {
                blueSafeZone.get(p.getSafeZoneLocation() - 1).getChildren().add(p.getCircle());
                p.setSafeZoneLocation(p.getSafeZoneLocation() - 1);
            }
            if (p.getSafeZoneLocation() == 0 && p.getCircle().getFill() == Color.LIMEGREEN) {
                tiles.get(2).getChildren().add(p.getCircle());
                p.setLocation(2);
                p.setInSafeZone(false);
            } else if(p.getCircle().getFill() == Color.LIMEGREEN){
                greenSafeZone.get(p.getSafeZoneLocation() - 1).getChildren().add(p.getCircle());
                p.setSafeZoneLocation(p.getSafeZoneLocation() - 1);
            }
        }
        //if go backwards around upper left corner
        else if (p.getLocation() == 0) {
            tiles.get(59).getChildren().add(p.getCircle());
            p.setLocation(59);

        } else {
            tiles.get(p.getLocation() - 1).getChildren().add(p.getCircle());
            p.setLocation(p.getLocation() - 1);

        }
    }

    /*
    Animates pawn forward a certain number of spaces
     */
    public void animateUserPawn(int spaces, Pawn p, boolean bump) {
        Timeline animate = new Timeline(new KeyFrame(Duration.seconds(.4), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (spaces == 4) {
                    movePawnBackwards(p);
                } else {
                    movePawn(p);
                }
            }
        }));
        animate.setCycleCount(spaces);
        animate.play();

        //If users turn, wait for animation to finish
        animate.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                numberMoves++;
                if(p.getSafeZoneLocation() == 5){
                    bluePawnsInHome++;
                    blueSafeZone.get(5).getChildren().remove(p.getCircle());
                    numBluePawnsHome.setText("Pawn's Home: "+bluePawnsInHome);
                    addPawnsToHome(bluePawnsInHome, Color.BLUE);
                }
                //Check for slides or bumps
                slide(p);
                if (bump) {
                    bump(occupied, Color.BLUE);
                }
                //Move again if card is 2
                if(currentCard.getMoves() == 2){
                    playerTurn.setText("Draw again and make another move");
                    playerOneTurn = true;
                }
                else{
                    if(bluePawnsInHome == 4){
                        endGame(nameOfPlayer.toUpperCase());
                    }
                    else{
                    computerTurn();
                    }
                }
            }
        });

    }

    /*
    Animates pawn forward a certain number of spaces
     */
    public void animateComputerPawn(int spaces, Pawn p, boolean bump) {
        Timeline animate = new Timeline(new KeyFrame(Duration.seconds(.4), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (spaces == 4) {
                    movePawnBackwards(p);
                } else {
                    movePawn(p);
                }
            }
        }));
        animate.setCycleCount(spaces);
        animate.play();

        // wait for animation to finish
        animate.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                numberMoves++;
                if(p.getSafeZoneLocation() == 5){
                    greenPawnsInHome++;
                    greenSafeZone.get(5).getChildren().remove(p.getCircle());
                    numGreenPawnsHome.setText("Pawn's Home: "+greenPawnsInHome);
                    addPawnsToHome(greenPawnsInHome, Color.LIMEGREEN);
                }
                slide(p);
                if (bump) {
                    bump(positions[2], Color.LIMEGREEN);
                }
                if(greenPawnsInHome == 4){
                    endGame("COMPUTER");
                }
                else{
                playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card, then click blue pawn to move");
                playerOneTurn = true;
                disablePawns(bluePawns);
                disablePawns(greenPawns);
                }

            }
        });

    }


    /*
    Disable all pawns except players pawns
     */
    public void disablePawns(Pawn[] p) {
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
    public void disableUsersPawns(Pawn[] pawns, Pawn p) {
        for (int i = 0; i < 4; i++) {
            pawns[i].getCircle().setDisable(true);
        }
        p.getCircle().setDisable(false);
    }

    /*
    Enables all pawns again
     */
    public void enablePawns(Pawn[] p) {
        for (int i = 0; i < 4; i++) {
            p[i].getCircle().setDisable(false);

        }
    }

    /*
    Computers turn. Gets card and makes move
     */
    public void computerTurn() {

        playerTurn.setText("Computers Turn");

        //Get Card, check if 1 or two
        boolean bump = false;
        playerOneTurn = true;
        playerTwoTurn = true;
        newCard();
        playerTwoTurn = false;

        //MOVE COMPUTER PAWN
        int [] positions = nice.makeMove(gameBoard, currentCard.getNumber());
        this.positions = positions;
        for(int i=3;i>=0;i--){
            if(greenHome.getChildren().contains(greenPawns[i].getCircle())){
                computerStart = i;
                break;
            }
        }
        if(positions[0] == -1){
            if (checkIfOccupied(tiles.get(4), greenPawns[computerStart]).equals("bump")) {
                bump(4, Color.LIMEGREEN);
            }
            movePawnFromStart(greenPawns[computerStart],Color.LIMEGREEN);
            playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card, then click blue pawn to move");
            playerOneTurn = true;
            disablePawns(bluePawns);
            disablePawns(greenPawns);
        }
        else if(positions[0] == 0 && positions[1] == 0){
            Timeline waitTwo = new Timeline(new KeyFrame(Duration.seconds(1.5), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card, then click blue pawn to move");
                    playerOneTurn = true;
                    disablePawns(bluePawns);
                    disablePawns(greenPawns);
                }
            }));
            waitTwo.setCycleCount(1);
            waitTwo.play();
        }

        //ERROR Here, If cant find pawn, computer wont move
        else{
            for(int i=0;i<4;i++){
                if(greenPawns[i].getLocation() ==positions[0] ){
                    if(positions[2]>59){

                    }
                    else if(checkIfOccupied(tiles.get(positions[2]),greenPawns[i]).equals("bump")){
                        bump = true;
                    }
                    animateComputerPawn(positions[1],greenPawns[i],bump);
                    break;
                }
                else if(greenPawns[i].inSafeZone() && greenPawns[i].getSafeZoneLocation() == positions[0]){
                    animateComputerPawn(positions[1],greenPawns[i],bump);
                    break;
                }
            }
        }

    }

    /*
    Check's to see if player has any moves.  Returns true if has moves, false if no moves
     */
    public boolean checkForMoves() {
        //If card is not 1 or 2 and no pawns are outside, then no moves exist
        int unmoveable = 0;
        //Check each pawn
        for (int i = 0; i < 4; i++) {
            if (!checkMoves(bluePawns[i],currentCard.getMoves())){
                unmoveable++;
            }
        }
        if(currentCard.getMoves() == 11 && unmoveable ==4){
            if(!checkSwaps()){
                unmoveable = 4;
            }
            else{
                unmoveable = 0;
            }
        }
        return unmoveable < 4;
    }

    /*
    Checks if Stack Pane contains a pawn.
    Returns false if not occupied, true if occupied.
     */
    public String checkIfOccupied(StackPane pane, Pawn p) {
        if (pane.getChildren().toString().equals("[]")) {
            return "false";
        } else {
            //if pawn is same color return true
            if (pane.getChildren().toString().contains(p.getCircle().getFill().toString())) {
                return "true";
            } else {
                return "bump";
            }
        }
    }

    /*
    Play Game
     */
    public void playGame() {

        playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card. Your pawn color is blue");
        disablePawns(bluePawns);

    }

    /*
    Check all available moves. Returns true if moves, false if no moves
     */
    public boolean checkMoves(Pawn b, int moves) {
        boolean move = true;
        boolean backwards = false;
        if (moves == 13 && !checkSorryMoves()) {
            move = false;
        }
        //Check if can move from home
        else if (moves != 13
                && blueHome.getChildren().contains(b.getCircle()) && moves > 2) {
            move = false;
        } else if (blueHome.getChildren().contains(b.getCircle())
                && currentCard.getMoves() <= 2) {
            //check if a blue pawn is in the way
            if (checkIfOccupied(tiles.get(34), b).equals("true")) {
                move = false;
            }
        }
        //Check if can move in home
        else if (b.getLocation() < 33 && b.getLocation() + moves > 38) {
            move = false;
        }
        //Check to see if user can move into home from safe zone
        else if (b.inSafeZone() &&
                b.getSafeZoneLocation() + moves > 5 && moves != 4) {
            move = false;
        }
        //Check if own pawn is in way in safe zone
        else if (b.inSafeZone() && b.getSafeZoneLocation() + moves <= 5) {
            int position = b.getSafeZoneLocation() + moves;

            //If pawn is in space you're trying to move to
            if (checkIfOccupied(blueSafeZone.get(position), b).equals("true")
                    && b.getSafeZoneLocation() + moves != 5) {
                move = false;
            }
        }
        //Check if pawn home
        else if(b.getSafeZoneLocation() ==5){
            move = false;
        }
        //Check for regular moves around board
        else if (!blueHome.getChildren().contains(b.getCircle())) {
            int occupied = 0;
            if (moves == 4) {
                occupied = b.getLocation() - moves;
                if (occupied < 0) {
                    occupied = 60 + occupied;
                    backwards = true;
                }
            } else {
                occupied = b.getLocation() + moves;
                if (occupied > 59) {
                    occupied = occupied - 60;
                }
            }
            if (!backwards && b.getLocation() <= 32 && occupied > 32) {
                int position = occupied - 33;
                if (position!= 5 && checkIfOccupied(blueSafeZone.get(position), b).equals("true")) {
                    move = false;
                }

            }

            else if (checkIfOccupied(tiles.get(occupied), b).equals("true")) {
                move = false;
            }
        }
        return move;
    }

    /*
    Bumps pawn back to home
     */
    public void bump(int location, Color color) {
        double xLocation = 0, yLocation = 0;
        int id = Integer.parseInt(tiles.get(location).getChildren().get(0).getId());
        switch (id) {
            case 1:
                xLocation = 20;
                break;
            case 2:
                xLocation = 0;
                yLocation = 20;
                break;
            case 3:
                xLocation = 20;
                yLocation = 20;

        }
        if (color == Color.BLUE) {
            greenHome.getChildren().add(greenPawns[id].getCircle());
            greenPawns[id].getCircle().relocate(xLocation, yLocation);
            greenPawns[id].setLocation(-1);
            nice.gotBumped(gameBoard,location);
        } else if (color == Color.LIMEGREEN) {
            blueHome.getChildren().add(bluePawns[id].getCircle());
            bluePawns[id].getCircle().relocate(xLocation, yLocation);
        }
    }

    /*
    Slide pawn
     */
    public void slide(Pawn p) {
        Color color = (Color) p.getCircle().getFill();
        Color color2;
        if(color == Color.BLUE){
           color2 = Color.LIMEGREEN;
        }
        else{
            color2 = Color.BLUE;
        }
        int move = 0;
        List<Integer> allSlides = Arrays.asList(16,24,46,54);
        List<Integer> blueSlides = Arrays.asList(31,39);
        List<Integer> greenSlides = Arrays.asList(1,9);
        int location = p.getLocation();
        if (allSlides.contains(location)){
            if(location == 16 || location == 46){
                move = 3;
            }
            else{
                move = 4;
            }
            for (int i = 0; i < move; i++) {
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("bump")) {
                    bump(p.getLocation() + 1, color);
                }
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("true")) {
                    bump(p.getLocation() + 1, color2);
                }
                movePawn(p);
            }
        }
        else if (blueSlides.contains(location) || greenSlides.contains(location)){
            if(location == 1 || location == 31){
                move = 3;
            }
            else{
                move = 4;
            }
            if((blueSlides.contains(location) && color == Color.LIMEGREEN) ||
                    (greenSlides.contains(location) && color == Color.BLUE)){
                for (int i = 0; i < move; i++) {
                    if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("bump")) {
                        bump(p.getLocation() + 1, color);
                    }
                    if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("true")) {
                        bump(p.getLocation() + 1, color2);
                    }
                    movePawn(p);

                }
            }
        }

    }

    /*
    Checks for sorry moves. Returns true if there is a move, False if no move
     */
    public boolean checkSorryMoves() {
        int unmoveable = 0;
        for (int i = 0; i < 4; i++) {
            if (greenHome.getChildren().contains(greenPawns[i].getCircle())) {
                unmoveable++;
            }
            else if(greenPawns[i].inSafeZone()){
                unmoveable++;
            }
            // ADD IF LIMEGREEN IN SAFE ZONE
        }
        if (blueHome.getChildren().isEmpty()) {
            unmoveable = 4;
        }
        return unmoveable < 4;
    }

    /*
    Checks for swaps.  Returns true if there is a move, false if no move
     */
    public boolean checkSwaps(){
        int unmoveable = 0;
        int unmoveable1 = 0;
        for (int i = 0; i < 4; i++) {
            if (greenHome.getChildren().contains(greenPawns[i].getCircle())) {
                unmoveable++;
            }
            else if(greenPawns[i].inSafeZone()){
                unmoveable++;
            }// ADD IF LIMEGREEN IN SAFE ZONE
            if (blueHome.getChildren().contains(bluePawns[i].getCircle())) {
                unmoveable1++;
            }
            else if(bluePawns[i].inSafeZone()){
                unmoveable1++;
            }
        }

        if(unmoveable==4){
            return false;
        }
        else if(unmoveable1 == 4){
            return false;
        }
        return true;
    }

    /*
    Handle Sorry! Card
     */
    public void handleSorry(Pawn p1, Pawn p2) {
        if (p1.getCircle().getFill() == Color.BLUE) {
            int location = p2.getLocation();
            tiles.get(location).getChildren().add(p1.getCircle());
            p1.setLocation(location);
            bump(location, Color.BLUE);
        }
    }

    /*
    Swap Pawns
     */
    public void handleSwap(Pawn p1, Pawn p2){
        if (p1.getCircle().getFill() == Color.BLUE) {
            int location1 = p2.getLocation();
            int location2 = p1.getLocation();
            tiles.get(location1).getChildren().add(p1.getCircle());
            p1.setLocation(location1);
            tiles.get(location2).getChildren().add(p2.getCircle());
            p2.setLocation(location2);
            //bump(location, Color.BLUE);
            //nice.gotBumped(gameBoard,location);
        }
    }

    public void newWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player.fxml"));
            //Parent root = fxmlLoader.load();
            Stage playerStage = new Stage();
            playerStage.setTitle("Pick your move");
            playerStage.initStyle(StageStyle.UNDECORATED);
            playerStage.setScene(new Scene((Pane) fxmlLoader.load()));
            playerStage.show();
            ControllerPopup controller =
                    fxmlLoader.<ControllerPopup>getController();
            this.controller = controller;
            controller.setController(this);
            if(currentCard.getMoves() == 10){
            controller.handleTen();
            }
            else if(currentCard.getMoves() == 11){
            controller.handleEleven();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void helpClicked(){
        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab1 = new Tab("Page 1");
        Tab tab2 = new Tab("Page 2");
        root.getTabs().addAll(tab1,tab2);
        Stage helpStage = new Stage();
        helpStage.setTitle("HELP");
        helpStage.setScene(new Scene(root, 500,640));
        helpStage.show();
        File file = new File("rules1.jpg");
        Image rules1 = new Image(file.toURI().toString());
        File file1 = new File("rules2.jpg");
        Image rules2 = new Image(file1.toURI().toString());
        ImageView showRules1 = new ImageView();
        ImageView showRules2 = new ImageView();
        showRules1.setImage(rules1);
        tab1.setContent(showRules1);
        showRules2.setImage(rules2);
        tab2.setContent(showRules2);

    }

    /*
    Adds pawns home
     */
    public void addPawnsToHome(int index, Color c){
        index -= 1;
        int xLocation = 0;
        int yLocation = 0;
        switch (index) {
            case 1:
                xLocation = 20;
                break;
            case 2:
                xLocation = 0;
                yLocation = 20;
                break;
            case 3:
                xLocation = 20;
                yLocation = 20;

        }
        if(c == Color.BLUE) {
            Circle circle = new Circle(10);
            circle.setFill(c);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1.5);
            blueHomeCircle.getChildren().add(circle);
            circle.relocate(xLocation,yLocation);
        }
        else if(c == Color.LIMEGREEN){
            Circle circle = new Circle(10);
            circle.setFill(c);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1.5);
            greenHomeCircle.getChildren().add(circle);
            circle.relocate(xLocation,yLocation);
        }

    }

    /*
    Ends Game
     */
    public void endGame(String winner){
        disablePawns(bluePawns);
        playerOneTurn = false;
        playerTurn.setText(winner+ " WINS!");
        playerTurn.setFont(Font.font ("Verdana", 14));
    }
}







