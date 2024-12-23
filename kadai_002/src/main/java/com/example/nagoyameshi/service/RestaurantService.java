package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Service
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final CategoryRepository categoryRepository;
	
    // コンストラクタでリポジトリを注入
    public RestaurantService(RestaurantRepository restaurantRepository,
    		CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }
	
    // 飲食店登録
	@Transactional
    public void create(RestaurantRegisterForm restaurantRegisterForm) {
        Restaurant restaurant = new Restaurant();        
        MultipartFile imageFile = restaurantRegisterForm.getImageFile();
        
        // カテゴリIDからcategoryを取得し設定
        Integer categoryId = restaurantRegisterForm.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
        		.orElseThrow(() -> new IllegalArgumentException("カテゴリIDが無効です：" + categoryId));
        restaurant.setCategory(category);
        
        if(!imageFile.isEmpty()) {
        	String imageName = imageFile.getOriginalFilename();
        	String hashedImageName = generateNewFileName(imageName);
        	Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
        	copyImageFile(imageFile, filePath);
        	restaurant.setImageName(hashedImageName);
        }
        
        restaurant.setName(restaurantRegisterForm.getName());
        restaurant.setBusinessHours(restaurantRegisterForm.getBusinessHours());
        restaurant.setRegularClosingDay(restaurantRegisterForm.getRegularClosingDay());
        restaurant.setPriceRange(restaurantRegisterForm.getPriceRange());
        restaurant.setNumOfSeats(restaurantRegisterForm.getNumOfSeats());
        restaurant.setPhoneNumber(restaurantRegisterForm.getPhoneNumber());
        restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
        restaurant.setAddress(restaurantRegisterForm.getAddress());
        restaurant.setDescription(restaurantRegisterForm.getDescription());
        
        restaurantRepository.save(restaurant);
	}
	
	// 飲食店更新
	@Transactional
	public void update(RestaurantEditForm restaurantEditForm) {
		//getReferenceById()は特定のIDに基づいてエンティティの参照を取得するために使用される
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantEditForm.getId());
		MultipartFile imageFile = restaurantEditForm.getImageFile();
		
        // カテゴリIDからcategoryを取得し設定
        Integer categoryId = restaurantEditForm.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
        		.orElseThrow(() -> new IllegalArgumentException("カテゴリIDが無効です：" + categoryId));
        restaurant.setCategory(category);
		
        // 画像ファイルがアップロードされた場合に画像を保存し、保存先のファイル名をエンティティに設定する
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile,filePath);
			restaurant.setImageName(hashedImageName);
		}
		
        restaurant.setName(restaurantEditForm.getName());
        restaurant.setBusinessHours(restaurantEditForm.getBusinessHours());
        restaurant.setRegularClosingDay(restaurantEditForm.getRegularClosingDay());
        restaurant.setPriceRange(restaurantEditForm.getPriceRange());
        restaurant.setNumOfSeats(restaurantEditForm.getNumOfSeats());
        restaurant.setPhoneNumber(restaurantEditForm.getPhoneNumber());
        restaurant.setPostalCode(restaurantEditForm.getPostalCode());
        restaurant.setAddress(restaurantEditForm.getAddress());
        restaurant.setDescription(restaurantEditForm.getDescription());
		
		restaurantRepository.save(restaurant);
	}
	
	// UUIDを使用して生成したファイル名を返す（ファイル名が重複しないように）
	public String generateNewFileName(String fileName) {
		// 渡された元のファイル名fileNameをドット.で分割
		String[] fileNames = fileName.split("\\.");
		//ファイル名部分（拡張子以外）を新しい名前に置き換える
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		// 分割していた配列を再びドット.で結合　
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}
	
	// 画像ファイルを指定パスにコピーする
	/* MultipartFIle imageFile HTTPリクエストからアップロードされたファイルを表すオブジェクト
	 メソッドを通じて、アップロードされたファイルの内容やメタ情報（ファイル名、サイズなど）にアクセスできる */
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			/* javaのFile.copyメソッドを使用してimageFileの中身を指定されたパスにコピーする。
			 * imageFile.getInputStream() アップロードされたファイルの内容をストリーム形式で取得*/
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			throw new RuntimeException("画像のアップロードに失敗しました。", e);
		}
	}
	
	@Transactional(readOnly = true)
	public Optional<Restaurant> findById(Integer id) {
	    return restaurantRepository.findById(id);
	}
}
