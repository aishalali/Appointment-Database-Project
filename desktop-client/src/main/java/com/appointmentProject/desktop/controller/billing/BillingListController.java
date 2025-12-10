/********************************************************************
 *  BillingListController.java
 *
 *          This controller is responsible for providing a list of
 *          billings.
 *
 * @author Jack Mitchell
 * @version 1.0
 * @since 12/9/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller.billing;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BillingListController {

    @FXML private TableView<BillingRow> billingsTable;
    @FXML private TableColumn<BillingRow, Integer> idCol;
    @FXML private TableColumn<BillingRow, String> costCol;
    @FXML private TableColumn<BillingRow, String> statusOfPaymentCol;
    @FXML private TableColumn<BillingRow, String> paymentTypeCol;
    @FXML private TableColumn<BillingRow, String> insuranceIdCol;

    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    private final ObservableList<BillingRow> masterList = FXCollections.observableArrayList();

    public static class BillingRow {
        private final int id;
        private final String cost;
        private final String statusOfPayment;
        private final String paymentType;
        private final String insuranceId;

        public BillingRow(int id, String cost, String statusOfPayment, String paymentType, String insuranceId) {
            this.id = id;
            this.cost = cost;
            this.statusOfPayment = statusOfPayment;
            this.paymentType = paymentType;
            this.insuranceId = insuranceId;
        }

        public int getId() { return id; }
        public String getCost() { return cost; }
        public String getStatusOfPayment() { return statusOfPayment; }
        public String getPaymentType() { return paymentType; }
        public String getInsuranceId() { return insuranceId; }
    }

    @FXML
    private void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        statusOfPaymentCol.setCellValueFactory(new PropertyValueFactory<>("statusOfPayment"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        insuranceIdCol.setCellValueFactory(new PropertyValueFactory<>("insuranceId"));

        applyColumnStyling();
        loadBillings();

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBillings(newVal));
        }
    }

    private void applyColumnStyling() {
        billingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        centerAlign(idCol);
        centerAlign(costCol);
        leftAlign(statusOfPaymentCol);
        leftAlign(paymentTypeCol);
        leftAlign(insuranceIdCol);

        idCol.setMinWidth(60);
        costCol.setMinWidth(140);
        statusOfPaymentCol.setMinWidth(140);
        paymentTypeCol.setMinWidth(130);
        insuranceIdCol.setMinWidth(200);
    }

    private <T> void centerAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    private <T> void leftAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    private void loadBillings() {
        try {
            URL url = new URL("http://localhost:8080/billing/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            JsonArray arr = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonArray();

            ObservableList<BillingRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String cost = obj.get("cost").getAsString();
                String statusOfPayment = obj.get("statusOfPayment").getAsString();
                String paymentType = obj.get("paymentType").getAsString();
                String insuranceId = obj.get("insuranceId").getAsString();

                rows.add(new BillingRow(id, cost, statusOfPayment, paymentType, insuranceId));
            }

            masterList.setAll(rows);
            billingsTable.setItems(masterList);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading billings.");
        }
    }

    private void filterBillings(String query) {
        if (query == null || query.isBlank()) {
            billingsTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<BillingRow> filtered = masterList.filtered(n ->
                n.getCost().toLowerCase().contains(lower) ||
                        n.getStatusOfPayment().toLowerCase().contains(lower) ||
                        n.getPaymentType().toLowerCase().contains(lower) ||
                        n.getInsuranceId().toLowerCase().contains(lower) ||
                        String.valueOf(n.getId()).contains(lower)
        );

        billingsTable.setItems(filtered);
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }

    @FXML
    public void handleCreateBilling() {
        SceneNavigator.switchTo("/fxml/billing_create.fxml");
    }

    @FXML
    public void handleEditBilling() {
        BillingRow row = billingsTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            messageLabel.setText("Please select a billing to edit.");
            return;
        }

        BillingEditController.selectedBillingId = row.getId();
        SceneNavigator.switchTo("/fxml/billing_edit.fxml");
    }
}
