package poly.aps.smo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainWindow extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        URL xmlUrl = getClass().getResource("/poly/aps/smo/hello-view.fxml");
//        loader.setLocation(xmlUrl);
//        Parent root = loader.load();
//        stage.setScene(new Scene(root));
//        stage.show();

        Controller controller = new Controller(4, 2, 1, 10, 2000);
        controller.executeAuto();
    }

    public static void main(String[] args) {
        launch();
    }
}