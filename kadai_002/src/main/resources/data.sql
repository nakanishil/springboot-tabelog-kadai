--役職テーブルへの追加
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_PAYING_MEMBER'),
(3, 'ROLE_FREE_MEMBER');


--ユーザテーブルへの追加
INSERT IGNORE INTO users (id, mail_address, password, name, phone_number, postal_code, address, created_at, updated_at, enabled, role_id) VALUES
(1, 'admin1@example.com', '$2a$10$XOB9GcHnySFkr3uC3xr3E.y7vpbC21HdTMoyPUkVpwTPzGsijf3/m', '管理者1', '08012345678', '123-4567', '東京都千代田区1-1-1', NOW(), NOW(), TRUE, 1),
(2, 'admin2@example.com', '$2a$10$XOB9GcHnySFkr3uC3xr3E.y7vpbC21HdTMoyPUkVpwTPzGsijf3/m', '管理者2', '08012345679', '234-5678', '東京都千代田区1-1-2', NOW(), NOW(), TRUE, 1),
(3, 'paid@example.com', '$2a$10$SBeglu93VKRrokevVCnA1OdHbahcjh0aKhlA.9rGaq9iRcX0RJTYK', '有料会員1', '09012345678', '345-6789', '東京都渋谷区1-1-1', NOW(), NOW(), TRUE, 2),
(4, 'free@example.com', '$2a$10$fMFL4zDs2axUMuY9T4w48uLRWQWO5QbG/hdsAA.JA016XO2A4sRH6', '無料会員1', '09012345679', '456-7890', '東京都新宿区1-1-1', NOW(), NOW(), TRUE, 3);

--カテゴリテーブルへの追加
INSERT IGNORE INTO categories (id, category_name) VALUES
(1, 'レストラン'),
(2, '料亭'),
(3, '居酒屋'),
(4, 'カフェ・喫茶店'),
(5, '食堂'),
(6, '焼肉'),
(7, '和食'),
(8, '洋食'),
(9, '中華'),
(10, 'イタリアン'),
(11, 'スイーツ');

