package com.example.basiclogin.Service;

import com.example.basiclogin.Model.Booking;
import com.example.basiclogin.Model.User;
import com.example.basiclogin.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }
    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public void updateBooking(Booking updatedBooking) {
        Booking existingBooking = findBookingById(updatedBooking.getId());

        existingBooking.setVehicleName(updatedBooking.getVehicleName());
        existingBooking.setBookingDate(updatedBooking.getBookingDate());

        bookingRepository.save(existingBooking);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
