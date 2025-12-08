/*********************************************************************
 *   SceneNavigator.java
 *
 *      This class is responsible for redirecting the users to
 *      specific dashboards depending on their user's account type.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/1/2025
 *********************************************************************/
package com.appointmentProject.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            System.out.println("[SceneNavigator] Loading: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();   // <-- REAL loader (no silent failure!)

            Scene scene = new Scene(root);
            mainStage.setScene(scene);

            // Special case: login screen resets window size
            if (fxmlPath.toLowerCase().contains("login")) {
                mainStage.setFullScreen(false);
                mainStage.setMaximized(false);
                mainStage.setWidth(600);
                mainStage.setHeight(400);
                mainStage.centerOnScreen();
            }

            mainStage.show();
            System.out.println("[SceneNavigator] Scene switched successfully.");

        } catch (Exception e) {
            System.err.println("\n*********************************************");
            System.err.println("SCENE LOAD FAILED: " + fxmlPath);
            System.err.println("*********************************************");
            e.printStackTrace();
            System.err.println("*********************************************\n");
        }
    }
}
