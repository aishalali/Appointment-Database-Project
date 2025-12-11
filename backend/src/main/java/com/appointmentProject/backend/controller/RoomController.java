package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Room;
import com.appointmentProject.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Get all rooms
    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Get one room
    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> getRoom(@PathVariable("roomNumber") String roomNumber) {
        return ResponseEntity.ok(roomService.getRoomByNumber(roomNumber));
    }

    // Create room
    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.addRoom(room));
    }

    // Update room
    @PutMapping("/update/{roomNumber}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable("roomNumber") String roomNumber,
            @RequestBody Room room
    ) {
        return ResponseEntity.ok(roomService.updateRoom(roomNumber, room));
    }

    // Delete room
    @DeleteMapping("/delete/{roomNumber}")
    public ResponseEntity<String> deleteRoom(@PathVariable("roomNumber") String roomNumber) {
        roomService.deleteRoom(roomNumber);
        return ResponseEntity.ok("Room deleted.");
    }
}
