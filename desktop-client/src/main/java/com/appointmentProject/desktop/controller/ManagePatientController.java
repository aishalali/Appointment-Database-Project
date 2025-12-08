/********************************************************************
 *  PatientMenuController.java
 *
 *          This controller is responsible for providing a list of
 *          certain features (adding, removing, updating, and viewing)
 *          patients depending on the user type.
 *
 *
 * @author Matthew Kiyono
 * @version 1.2
 * @since 12/7/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableRow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManagePatientController {

    public static String previousPage = "/fxml/login.fxml";

    // NEW → added shared variables
    public static int selectedPatientId;
    public static String deletionMessage = "";

    @FXML private TableView<PatientRow> patientTable;
    @FXML private TableColumn<PatientRow, Integer> idCol;
    @FXML private TableColumn<PatientRow, String> firstNameCol;
    @FXML private TableColumn<PatientRow, String> lastNameCol;
    @FXML private TableColumn<PatientRow, String> dobCol;
    @FXML private TableColumn<PatientRow, String> emailCol;
    @FXML private TableColumn<PatientRow, String> phoneCol;

    @FXML private TextField searchField;
    @FXML private Button createPatientButton;
    @FXML private Label deletionMessageLabel;

    private ObservableList<PatientRow> masterList = FXCollections.observableArrayList();

    public static class PatientRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String dob;
        private final String email;
        private final String phone;

        public PatientRow(int id, String firstName, String lastName,
                          String dob, String email, String phone) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.email = email;
            this.phone = phone;
        }

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDob() { return dob; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }

    @FXML
    private void initialize() {

        // Show deletion message once
        if (!deletionMessage.isEmpty()) {
            deletionMessageLabel.setText(deletionMessage);
            deletionMessage = ""; // reset after showing
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        applyColumnStyling();
        loadPatients();

        searchField.textProperty().addListener((obs, o, n) -> filterPatients(n));

        if (previousPage.equals("/fxml/admin_dashboard.fxml") ||
                previousPage.equals("/fxml/receptionist_dashboard.fxml")) {

            createPatientButton.setVisible(true);
            createPatientButton.setManaged(true);

        } else {
            createPatientButton.setVisible(false);
            createPatientButton.setManaged(false);
        }

        patientTable.setRowFactory(table -> {
            TableRow<PatientRow> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    PatientRow selected = row.getItem();
                    selectedPatientId = selected.getId();
                    SceneNavigator.switchTo("/fxml/patient_details.fxml");
                }
            });

            return row;
        });
    }

    private void applyColumnStyling() {
        patientTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        centerAlignColumn(idCol);
        centerAlignColumn(dobCol);
        centerAlignColumn(phoneCol);

        leftAlignColumn(firstNameCol);
        leftAlignColumn(lastNameCol);
        leftAlignColumn(emailCol);

        idCol.setMinWidth(60);
        firstNameCol.setMinWidth(140);
        lastNameCol.setMinWidth(140);
        dobCol.setMinWidth(110);
        emailCol.setMinWidth(200);
        phoneCol.setMinWidth(120);
    }

    private <T> void centerAlignColumn(TableColumn<T, ?> column) {
        column.setStyle("-fx-alignment: CENTER;");
    }

    private <T> void leftAlignColumn(TableColumn<T, ?> column) {
        column.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    private void loadPatients() {
        try {
            URL url = new URL("http://localhost:8080/patient/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();
            ObservableList<PatientRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String fn = obj.get("firstName").getAsString();
                String ln = obj.get("lastName").getAsString();
                String dob = obj.get("doB").getAsString();
                String email = obj.get("email").isJsonNull() ? "" : obj.get("email").getAsString();
                String phone = obj.get("phone").getAsString();

                rows.add(new PatientRow(id, fn, ln, dob, email, phone));
            }

            masterList.setAll(rows);
            patientTable.setItems(masterList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterPatients(String query) {

        if (query == null || query.isBlank()) {
            patientTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<PatientRow> filtered = masterList.filtered(p ->
                p.getFirstName().toLowerCase().contains(lower) ||
                        p.getLastName().toLowerCase().contains(lower) ||
                        p.getEmail().toLowerCase().contains(lower) ||
                        p.getPhone().toLowerCase().contains(lower) ||
                        p.getDob().toLowerCase().contains(lower) ||
                        String.valueOf(p.getId()).contains(lower)
        );

        patientTable.setItems(filtered);
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }

    @FXML
    public void handleCreatePatient() {
        SceneNavigator.switchTo("/fxml/patient_create.fxml");
    }
}
