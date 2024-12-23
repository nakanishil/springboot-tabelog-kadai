package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantService;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final CategoryService categoryService;
	private final RestaurantService restaurantService;
	
	public AdminRestaurantController(RestaurantRepository restaurantRepository, CategoryService categoryService,
			 RestaurantService restaurantService) {
		this.restaurantRepository = restaurantRepository;
		this.categoryService = categoryService;
		this.restaurantService = restaurantService;
	}
	
	@GetMapping
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)
	Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		Page<Restaurant> restaurantsPage;
		
		// キーワード検索
        if (keyword != null && !keyword.isEmpty()) {
            restaurantsPage = restaurantRepository.findByNameLike("%" + keyword + "%", pageable);                
        } else {
            restaurantsPage = restaurantRepository.findAll(pageable);
        }  
		
		model.addAttribute("restaurantsPage", restaurantsPage);
		model.addAttribute("totalElements", restaurantsPage.getTotalElements()); //総件数を渡す
		model.addAttribute("keyword", keyword);
		
		return "admin/restaurants/index";
	}
	
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        
        model.addAttribute("restaurant", restaurant);
        
        return "admin/restaurants/show";
    }    
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/restaurants/register";
    }    
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated RestaurantRegisterForm restaurantRegisterForm,
    		BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	if (bindingResult.hasErrors()) {
    		// カテゴリ情報を再設定
    		model.addAttribute("categories", categoryService.getAllCategories());
    		return "admin/restaurants/register";
    	}
    	
    	restaurantService.create(restaurantRegisterForm);
    	redirectAttributes.addFlashAttribute("successMessage", "飲食店を登録しました。");
    	
    	return "redirect:/admin/restaurants";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	String imageName = restaurant.getImageName();
    	// フォームクラスをインスタンス化する
    	RestaurantEditForm restaurantEditForm = new RestaurantEditForm(restaurant.getId(),restaurant.getName(),
    			null, restaurant.getBusinessHours(), restaurant.getRegularClosingDay(), restaurant.getPriceRange(),
    			null, restaurant.getNumOfSeats(),restaurant.getPhoneNumber(), restaurant.getPostalCode(), 
    			restaurant.getDescription(), restaurant.getAddress());
    			
    	
		    	// カテゴリ情報を再設定
				model.addAttribute("categories", categoryService.getAllCategories());
    			// 飲食店画像のファイル名をビューに渡す
    			model.addAttribute("imageName", imageName);
    			// 生成したインスタンスをビューに渡す
    			model.addAttribute("restaurantEditForm", restaurantEditForm);
    			
    			return "admin/restaurants/edit";
    	
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated RestaurantEditForm restaurantEditForm, BindingResult bindingResult,
    		RedirectAttributes redirectAttributes, Model model) {
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("categories", categoryService.getAllCategories());
    		return "admin/restaurants/edit";
    	}
    	
    	// 更新処理
    	restaurantService.update(restaurantEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "飲食店情報を編集しました。");
    	
    	return "redirect:/admin/restaurants";
    }
    
    @PostMapping("/{id}/delete")
    public String dalete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
    	restaurantRepository.deleteById(id);
    	
    	redirectAttributes.addFlashAttribute("successMessage", "飲食店を削除しました。");
    	
    	return "redirect:/admin/restaurants";
    }
    
    //リクエストが大きい場合（ファイルサイズ）のエラーをキャッチし、ユーザに表示する。
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("errorMessage", "ファイルサイズが大きすぎます。最大5MBまでアップロード可能です。");
            return "redirect:/admin/restaurants/register";
        }
    }
}
