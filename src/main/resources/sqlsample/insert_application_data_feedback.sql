-- 성격 BIG 5 요소 (신경성, 외향성, 개방성, 우호성, 성실성)
-- https://namu.wiki/w/Big5
INSERT INTO feedback (feedback_author_type, korean_description, english_description, factor_neuroticism, factor_extraversion, factor_openness, factor_agreeableness, factor_conscientiousness) VALUES
  ( 1, '마음이 불안한 사람', 'ENGLISH',				 3, -1, -1, -1,  0),
  ( 2, '스트레스 심한 사람', 'ENGLISH',				 3, -1,  0,  0,  1),
  ( 3, '걱정이 많은 사람', 'ENGLISH',				 	 3,  0,  0,  0,  1),
  ( 4, '무덤덤한 사람', 'ENGLISH',					-3,  0,  1,  0,  0),
  ( 5, '스트레스 안받는 사람', 'ENGLISH',				-3,  0,  1,  0,  0),
  ( 6, '막무가내인 사람', 'ENGLISH',				 	-3,  0,  1,  0,  0),
  
  ( 7, '상냥한 사람', 'ENGLISH',						 0,  3,  0,  1,  0),
  ( 8, '자신감이 넘치는 사람', 'ENGLISH',				 0,  3,  1,  1,  0),
  ( 9, '활기찬 사람', 'ENGLISH',						-1,  3,  0,  1,  1),
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