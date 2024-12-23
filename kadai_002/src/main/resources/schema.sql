-- NAGOYAMESHI データベース

--roles 役職テーブル
CREATE TABLE IF NOT EXISTS roles (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, --役職番号
	name VARCHAR(50) NOT NULL --役職名
);

--users ユーザテーブル
CREATE TABLE IF NOT EXISTS users (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  --ユーザ番号
	mail_address VARCHAR(40) NOT NULL UNIQUE, --メールアドレス 重複×
	password VARCHAR(255) NOT NULL, --パスワード
	name VARCHAR(35) NOT NULL, --名前
	phone_number VARCHAR(30) NOT NULL, --電話番号
	postal_code VARCHAR(10) NOT NULL, --郵便番号
	address VARCHAR(100) NOT NULL, --住所
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, --更新日
	enabled BOOLEAN NOT NULL DEFAULT TRUE, --有効性
	role_id INT NOT NULL, --役職番号
	subscription_id VARCHAR(255), --サブスクリプションID
	FOREIGN KEY (role_id) REFERENCES roles(id)
);

--categories カテゴリテーブル
CREATE TABLE IF NOT EXISTS categories(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, --カテゴリ番号
	category_name VARCHAR(20) NOT NULL UNIQUE, --カテゴリ名 重複×
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP --更新日
);

--restaurants 飲食店テーブル
CREATE TABLE IF NOT EXISTS restaurants (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, --飲食店番号
	name VARCHAR(30) NOT NULL UNIQUE, --飲食店名 重複×
	business_hours VARCHAR(30) NOT NULL, --営業時間
	regular_closing_day VARCHAR(10) NOT NULL, --定休日
	price_range INT NOT NULL, --価格帯
	category_id INT NOT NULL, --カテゴリ番号
	num_of_seats INT NOT NULL, --席数
	phone_number VARCHAR(30) NOT NULL, --電話番号
	postal_code VARCHAR(10) NOT NULL, --郵便番号
	address VARCHAR(255) NOT NULL, --住所
	description TEXT NOT NULL, --店舗説明文
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, --更新日
	enabled BOOLEAN NOT NULL DEFAULT TRUE, --有効性
	image_name VARCHAR(255), --画像名
	FOREIGN KEY (category_id) REFERENCES categories(id)
);


--reviews レビューテーブル
CREATE TABLE IF NOT EXISTS reviews (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  --レビュー番号
	point INT NOT NULL CHECK (point BETWEEN 1 AND 5), --評価点数
	user_id INT NOT NULL, --ユーザ番号
	restaurant_id INT NOT NULL, --飲食店番号
	review_comment TEXT NOT NULL, --コメント
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, --更新日
	enabled BOOLEAN NOT NULL DEFAULT TRUE, --有効性
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
	UNIQUE (user_id, restaurant_id) --同店舗に２回目のコメントは編集のみ
);


-- favorites お気に入りテーブル
CREATE TABLE IF NOT EXISTS favorites (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, --お気に入り番号
	user_id INT NOT NULL, --ユーザ番号
	restaurant_id INT NOT NULL, --飲食店番号
	favorite_comment TEXT, --コメント 未入力可
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, --更新日
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
	UNIQUE (user_id, restaurant_id) --お気に入りを解除しないと登録できない
);

--resercations 飲食店予約テーブル
CREATE TABLE IF NOT EXISTS reservations (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL, --ユーザ番号
	restaurant_id INT NOT NULL, --飲食店番号
	appointment_date DATETIME NOT NULL,
	num_of_people INT NOT NULL, --希望人数
	amount INT, --最低価格
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, --作成日
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, --更新日
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

--メール認証用トークン
 CREATE TABLE IF NOT EXISTS verification_tokens (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     user_id INT NOT NULL UNIQUE,		--ユーザ番号
     token VARCHAR(255) NOT NULL,        --トークン
     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,	--作成日
     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,	--更新日
     FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
 );
 
-- パスワードリセット用トークン
 CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME,          -- 有効期限
    used BOOLEAN NOT NULL DEFAULT FALSE, -- すでに使用済みかどうか
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