--飲食店テーブルへの追加
INSERT IGNORE INTO restaurants (id, name, business_hours, regular_closing_day, price_range, category_id, num_of_seats, phone_number, postal_code, address, description, created_at, updated_at, enabled, image_name) VALUES
(1, '味噌カツパラダイス', '11:00-22:00', '月曜日', 700, 7, 40, '052-111-2222', '460-0008', '名古屋市中区栄1-2-3', '当店の味噌カツは名古屋伝統の甘味噌を使用し、独特のコクと旨味を引き出した逸品です。地元のお客様から観光客まで多くの方に愛され、一口食べればその魅力にハマること間違いなし。ぜひご賞味ください。', NOW(), NOW(), TRUE, 'restaurant(1).jpg'),
(2, '台湾ラーメン天国', '12:00-02:00', '火曜日', 500, 9, 30, '052-123-4567', '460-0003', '名古屋市中区錦2-4-5', '当店は名古屋発祥の台湾ラーメンを深夜まで提供しています。スパイシーなスープに肉味噌とニラを合わせた熱々の一杯は、寒い夜にもピッタリ。地元サラリーマンや観光客が〆の一杯に訪れる人気店です。', NOW(), NOW(), TRUE, 'restaurant(2).jpg'),
(3, '喫茶ひつまぶしモーニング', '07:00-18:00', '日曜日', 300, 4, 20, '052-987-6543', '460-0004', '名古屋市昭和区丸の内3-5-1', '名古屋文化が詰まった当店では、ひつまぶしとモーニングを融合させた独特のメニューを朝から提供。小倉トーストや鉄板スパゲッティなど名物メニューも多数揃えています。観光の始まりや一息入れたい時にぜひお立ち寄りください。', NOW(), NOW(), TRUE, 'restaurant(3).jpg'),
(4, 'どて煮横丁', '10:00-23:00', '水曜日', 600, 5, 50, '052-245-0011', '460-0005', '名古屋市中区丸の内4-2-10', '名物どて煮は甘い味噌の香り漂うホルモン煮込み。昼はビジネスマン、夜は観光客で賑わう下町食堂です。', NOW(), NOW(), TRUE, 'restaurant(4).jpg'),
(5, 'きしめん路地裏屋', '09:00-20:00', 'なし', 400, 7, 30, '052-345-0002', '460-0006', '名古屋市中区南椿町1-7', '平打ち麺が自慢のきしめんは鰹だしが効いた昔ながらの味。路地裏の小さな和食店で地元民から愛されています。', NOW(), NOW(), TRUE, 'restaurant(5).jpg'),
(6, '鉄板スパゲッティ楽園', '11:00-21:00', '金曜日', 500, 8, 35, '052-456-1122', '460-0007', '名古屋市中区新栄3-4-2', '熱々鉄板に絡むバターとスパゲッティ、懐かしさ溢れる洋食店で至福の一皿をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(6).jpg'),
(7, '味噌煮込みうどん大陸', '10:00-22:00', '月曜日', 800, 7, 45, '052-363-2211', '460-0001', '名古屋市中区橘1-3-5', '濃厚な赤味噌仕立ての味噌煮込みうどんが看板メニュー。コシの強い麺と旨み溢れるスープが自慢。', NOW(), NOW(), TRUE, 'restaurant(7).jpg'),
(8, '味噌おでん名人', '09:00-19:00', '木曜日', 400, 3, 25, '052-443-3322', '460-0002', '名古屋市中区葵2-7-1', 'とろとろ大根と濃厚味噌だれが絶品の味噌おでん。カウンター越しの女将との会話も魅力です。', NOW(), NOW(), TRUE, 'restaurant(8).jpg'),
(9, 'カレーうどん劇場', '10:00-22:00', '日曜日', 600, 9, 40, '052-455-2233', '460-0009', '名古屋市中区錦3-1-10', 'スパイス香るカレーうどんが人気。刺激的な味わいで昼夜問わず人が集まります。', NOW(), NOW(), TRUE, 'restaurant(9).jpg'),
(10, '手羽先ソウルフード', '17:00-02:00', '月曜日', 500, 3, 60, '052-500-0033', '460-0010', '名古屋市中区大須3-11-5', 'パリッと揚げた手羽先を秘伝のタレで味わい尽くす居酒屋。ビール片手に名古屋の夜を楽しんで。', NOW(), NOW(), TRUE, NULL),
(11, 'ひつまぶし炭火庵', '11:00-21:00', '水曜日', 1200, 7, 40, '052-514-0004', '460-0011', '名古屋市中区栄5-9-2', '香ばしく焼いた鰻を贅沢に使用したひつまぶし。炭火の香りと共にゆったりとした和空間で味わえます。', NOW(), NOW(), TRUE, 'restaurant(11).jpg'),
(12, '小倉トースト甘味屋', '08:00-18:00', 'なし', 300, 11, 20, '052-621-1212', '460-0012', '名古屋市中区千代田2-1-5', '小倉トーストを朝から楽しめる甘味処。控えめな甘さの小倉あんがパンに染み込み至福の味わい。', NOW(), NOW(), TRUE, 'restaurant(12).jpg'),
(13, '味噌カツバーガー工房', '10:00-20:00', '火曜日', 500, 8, 28, '052-332-0005', '460-0013', '名古屋市中区富士見1-2-3', 'パンに挟んだ味噌カツが新感覚！濃厚ソースが食欲をそそる洋食バーガーショップです。', NOW(), NOW(), TRUE, 'restaurant(13).jpg'),
(14, 'どて丼本舗', '11:00-21:00', '木曜日', 600, 5, 36, '052-877-5412', '460-0014', '名古屋市中区正木4-5-6', '甘辛味噌で煮込んだホルモンをご飯にたっぷりかけたどて丼が大人気。腹ペコさんいらっしゃい！', NOW(), NOW(), TRUE, 'restaurant(14).jpg'),
(15, '味噌煮込みパスタ庵', '09:00-21:00', '金曜日', 700, 8, 24, '052-873-4212', '460-0015', '名古屋市中区大井町2-3-4', '赤味噌クリームソースのパスタは和洋折衷の一品。意外な組み合わせにリピーター続出！', NOW(), NOW(), TRUE, 'restaurant(15).jpg'),
(16, '台湾まぜそば極楽', '12:00-23:00', '月曜日', 700, 9, 32, '052-333-5566', '460-0016', '名古屋市中区栄生2-1-8', '濃厚なタレとニラ、挽肉が絡む台湾まぜそば。締めの追い飯まで味の変化を楽しめます。', NOW(), NOW(), TRUE, 'restaurant(16).jpg'),
(17, '名古屋味噌グリル', '11:00-22:00', 'なし', 900, 1, 45, '052-441-2244', '460-0017', '名古屋市中区上前津3-4-3', '厳選食材と名古屋味噌が融合した創作レストラン。地元の伝統をモダンに昇華させた一皿を。', NOW(), NOW(), TRUE, 'restaurant(17).jpg'),
(18, '味噌串カツ横丁', '17:00-01:00', '火曜日', 500, 3, 38, '052-456-2211', '460-0018', '名古屋市中区東桜2-3-10', '串カツを甘い味噌ダレで楽しむ居酒屋。下町情緒溢れる賑やかな雰囲気が食欲を刺激します。', NOW(), NOW(), TRUE, 'restaurant(18).jpg'),
(19, '味噌鍋シチュー亭', '10:00-20:00', '水曜日', 800, 8, 26, '052-312-7712', '460-0019', '名古屋市中区栄町1-1-1', '赤味噌仕立てのシチューは濃厚で深みのある味わい。パンを浸して召し上がれ。', NOW(), NOW(), TRUE, 'restaurant(19).jpg'),
(20, '手羽先バルCHU', '16:00-01:00', '日曜日', 600, 3, 50, '052-555-8888', '460-0020', '名古屋市中区金山5-2-4', '揚げたて手羽先とワインが楽しめるバルスタイル居酒屋。女子会やデートにも最適です。', NOW(), NOW(), TRUE, NULL),
(21, '味噌煮込みカフェMISO-Morning', '07:00-17:00', 'なし', 400, 4, 18, '052-698-1200', '460-0021', '名古屋市中区平和2-6-2', '朝から味噌煮込みうどんが味わえるカフェ。小倉トーストもあり名古屋文化を満喫！', NOW(), NOW(), TRUE, 'restaurant(21).jpg'),
(22, 'ひつまぶしガーデン', '11:00-22:00', '金曜日', 1300, 7, 35, '052-678-4444', '460-0022', '名古屋市中区松原3-5-6', '炭火焼き鰻たっぷりのひつまぶしが自慢。庭園風店内で贅沢なひと時を。', NOW(), NOW(), TRUE, 'restaurant(22).jpg'),
(23, 'きしめんスタンドNAGO', '09:00-19:00', '月曜日', 400, 5, 22, '052-665-4411', '460-0023', '名古屋市中区古渡町1-2-3', '駅近で手軽にきしめんを楽しめるスタンド食堂。忙しい朝やランチに最適です。', NOW(), NOW(), TRUE, 'restaurant(23).jpg'),
(24, '台湾ラーメンBar一麺', '18:00-03:00', '木曜日', 600, 9, 28, '052-113-2222', '460-0024', '名古屋市中区橦木町4-5-2', '深夜営業の台湾ラーメンバー。辛味がクセになる一杯とお酒で夜を締めくくろう。', NOW(), NOW(), TRUE, 'restaurant(24).jpg'),
(25, '味噌カツデリシャス', '10:00-22:00', 'なし', 700, 7, 40, '052-841-5123', '460-0025', '名古屋市中区伊勢山2-4-8', '昔ながらの味噌カツが旨い老舗。家族連れでほっこりする和食店。', NOW(), NOW(), TRUE, 'restaurant(25).jpg'),
(26, 'ひつまぶし洋風アレンジ', '11:00-20:00', '火曜日', 1100, 8, 30, '052-333-4444', '460-0026', '名古屋市中区金屋町3-2-1', 'ひつまぶしを洋風にアレンジ！バターライスと鰻、白ワインが織りなす新感覚の味。', NOW(), NOW(), TRUE, 'restaurant(26).jpg'),
(27, '味噌おでん昭和横丁', '16:00-00:00', '水曜日', 400, 3, 32, '052-987-1111', '460-0027', '名古屋市中区平田町5-6-7', '昭和レトロな雰囲気で味噌おでんを味わう居酒屋。心が和む一杯をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(27).jpg'),
(28, '台湾まぜそば屋台', '12:00-23:00', '日曜日', 500, 9, 25, '052-661-1234', '460-0028', '名古屋市中区高岳1-8-9', '屋台風の店先で台湾まぜそばを楽しむ臨場感。ニラと挽肉の旨みが麺に絡みます。', NOW(), NOW(), TRUE, 'restaurant(28).jpg'),
(29, 'きしめんBAR Udonista', '17:00-00:00', '月曜日', 500, 3, 28, '052-978-2223', '460-0029', '名古屋市中区竹橋町2-4', 'きしめんをつまみにお酒を楽しむ新感覚バー。大人の隠れ家です。', NOW(), NOW(), TRUE, 'restaurant(29).jpg'),
(30, '味噌カツラボ', '11:00-22:00', '金曜日', 800, 7, 50, '052-555-9999', '460-0030', '名古屋市中区江川1-2-3', '味噌カツを究極まで追求する研究所的食堂。独自ブレンドの味噌ダレにファン急増中。', NOW(), NOW(), TRUE, NULL),
(31, 'どて煮食堂まるた', '10:00-20:00', '火曜日', 500, 5, 34, '052-844-1112', '460-0031', '名古屋市中区千種2-1-1', 'じっくり煮込んだどて煮がご飯にピッタリ。地元リピーター多数の定食屋。', NOW(), NOW(), TRUE, 'restaurant(31).jpg'),
(32, '手羽先ガストロノミー', '17:00-23:00', '水曜日', 900, 1, 40, '052-777-5555', '460-0032', '名古屋市中区鶴舞1-3-2', '手羽先をフレンチ風に仕上げた創作レストラン。高級感漂う特別な一皿。', NOW(), NOW(), TRUE, 'restaurant(32).jpg'),
(33, 'ひつまぶし喫茶', '07:00-16:00', 'なし', 1000, 4, 20, '052-333-5656', '460-0033', '名古屋市中区春日井町3-4-5', '朝からひつまぶしを提供する不思議な喫茶店。コーヒーと鰻の意外なマッチングをお楽しみください。', NOW(), NOW(), TRUE, 'restaurant(33).jpg'),
(34, '味噌煮込みファクトリー', '10:00-22:00', '木曜日', 700, 7, 44, '052-987-4567', '460-0034', '名古屋市中区松原町4-6-8', '自家製味噌にこだわる味噌煮込みうどん専門店。コシの強い麺が魅力。', NOW(), NOW(), TRUE, 'restaurant(34).jpg'),
(35, '味噌カツ&ワインバル', '17:00-00:00', '日曜日', 800, 3, 30, '052-223-1144', '460-0035', '名古屋市中区島田1-2-3', '味噌カツとワインの新提案。ソムリエがワイン選びをサポートする大人の居酒屋。', NOW(), NOW(), TRUE, 'restaurant(35).jpg'),
(36, '台湾ラーメンモダン', '11:00-23:00', '月曜日', 600, 9, 36, '052-963-7744', '460-0036', '名古屋市中区正和町2-2-2', '洗練された空間で味わう台湾ラーメンは辛さと旨味が絶妙。深夜まで賑わいます。', NOW(), NOW(), TRUE, 'restaurant(36).jpg'),
(37, 'きしめんカフェNishiki', '08:00-17:00', '金曜日', 400, 4, 18, '052-124-6677', '460-0037', '名古屋市中区錦町1-9-1', '朝はきしめん、午後はスイーツまで楽しめる喫茶店。名古屋文化を一日中堪能。', NOW(), NOW(), TRUE, 'restaurant(37).jpg'),
(38, '味噌豚カツ食堂', '10:00-22:00', '火曜日', 600, 5, 40, '052-287-2222', '460-0038', '名古屋市中区大井戸1-2-4', '豚カツを赤味噌で煮込んだ定食が人気。ご飯との相性抜群で学生に大好評。', NOW(), NOW(), TRUE, 'restaurant(38).jpg'),
(39, '台湾まぜそばDINER', '12:00-02:00', '水曜日', 700, 9, 28, '052-454-9990', '460-0039', '名古屋市中区神楽町3-4-5', '深夜まで営業のダイナーで台湾まぜそばを。追い飯で最後まで旨味堪能。', NOW(), NOW(), TRUE, 'restaurant(39).jpg'),
(40, '味噌おでんバル', '17:00-23:00', 'なし', 500, 3, 34, '052-998-7777', '460-0040', '名古屋市中区大須2-1-9', 'モダンなバル空間で味噌おでんをつまみにクラフトビールを。若者にも人気。', NOW(), NOW(), TRUE, NULL),
(41, '鉄板スパGO', '11:00-20:00', '木曜日', 500, 8, 22, '052-331-1212', '460-0041', '名古屋市中区鶯谷1-3-3', '熱々鉄板スパゲッティが人気の洋食店。昭和レトロな味で心も温まる。', NOW(), NOW(), TRUE, 'restaurant(41).jpg'),
(42, '味噌天ぷら庵', '11:00-21:00', '月曜日', 800, 7, 38, '052-454-1234', '460-0042', '名古屋市中区久屋町4-5-6', '味噌を活かした天ぷらが絶品。上品な味わいで大人の和食時間をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(42).jpg'),
(43, '手羽先エスプレッソ', '09:00-18:00', '金曜日', 400, 4, 16, '052-777-3333', '460-0043', '名古屋市中区花園3-2-1', '手羽先とエスプレッソの奇妙な組み合わせが話題。新感覚のカフェ体験を。', NOW(), NOW(), TRUE, 'restaurant(43).jpg'),
(44, '味噌煮込みリストランテ', '17:00-23:00', '日曜日', 1000, 10, 30, '052-888-4444', '460-0044', '名古屋市中区柳橋1-4-2', 'イタリアンと味噌煮込みうどんが融合！赤味噌ソースと生パスタで新たな美味しさ発見。', NOW(), NOW(), TRUE, 'restaurant(44).jpg'),
(45, 'ひつまぶしドルチェ', '11:00-19:00', '火曜日', 1200, 11, 25, '052-669-1233', '460-0045', '名古屋市中区正成町2-1-4', 'スイーツとひつまぶしのコースが楽しめる新感覚スイーツ店。甘味と鰻の絶妙調和。', NOW(), NOW(), TRUE, 'restaurant(45).jpg'),
(46, '手羽先カレー屋', '10:00-20:00', '水曜日', 600, 5, 28, '052-333-2211', '460-0046', '名古屋市中区葵南1-2-3', 'カレーうどんに手羽先トッピング！ボリューム満点で癖になる一皿。', NOW(), NOW(), TRUE, 'restaurant(46).jpg'),
(47, 'どて煮ビストロ', '17:00-22:00', '木曜日', 800, 1, 35, '052-114-8899', '460-0047', '名古屋市中区若葉町4-4-4', 'どて煮をフレンチテイストに仕上げたビストロ。ワインと共にお楽しみください。', NOW(), NOW(), TRUE, 'restaurant(47).jpg'),
(48, 'きしめんピッツェリア', '11:00-21:00', '金曜日', 900, 10, 30, '052-556-1122', '460-0048', '名古屋市中区千種町5-5-5', 'きしめん生地を使ったピザが名物。もちもち食感とチーズのコラボが絶妙。', NOW(), NOW(), TRUE, 'restaurant(48).jpg'),
(49, '味噌カツカフェMisoBre', '09:00-17:00', '月曜日', 500, 4, 20, '052-777-6666', '460-0049', '名古屋市中区泉2-3-7', '味噌カツサンドとコーヒーを楽しむカフェ。朝から名古屋味を満喫できます。', NOW(), NOW(), TRUE, 'restaurant(49).jpg'),
(50, '台湾ラーメンスイーツ', '10:00-19:00', 'なし', 600, 11, 18, '052-444-7777', '460-0050', '名古屋市中区紫陽花1-6-2', '辛い台湾ラーメンと甘いスイーツを同時提供する挑戦的なお店。味覚の冒険へようこそ。', NOW(), NOW(), TRUE, NULL),
(51, 'ひつまぶしGarden茶寮', '11:00-21:00', '火曜日', 1300, 7, 36, '052-514-2255', '460-0051', '名古屋市中区松重町2-2-2', '静かな和空間で味わう本格ひつまぶし。丁寧な仕事が鰻の旨さを引き立てます。', NOW(), NOW(), TRUE, 'restaurant(51).jpg'),
(52, '味噌おでんCafe', '09:00-18:00', '水曜日', 400, 4, 16, '052-858-9966', '460-0052', '名古屋市中区新栄西3-1-1', 'ほっと和むカフェで味噌おでんと和スイーツを。心休まるひと時をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(52).jpg'),
(53, '台湾まぜそば洋食堂', '11:00-20:00', '木曜日', 700, 8, 25, '052-741-3333', '460-0053', '名古屋市中区橘北2-4-9', '台湾まぜそばを洋風にアレンジ！トマトソースとチーズが新たなハーモニーを生む。', NOW(), NOW(), TRUE, 'restaurant(53).jpg'),
(54, '手羽先イタリアーノ', '17:00-23:00', 'なし', 800, 10, 30, '052-999-0001', '460-0054', '名古屋市中区矢場町1-2-3', '手羽先をガーリックバターで味わうイタリアンバル。ワインが進む一皿。', NOW(), NOW(), TRUE, 'restaurant(54).jpg'),
(55, 'きしめんパンケーキ', '08:00-16:00', '金曜日', 500, 11, 16, '052-777-9191', '460-0055', '名古屋市中区栄東2-8-8', 'きしめん粉で作ったパンケーキが話題。もちもち食感が癖になるスイーツ店。', NOW(), NOW(), TRUE, 'restaurant(55).jpg'),
(56, '味噌カツDining和', '10:00-22:00', '月曜日', 700, 7, 40, '052-611-4488', '460-0056', '名古屋市中区葵西1-4-3', '上質な味噌カツを和モダンな空間で。丁寧な接客でおもてなし。', NOW(), NOW(), TRUE, 'restaurant(56).jpg'),
(57, 'ひつまぶしBrunch', '09:00-14:00', '日曜日', 1200, 4, 24, '052-100-3232', '460-0057', '名古屋市中区丸の内東3-5-7', 'ブランチタイムにひつまぶしを楽しむ喫茶店。贅沢な朝をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(57).jpg'),
(58, 'どて煮ビュッフェ', '11:00-20:00', '火曜日', 600, 5, 42, '052-321-5550', '460-0058', '名古屋市中区伏見1-1-5', 'どて煮を好きなだけ楽しめるビュッフェ。ご飯が進む止まらない！', NOW(), NOW(), TRUE, 'restaurant(58).jpg'),
(59, '台湾ラーメンTeppan', '12:00-23:00', '水曜日', 600, 9, 28, '052-697-2255', '460-0059', '名古屋市中区鶴舞南2-6-2', '鉄板仕上げの台湾ラーメンは香ばしさ倍増。ライブ感ある調理が人気。', NOW(), NOW(), TRUE, 'restaurant(59).jpg'),
(60, '手羽先ハウス', '16:00-00:00', '木曜日', 500, 3, 40, '052-414-7770', '460-0060', '名古屋市中区鍋屋上野町3-2-1', '手羽先専門店でビールと共に乾杯！地元の定番居酒屋で一息。', NOW(), NOW(), TRUE, NULL),
(61, '味噌鍋カウンター', '10:00-21:00', 'なし', 700, 7, 22, '052-888-2222', '460-0061', '名古屋市中区上前津西1-9-3', 'カウンター越しで味噌鍋をゆっくり楽しむ和食店。1人利用も大歓迎。', NOW(), NOW(), TRUE, 'restaurant(61).jpg'),
(62, 'ひつまぶし大人食堂', '11:00-22:00', '金曜日', 1300, 5, 30, '052-741-7772', '460-0062', '名古屋市中区中川端2-4-6', '落ち着いた空間で贅沢なひつまぶしを。特別な日のディナーにどうぞ。', NOW(), NOW(), TRUE, 'restaurant(62).jpg'),
(63, '味噌おでんギャラリー', '09:00-17:00', '月曜日', 400, 4, 20, '052-531-9991', '460-0063', '名古屋市中区西日置1-3-4', 'アート作品と味噌おでんが楽しめるギャラリーカフェ。五感で味わう新体験。', NOW(), NOW(), TRUE, 'restaurant(63).jpg'),
(64, '台湾まぜそば斬新', '11:00-23:00', '火曜日', 600, 9, 32, '052-888-5599', '460-0064', '名古屋市中区熱田町5-4-3', '独自ブレンドソースで味わう台湾まぜそば。刺激的な味が若者を惹きつける。', NOW(), NOW(), TRUE, 'restaurant(64).jpg'),
(65, 'きしめん茶房', '07:00-15:00', '水曜日', 300, 4, 18, '052-852-3300', '460-0065', '名古屋市中区大津町2-8-8', '朝食にきしめんが人気の茶房。静かな朝をゆったりと。', NOW(), NOW(), TRUE, 'restaurant(65).jpg'),
(66, '味噌カツクラシック', '10:00-20:00', '木曜日', 600, 7, 28, '052-333-9955', '460-0066', '名古屋市中区松原東1-3-2', '昔懐かしい味噌カツを守り続ける老舗。変わらぬ味が心を和ませる。', NOW(), NOW(), TRUE, 'restaurant(66).jpg'),
(67, 'ひつまぶしAperitivo', '17:00-22:00', '日曜日', 1200, 1, 26, '052-114-7774', '460-0067', '名古屋市中区白鳥1-2-5', 'アペリティーボにひつまぶしとシャンパンを組み合わせた贅沢レストラン。大人の嗜みを。', NOW(), NOW(), TRUE, 'restaurant(67).jpg'),
(68, '味噌おでんandスコーン', '08:00-16:00', '月曜日', 400, 11, 20, '052-414-5656', '460-0068', '名古屋市中区白川町4-4-4', '味噌おでんとスコーンの奇妙な組合せが話題のスイーツ店。不思議な甘辛ハーモニー。', NOW(), NOW(), TRUE, 'restaurant(68).jpg'),
(69, '台湾まぜそばPub', '18:00-02:00', 'なし', 700, 3, 30, '052-999-4141', '460-0069', '名古屋市中区神宮西2-6-1', 'お酒を飲みつつ台湾まぜそばをすする新感覚パブ。深夜の胃袋を満たします。', NOW(), NOW(), TRUE, 'restaurant(69).jpg'),
(70, 'きしめんラウンジ', '10:00-20:00', '金曜日', 500, 8, 22, '052-741-5555', '460-0070', '名古屋市中区久屋南1-1-1', 'シックなラウンジできしめんや洋食を。出張帰りの憩いの場。', NOW(), NOW(), TRUE, NULL),
(71, '味噌カツハウスBon', '11:00-21:00', '火曜日', 700, 7, 30, '052-645-8888', '460-0071', '名古屋市中区平和北3-7-7', '家庭的な雰囲気で味噌カツを提供。子供からお年寄りまで大満足。', NOW(), NOW(), TRUE, 'restaurant(71).jpg'),
(72, 'ひつまぶし旅館風', '12:00-22:00', '水曜日', 1300, 1, 24, '052-223-5552', '460-0072', '名古屋市中区内山2-9-9', '旅館を彷彿とさせる和空間でひつまぶしを。特別な日の思い出に。', NOW(), NOW(), TRUE, 'restaurant(72).jpg'),
(73, '味噌おでんミルクティー', '09:00-17:00', '木曜日', 400, 4, 16, '052-333-2252', '460-0073', '名古屋市中区虹ヶ丘3-4-5', 'ミルクティーと味噌おでんが同居する喫茶店。意外な組み合わせが話題。', NOW(), NOW(), TRUE, 'restaurant(73).jpg'),
(74, '台湾まぜそばTrattoria', '11:00-23:00', '金曜日', 800, 10, 28, '052-747-2222', '460-0074', '名古屋市中区南久屋町2-3-1', '台湾まぜそばをイタリアン風に。ハーブやチーズが生み出す新世界の味。', NOW(), NOW(), TRUE, 'restaurant(74).jpg'),
(75, '手羽先Chocolat', '08:00-16:00', '日曜日', 500, 11, 18, '052-000-4444', '460-0075', '名古屋市中区富士見台1-2-3', '手羽先とチョコレートスイーツを同時提供する冒険的スイーツ店。甘塩っぱい新感覚。', NOW(), NOW(), TRUE, 'restaurant(75).jpg'),
(76, '味噌煮込みデリ', '10:00-20:00', '月曜日', 600, 5, 26, '052-414-1114', '460-0076', '名古屋市中区鶯南2-4-4', 'テイクアウト専門の味噌煮込みうどん弁当。旅のお供にも最適。', NOW(), NOW(), TRUE, 'restaurant(76).jpg'),
(77, 'ひつまぶしSushi Bar', '17:00-23:00', '火曜日', 1400, 1, 28, '052-555-1212', '460-0077', '名古屋市中区寿町4-1-1', 'ひつまぶしと寿司を一緒に楽しめる和バー。特上うなぎと地酒で贅沢な夜を。', NOW(), NOW(), TRUE, 'restaurant(77).jpg'),
(78, '味噌おでんChai', '09:00-18:00', '水曜日', 400, 4, 16, '052-888-4141', '460-0078', '名古屋市中区百合町5-2-2', 'チャイと味噌おでんの意外な組み合わせ。まったり午後の喫茶時間。', NOW(), NOW(), TRUE, 'restaurant(78).jpg'),
(79, '台湾まぜそばKartell', '11:00-22:00', '木曜日', 700, 8, 30, '052-999-5454', '460-0079', '名古屋市中区清水橋1-3-2', 'おしゃれな洋食空間で台湾まぜそばをアレンジ。若いカップルに大人気。', NOW(), NOW(), TRUE, 'restaurant(79).jpg'),
(80, 'きしめんデザート', '10:00-19:00', 'なし', 400, 11, 18, '052-999-3333', '460-0080', '名古屋市中区菊井1-1-1', 'きしめん粉の和スイーツが楽しめる不思議な店。モチっと新食感に虜。', NOW(), NOW(), TRUE, NULL),
(81, '味噌カツCantina', '17:00-00:00', '金曜日', 700, 3, 32, '052-441-5123', '460-0081', '名古屋市中区太閤1-3-2', 'カンティーナ風居酒屋で味噌カツを。陽気な雰囲気と濃厚味噌が食欲を刺激。', NOW(), NOW(), TRUE, 'restaurant(81).jpg'),
(82, 'ひつまぶしRistorante', '11:00-21:00', '月曜日', 1300, 10, 26, '052-777-8989', '460-0082', '名古屋市中区古渡西2-2-2', 'ひつまぶしをイタリアン仕立てで提供。バルサミコが鰻の旨味を引き立てます。', NOW(), NOW(), TRUE, 'restaurant(82).jpg'),
(83, '味噌おでんCreperie', '09:00-17:00', '火曜日', 400, 11, 18, '052-222-1113', '460-0083', '名古屋市中区錦南3-3-3', '味噌おでんとクレープの不思議な組み合わせ。甘辛い風味がクセになる。', NOW(), NOW(), TRUE, 'restaurant(83).jpg'),
(84, '台湾まぜそばBistro', '12:00-23:00', '水曜日', 700, 1, 28, '052-444-7878', '460-0084', '名古屋市中区栄南5-4-1', 'ビストロ風台湾まぜそばはワインとの相性抜群。大人の隠れ家。', NOW(), NOW(), TRUE, 'restaurant(84).jpg'),
(85, 'きしめんWine Bar', '17:00-23:00', '木曜日', 600, 3, 30, '052-999-5656', '460-0085', '名古屋市中区金山東1-3-6', 'ワインを片手にきしめんを啜る新感覚バー。大人の夜を演出。', NOW(), NOW(), TRUE, 'restaurant(85).jpg'),
(86, '味噌カツCurry', '10:00-20:00', 'なし', 600, 5, 34, '052-414-4545', '460-0086', '名古屋市中区新須川2-1-2', 'カレーに味噌カツをのせたガッツリ定食。ボリューム満点でリピーター続出。', NOW(), NOW(), TRUE, 'restaurant(86).jpg'),
(87, 'ひつまぶしDeli', '11:00-19:00', '金曜日', 1100, 8, 24, '052-666-9999', '460-0087', '名古屋市中区華陽町3-3-3', 'テイクアウトひつまぶし専門店。ホテルや自宅で名古屋の味を楽しめます。', NOW(), NOW(), TRUE, 'restaurant(87).jpg'),
(88, 'どて煮Espresso', '08:00-16:00', '月曜日', 400, 4, 16, '052-333-0006', '460-0088', '名古屋市中区宝町5-5-5', 'エスプレッソとどて煮の不思議なセット。意外と合うと評判。', NOW(), NOW(), TRUE, 'restaurant(88).jpg'),
(89, '台湾ラーメンTapas', '18:00-02:00', '火曜日', 600, 3, 28, '052-777-2022', '460-0089', '名古屋市中区柳原西2-2-2', '小皿料理と台湾ラーメンを合わせる居酒屋。辛さにハマる夜を。', NOW(), NOW(), TRUE, 'restaurant(89).jpg'),
(90, '手羽先Noir', '17:00-23:00', '水曜日', 500, 3, 26, '052-505-4455', '460-0090', '名古屋市中区錦東1-1-1', '黒胡椒手羽先が人気のシックな居酒屋。大人の夜にぴったり。', NOW(), NOW(), TRUE, NULL),
(91, '味噌鍋Rustico', '11:00-21:00', '木曜日', 700, 1, 28, '052-777-3330', '460-0091', '名古屋市中区葵南西2-3-3', '名古屋味噌をフレンチ風鍋で楽しむレストラン。rusticな空間で上質な時間を。', NOW(), NOW(), TRUE, 'restaurant(91).jpg'),
(92, 'ひつまぶしEtude', '12:00-22:00', '金曜日', 1300, 10, 26, '052-523-7474', '460-0092', '名古屋市中区鶴舞北1-4-1', 'クラシック音楽が流れる中で味わうひつまぶしは格別。優雅なひと時をどうぞ。', NOW(), NOW(), TRUE, 'restaurant(92).jpg'),
(93, '味噌おでんMacaroon', '09:00-17:00', 'なし', 400, 11, 16, '052-100-4141', '460-0093', '名古屋市中区栄北5-5-5', '味噌おでんとマカロンが同時に楽しめるスイーツ店。甘辛い刺激が新鮮！', NOW(), NOW(), TRUE, 'restaurant(93).jpg'),
(94, '台湾まぜそばFusion', '11:00-23:00', '月曜日', 700, 8, 30, '052-888-9991', '460-0094', '名古屋市中区東桜南2-2-2', '台湾まぜそばと洋食の融合ダイニング。独創的な味に挑戦。', NOW(), NOW(), TRUE, 'restaurant(94).jpg'),
(95, 'きしめんChic', '10:00-20:00', '火曜日', 500, 5, 20, '052-223-5151', '460-0095', '名古屋市中区白金町3-1-1', 'シックな内装の食堂できしめんをじっくり味わう。出汁香る至福の一杯。', NOW(), NOW(), TRUE, 'restaurant(95).jpg'),
(96, '味噌カツViennoiserie', '08:00-16:00', '水曜日', 600, 11, 18, '052-444-4545', '460-0096', '名古屋市中区伏見南1-2-2', '味噌カツとヴィエノワズリーを組み合わせた斬新なスイーツパン店。朝食にも最適。', NOW(), NOW(), TRUE, 'restaurant(96).jpg'),
(97, 'ひつまぶし和カフェ', '09:00-18:00', '木曜日', 1200, 4, 20, '052-441-1199', '460-0097', '名古屋市中区大須南3-3-3', '和スイーツとひつまぶしを同時に楽しむ和カフェ。抹茶と鰻の意外な調和。', NOW(), NOW(), TRUE, 'restaurant(97).jpg'),
(98, 'どて煮Comptoir', '17:00-23:00', '金曜日', 500, 3, 24, '052-747-2220', '460-0098', '名古屋市中区金山北2-4-4', 'カウンターでどて煮とワインを嗜む大人の居酒屋。コク深い味が舌を満足させる。', NOW(), NOW(), TRUE, 'restaurant(98).jpg'),
(99, '台湾ラーメンGlace', '12:00-02:00', '日曜日', 600, 3, 28, '052-999-7772', '460-0099', '名古屋市中区錦北1-3-3', '冷やし台湾ラーメンやデザート麺など革新的メニュー多数。夜更けの一杯に最適。', NOW(), NOW(), TRUE, 'restaurant(99).jpg'),
(100, '手羽先Confeito', '08:00-15:00', '月曜日', 500, 11, 16, '052-223-9922', '460-0100', '名古屋市中区桜通1-1-1', '手羽先と金平糖の奇妙な組み合わせがSNSで話題。甘じょっぱい不思議な体験を。', NOW(), NOW(), TRUE, NULL);


