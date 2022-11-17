package poly.aps.smo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;



public class MainWindow extends Application {
    @FXML
    private Button buttonApply;

    @FXML
    public void applySettings(ActionEvent actionEvent) {
        buttonApply.setText("Thanks!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/poly/aps/smo/hello-view.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

//        TextField textField = (TextField) root.lookup("sourceText");
//        int sourceCount = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#deviceText");
//        int deviceCount = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#requestText");
//        int requestCount = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#buffersizeText");
//        int bufferSize = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#alphaText");
//        int alpha = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#betaText");
//        int beta = Integer.parseInt(textField.getText());
//        textField = (TextField) root.lookup("#lambdaText");
//        int lambda = Integer.parseInt(textField.getText());
//        Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
//        controller.executeAuto();
    }

    public static void main(String[] args) {
        launch();
    }
}