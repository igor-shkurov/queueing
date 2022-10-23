package poly.aps.smo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(640);
        stage.setHeight(480);
        stage.setResizable(false);

        final Label label = new Label("Список заявок");
        label.setFont(new Font("Arial", 20));

        final TableView table = new TableView();

        TableColumn<Void, Void> numberCol = new TableColumn<>("Номер заявки");
        TableColumn<Void, Void> deviceCol = new TableColumn<>("Устройство");
        TableColumn<Void, Void> sourceCol = new TableColumn<>("Источник");

        numberCol.setResizable(false);
        deviceCol.setResizable(false);
        sourceCol.setResizable(false);

        numberCol.prefWidthProperty().bind(table.widthProperty().multiply(1 / 3.0));
        deviceCol.prefWidthProperty().bind(table.widthProperty().multiply(1 / 3.0));
        sourceCol.prefWidthProperty().bind(table.widthProperty().multiply(1 / 3.0));

        table.getColumns().addAll(numberCol, sourceCol, deviceCol);

        final VBox vbox1 = new VBox(10);
        vbox1.prefWidthProperty().bind(scene.widthProperty().multiply(0.99));
        vbox1.prefHeightProperty().bind(scene.heightProperty().multiply(0.3));
        vbox1.setSpacing(5);
        vbox1.setPadding(new Insets(10, 0, 0, 10));
        vbox1.getChildren().addAll(label, table);

        Button buttonAdd = new Button();
        buttonAdd.setText("Добавить заявку");

        buttonAdd.setAlignment(Pos.BOTTOM_RIGHT);



        ((Group) scene.getRoot()).getChildren().addAll(vbox1, buttonAdd);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}