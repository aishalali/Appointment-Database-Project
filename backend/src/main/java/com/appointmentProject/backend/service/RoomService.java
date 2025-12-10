package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Room;
import com.appointmentProject.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepo;

    // validation
    private void validateRoom(Room r) {

        String num = r.getRoomNumber();

        // null check
        if (num == null) {
            throw new IllegalArgumentException("room_number cannot be null.");
        }

        // trim before validation
        num = num.trim();

        int floor = r.getFloorNumber();

        // length check
        if (num.length() < 3 || num.length() > 4) {
            throw new IllegalArgumentException("room_number must be length 3 or 4.");
        }

        // 3-digit room: floor must match first digit
        if (num.length() == 3) {
            int expected = Character.getNumericValue(num.charAt(0));
            if (floor != expected) {
                throw new IllegalArgumentException("floor_number must match first digit of room_number.");
            }
        }

        // 4-digit room: floor must match first two digits
        if (num.length() == 4) {
            int expected = Integer.parseInt(num.substring(0, 2));
            if (floor != expected) {
                throw new IllegalArgumentException("floor_number must match first two digits of room_number.");
            }
        }
    }

    // get all rooms
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    // get one room
    public Room getRoomByNumber(String roomNumber) {
        return roomRepo.findById(roomNumber)
                .orElseThrow(() ->
                        new RecordNotFoundException("Room with number " + roomNumber + " not found."));
    }

    // create room
    public Room addRoom(Room room) {
        validateRoom(room);
        return roomRepo.save(room);
    }

    // update room
    public Room updateRoom(String oldRoomNumber, Room updated) {

        // Validate the NEW room info first
        validateRoom(updated);

        // Make sure the old room exists
        if (!roomRepo.existsById(oldRoomNumber)) {
            throw new RecordNotFoundException("Room with number " + oldRoomNumber + " not found.");
        }

        // Delete old room (old PK)
        roomRepo.deleteById(oldRoomNumber);

        // Insert room with NEW room_number
        return roomRepo.save(updated);
    }

    // delete room
    public void deleteRoom(String roomNumber) {

        if (!roomRepo.existsById(roomNumber)) {
            throw new RecordNotFoundException("Room with number " + roomNumber + " not found.");
        }

        roomRepo.deleteById(roomNumber);
    }
}