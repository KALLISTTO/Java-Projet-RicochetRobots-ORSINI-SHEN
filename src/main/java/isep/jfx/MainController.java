package isep.jfx;

import isep.ricrob.Game;
import isep.ricrob.Token;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.util.Duration;

import java.util.*;

import static isep.ricrob.Game.Status.*;
import static isep.ricrob.Token.Color.*;

public class MainController {

    public final int TILE_SIZE = 40;

    @FXML
    public GridPane boardPane;

    @FXML
    public Label statusLabel;
    @FXML
    ChoiceBox playerTurn = new ChoiceBox();
    @FXML
    TextField predictionTf = new TextField();
    @FXML
    Button confirmPredictionButton = new Button();
    @FXML
    Label printScore = new Label();
    @FXML
    Label printCount = new Label();
    @FXML
    Button restartButton = new Button();

    String orientationList[] = {"NE", "NW", "SE", "SW"};
    int randomOrientationIndex = ( new Random() ).nextInt( orientationList.length );

    Image tile = new Image("cell.png", TILE_SIZE, TILE_SIZE, false, true);
    Image wall = new Image("wall.png", TILE_SIZE, TILE_SIZE,false,false);
    Image blackWall = new Image("blackCell.png", TILE_SIZE, TILE_SIZE,false,false);

    // "initialize()" est appelé par JavaFX à l'affichage de la fenêtre
    @FXML
    public void initialize() {

        Game.start();

        setButtonDisable();


        for (String name: MenuController.playerNames) {
            playerTurn.getItems().add(name);

        }
        // Construction du plateau

        // ... "cell.png" doit être placé à la racine de "resources/" (sinon PB)
        for (int col = 0; col < Game.SIZE; col ++) {
            for (int lig = 0; lig < Game.SIZE; lig++) {
                ImageView tileGui = new ImageView(tile);
                final int lambdaCol = col;
                final int lambdaLig = lig;
                tileGui.setOnMouseClicked
                        (event -> {
                            String status = Game.context.processSelectTile
                                    (lambdaCol, lambdaLig);
                            if ( "MOVE".equals(status)) {
                                updateSelectedRobotPosition();
                            } else if (status != null) {
                                showWarning(status);
                            }
                        });
                boardPane.add(tileGui, col, lig);
                if (col == 7) {
                    for (int i=7; i<9; i++) {
                        ImageView wallGui = new ImageView(wall);
                        ImageView blackWallGui = new ImageView(blackWall);
                        boardPane.add(blackWallGui,col,i);
                        boardPane.add(wallGui, col, i);
                    }
                }
                if (col == 8) {
                    for (int i=7; i<9; i++) {
                        ImageView wallGui = new ImageView(wall);
                        ImageView blackWallGui = new ImageView(blackWall);
                        wallGui.setRotate(180);
                        boardPane.add(blackWallGui,col,i);
                        boardPane.add(wallGui, col, i);

                    }
                }
                if (lig == 7) {
                    for (int i=7; i<9; i++) {
                        ImageView wallGui = new ImageView(wall);
                        wallGui.setRotate(90);
                        boardPane.add(wallGui, i, lig);
                    }
                }
                if (lig == 8) {
                    for (int i=7; i<9; i++) {
                        ImageView wallGui = new ImageView(wall);
                        wallGui.setRotate(270);
                        boardPane.add(wallGui, i, lig);
                    }
                }
            }
        }

        addWall((int) Math.floor(Math.random()*(5-3+1)+3),0,"E");
        addWall(10,0,"E");
        addWall((int) Math.floor(Math.random()*(5-1+1)+1),1,orientationList[new Random().nextInt( orientationList.length )]);
        addWall(10,1,"SW");
        addWall(14,2,"NE");
        addWall(1,3,"SW");
        addWall(0,4,"S");
        addWall(6,4,"NW");
        addWall(10,4,"SE");
        addWall(15,4,"S");
        addWall(5,6,"NE");
        addWall(12,6,"NW");

        addWall(3,7,"SE");
        addWall(3,9,"SE");
        addWall(14,9,"NW");
        addWall(0,10,"S");
        addWall(11,10,"SW");
        addWall(15,10,"S");
        addWall(5,11,"NW");
        addWall(9,12,"NE");
        addWall(2,13,"SW");
        addWall(6,14,"NE");
        addWall(13,14,"SE");
        addWall(4,15,"E");
        addWall(10,15,"E");



        // Ajout des pièces
        addRobot(RED);
        addRobot(GREEN);
        addRobot(BLUE);
        addRobot(YELLOW);

        boardPane.add(
                new ImageView( new Image(
                        Game.context.getTarget().getColor() + "_target.png",
                        TILE_SIZE, TILE_SIZE, false, true
                ) ),
                Game.context.getTarget().getCol(),
                Game.context.getTarget().getLig()
        );

        // "Binding JFX" - Synchronisation du "Label" avec l'état du jeu
        statusLabel.textProperty().bind(Game.context.statusToolTipProperty);

    }

