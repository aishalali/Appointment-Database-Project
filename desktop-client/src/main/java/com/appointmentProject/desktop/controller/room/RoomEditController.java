package com.appointmentProject.desktop.controller.room;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomEditController {

    // passed from RoomListController
    public static String selectedRoomNumber;

    @FXML private TextField roomNumberField;
    @FXML private TextField floorNumberField;

    @FXML private Label messageLabel;

    private String originalRoomNumber;

    @FXML
    private void initialize() {
        // get room number selected from list
        originalRoomNumber = selectedRoomNumber;
        loadRoom(originalRoomNumber);
    }

    // load room info into fields
    private void loadRoom(String roomNumber) {
        try {
            URL url = new URL("http://localhost:8080/room/" + roomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            JsonObject obj = com.google.gson.JsonParser.parseReader(
                    new InputStreamReader(con.getInputStream())
            ).getAsJsonObject();

            roomNumberField.setText(obj.get("roomNumber").getAsString());
            floorNumberField.setText(obj.get("floorNumber").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load room.");
        }
    }

    @FXML
    private void handleSave() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("roomNumber", roomNumberField.getText());
            body.addProperty("floorNumber", floorNumberField.getText());

            URL url = new URL("http://localhost:8080/room/update/" + originalRoomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Room updated successfully!");
            } else {
                messageLabel.setText("Update failed.");
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
