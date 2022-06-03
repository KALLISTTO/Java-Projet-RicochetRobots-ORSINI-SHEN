package isep.jfx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import java.awt.Desktop;

public class MenuController {

    Integer playerNumber[] = {1, 2};
    @FXML
    ChoiceBox playerNumberChoice = new ChoiceBox(FXCollections.observableArrayList(playerNumber));
    @FXML
    Button jouer = new Button();
    @FXML
    Hyperlink regles = new Hyperlink("Règles");
    @FXML
    TextField name1 = new TextField();
    @FXML
    TextField name2 = new TextField();

    static public  List<String> playerNames = new ArrayList<String>();

    public void initialize(){
        name1.setVisible(false);
        name2.setVisible(false);
        jouer.setDisable(true);
    }

    @FXML
    protected void Html() throws URISyntaxException, IOException {
        File htmlFile = new File("src/main/resources/rules/index.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void onStartButtonClick() throws IOException {

        if ( (int) playerNumberChoice.getValue() == 1) {
            playerNames.add((String) name1.getText());
        } else if ( (int) playerNumberChoice.getValue() == 2) {
            playerNames.add((String) name1.getText());
            playerNames.add((String) name2.getText());
        }

        // Affiche la fenêtre principale du jeu
        FXMLLoader fxmlLoader = new FXMLLoader
                (MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainApplication.stage.setScene(scene);
        MainApplication.stage.show();
    }

    @FXML
    protected void howManyPlayers() throws IOException {
        jouer.setDisable(true);
        if ((int) playerNumberChoice.getValue() == 1) {
            name1.setVisible(true);
            name2.setVisible(false);

        } else if ((int) playerNumberChoice.getValue() == 2) {
            name1.setVisible(true);
            name2.setVisible(true);
            name2.setDisable(true);
        }
    }

    @FXML
    protected void tf1() throws IOException {
        name2.setDisable(false);
        if ( (int) playerNumberChoice.getValue() == 1) {
            jouer.setDisable(false);
        }
    }

    @FXML
    protected void tf2() throws IOException {
        if ( (int) playerNumberChoice.getValue() == 2) {
            jouer.setDisable(false);
        }
    }


}