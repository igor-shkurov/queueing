package poly.aps.smo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TableView<Object> tableView;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Новая заявка добавлена!");
    }
}