    private void setButtonDisable() {
        predictionTf.setDisable(true);
        confirmPredictionButton.setDisable(true);
    }

    private void addWall(int col, int lig, String orientation){
        if (orientation.equals("N")){
            ImageView wallGui = new ImageView(wall);
            wallGui.setRotate(90);
            boardPane.add(wallGui, col, lig);
        } else if (orientation.equals("W")){
            ImageView wallGui = new ImageView(wall);
            boardPane.add(wallGui, col, lig);
        } else if (orientation.equals("E")){
            ImageView wallGui = new ImageView(wall);
            wallGui.setRotate(180);
            boardPane.add(wallGui, col, lig);
        } else if (orientation.equals("S")){
            ImageView wallGui = new ImageView(wall);
            wallGui.setRotate(270);
            boardPane.add(wallGui, col, lig);
        } else if (orientation.equals("NE")) {
            ImageView wallGui = new ImageView(wall);
            wallGui.setRotate(90);
            boardPane.add(wallGui, col, lig);
            ImageView wallGui2 = new ImageView(wall);
            wallGui2.setRotate(180);
            boardPane.add(wallGui2, col, lig);
        }else if (orientation.equals("NW")) {
            ImageView wallGui = new ImageView(wall);
            boardPane.add(wallGui, col, lig);
            ImageView wallGui2 = new ImageView(wall);
            wallGui2.setRotate(90);
            boardPane.add(wallGui2, col, lig);
        }else if (orientation.equals("SE")) {
            ImageView wallGui = new ImageView(wall);
            wallGui.setRotate(270);
            boardPane.add(wallGui, col, lig);
            ImageView wallGui2 = new ImageView(wall);
            wallGui2.setRotate(180);
            boardPane.add(wallGui2, col, lig);
        }else if (orientation.equals("SW")) {
            ImageView wallGui = new ImageView(wall);
            boardPane.add(wallGui, col, lig);
            ImageView wallGui2 = new ImageView(wall);
            wallGui2.setRotate(270);
            boardPane.add(wallGui2, col, lig);
        }
    }
    private void addRobot(Token.Color color) {
        Token robot = Game.context.getRobots().get(color);
        ImageView robotGui = new ImageView( new Image(
                color.name() + "_robot.png",
                TILE_SIZE, TILE_SIZE, false, true
        ) );
        robotGui.setOnMouseClicked
                (event -> Game.context.processSelectRobot(color));
        boardPane.add(robotGui, robot.getCol(), robot.getLig());
        // Association de l' "ImageView" avec le robot stocké dans le jeu
        robot.setGui(robotGui);
    }
    public int countNumber;
    private void updateSelectedRobotPosition() {
        countNumber++;
        Token robot = Game.context.getSelectedRobot();
        GridPane.setConstraints(robot.getGui(), robot.getCol(), robot.getLig());
        printCount.setText(String.format("Nombre de coup actuel: %s",String.valueOf(countNumber)));
        if (Game.context.end == 1) {
            Game.context.setStatus(END_GAME);
        }
    }

    Dictionary<Object, String> dicPlayerPredictions = new Hashtable<Object, String>();
    int firstPlayer = 0;

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
        setButtonDisable();
        Game.context.setStatus(CHOOSE_ROBOT);
    }));

    private boolean isInt(TextField txt) {
        try
        {
            Integer.parseInt(txt.getText());
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    @FXML
    protected void confirmPrediction() {
        if (isInt(predictionTf)){
            dicPlayerPredictions.put(playerTurn.getValue(), predictionTf.getText());
            if (firstPlayer == 0){
                timeline.setCycleCount(1);
                timeline.play();
            }
            printScore.setText(String.format("Prévision des joueurs:\n%s",String.valueOf((dicPlayerPredictions))));
            firstPlayer = 1;
        } else {
            printScore.setText("Veuiller saisir un nombre !");
        }
    }

    @FXML
    protected void setPlayerTurn(){
        predictionTf.setDisable(false);
        confirmPredictionButton.setDisable(false);
    }

    @FXML
    protected void restart(){
        playerTurn.getItems().clear();
        Game.context = null;
        initialize();
        printCount.setText("");
        countNumber = 0;
        firstPlayer = 0;
    }
    private void showWarning(String message) {
        Alert startMessage
                = new Alert(Alert.AlertType.INFORMATION, message);
        startMessage.setHeaderText(null);
        startMessage.setGraphic(null);
        startMessage.showAndWait();
    }
}
