package batmanintegral.controller;

import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import batmanintegral.model.DrawerTask;
import batmanintegral.model.FPoint;
import batmanintegral.model.NewPointEvent;
import batmanintegral.model.NewPointListener;

import java.awt.image.BufferedImage;


public class Controller implements NewPointListener {

    private batmanintegral.controller.Main mainApp;
    private DrawerTask drawerTask;
    private int maxPointAmount;
    private int pointsCounter;
    private int pointsUnderCurve;
    private GraphicsContext gc;
    private BufferedImage bi;
    private final double MIN = -8;
    private final double MAX = 8;


    @FXML
    private Canvas canvas;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField pointsAmountTextField;
    @FXML
    private Label resultLabel;



    @FXML
    private void handleStopBtnAction(){
        drawerTask.cancel();
        showIntegralResult();
    }


    @FXML
    private void handleRunBtnAction(){
        try {
            maxPointAmount = Integer.parseInt(pointsAmountTextField.getText());
            if(maxPointAmount <= 0) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            showErrorDialog();
            return;
        }

        pointsCounter = 0;
        pointsUnderCurve = 0;
        gc = canvas.getGraphicsContext2D();
        bi = new BufferedImage((int) canvas.getWidth(), (int) canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

        drawerTask = new DrawerTask(maxPointAmount, MIN, MAX);
        drawerTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                drawImage();
                showIntegralResult();
            }
        });
        drawerTask.addNewPointListener(this);
        new Thread(drawerTask).start();
        progressBar.progressProperty().bind(drawerTask.progressProperty());
    }

    @FXML
    public void initialize() {
//        canvas.widthProperty().bind(canvasContainer.widthProperty());
//        canvas.heightProperty().bind(canvasContainer.heightProperty());
//        canvasContainer.getChildren().add(canvas);
    }

    @Override
    public synchronized void newPointDetected(NewPointEvent e) {
        //gc.setFill(Color.BLUEVIOLET);
        scaleCoordinatesOf(e.point);
        if(e.belongsToFunction) {
            bi.setRGB((int) e.point.x, (int) e.point.y, java.awt.Color.YELLOW.getRGB());
            pointsUnderCurve++;
        }
        else {
            bi.setRGB((int) e.point.x, (int) e.point.y, java.awt.Color.BLUE.getRGB());
        }
        pointsCounter++;
        if(pointsCounter % 1000 == 0) {
            drawImage();
        }
    }

    private void drawImage() {
        gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0, 0);
        //gc.fillRect(gc.getCanvas().getLayoutX(), gc.getCanvas().getLayoutY(), gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private void scaleCoordinatesOf(FPoint point) {
        point.y = -point.y;
        point.x = ((bi.getHeight()-0) * (point.x-MIN) / (MAX-MIN) + 0);
        point.y = ((bi.getHeight()-0) * (point.y-MIN) / (MAX-MIN) + 0);
    }

    public void setMainApp(batmanintegral.controller.Main mainApp) {
        this.mainApp = mainApp;
    }

    private void showIntegralResult() {
        resultLabel.setText("INTEGRAL RESULT:\n" + calculateIntegral());
        showResultDialog(calculateIntegral());
    }

    private double calculateIntegral() {
        return (Math.pow((MAX-MIN), 2) * pointsUnderCurve/pointsCounter);
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainApp.getMainWindow());
        alert.setTitle("Invalid data");
        alert.setHeaderText(null);
        alert.setContentText("The value must be positive non-zero number.");
        alert.showAndWait();
    }

    public void showResultDialog(double result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainApp.getMainWindow());
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText("Integral result is:\n" + result);
        alert.showAndWait();
    }
}
