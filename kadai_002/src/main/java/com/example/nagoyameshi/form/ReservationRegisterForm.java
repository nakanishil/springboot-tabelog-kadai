package com.example.nagoyameshi.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRegisterForm {
	private Integer restaurantId;
	
	private Integer userId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime appointmentDate;
	
	private Integer numOfPeople;
	
	private Integer amount;
}