-- necessary data
INSERT INTO author (author_id, author_code, nickname, description, chocolates) VALUES ('475a45d5-d139-4e3a-9828-e00e296c9040', 'tellmeyouloveme.', 'Administrator', 'Administrator OJW', 0);

-- nickname
INSERT INTO nickname (nickname, nickname_class_name, language) VALUES
  ('아나톨리아 셰퍼드', '개', 'kor'),
  ('시베리안 허스키', '개', 'kor'),
  
  ('코모도도마뱀', '도마뱀', 'kor'),
  ('코모도왕도마뱀', '도마뱀', 'kor'),
  ('코모도새끼도마뱀', '도마뱀', 'kor'),
  
  ('왕중의왕 사자', '사자', 'kor'),
  ('정글의왕 사자', '사자', 'kor'),
  ('밀림의왕 사자', '사자', 'kor'),
  ('맹수의왕 사자', '사자', 'kor'),
  ('왕 사자', '사자', 'kor'),
  ('왕왕 사자', '사자', 'kor'),
  ('발냄새의왕 사자', '사자', 'kor'),
  
  ('긴팔원숭이', '원숭이', 'kor'),
  ('긴수염원숭이', '원숭이', 'kor'),
  ('긴다리원숭이', '원숭이', 'kor'),
  ('긴팔다리원숭이', '원숭이', 'kor'),
  ('긴허리원숭이', '원숭이', 'kor'),
  ('긴눈원숭이', '원숭이', 'kor'),
  
  ('귀여운토끼', '토끼', 'kor'),
  ('귀요운토끼', '토끼', 'kor'),
  ('귀여니토끼', '토끼', 'kor'),
  ('귀여미토끼', '토끼', 'kor'),
  ('귀요미토끼', '토끼', 'kor'),
  
  ('변신로봇', '로봇', 'kor'),
  ('2단변신로봇', '로봇', 'kor'),
  ('3단변신로봇', '로봇', 'kor'),
  ('4단변신로봇', '로봇', 'kor'),
  ('5단변신로봇', '로봇', 'kor'),
  ('6단변신로봇', '로봇', 'kor'),
  ('7단변신로봇', '로봇', 'kor'),
  ('8단변신로봇', '로봇', 'kor'),
  ('9단변신로봇', '로봇', 'kor'),
  ('10단변신로봇', '로봇', 'kor'),
  ('11단변신로봇', '로봇', 'kor'),
  
  ('동네할아버지', '동네사람', 'kor'),
  ('동네할머니', '동네사람', 'kor'),
  ('동네아저씨', '동네사람', 'kor'),
  ('동네아줌마', '동네사람', 'kor'),
  ('동네형', '동네사람', 'kor'),
  ('동네꼬마', '동네사람', 'kor'),
  
  ('천재오정욱', '오정욱', 'kor'),
  ('미쳐버린천재오정욱', '오정욱', 'kor'),
  
  ('더러운똥오줌', '무생물', 'kor'),
  ('구토잔해물', '무생물', 'kor'),
  
  ('오메가', '오메가', 'kor')
  ;
  
  
INSERT INTO nickname (nickname, nickname_class_name, language) VALUES
  ('Anatolian Shepherd', 'Dog', 'eng'),
  ('Siberian husky', 'Dog', 'eng'),
  
  ('Komodo Dragon', 'Lizard', 'eng'),
  
  ('Lion, King of kings', 'Lion', 'eng'),
  ('Lion, King of the Jungle', 'Lion', 'eng'),
  
  ('Gibbon', 'Monkey', 'eng'),
  ('Rabbit', 'Rabbit', 'eng'),
  
  ('Omega', 'Omega', 'eng')
  ;

-- 성격 BIG 5 요소 (신경성, 외향성, 개방성, 우호성, 성실성)
-- https://namu.wiki/w/Big5
INSERT INTO feedback (feedback_author_type, korean_description, english_description, factor_neuroticism, factor_extraversion, factor_openness, factor_agreeableness, factor_conscientiousness) VALUES
  ( 1, '마음이 불안한 사람', 'ENGLISH',				 3, -1, -1, -1,  0),
  ( 2, '스트레스 심한 사람', 'ENGLISH',				 3, -1,  0,  0,  1),
  ( 3, '걱정이 많은 사람', 'ENGLISH',				 	 3,  0,  0,  0,  1),
  ( 4, '무덤덤한 사람', 'ENGLISH',					-3,  0,  1,  0,  0),
  ( 5, '스트레스 안받는 사람', 'ENGLISH',				-3,  0,  1,  0,  0),
  ( 6, '막무가내인 사람', 'ENGLISH',				 	-3,  0,  1,  0,  0),
  
  (7, '상냥한 사람', 'ENGLISH',						 0,  3,  0,  1,  0),
  (8, '자신감이 넘치는 사람', 'ENGLISH',				 0,  3,  1,  1,  0),
  (9, '활기찬 사람', 'ENGLISH',						-1,  3,  0,  1,  1),
  (10, '불친절한 사람', 'ENGLISH',					 1, -3,  0, -1,  0),
  (11, '자기 중심적인 사람', 'ENGLISH',				 0, -3,  0, -1,  0),
  (12, '내성적인 사람', 'ENGLISH',					 0, -3,  0,  0,  1),
  
  (13, '상상력이 풍부한 사람', 'ENGLISH',				 0,  0,  3,  0,  0),
  (14, '감성적인 사람', 'ENGLISH',				 	 0, -1,  3,  0,  0),
  (15, '엉뚱한 사람', 'ENGLISH',						 0,  0,  3,  1, -1),
  (16, '보수적인 사람', 'ENGLISH',					 0,  0, -3,  0,  1),
  (17, '게으른 사람', 'ENGLISH',				 		-1,  0, -3,  0, -1),
  (18, '생각을 안하는 사람', 'ENGLISH',				-1,  0, -3,  1, -1),
  
  (19, '믿을 수 있는 사람', 'ENGLISH',					 0,  0,  0,  3,  1),
  (20, '겸손한 사람', 'ENGLISH',						 0, -1,  0,  3,  1),
  (21, '순종적인 사람', 'ENGLISH',					 0, -1, -1,  3,  0),
  (22, '비판적인 사람', 'ENGLISH',			 		 1,  0, -1, -3,  1),
  (23, '의심이 많은 사람', 'ENGLISH',				 	 1, -1, -1, -3,  0),
  (24, '이성적인 사람', 'ENGLISH',			 		 0,  0,  0, -3,  1),
  
  (25, '능력 있는 사람', 'ENGLISH',					 0,  1,  0,  0,  3),
  (26, '책임감 있는 사람', 'ENGLISH',					 1,  0,  0,  0,  3),
  (27, '절제하는 사람', 'ENGLISH',			 		 0, -1, -1,  0,  3),
  (28, '즉흥적인 사람', 'ENGLISH',					-1,  1,  0,  0, -3),
  (29, '자유료운 사람', 'ENGLISH',					-1,  1,  1,  0, -3),
  (30, '게으른 사람', 'ENGLISH',				 		 0,  0, -1,  0, -3)
  ;