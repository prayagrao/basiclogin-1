package com.example.basiclogin.Controller;


import com.example.basiclogin.Model.Vehicle;
import com.example.basiclogin.Repository.VehicleRepository;
import com.example.basiclogin.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired

    private VehicleService vehicleService;

    // Show all available vehicles
    @GetMapping("/user/vehicles")
    public String showVehicles(Model model) {
        //List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findByAvailableTrue();
        model.addAttribute("vehicles", vehicles);
        return "users1";
    }

    // Upload image (via form)
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("id") Long vehicleId,
                              @RequestParam("file") MultipartFile file) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();
        vehicle.setImage(file.getBytes());
        vehicleRepository.save(vehicle);
        return "redirect:/user/vehicles";
    }

    // Serve image bytes to frontend
    @GetMapping("/vehicle/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(vehicle.getImage());
    }


   /* @GetMapping("/vehiclesList")
    public String getAllVehicles(@RequestParam(value = "type", required = false) String type, Model model) {
        //List<Vehicle> vehicles = vehicleRepository.findAll(); // Fetch all vehicles from DB
        //model.addAttribute("vehicles", vehicles); // Add data to the model
        List<Vehicle> vehicles;
        if (type != null && !type.isEmpty()) {
            vehicles = vehicleRepository.findByType(type);  // You must have this in your repository
            model.addAttribute("selectedType", type);
        } else {
            vehicles = vehicleRepository.findAll();
        }

        List<String> vehicleTypes = vehicleRepository.findDistinctTypes();
        System.out.println("Vehicle Types: " + vehicleTypes);// Custom query required
        model.addAttribute("vehicleTypes", vehicleTypes);
        model.addAttribute("vehicles", vehicles);
        return "VehiclesList"; // The name of your Thymeleaf HTML file (without .html)
    } */

    @GetMapping("/vehiclesList")
    public String getAllVehicles(@RequestParam(value = "type", required = false) String type, Model model) {
        List<Vehicle> vehicles;
        if (type != null && !type.isEmpty()) {
            vehicles = vehicleRepository.findByTypeAndAvailableTrue(type);
            model.addAttribute("selectedType", type);
        } else {
            vehicles = vehicleRepository.findByAvailableTrue();
        }

        List<String> vehicleTypes = vehicleRepository.findDistinctTypes();
        model.addAttribute("vehicleTypes", vehicleTypes);
        model.addAttribute("vehicles", vehicles);
        return "VehiclesList";
    }


    @GetMapping("/addVehicle")
    public String showAddVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "addVehicle";  // The name of the HTML page (addVehicle.html)
    }

    @PostMapping("/saveVehicle")
    public String saveVehicle(@ModelAttribute Vehicle vehicle, @RequestParam("file") MultipartFile file) {
        // Save the vehicle details to the database
        vehicle.setAvailable(true);
        vehicleService.saveVehicle(vehicle, file);
        System.out.println(vehicle.getName());
        System.out.println(vehicle.getAvailable());
        //return "redirect:/availableVehicles";  // Redirect to the available vehicles page
        //return "redirect:/user/vehicles";
        return "redirect:/admin/dashboard";
    }


    // Show Edit Form
    @GetMapping("/editVehicle/{id}")
    public String showEditVehicleForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        return "addVehicle"; // View name for the edit form (you need to create this view).
    }

    // Handle Edit Vehicle
    @PostMapping("/editVehicle/{id}")
    public String editVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle) {
        vehicle.setId(id);  // Ensure the ID is set before saving
        vehicleService.save(vehicle);
        return "redirect:/admin/dashboard"; // Redirect to the admin dashboard after editing
    }

    // Delete Vehicle
    @PostMapping("/deleteVehicle/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        return "redirect:/admin/dashboard"; // Redirect to the admin dashboard after deletion
    }
}

