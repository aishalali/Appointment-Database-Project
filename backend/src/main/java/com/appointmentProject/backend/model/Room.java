package com.appointmentProject.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;

/******************************************************************************
 * Room.java
 *
 *     Represents a Room entity from the database in object
 *     format for transferring from the database to the frontend.
 *      - "roomNumber": the number that is assigned to the room (3-4 char length)
 *      - "floorNumber": the floor the room is on.
 *
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 11/4/2025
 ********************************************************************************/
@Entity
@Table(name = "room")
@DynamicUpdate
public class Room {

    //variables
    @Id
    @NotNull
    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @NotNull
    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    //Test Constructor Only!
    protected Room() {}

    //constructor
    public Room(String roomNumber, int floorNumber) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
    }

    //getter methods
    public String getRoomNumber() {return roomNumber;}
    public int getFloorNumber() {return floorNumber;}

    //setter methods
    public void setRoomNumber(String room_number) {this.roomNumber = room_number;}
    public void setFloorNumber(int floor_number) {this.floorNumber = floor_number;}

    //toString method
    @Override
    public String toString() {
        return "Room:" +
                "\nNumber: " +  roomNumber +
                "\nFloor: " + floorNumber + "\n";
    }

}
