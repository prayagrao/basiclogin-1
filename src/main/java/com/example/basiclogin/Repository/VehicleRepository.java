package com.example.basiclogin.Repository;



import com.example.basiclogin.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByType(String type);
    @Query("SELECT DISTINCT v.type FROM Vehicle v")
    List<String> findDistinctTypes();
    List<Vehicle> findByAvailableTrue();

    Vehicle findByName(String name);

    List<Vehicle> findByTypeAndAvailableTrue(String type);
}

