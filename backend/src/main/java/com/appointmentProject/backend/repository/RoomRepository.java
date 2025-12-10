/********************************************************************************************
 * RoomRepository
 *      The repository interface involving Room.
 *
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

}
