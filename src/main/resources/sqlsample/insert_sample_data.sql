INSERT INTO author (author_id, author_code, nickname, description, chocolates) VALUES ('475a45d5-d139-4e3a-9828-e00e296c9040', 'tellmeyouloveme.', 'Administrator', 'Administrator OJW', 0);
INSERT INTO author (author_id, author_code, nickname, description, chocolates) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811', 'RcDHKCZRhQXLe3Cj', 'genius OJW', 'He is obviously a genius.', 0);
INSERT INTO author (author_id, author_code, nickname, description, chocolates) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46812', 'RcDHKCZRhQXLe3Cj', 'Super Man', 'Super Man~~~', 30);



INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811', '20190502', '', '다른 복수개의 단말에서 동시에 일기를 쓴다면? 먼저 업로드 된 쪽이 임자인가? 12시에 업로드가 몰리는 현상은 없을까?', 'kor', 'KOR', 'Asia/Seoul');
INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811', '20190504', '일기', '테스트 일기', 'kor', 'KOR', 'Asia/Seoul');
INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811', '20190519', '졸립다..', '자고 싶지만 자고 싶진 않다.', 'kor', 'KOR', 'Asia/Seoul');
INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46812', '20190504', '일기1', '일기 내용1', 'kor', 'KOR', 'Asia/Seoul');
INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46812', '20190515', '일기2', '일기 내용2', 'kor', 'KOR', 'Asia/Seoul');
INSERT INTO author_diary (author_id, diary_date, title, content, language, country, time_zone_id) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46812', '20190520', '일기3', '일기 내용3', 'kor', 'KOR', 'Asia/Seoul');

INSERT INTO account (account_email, password_enc, first_author_id, last_author_id) VALUES ('jerrioh@gmail.com', '$2a$10$3H7NsGpow4elFYWQNrFLke.gbL8mPK9GnPfMKtvhS8U.Rx7aL/9Hm', '92f44a4e-09ea-4fa5-ab54-df3c10a46811', '92f44a4e-09ea-4fa5-ab54-df3c10a46811');
INSERT INTO account (account_email, password_enc, first_author_id, last_author_id) VALUES ('jerrioh@naver.com', '$2a$10$3H7NsGpow4elFYWQNrFLke.gbL8mPK9GnPfMKtvhS8U.Rx7aL/9Hm', '92f44a4e-09ea-4fa5-ab54-df3c10a46811', '92f44a4e-09ea-4fa5-ab54-df3c10a46811');

INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190201', '글쎄...', '글쎄는 글쎄.. 글쎄로군');
INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190428', '4월 28일의 일기', '별로 특별한게 없는 날');
INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190430', '오늘의 일기', '역시 별로 특별한게 없는 날');
INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190501', '', '역시 쓸 말이 없다.');
INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190502', '', '다른 복수개의 단말에서 동시에 일기를 쓴다면? 먼저 업로드 된 쪽이 임자인가? 12시에 업로드가 몰리는 현상은 없을까?');
INSERT INTO account_diary (account_email, diary_date, title, content) VALUES ('jerrioh@gmail.com', '20190504', '일기', '테스트 일기');

INSERT INTO letter (letter_id, letter_type, from_author_id, from_author_nickname, to_author_id, to_author_nickname, content) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811-123456788', 0, '92f44a4e-09ea-4fa5-ab54-df3c10a46811', 'genius OJW', '92f44a4e-09ea-4fa5-ab54-df3c10a46812', 'helloman', 'ㅎㅇ');
INSERT INTO letter (letter_id, letter_type, from_author_id, from_author_nickname, to_author_id, to_author_nickname, content) VALUES ('92f44a4e-09ea-4fa5-ab54-df3c10a46811-123456789', 0, '92f44a4e-09ea-4fa5-ab54-df3c10a46811', 'genius OJW', '92f44a4e-09ea-4fa5-ab54-df3c10a46812', 'helloworldman', 'ㅎㅇ2');
