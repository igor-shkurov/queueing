module poly.aps.smo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens poly.aps.qs to javafx.fxml;
    exports poly.aps.qs;
}