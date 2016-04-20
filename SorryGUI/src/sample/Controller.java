package sample;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class Controller implements Initializable,EventHandler<MouseEvent> {

    @FXML
    public BorderPane borderPane;

    @FXML
    public HBox topPane, bottomPane;

    @FXML
    public VBox rightPane, leftPane, blueSafe;

    @FXML
    public Pane discardCard, blueHome, yellowHome, greenHome, redHome;

    @FXML
    public Button pickCard;

    @FXML
    public TextField playerName;

    @FXML
    public Text textOutput, playerTurn;

    public Deck deck;
    public ArrayList<StackPane> tiles;
    public ArrayList<StackPane> blueSafeZone;
    public Pawn[] bluePawns;
    public Pawn[] yellowPawns;
    public Pawn[] redPawns;
    public Pawn[] greenPawns;
    public Card currentCard;
    public int startGameClicks;
    boolean playerOneTurn = true;
    boolean playerTwoTurn = false;
    String nameOfPlayer;
    int occupied; //The space where the pawn will move to
    int sorryClicks = 0; //How many times sorry function has been called
    Pawn sorryPawn1;
    Pawn sorryPawn2;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        deck = new Deck();
        //noinspection unchecked
        tiles = new ArrayList(64);//Create a list that holds all stack panes for top row
        //noinspection unchecked
        blueSafeZone = new ArrayList(6);
        //noinspection unchecked
        @SuppressWarnings("unchecked") ArrayList<StackPane> leftTiles = new ArrayList(16);
        //noinspection unchecked
        @SuppressWarnings("unchecked") ArrayList<StackPane> bottomTiles = new ArrayList(16);

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
                this.greenPawns = spawnPawns(greenHome, Color.GREEN);
                this.redPawns = spawnPawns(redHome, Color.RED);


                for (int i = 0; i < 4; i++) {
                    bluePawns[i].getCircle().setOnMouseClicked(this);
                    greenPawns[i].getCircle().setOnMouseClicked(this);
                }
                playerName.setDisable(true);
                nameOfPlayer = playerName.getText();
                tiles.get(30).getChildren().add(greenPawns[0].getCircle());
                greenPawns[0].setLocation(30);
                playGame();
                newWindow();
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
            if (currentCard.getMoves() == 13) {
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
            } else if (sorryClicks == 1 && c.getFill() == Color.GREEN) {
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
                computerTurn();
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
                    if (checkIfOccupied(blueSafeZone.get(position), b).equals("true")) {
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
        } else if (color == Color.GREEN) {
            p.setLocation(4);
            disableUsersPawns(greenPawns, p);
            tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        }

    }

    /*
    Moves pawn 1 space forward
     */
    public void movePawn(Pawn p) {

        if (p.getLocation() == 59) {
            tiles.get(0).getChildren().add(p.getCircle());
            p.setLocation(0);

        } else if (p.getLocation() == 32 && p.getCircle().getFill() == Color.BLUE && p.inSafeZone() == false) {
            blueSafeZone.get(0).getChildren().add(p.getCircle());
            p.setSafeZoneLocation(0);
            p.setInSafeZone(true);
        } else if (p.inSafeZone()) {
            blueSafeZone.get(p.getSafeZoneLocation() + 1).getChildren().add(p.getCircle());
            p.setSafeZoneLocation(p.getSafeZoneLocation() + 1);

        } else {
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
            if (p.getSafeZoneLocation() == 0) {
                tiles.get(32).getChildren().add(p.getCircle());
                p.setLocation(32);
                p.setInSafeZone(false);
            } else {
                blueSafeZone.get(p.getSafeZoneLocation() - 1).getChildren().add(p.getCircle());
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
        Timeline animate = new Timeline(new KeyFrame(Duration.seconds(.5), new EventHandler<ActionEvent>() {

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
                slide(p);
                if (bump) {
                    bump(occupied, Color.BLUE);
                }
                computerTurn();
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
        playerOneTurn = true;
        playerTwoTurn = true;
        newCard();
        playerTwoTurn = false;
        if (currentCard.getMoves() <= 2) {
            movePawnFromStart(greenPawns[1], Color.GREEN);
        }
        playerOneTurn = true;
        disablePawns(bluePawns);
        disablePawns(greenPawns);

        //Animate Computer pawn, when animation done, print that it is users turn. If no moves, print No moves

        //Pause 2 Seconds
        Timeline waitTwo = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card. Your pawn color is blue");
            }
        }));
        waitTwo.setCycleCount(1);
        waitTwo.play();
    }

    /*
    Check's to see if player has any moves.  Returns true if has moves, false if no moves
     */
    public boolean checkForMoves() {
        //If card is not 1 or 2 and no pawns are outside, then no moves exist
        int unmoveable = 0;
        //Check each pawn
        for (int i = 0; i < 4; i++) {
            if (!checkMoves(bluePawns[i])) {
                unmoveable++;
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

        //game Logic here
        playerTurn.setText(nameOfPlayer + "'s Turn. Click deck to get card. Your pawn color is blue");
        disablePawns(bluePawns);

    }

    /*
    Check all available moves. Returns true if moves, false if no moves
     */
    public boolean checkMoves(Pawn b) {
        boolean move = true;
        boolean backwards = false;
        if (currentCard.getMoves() == 13 && !checkSorryMoves()) {
            move = false;
        }
        //Check if can move from home
        else if (currentCard.getMoves() != 13
                && blueHome.getChildren().contains(b.getCircle()) && currentCard.getMoves() > 2) {
            move = false;
        } else if (blueHome.getChildren().contains(b.getCircle())
                && currentCard.getMoves() <= 2) {
            //check if a blue pawn is in the way
            if (checkIfOccupied(tiles.get(34), b).equals("true")) {
                move = false;
            }
        }
        //Check if can move in home
        else if (b.getLocation() < 33 && b.getLocation() + currentCard.getMoves() > 38) {
            move = false;
        }
        //Check to see if user can move into home from safe zone
        else if (b.inSafeZone() &&
                b.getSafeZoneLocation() + currentCard.getMoves() > 5 && currentCard.getMoves() != 4) {
            move = false;
        }
        //Check if own pawn is in way in safe zone
        else if (b.inSafeZone() && b.getSafeZoneLocation() + currentCard.getMoves() <= 5) {
            int position = b.getSafeZoneLocation() + currentCard.getMoves();

            //If pawn is in space you're trying to move to
            if (checkIfOccupied(blueSafeZone.get(position), b).equals("true")
                    && b.getSafeZoneLocation() + currentCard.getMoves() != 5) {
                move = false;
            }
        }
        //Check for regular moves around board
        else if (!blueHome.getChildren().contains(b.getCircle())) {
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
            if (!backwards && b.getLocation() <= 32 && occupied > 32) {
                int position = occupied - 33;
                if (checkIfOccupied(blueSafeZone.get(position), b).equals("true")) {
                    move = false;
                }
            } else if (checkIfOccupied(tiles.get(occupied), b).equals("true")) {
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
        } else if (color == Color.GREEN) {
            blueHome.getChildren().add(bluePawns[id].getCircle());
            bluePawns[id].getCircle().relocate(xLocation, yLocation);
        }
    }

    /*
    Slide pawn
     */
    public void slide(Pawn p) {
        if (p.getLocation() == 1 || p.getLocation() == 16 || p.getLocation() == 46) {
            for (int i = 0; i < 3; i++) {
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("bump")) {
                    bump(p.getLocation() + 1, Color.BLUE);
                }
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("true")) {
                    bump(p.getLocation() + 1, Color.GREEN);
                }
                movePawn(p);

            }
        }

        if (p.getLocation() == 9 || p.getLocation() == 24 || p.getLocation() == 54) {
            for (int i = 0; i < 4; i++) {
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("bump")) {
                    bump(p.getLocation() + 1, Color.BLUE);
                }
                if (checkIfOccupied(tiles.get(p.getLocation() + 1), p).equals("true")) {
                    bump(p.getLocation() + 1, Color.GREEN);
                }
                movePawn(p);
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
            // ADD IF GREEN IN SAFE ZONE
        }
        if (blueHome.getChildren().isEmpty()) {
            unmoveable = 4;
        }
        return unmoveable < 4;
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

    public void newWindow() {
        try {
            Group root = new Group();
            Stage playerStage = new Stage();
            playerStage.setTitle("Player");
            playerStage.setScene(new Scene(root, 200, 50));
            playerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}







