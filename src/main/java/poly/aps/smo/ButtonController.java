package poly.aps.smo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ButtonController {
    @FXML
    private Button buttonApply;
    @FXML
    private TextField sourceText;
    @FXML
    private TextField deviceText;
    @FXML
    private TextField requestText;
    @FXML
    private TextField buffersizeText;
    @FXML
    private TextField alphaText;
    @FXML
    private TextField betaText;
    @FXML
    private TextField lambdaText;

    @FXML
    public void applySettings(ActionEvent actionEvent) {
        int sourceCount = Integer.parseInt(sourceText.getText());
        int deviceCount = Integer.parseInt(deviceText.getText());
        int requestCount = Integer.parseInt(requestText.getText());
        int bufferSize = Integer.parseInt(buffersizeText.getText());
        int alpha = Integer.parseInt(alphaText.getText());
        int beta = Integer.parseInt(betaText.getText());
        int lambda = Integer.parseInt(lambdaText.getText());

        Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
        try {
            controller.executeAuto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
