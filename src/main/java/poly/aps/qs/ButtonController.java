package poly.aps.qs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.TreeSet;

public class ButtonController {
    @FXML
    LineChart<Number, Number> sourceCancel, sourceAverage, sourceBusiness, deviceCancel, deviceAverage,
                                deviceBusiness, bufferCancel, bufferAverage, bufferBusiness;
    @FXML
    private TextField sourceText, deviceText, requestText, buffersizeText, alphaText, betaText, lambdaText;
    @FXML
    private Label timeField, tasksCompleted;
    @FXML
    public Label cancelNum;
    @FXML
    private Button startStepping, doStep, btnAuto, buttonApply, buttonTable, buttonPlotting;
    @FXML
    private Label s1, s2, s3, s4, s5, s6, s7, s8, s9, s10;
    @FXML
    private Rectangle rs1, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rs9, rs10;
    @FXML
    private Label d1, d2, d3, d4, d5, d6, d7, d8, d9, d10;
    @FXML
    private Rectangle rd1, rd2, rd3, rd4, rd5, rd6, rd7, rd8, rd9, rd10;
    @FXML
    private Label b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
    @FXML
    private Rectangle br1, br2, br3, br4, br5, br6, br7, br8, br9, br10;
    private int sourceCount, deviceCount, requestCount, bufferSize, alpha, beta;
    private double lambda;

    @FXML
    public void applySettings(ActionEvent actionEvent) {
        buttonTable.setDisable(false);
        buttonPlotting.setDisable(false);
        buttonApply.setText("Reapply");
        sourceCount = Integer.parseInt(sourceText.getText());
        deviceCount = Integer.parseInt(deviceText.getText());
        requestCount = Integer.parseInt(requestText.getText());
        bufferSize = Integer.parseInt(buffersizeText.getText());
        alpha = Integer.parseInt(alphaText.getText());
        beta = Integer.parseInt(betaText.getText());
        lambda = Double.parseDouble(lambdaText.getText());
        startStepping.setDisable(false);
    }

    @FXML
    public void doStep(ActionEvent actionEvent) {
        Label[] sourceLabels = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10};
        Label[] deviceLabels = {d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
        Label[] bufferLabels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};
        Controller controller = Controller.instance;
        StatController statController = StatController.instance;

        SpecialEvent event = controller.getFirstEvent();

        TreeSet<SpecialEvent> events = controller.goToNextState();
        if (events.isEmpty()) {
            doStep.setDisable(true);
        }
        ArrayList<Device> devices = controller.getDevices();
        Buffer buffer = controller.getBuffer();

        for (Label x: sourceLabels) {
            x.setVisible(false);
        }

        Label bufferLabel;
        Label deviceLabel;

        for (Device d: devices) {
            if (d.isBusy()) {
                deviceLabel = deviceLabels[d.getDeviceNumber()];
                deviceLabel.setVisible(true);
                deviceLabel.setText("Occupied");
            }
        }

        for (int i = 0; i < buffer.getCapacity(); i++) {
            if (buffer.posBusy(i)) {
                bufferLabels[i].setVisible(true);
                bufferLabels[i].setText("Occupied");
            }
            else {
                bufferLabels[i].setVisible(false);
            }
        }

