
/***************************************************************************************
 * Billing.java
 *
 *      This is a blueprint for storing information related to Billing and acts as
 *      a model to transfer information between the backend and the frontend.
 *
 *      Contains identifying variables of Billing and is used by the service layer
 *      for retrieval and updates.
 *      - "id": is a unique identifier that differentiates between Billing transactions.
 *          It is also the primary way of accessing specific Billing records. This is
 *          immutable. The number combination is created by the auto-increment feature in MySQL.
 *      - "insurance_id": (nullable) the id for the insurance that could be covering it.
 *          If it"s NULL, it is assumed that the Patient receiving this bill does not have
 *          insurance OR the insurance company will not cover anything in the bill. This
 *          means that as long as the insurance covers something, then the insurance will
 *          show up in the bill.
 *      - "cost": the total cost of the appointment. The calculation will be conducted through
 *          a service class.
 *      - "status_of_payment": Shows the general progress of payment completion.
 *          - Acceptable Payment Statuses:
 *              - "PAID": the bill has been paid off.
 *              - "PENDING": the bill is still being processed or is awaiting confirmation
 *              - "INCOMPLETE": the full balance of the bill is due
 *              - "WAIVED": financial responsibility has been forgiven due to settlement or
 *                      financial hardship.
 *              - "PARTIAL": some of the payment is paid for by insurance or the patient, and
 *                      the remainder is still needing to be paid off.
 *      - "payment_type": the payment type in which the patient pays
 *          the bill.
 *          - Acceptable Payment Types:
 *              - "CREDIT": credit card
 *              - "CHECK": check
 *              - "DEBIT": debit
 *              - "CASH": cash
 *              - "DIGITAL": digital wallet
 *              - "INSURANCE": insurance covers the full thing
 *              - "N/A": Only Available if Waived or Incomplete is the current status.
 *          - Additional NOTE: if the Insurance ID is not null and the acceptable payment
 *                  type is anything but INSURANCE, this implies that the Insurance will
 *                  be covering a portion of it. Also, if there is no Insurance ID in the
 *                  assigned Appointment's Insurance ID or its assigned LabOrders or
 *                  Prescription's Insurance IDs, then the Insurance ID cannot be present
 *                  on the current billing. This will be handled in a separate service class.
 *
 * @author Matthew Kiyono
 * @version 1.3
 * @since 10/30/2026
 *******************************************************************************************/
package com.appointmentProject.backend.model;
import com.appointmentProject.backend.util.NullString;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "billing")
@DynamicUpdate
public class Billing {

    //required variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id",  nullable = false, unique = true)
    private int id;

    @NotNull
    @Column(name = "cost", nullable = false)
    private double cost;

    @NotNull
    @Column(name = "status_of_payment", nullable = false)
    private String statusOfPayment;

    @NotNull
    @Column(name = "payment_type", nullable = false)
    private String paymentType;


    //optional variables
    @Column(name = "insurance_id")
    private Integer insuranceId;

    //Test Constructor Only!
    protected Billing() {}

    //constructor
    private Billing(Builder builder){
        this.id = builder.id;
        this.cost = builder.cost;
        this.statusOfPayment = builder.statusOfPayment;
        this.paymentType = builder.paymentType;
        this.insuranceId = builder.insuranceId;
    }

    //getter method
    public int getId() {return id;}
    public double getCost() {return cost;}
    public String getStatusOfPayment() {return statusOfPayment;}
    public String getPaymentType() {return paymentType;}
    public Integer getInsuranceId() {return insuranceId;}

    //setter method
    public void setId(int id) {this.id = id;}
    public void setCost(double cost) {this.cost = cost;}
    public void setStatusOfPayment(String statusOfPayment) {this.statusOfPayment = statusOfPayment;}
    public void setPaymentType(String paymentType) {this.paymentType = paymentType;}
    public void setInsuranceId(Integer insuranceId) {this.insuranceId = insuranceId;}

    //toString method
    @Override
    public String toString(){
        return "Billing: " +
                "\nTransaction ID: " + id +
                "\nTotal Cost: " + cost +
                "\nPayment Status: " + statusOfPayment +
                "\nPayment Type: " + paymentType +
                "\nInsurance ID: " + NullString.check(insuranceId) + "\n";
    }

    //builder constructor
    public static class Builder{

        //required variables
        private int id;
        private double cost;
        private String statusOfPayment;
        private String paymentType;
        //optional variables
        private Integer insuranceId;

        //required parameter constructor
        public Builder(int id, double cost,String statusOfPayment,String paymentType){
            this.id = id;
            this.cost = cost;
            this.statusOfPayment = statusOfPayment;
            this.paymentType = paymentType;
        }

        //optional parameter checks
        public Builder insuranceId(Integer insuranceId){
            this.insuranceId = insuranceId;
            return this;
        }

        //putting them together
        public Billing build(){return new Billing(this);}
    }


}
