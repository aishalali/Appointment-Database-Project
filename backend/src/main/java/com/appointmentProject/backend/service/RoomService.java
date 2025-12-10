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

    //validation
    private void validateRoom(Room r) {
        String num = r.getRoomNumber();
        int floor = r.getFloorNumber();

        if (num == null || num.length() < 3 || num.length() > 4) {
            throw new IllegalArgumentException("room_number must be length 3 or 4.");
        }

        if (num.length() == 3) {
            int expected = Character.getNumericValue(num.charAt(0));
            if (floor != expected) {
                throw new IllegalArgumentException("floor_number must match first digit of room_number.");
            }
        }

        if (num.length() == 4) {
            int expected = Integer.parseInt(num.substring(0, 2));
            if (floor != expected) {
                throw new IllegalArgumentException("floor_number must match first two digits of room_number.");
            }
        }
    }

    //get all
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    //get one
    public Room getRoomByNumber(String roomNumber) {
        return roomRepo.findById(roomNumber)
                .orElseThrow(() ->
                        new RecordNotFoundException("Room with number " + roomNumber + " not found."));
    }

    //create
    public Room addRoom(Room room) {
        validateRoom(room);
        return roomRepo.save(room);
    }

    //update
    public Room updateRoom(String roomNumber, Room updated) {

        Room existing = roomRepo.findById(roomNumber)
                .orElseThrow(() ->
                        new RecordNotFoundException("Room with number " + roomNumber + " not found."));

        validateRoom(updated);

        existing.setRoomNumber(updated.getRoomNumber());
        existing.setFloorNumber(updated.getFloorNumber());

        return roomRepo.save(existing);
    }

    //delete
    public void deleteRoom(String roomNumber) {

        if (!roomRepo.existsById(roomNumber)) {
            throw new RecordNotFoundException("Room with number " + roomNumber + " not found.");
        }

        roomRepo.deleteById(roomNumber);
    }
}