--(1, '店舗1', '8:00-22:00', '月曜日', 500, 6, 120, '08012345678', '123-4567', '東京都千代田区1-1-1', '説明文1', NOW(), NOW(), TRUE, 'restaurant(1).jpg'),
--(2, '店舗2', '7:00-23:00', '火曜日', 1000, 4, 80, '08023456789', '124-5678', '東京都港区2-2-2', '説明文2', NOW(), NOW(), TRUE, 'restaurant(2).jpg'),
--(3, '店舗3', '9:00-21:00', '水曜日', 1500, 8, 70, '08034567890', '125-6789', '東京都新宿区3-3-3', '説明文3', NOW(), NOW(), TRUE, 'restaurant(3).jpg'),
--(4, '店舗4', '10:00-20:00', '木曜日', 2000, 1, 50, '08045678901', '126-7890', '東京都渋谷区4-4-4', '説明文4', NOW(), NOW(), TRUE, 'restaurant(4).jpg'),
--(5, '店舗5', '7:00-22:00', '金曜日', 3000, 5, 90, '08056789012', '127-8901', '東京都豊島区5-5-5', '説明文5', NOW(), NOW(), TRUE, 'restaurant(5).jpg'),
--(6, '店舗6', '9:00-23:00', '土曜日', 500, 7, 60, '08067890123', '128-9012', '東京都品川区6-6-6', '説明文6', NOW(), NOW(), TRUE, 'restaurant(6).jpg'),
--(7, '店舗7', '8:00-21:00', '日曜日', 1000, 8, 75, '08078901234', '129-0123', '東京都目黒区7-7-7', '説明文7', NOW(), NOW(), TRUE, 'restaurant(7).jpg'),
--(8, '店舗8', '10:00-22:00', '月曜日', 1500, 3, 50, '08089012345', '130-1234', '東京都中野区8-8-8', '説明文8', NOW(), NOW(), TRUE, 'restaurant(8).jpg'),
--(9, '店舗9', '7:00-20:00', '火曜日', 2000, 2, 45, '08090123456', '131-2345', '東京都足立区9-9-9', '説明文9', NOW(), NOW(), TRUE, 'restaurant(9).jpg'),
--(10, '店舗10', '9:00-21:00', '水曜日', 3000, 5, 40, '08001234567', '132-3456', '東京都葛飾区10-10-10', '説明文10', NOW(), NOW(), TRUE, 'restaurant(10).jpg'),
--(11, '店舗11', '8:00-22:00', '木曜日', 500, 1, 80, '08011234567', '133-4567', '東京都千代田区1-1-1', '説明文11', NOW(), NOW(), TRUE, 'restaurant(11).jpg'),
--(12, '店舗12', '10:00-23:00', '金曜日', 1000, 4, 95, '08022345678', '134-5678', '東京都港区2-2-2', '説明文12', NOW(), NOW(), TRUE, 'restaurant(12).jpg'),
--(13, '店舗13', '9:00-22:00', '土曜日', 1500, 6, 110, '08033456789', '135-6789', '東京都新宿区3-3-3', '説明文13', NOW(), NOW(), TRUE, 'restaurant(13).jpg'),
--(14, '店舗14', '7:00-20:00', '日曜日', 2000, 9, 30, '08044567890', '136-7890', '東京都渋谷区4-4-4', '説明文14', NOW(), NOW(), TRUE, 'restaurant(14).jpg'),
--(15, '店舗15', '8:00-21:00', '月曜日', 3000, 11, 40, '08055678901', '137-8901', '東京都豊島区5-5-5', '説明文15', NOW(), NOW(), TRUE, 'restaurant(15).jpg'),
--(16, '店舗16', '10:00-23:00', '火曜日', 500, 7, 30, '08066789012', '138-9012', '東京都品川区6-6-6', '説明文16', NOW(), NOW(), TRUE, 'restaurant(16).jpg'),
--(17, '店舗17', '8:00-22:00', '水曜日', 1000, 6, 60, '08077890123', '139-0123', '東京都目黒区7-7-7', '説明文17', NOW(), NOW(), TRUE, 'restaurant(17).jpg'),
--(18, '店舗18', '9:00-21:00', '木曜日', 1500, 5, 45, '08088901234', '140-1234', '東京都中野区8-8-8', '説明文18', NOW(), NOW(), TRUE, 'restaurant(18).jpg'),
--(19, '店舗19', '7:00-20:00', '金曜日', 2000, 3, 35, '08099901234', '141-2345', '東京都足立区9-9-9', '説明文19', NOW(), NOW(), TRUE, 'restaurant(19).jpg'),
--(20, '店舗20', '9:00-22:00', '月曜日', 3000, 8, 50, '08011123456', '142-3456', '東京都葛飾区10-10-10', '説明文20', NOW(), NOW(), TRUE, 'restaurant(20).jpg');

