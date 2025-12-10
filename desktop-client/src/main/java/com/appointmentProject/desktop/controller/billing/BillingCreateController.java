/********************************************************************
 *  BillingCreateController.java
 *
 *          This controller is responsible for providing an admin
 *          user the ability to create a new billing.
 *
 *
 * @author Jack Mitchell
 * @version 1.0
 * @since 12/9/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller.billing;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BillingCreateController {

    @FXML private TextField costField;
    @FXML private TextField statusOfPaymentField;
    @FXML private TextField paymentTypeField;
    @FXML private TextField insuranceIdField;

    @FXML private Label messageLabel;

    @FXML
    private void handleCreateBilling() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("cost", costField.getText());
            body.addProperty("statusOfPayment", statusOfPaymentField.getText());
            body.addProperty("paymentType", paymentTypeField.getText());
            body.addProperty("insuranceId", insuranceIdField.getText());

            URL url = new URL("http://localhost:8080/billing/add");
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
                messageLabel.setText("Billing created successfully!");
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
        SceneNavigator.switchTo("/fxml/billing_list.fxml");
    }
}