        timeField.setText(String.valueOf(controller.getCurrentTime()));
        tasksCompleted.setText(statController.getTotalTasksProcessed() + "/" + controller.getTotalTasksRequired());
        switch (event.getEventTypeOrdinal()) {
            case 0 -> {
                Label sourceLabel = sourceLabels[event.getAssignedDevice()];
                sourceLabel.setText("Generated a request");
                sourceLabel.setVisible(true);
            }
            case 1 -> {
                bufferLabel = bufferLabels[(int) buffer.getCancelPosition()];
                if (buffer.isFlagCancel()) {
                    bufferLabel.setText("Rejected");
                    bufferLabel.setVisible(true);
                }
            }
            case 2 -> {
                deviceLabel = deviceLabels[event.getAssignedDevice()];
                deviceLabel.setVisible(true);
                deviceLabel.setText("Freed up");
            }
        }
    }

    @FXML
    public void startStepping(ActionEvent actionEvent) { // @todo: REMOVE DUPLICATE CODE!!!
        startStepping.setText("Relaunch");

        Label[] sourceLabels = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10};
        Label[] deviceLabels = {d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
        Label[] bufferLabels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};
        Rectangle[] sourceRectangles = {rs1, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rs9, rs10};
        Rectangle[] deviceRectangles = {rd1, rd2, rd3, rd4, rd5, rd6, rd7, rd8, rd9, rd10};
        Rectangle[] bufferRectangles = {br1, br2, br3, br4, br5, br6, br7, br8, br9, br10};
        for (int i = 0; i < 10; i++) {
            sourceRectangles[i].setVisible(false);
            sourceLabels[i].setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            deviceRectangles[i].setVisible(false);
            deviceLabels[i].setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            bufferRectangles[i].setVisible(false);
            bufferLabels[i].setVisible(false);
        }

        Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
        StatController statController = StatController.instance;
        timeField.setText(String.valueOf(controller.getCurrentTime()));
        doStep.setDisable(false);
        for (int i = 0; i < statController.getSourceCount(); i++) {
            sourceRectangles[i].setVisible(true);
        }
        for (int i = 0; i < statController.getDeviceCount(); i++) {
            deviceRectangles[i].setVisible(true);
        }
        for (int i = 0; i < controller.getBuffer().getCapacity(); i++) {
            bufferRectangles[i].setVisible(true);
        }
    }

    private void cleanStepping() { // @todo: REMOVE DUPLICATE CODE!!!
        Label[] sourceLabels = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10};
        Label[] deviceLabels = {d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
        Label[] bufferLabels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};
        Rectangle[] sourceRectangles = {rs1, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rs9, rs10};
        Rectangle[] deviceRectangles = {rd1, rd2, rd3, rd4, rd5, rd6, rd7, rd8, rd9, rd10};
        Rectangle[] bufferRectangles = {br1, br2, br3, br4, br5, br6, br7, br8, br9, br10};
        for (int i = 0; i < 10; i++) {
            sourceRectangles[i].setVisible(false);
            sourceLabels[i].setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            deviceRectangles[i].setVisible(false);
            deviceLabels[i].setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            bufferRectangles[i].setVisible(false);
            bufferLabels[i].setVisible(false);
        }

        timeField.setText("");
        tasksCompleted.setText("");
    }

    @FXML
    public void launchAuto(ActionEvent actionEvent) {
        cleanStepping();
        doStep.setDisable(true);
        startStepping.setText("Relaunch");

        Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
        try {
            controller.executeAuto();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatController.instance.sourceStatTable();
        StatController.instance.deviceStatTable();
    }

    @FXML
    public void drawPlots(ActionEvent actionEvent) {
        cleanStepping();
        doStep.setDisable(true);
        startStepping.setText("Relaunch");

        sourceCancel.getData().clear();
        sourceAverage.getData().clear();
        sourceBusiness.getData().clear();

        deviceCancel.getData().clear();
        deviceAverage.getData().clear();
        deviceBusiness.getData().clear();

        bufferCancel.getData().clear();
        bufferAverage.getData().clear();
        bufferBusiness.getData().clear();

        int bufferSize = 3;
        int sourceCount;
        int deviceCount = 3;

        XYChart.Series<Number, Number> seriesSourceCancel = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesSourceAverage = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesSourceBusiness = new XYChart.Series<>();
        for (sourceCount = 1; sourceCount < 80; sourceCount++) {
            try {
                Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
                controller.executeAuto();
                StatController statController = StatController.instance;

                seriesSourceCancel.getData().add(new XYChart.Data<>(sourceCount, statController.getCancelProb()));
                seriesSourceAverage.getData().add(new XYChart.Data<>(sourceCount, statController.getAverageTime()));
                seriesSourceBusiness.getData().add(new XYChart.Data<>(sourceCount, statController.getAverageWorkingTime() / controller.getCurrentTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sourceCancel.setLegendVisible(false);
        sourceAverage.setLegendVisible(false);
        sourceBusiness.setLegendVisible(false);
        sourceCancel.getData().add(seriesSourceCancel);
        sourceAverage.getData().add(seriesSourceAverage);
        sourceBusiness.getData().add(seriesSourceBusiness);

        sourceCount = 10;
        XYChart.Series<Number, Number> seriesDeviceCancel = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesDeviceAverage = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesDeviceBusiness = new XYChart.Series<>();
        for (deviceCount = 1; deviceCount < 80; deviceCount++) {
            try {
                Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
                controller.executeAuto();
                StatController statController = StatController.instance;

                seriesDeviceCancel.getData().add(new XYChart.Data<>(deviceCount, statController.getCancelProb()));
                seriesDeviceAverage.getData().add(new XYChart.Data<>(deviceCount, statController.getAverageTime()));
                seriesDeviceBusiness.getData().add(new XYChart.Data<>(deviceCount, statController.getAverageWorkingTime() / controller.getCurrentTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        deviceCancel.setLegendVisible(false);
        deviceAverage.setLegendVisible(false);
        deviceBusiness.setLegendVisible(false);
        deviceCancel.getData().add(seriesDeviceCancel);
        deviceAverage.getData().add(seriesDeviceAverage);
        deviceBusiness.getData().add(seriesDeviceBusiness);

        deviceCount = 3;
        XYChart.Series<Number, Number> seriesBufferCancel = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesBufferAverage = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesBufferBusiness = new XYChart.Series<>();
        for (bufferSize = 1; bufferSize < 80; bufferSize++) {
            try {
                Controller controller = new Controller(alpha, beta, lambda, bufferSize, requestCount, sourceCount, deviceCount);
                controller.executeAuto();
                StatController statController = StatController.instance;

                seriesBufferCancel.getData().add(new XYChart.Data<>(bufferSize, statController.getCancelProb()));
                seriesBufferAverage.getData().add(new XYChart.Data<>(bufferSize, statController.getAverageTime()));
                seriesBufferBusiness.getData().add(new XYChart.Data<>(bufferSize, statController.getAverageWorkingTime() / controller.getCurrentTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bufferCancel.setLegendVisible(false);
        bufferAverage.setLegendVisible(false);
        bufferBusiness.setLegendVisible(false);
        bufferCancel.getData().add(seriesBufferCancel);
        bufferAverage.getData().add(seriesBufferAverage);
        bufferBusiness.getData().add(seriesBufferBusiness);
    }


}
