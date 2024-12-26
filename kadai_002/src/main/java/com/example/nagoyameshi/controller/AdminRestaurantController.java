package com.example.nagoyameshi.controller;

import java.io.File;  // 2024-12-25 SAMURAI
import java.io.IOException;
import java.net.URLEncoder;  // 2024-12-25 SAMURAI
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.nagoyameshi.csv.RestaurantCsvData;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantService;
import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.CsvEntityListHandler;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final CategoryService categoryService;
	private final RestaurantService restaurantService;
	
	// クラスの自動インスタンス化 2024-12-25 SAMURAI
		@Autowired
		private ModelMapper modelMapper; // EntityをCSV出力用のクラスにコピーするための仕掛け 2024-12-25 SAMURAI
	
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
    
	/* CSV出力用に変更 2024-12-25 SAMURAI */
	@GetMapping("/csv")
	public void showCsvPage(@RequestParam(name = "keyword2", required = false) String keyword,
			HttpServletResponse response) throws IOException {
		// CSV出力データリスト
		List<Restaurant> restaurantsList = null;

		/* キーワード検索ボックスの入力有無確認 */
		// 入力あり
		if (keyword != null && !keyword.isEmpty()) {
			// キーワードに部分一致するデータ取得
			restaurantsList = restaurantRepository.findByNameLike("%" + keyword + "%");

		}
		// 入力なし 
		else {
			// 全件取得
			restaurantsList = restaurantRepository.findAll();
		}

		/* CSV出力データ有無のチェック */
		// 出力データあり
		if (restaurantsList != null && restaurantsList.size() != 0) {
			try {
				/* EntityリストをCSV用データリストへ変換 */
				// CSVリスト
				List<RestaurantCsvData> csvDataList = new ArrayList<>();
				for (Restaurant restaurant : restaurantsList) {
					RestaurantCsvData restaurantCsvData = modelMapper.map(restaurant, RestaurantCsvData.class);
					csvDataList.add(restaurantCsvData);
				}
				/** ファイル名取得 */
				ResourceBundle rb = ResourceBundle.getBundle("nagoyameshi");
				// CSV保存用パス
				String csvpath = rb.getString("csv.dirpath");

				// CSVファイル名取得
				String filename = rb.getString("csv.filename.restaurants");

				// CSV出力用フォルダの作成
				File dir = new File(csvpath);
				dir.mkdir();

				// CSV出力情報作成
				csvpath = csvpath + filename;

				// 区切り文字と囲み文字、エスケープ文字を指定して CSV 形式設定情報を構築
				CsvConfig cfg = new CsvConfig(',', '"', '"');

				File fileout = new File(csvpath);
				String encode = "MS932";
				Csv.save(
						csvDataList,
						fileout,
						encode,
						cfg,
						new CsvEntityListHandler<RestaurantCsvData>(RestaurantCsvData.class));
			} catch (Exception e) {

			}
		}

		/* 呼び出し元の画面の状態を維持するための処理 */
		// キーワードをURLエンコードする(URLエンコードしないと全角文字はエラーとなる)
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
		String url = "/admin/restaurants?keyword=" + encodedKeyword;
		/* 
		 * 呼び出し元の画面にリダイレクト
		 * 今回は手っ取り早く会員一覧画面のキーワード検索ボックスの値を
		 * URLパラメータに設定し、会員一覧画面より検索処理を実施したように
		 * 処理することにより、CSVボタン押下時の状態を作り出す方法を選択
		 * 
		 */
		response.sendRedirect(url);
	}
}
