package com.appointmentProject.desktop.controller.room;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomEditController {

    public static String selectedRoomNumber;   // set by RoomListController

    @FXML private TextField roomNumberField;
    @FXML private TextField floorNumberField;

    @FXML private Label messageLabel;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        loadRoomData();
    }

    private void loadRoomData() {
        try {
            URL url = new URL("http://localhost:8080/room/" + selectedRoomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonObject obj = gson.fromJson(br.readLine(), JsonObject.class);
            br.close();

            // Load values
            roomNumberField.setText(obj.get("roomNumber").getAsString());

            floorNumberField.setText(obj.get("floorNumber").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading room.");
        }
    }

    @FXML
    private void handleSaveRoom() {
        try {
            JsonObject body = new JsonObject();

            body.addProperty("roomNumber", roomNumberField.getText().trim());
            body.addProperty("floorNumber",
                    Integer.parseInt(floorNumberField.getText().trim()));

            URL url = new URL("http://localhost:8080/room/update/" + selectedRoomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {

                selectedRoomNumber = roomNumberField.getText().trim();

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
    private void handleDeleteRoom() {
        try {
            URL url = new URL("http://localhost:8080/room/delete/" + selectedRoomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            con.getInputStream().close();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Room deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error deleting room.");
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/room_list.fxml");
    }
}
