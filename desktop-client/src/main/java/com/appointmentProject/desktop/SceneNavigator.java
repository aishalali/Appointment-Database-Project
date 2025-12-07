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
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class SceneNavigator {

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
            Scene scene = new Scene(root);

            mainStage.setScene(scene);

            // If we're switching TO login → turn OFF fullscreen
            if (fxmlPath.toLowerCase().contains("login")) {
                mainStage.setFullScreen(false);
                mainStage.setMaximized(false);

                // Optional: reset window size for login screen
                mainStage.setWidth(600);
                mainStage.setHeight(400);
                mainStage.centerOnScreen();

            }

            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
