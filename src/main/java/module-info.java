module isep.jfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens isep.jfx to javafx.fxml;
    exports isep.jfx;
}