package com.appointmentProject.desktop.controller.room;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomCreateController {

    @FXML private TextField roomNumberField;
    @FXML private TextField floorNumberField;

    @FXML private Label messageLabel;

    @FXML
    private void handleCreateRoom() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("roomNumber", roomNumberField.getText());
            body.addProperty("floorNumber", floorNumberField.getText());

            URL url = new URL("http://localhost:8080/room/add");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Room created successfully!");
            } else {
                messageLabel.setText("Creation failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Server error.");
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }

}
