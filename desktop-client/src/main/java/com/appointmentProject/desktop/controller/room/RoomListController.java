package com.appointmentProject.desktop.controller.room;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomListController {

    @FXML private TableView<JsonObject> roomTable;
    @FXML private TableColumn<JsonObject, String> roomNumberColumn;
    @FXML private TableColumn<JsonObject, String> floorNumberColumn;
    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    private final ObservableList<JsonObject> roomList = FXCollections.observableArrayList();
    private FilteredList<JsonObject> filteredRooms;

    @FXML
    public void initialize() {
        // table setup
        roomNumberColumn.setCellValueFactory(data ->
                Bindings.createStringBinding(() ->
                        data.getValue().get("roomNumber").getAsString()
                ));

        floorNumberColumn.setCellValueFactory(data ->
                Bindings.createStringBinding(() ->
                        data.getValue().get("floorNumber").getAsString()
                ));

        // filtered list wrapper
        filteredRooms = new FilteredList<>(roomList, p -> true);
        roomTable.setItems(filteredRooms);

        setupSearch();
        loadRooms();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();

            filteredRooms.setPredicate(room -> {
                if (lower.isEmpty()) return true;

                String rn = room.get("roomNumber").getAsString().toLowerCase();
                String fn = room.get("floorNumber").getAsString().toLowerCase();

                return rn.contains(lower) || fn.contains(lower);
            });
        });
    }

    // load all rooms
    private void loadRooms() {
        try {
            URL url = new URL("http://localhost:8080/room/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            JsonArray arr = com.google.gson.JsonParser
                    .parseReader(new InputStreamReader(con.getInputStream()))
                    .getAsJsonArray();

            roomList.clear();
            for (var el : arr) {
                roomList.add(el.getAsJsonObject());
            }

        } catch (Exception e) {
            messageLabel.setText("Failed to load rooms.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateRoom() {
        SceneNavigator.switchTo("/fxml/room_create.fxml");
    }

    @FXML
    private void handleEditRoom() {
        JsonObject selected = roomTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Please select a room.");
            return;
        }

        RoomEditController.selectedRoomNumber = selected.get("roomNumber").getAsString();
        SceneNavigator.switchTo("/fxml/room_edit.fxml");
    }

    @FXML
    private void handleDeleteRoom() {
        JsonObject selected = roomTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Please select a room.");
            return;
        }

        try {
            String roomNumber = selected.get("roomNumber").getAsString();

            URL url = new URL("http://localhost:8080/room/delete/" + roomNumber);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            if (con.getResponseCode() == 200) {
                loadRooms();
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Room deleted.");
            } else {
                messageLabel.setText("Delete failed.");
            }

        } catch (Exception e) {
            messageLabel.setText("Server error.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }
}