--レビューテーブルへの追加
INSERT IGNORE INTO reviews (id, point, user_id, restaurant_id, review_comment, created_at, updated_at, enabled) VALUES
(1, 4, 1, 1, '素晴らしい料理でした！', NOW(), NOW(), TRUE),
(2, 5, 1, 2, 'また行きたいです！', NOW(), NOW(), TRUE),
(3, 3, 2, 3, '普通の味でした。', NOW(), NOW(), TRUE),
(4, 2, 3, 4, 'サービスが少し残念でした。', NOW(), NOW(), TRUE),
(5, 4, 4, 5, 'とても満足しました。', NOW(), NOW(), TRUE),
(6, 5, 5, 6, '一生忘れられない味です。', NOW(), NOW(), TRUE),
(7, 3, 6, 7, '悪くはないですが、特別感はありません。', NOW(), NOW(), TRUE),
(8, 4, 7, 8, 'また行きます！', NOW(), NOW(), TRUE),
(9, 1, 8, 9, '期待外れでした。', NOW(), NOW(), TRUE),
(10, 5, 9, 10, '最高の時間を過ごしました！', NOW(), NOW(), TRUE),
-- 以下続く --
(100, 4, 50, 30, '大変満足です。', NOW(), NOW(), TRUE);

--お気に入りテーブルへの追加
INSERT IGNORE INTO favorites (id, user_id, restaurant_id, favorite_comment, created_at, updated_at) VALUES
(1, 1, 1, '雰囲気が良かったです。', NOW(), NOW()),
(2, 1, 2, NULL, NOW(), NOW()),
(3, 2, 3, '料理が美味しい！', NOW(), NOW()),
(4, 2, 4, NULL, NOW(), NOW()),
(5, 3, 5, '何度も行きたくなるお店です。', NOW(), NOW()),
(6, 4, 6, NULL, NOW(), NOW()),
(7, 5, 7, 'おすすめのお店です。', NOW(), NOW()),
(8, 6, 8, NULL, NOW(), NOW()),
(9, 7, 9, 'とても快適でした。', NOW(), NOW()),
(10, 8, 10, NULL, NOW(), NOW()),
-- 以下続く --
(100, 50, 30, 'また行きたいと思います。', NOW(), NOW());

