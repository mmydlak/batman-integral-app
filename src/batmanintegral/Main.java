package batmanintegral.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage mainWindow;
    private static AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        primaryStage.setTitle("MonteCarlo App");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = fxmlLoader.load();

            Controller controller = fxmlLoader.getController();
            controller.setMainApp(this);

            mainWindow.setScene(new Scene(rootLayout, 600, 500));
            mainWindow.setResizable(false);
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
//        double d = 0;
//        double d0 = 8.48;
//        double t=20;
//        double T=1250+273.15;
//        d=859.58*d0*(Math.log(0.9949*Math.pow((t+1),0.001)) + Math.pow(Math.pow(Math.E, -357600/(8.31*T)),0.144));
//        System.out.println(d);
    }

    public Stage getMainWindow() {
        return mainWindow;
    }

    public Main() { }


}