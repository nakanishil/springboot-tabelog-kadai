package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReservationService reservationService;
	
	public ReservationController(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.reservationService = reservationService;
	}
	
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		
		
		return "reservations/index";
	}
	
	@PostMapping("/restaurants/{id}/reservations/input")
	public String input(@PathVariable(name = "id") Integer id,
						@ModelAttribute @Validated ReservationInputForm reservationInputForm,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		// デバッグ出力
		System.out.println("appointmentDate: " + reservationInputForm.getAppointmentDate());
		System.out.println("numOfPeople: " + reservationInputForm.getNumOfPeople());


		
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Integer numOfPeople = reservationInputForm.getNumOfPeople();
		Integer numOfSeats = restaurant.getNumOfSeats();
		
		if (numOfPeople != null) {
			if (!reservationService.isWithinCapacity(numOfPeople, numOfSeats)) {
				FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numOfPeople", "予約人数が定員を超えています。");
				bindingResult.addError(fieldError);
			}
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurant", restaurant);
			model.addAttribute("errorMessage", "予約内容に不備があります。");
			bindingResult.getFieldErrors().forEach(error -> {
			    System.out.println("Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
			});

			return "restaurants/show";
		}
		

		
		redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);

		
		return "redirect:/restaurants/{id}/reservations/confirm";

	}
	
	@GetMapping("/restaurants/{id}/reservations/confirm")
	public String confirm(@PathVariable(name = "id") Integer id,
						  @ModelAttribute ReservationInputForm reservationInputForm,
						  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						  Model model) {
		
		System.out.println("appointmentDate in confirm: " + reservationInputForm.getAppointmentDate());
		System.out.println("numOfPeople in confirm: " + reservationInputForm.getNumOfPeople());

		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		// 予約日の取得
		LocalDateTime appointmentDate = reservationInputForm.getAppointmentDate();
		
		//料金の計算
		Integer numOfPeople = reservationInputForm.getNumOfPeople();
		Integer numOfSeats = restaurant.getNumOfSeats();
		Integer priceRange = restaurant.getPriceRange();
		Integer amount = reservationService.calculateAmount(numOfPeople, priceRange);
		
		ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(), appointmentDate,
				reservationInputForm.getNumOfPeople(), amount);
		
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reservationRegisterForm", reservationRegisterForm);
		
		return "reservations/confirm";
	}
	
//	// show.htmlにてappointmentDateの値をLocalDateTimeに自動変換する
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//	    binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
//	        @Override
//	        public void setAsText(String text) {
//	            try {
//	            	// "yyyy-MM-dd'T'HH:mm"に変更して'T'を含むフォーマットに合わせる
//	            	setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
//	            } catch (Exception e) {
//	                setValue(null); // 無効なフォーマットの場合はnullを設定
//	            }
//	        }
//	    });
//	}
	
    @PostMapping("/restaurants/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {    
        // ReservationRegisterFormの内容をログに出力
        System.out.println("Reservation Register Form: " + reservationRegisterForm);
        System.out.println("Restaurant ID: " + reservationRegisterForm.getRestaurantId());
        System.out.println("User ID: " + reservationRegisterForm.getUserId());
        System.out.println("Appointment Date: " + reservationRegisterForm.getAppointmentDate());
        System.out.println("Num of People: " + reservationRegisterForm.getNumOfPeople());

        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    // 予約キャンセル機能
    @PostMapping("/reservations/{id}/cancel")
    public String cancel(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            RedirectAttributes redirectAttributes
    ) {
        // 1. idに対応する予約データを取得
        //    見つからない場合は例外が発生するか、nullになるかの対応
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation == null) {
            // 該当の予約が見つからない場合は一覧にリダイレクトするなど
            redirectAttributes.addFlashAttribute("error", "予約が見つかりませんでした。");
            return "redirect:/reservations";
        }

        // 2. ログイン中のユーザーかどうかを確認
        //    (予約したユーザー本人でなければキャンセル不可)
        User user = userDetailsImpl.getUser();
        if (!reservation.getUser().getId().equals(user.getId())) {
            // 権限がない場合
            redirectAttributes.addFlashAttribute("error", "他のユーザーの予約はキャンセルできません。");
            return "redirect:/reservations";
        }

        // 3. 予約を物理削除
        reservationRepository.delete(reservation);

        // 4. メッセージをフラッシュスコープに設定し、予約一覧にリダイレクト
        redirectAttributes.addFlashAttribute("cancelled", true);
        return "redirect:/reservations";
    }



}