--予約テーブルへの追加
INSERT IGNORE INTO reservations (id, user_id, restaurant_id, appointment_date, num_of_people, created_at, updated_at) VALUES
(1, 1, 1, '2024-12-15 18:30:00', 4, NOW(), NOW()),
(2, 1, 2, '2024-12-20 12:00:00', 3, NOW(), NOW()),
(3, 2, 3, '2024-12-25 19:00:00', 5, NOW(), NOW()),
(4, 2, 4, '2024-12-30 14:00:00', 2, NOW(), NOW()),
(5, 3, 5, '2024-12-17 20:00:00', 6, NOW(), NOW()),
(6, 4, 6, '2024-12-18 09:30:00', 1, NOW(), NOW()),
(7, 5, 7, '2024-12-22 18:00:00', 8, NOW(), NOW()),
(8, 6, 8, '2024-12-28 12:45:00', 3, NOW(), NOW()),
(9, 7, 9, '2024-12-19 13:15:00', 2, NOW(), NOW()),
(10, 8, 10, '2024-12-24 21:00:00', 4, NOW(), NOW());
--(1, 1, 1, '2024-12-15', 4, NOW(), NOW()),
--(2, 1, 2, '2024-12-20', 3, NOW(), NOW()),
--(3, 2, 3, '2024-12-25', 5, NOW(), NOW()),
--(4, 2, 4, '2024-12-30', 2, NOW(), NOW()),
--(5, 3, 5, '2024-12-17', 6, NOW(), NOW()),
--(6, 4, 6, '2024-12-18', 1, NOW(), NOW()),
--(7, 5, 7, '2024-12-22', 8, NOW(), NOW()),
--(8, 6, 8, '2024-12-28', 3, NOW(), NOW()),
--(9, 7, 9, '2024-12-19', 2, NOW(), NOW()),
--(10, 8, 10, '2024-12-24', 4, NOW(), NOW()),
