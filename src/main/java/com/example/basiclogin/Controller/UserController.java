package com.example.basiclogin.Controller;

import com.example.basiclogin.Model.Booking;
import com.example.basiclogin.Model.User;
import com.example.basiclogin.Service.BookingService;
import com.example.basiclogin.Service.UserService;
import com.example.basiclogin.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @Autowired
    private VehicleService vehicleService;

    // Register new user
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login"; // Redirect to login page after registration
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Login page
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<Booking> bookings = bookingService.getBookingsByUser(user);

        model.addAttribute("username", username);
        model.addAttribute("bookings", bookings);
        model.addAttribute("vehicles", vehicleService.findAllVehicles());

        return "users1";
    }
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("vehicles", vehicleService.findAllVehicles());
        model.addAttribute("allBookings", bookingService.findAllBookings());

        return "adminDashboard";
    }



}

