package com.example.nagoyameshi.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReservationInputForm {
	
    @NotNull(message = "日時を選択してください。")
    @Future(message = "日時は未来の日付を指定してください。")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime appointmentDate;
	
	@NotNull(message = "人数を入力してください。")
	@Min(value = 1, message = "人数は１人以上に設定してください。")
	private Integer numOfPeople;
}
