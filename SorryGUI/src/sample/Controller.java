package sample;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class Controller implements Initializable,EventHandler<MouseEvent>{

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox topPane,bottomPane;

    @FXML
    private VBox rightPane,leftPane;

    @FXML
    private Pane discardCard, blueHome,yellowHome,greenHome,redHome;

    @FXML
    private Button pickCard;


    private ArrayList<StackPane> tiles;
    private Pawn[] bluePawns;
    private Pawn[] yellowPawns;
    private Pawn[] redPawns;
    private Pawn[] greenPawns;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
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

        //Spawn all pawns in Home base
        this.bluePawns = spawnPawns(blueHome,Color.BLUE);
        this.yellowPawns = spawnPawns(yellowHome,Color.YELLOW);
        this.greenPawns = spawnPawns(greenHome,Color.GREEN);
        this.redPawns = spawnPawns(redHome,Color.RED);

        for(int i=0;i<5;i++) {
            bluePawns[i].getCircle().setOnMouseClicked(this);
            yellowPawns[i].getCircle().setOnMouseClicked(this);
            greenPawns[i].getCircle().setOnMouseClicked(this);
            redPawns[i].getCircle().setOnMouseClicked(this);
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
        Pawn[] pawns = new Pawn[5];
        for(int i=0;i<5;i++){
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
        //getCard() from Deck and then display that card
        File file = new File("frontCard.png");
        Image card = new Image(file.toURI().toString());
        ImageView showCard = new ImageView();
        showCard.setImage(card);
        discardCard.getChildren().add(showCard);
    }


    @Override
    public void handle(MouseEvent event) {
            if (blueHome.getChildren().contains(event.getSource())) {
                Circle c = (Circle) event.getSource();
                movePawnFromStart(bluePawns[Integer.parseInt(c.getId())],Color.BLUE);
            }

            if (yellowHome.getChildren().contains(event.getSource())) {
                Circle c = (Circle) event.getSource();
                movePawnFromStart(yellowPawns[Integer.parseInt(c.getId())],Color.YELLOW);
            }

            if (redHome.getChildren().contains(event.getSource())){
                Circle c = (Circle) event.getSource();
                movePawnFromStart(redPawns[Integer.parseInt(c.getId())],Color.RED);
            }

            if (greenHome.getChildren().contains(event.getSource())){
                Circle c = (Circle) event.getSource();
                movePawnFromStart(greenPawns[Integer.parseInt(c.getId())],Color.GREEN);
            }

            else{
                Circle c = (Circle) event.getSource();
                Paint color = c.getFill();
                if(color == Color.BLUE){
                   movePawn(bluePawns[Integer.parseInt(c.getId())]);
                }
                if(color == Color.RED){
                    movePawn(redPawns[Integer.parseInt(c.getId())]);
                }
                if(color == Color.YELLOW){
                    movePawn(yellowPawns[Integer.parseInt(c.getId())]);
                }
                if(color == Color.GREEN){
                    movePawn(greenPawns[Integer.parseInt(c.getId())]);
                }
            }
    }

    /*
    Moves the pawn from home base out 1 position
     */
    public void movePawnFromStart(Pawn p, Color color){
        if(color == Color.BLUE){
            p.setLocation(33);
        }
        else if(color == Color.YELLOW){
            p.setLocation(48);
        }
        else if(color == Color.GREEN){
           p.setLocation(4);
        }
        else if(color == Color.RED){
            p.setLocation(18);
        }
        tiles.get(p.getLocation()).getChildren().add(p.getCircle());
        p.setLocation(p.getLocation());
    }

    /*
    Moves pawn 1 space forward
     */
    public void movePawn(Pawn p){
        if(p.getLocation() == 59){
            tiles.get(0).getChildren().add(p.getCircle());
            p.setLocation(0);
        }

        else{
            if(p.getLocation() == 0){
            slide(p);
            }
            else{
                tiles.get(p.getLocation() +1).getChildren().add(p.getCircle());
                p.setLocation(p.getLocation()+1);
            }
        }
    }

    /*
    If pawns lands on slide, slides to end
     */
    public void slide(Pawn p){
        tiles.get(p.getLocation() +4).getChildren().add(p.getCircle());
        p.setLocation(p.getLocation()+4);
    }

}

