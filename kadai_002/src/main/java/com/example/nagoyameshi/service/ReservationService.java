package com.example.nagoyameshi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class ReservationService {
	
    private final ReservationRepository reservationRepository;  
    private final RestaurantRepository restaurantRepository;  
    private final UserRepository userRepository;  
    
    public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;  
        this.restaurantRepository = restaurantRepository;  
        this.userRepository = userRepository;  
    }    
    
    @Transactional
    public void create(ReservationRegisterForm reservationRegisterForm) { 
        Reservation reservation = new Reservation();
        Restaurant restaurant = restaurantRepository.getReferenceById(reservationRegisterForm.getRestaurantId());
        User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
        LocalDateTime appointmentDate =reservationRegisterForm.getAppointmentDate();
                
        reservation.setRestaurant(restaurant);
        reservation.setUser(user);
        reservation.setAppointmentDate(appointmentDate);
        reservation.setNumOfPeople(reservationRegisterForm.getNumOfPeople());
        reservation.setAmount(reservationRegisterForm.getAmount());
        
        reservationRepository.save(reservation);
    }    
	// 人数が席数以下かどうかをチェックする
	public boolean isWithinCapacity(Integer numOfPeople, Integer numOfSeats) {
		return numOfPeople <= numOfSeats;
	}
	
	// 最低価格を計算する
	public Integer calculateAmount(Integer numOfPeople, Integer priceRange) {
		int amount = priceRange * (int)numOfPeople;
		return amount;
	}
}
