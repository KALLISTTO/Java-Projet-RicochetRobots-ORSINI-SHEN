package isep.ricrob;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static isep.ricrob.Game.Status.*;
import static isep.ricrob.Token.Color.*;


public class Game {


    // * Instance globale de gestion du jeu

    public static Game context;

    public static void start() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Impossible de lancer plusieurs fois la partie...");
        }
        Game.context = new Game();
        Game.context.setStatus(CHOOSE_PLAYER);
    }

    // * ---

    // Taille du plateau (SIZE x SIZE)
    public static final int SIZE = 16;

    // Constructeur privé (instance unique créée par "start()" )
    private Game() {

        board = new Tile[SIZE][SIZE];

        robots = new HashMap<>();
        robots.put(RED, new Token(RED));
        robots.put(GREEN, new Token(GREEN));
        robots.put(BLUE, new Token(BLUE));
        robots.put(YELLOW, new Token(YELLOW));

        Token.Color[] colors = Token.Color.values();
        int randomColorIndex = ( new Random() ).nextInt( colors.length );
        target = new Token( colors[randomColorIndex] );

    }


    public void processSelectRobot(Token.Color color) {
        if (this.status == CHOOSE_ROBOT) {
            this.selectedRobot = this.robots.get(color);
            // Action suivante attendue : choisir la case cible
            setStatus(CHOOSE_TILE);
        }
    }

    public int end = 0;
    public String processSelectTile(int col, int lig) {
        if (this.status == CHOOSE_TILE) {
            if (
                    (this.selectedRobot.getCol() != col)
                &&
                    (this.selectedRobot.getLig() != lig)
            ) {
                return "Les déplacements en diagonale sont interdits";
            } else {
                this.selectedRobot.setPosition(col,lig);
                if (this.selectedRobot.getCol() == target.getCol() && this.selectedRobot.getLig() == target.getLig() && this.selectedRobot.getColor().equals(target.getColor())){
                    end = 1;
                }
                setStatus(CHOOSE_ROBOT);
                return "MOVE";

                // Action suivante attendue : choisir un robot
            }
        }
        return null;
    }

    // * ---

    // * Etat courant du jeu

    public enum Status {
        END_GAME("Tour terminé"),
        CHOOSE_PLAYER("Cliquez sur le bouton valider"),
        CHOOSE_ROBOT("Cliquez sur le robot à déplacer"),
        CHOOSE_TILE("Cliquez sur la case destination");
        Status(String toolTip) { this.toolTip = toolTip; }
        private String toolTip;
        public String getToolTip() { return this.toolTip; }
    }
    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        this.status = status;
        // Mise à jour du libellé d'état sur l'affichage
        StringBuilder statusMessage = new StringBuilder();
        statusMessage.append( status.getToolTip() );
        this.statusToolTipProperty.set( statusMessage.toString() );
    }
    private Status status;
    // "Binding JFX" - Synchronisation avec "MainController.statusLabel"
    public StringProperty statusToolTipProperty = new SimpleStringProperty();

    private Token selectedRobot;
    public Token getSelectedRobot() { return this.selectedRobot; }

    // * ---

    // Le plateau de SIZE x SIZE cases
    private Tile[][] board;

    // Les 4 robots
    private Map<Token.Color, Token> robots;
    public Map<Token.Color, Token> getRobots() { return this.robots; }

    // La cible
    private Token target;
    public Token getTarget() { return this.target; }

}
