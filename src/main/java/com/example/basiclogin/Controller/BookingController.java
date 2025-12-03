package com.example.basiclogin.Controller;

import com.example.basiclogin.Model.Booking;
import com.example.basiclogin.Model.User;
import com.example.basiclogin.Model.Vehicle;
import com.example.basiclogin.Repository.BookingRepository;
import com.example.basiclogin.Service.BookingService;
import com.example.basiclogin.Service.UserService;
import com.example.basiclogin.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired

    private VehicleService vehicleService;
   /* @PostMapping("/bookVehicle")
    public String bookVehicle(@RequestParam("vehicleName") String vehicleName,
                              Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username); // Assume you have userService
        if (user != null) {
            // Create new booking
            Booking booking = new Booking();
            booking.setVehicleName(vehicleName);
            booking.setBookingDate(LocalDate.now().toString()); // today's date
            booking.setUser(user);

            // Save the booking to the database
            bookingService.saveBooking(booking);
        }

        // Redirect back to the vehicles page or dashboard after booking
        return "redirect:/dashboard"; // Adjust to your correct route if needed
    } */
   @PostMapping("/bookVehicle")
   public String bookVehicle(@RequestParam("vehicleName") String vehicleName, Principal principal) {
       String username = principal.getName();
       User user = userService.findByUsername(username);

       if (user != null) {
           // Create new booking
           Booking booking = new Booking();
           booking.setVehicleName(vehicleName);
           booking.setBookingDate(LocalDate.now().toString()); // today's date
           booking.setUser(user);

           // Save the booking
           bookingService.saveBooking(booking);

           // ✅ Mark the booked vehicle as unavailable
           Vehicle vehicle = vehicleService.findByName(vehicleName);
           if (vehicle != null) {
               vehicle.setAvailable(false);
               System.out.println(vehicle.getAvailable());
               vehicleService.save(vehicle); // Update vehicle availability
           }
       }

       return "redirect:/dashboard";
   }


    @GetMapping("/startBooking")
    public String showBookingForm(@RequestParam("vehicleName") String vehicleName, Model model) {
        Booking booking = new Booking();
        booking.setVehicleName(vehicleName);
        model.addAttribute("booking", booking);
        return "bookingForm";  // New Thymeleaf page
    }

    @GetMapping("/user/bookings")
    public String viewUserBookings(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<Booking> bookings = bookingService.getBookingsByUser(user);

        model.addAttribute("username", username);
        model.addAttribute("bookings", bookings);
        return "userBookings"; // This should be a new Thymeleaf page
    }

    @GetMapping("/editBooking/{id}")
    public String showEditBookingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findBookingById(id);
        model.addAttribute("booking", booking);
        return "editBooking"; // Create an editBooking.html page
    }

    @PostMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        return "redirect:/user/bookings"; // After delete, redirect back to bookings page
    }

    @PostMapping("/updateBooking")
    public String updateBooking(Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(updatedBooking.getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        existingBooking.setVehicleName(updatedBooking.getVehicleName());
        existingBooking.setBookingDate(updatedBooking.getBookingDate());

        bookingRepository.save(existingBooking);
        return "redirect:/user/bookings";
    }

    @PostMapping("/updateBooking1")
    public String updateBooking1(@RequestParam("id") Long id,
                                 @RequestParam("vehicleName") String vehicleName,
                                 @RequestParam("fromDate") String fromDate,
                                 @RequestParam("toDate") String toDate) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        existingBooking.setVehicleName(vehicleName);
        existingBooking.setFromDate(fromDate); // set fromDate
        existingBooking.setToDate(toDate);     // set toDate

        bookingRepository.save(existingBooking);

        return "redirect:/bookings"; // ✅ Redirect properly
    }

    @PostMapping("/deleteBooking1/{id}")
    public String deleteBooking1(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        return "redirect:/bookings"; // After delete, redirect back to admin bookings page
    }

    @GetMapping("/editBooking1/{id}")
    public String showEditBookingForm1(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findBookingById(id);
        model.addAttribute("booking", booking);
        return "adminEditBooking"; // Create an editBooking.html page
    }

    @PostMapping("/confirmBooking")
    public String confirmBooking(@ModelAttribute Booking booking, Principal principal) {
        // Assuming you have a service to find user by username
        User user = userService.findByUsername(principal.getName());
        booking.setUser(user);

        bookingRepository.save(booking);
        Vehicle vehicle = vehicleService.findByName(booking.getVehicleName());
        if (vehicle != null) {
            vehicle.setAvailable(false);
            vehicleService.save(vehicle);  // Save updated vehicle
        }
        return "redirect:/user/bookings";  // After booking, go to user's bookings page
    }

    @GetMapping("/bookings")
    public String viewAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings(); // Fetch all bookings
        model.addAttribute("allBookings", bookings);
        return "adminBookings"; // points to bookings.html
    }


}
