package com.example.basiclogin.Service;



import com.example.basiclogin.Model.Vehicle;
import com.example.basiclogin.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    /*public void saveVehicle(Vehicle vehicle, MultipartFile file) {
        // Handle image upload (save the file somewhere, like a directory or cloud storage)
        // If the file is not empty, save the image and set the path in the vehicle entity
        if (!file.isEmpty()) {
            String imagePath = saveImage(file);  // Implement image saving logic
            vehicle.setImage(imagePath.getBytes());
        }
        vehicleRepository.save(vehicle);  // Save the vehicle details to the database
    }

    private String saveImage(MultipartFile file) {
        // Implement your image saving logic (e.g., store on disk and return the image path)
        return "/path/to/image.jpg";
    } */

    public void saveVehicle(Vehicle vehicle, MultipartFile file) {
        try {
            // Handle image upload and save the file to the file system or database
            if (!file.isEmpty()) {
                // Saving image as bytes
                vehicle.setImage(file.getBytes());
            }
            vehicleRepository.save(vehicle);  // Save vehicle details to DB
        } catch (IOException e) {
            // Handle image saving failure
            e.printStackTrace();
            // Optionally, return some error message to the frontend
        }
    }
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    // Save or update a vehicle
    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    // Delete a vehicle by ID
    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }
    public Vehicle findByName(String name) {
        return vehicleRepository.findByName(name);
    }


}

