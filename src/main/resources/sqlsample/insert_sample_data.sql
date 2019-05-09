INSERT INTO account (user_id, password_enc, member_user_id) VALUES ('jerrioh@gmail.com', '$2a$10$3H7NsGpow4elFYWQNrFLke.gbL8mPK9GnPfMKtvhS8U.Rx7aL/9Hm', null);
INSERT INTO account (user_id, password_enc, member_user_id) VALUES ('jerrioh@naver.com', '$2a$10$3H7NsGpow4elFYWQNrFLke.gbL8mPK9GnPfMKtvhS8U.Rx7aL/9Hm', null);


INSERT INTO diary (write_day, write_user_id, title, content) VALUES ('20190201', 'jerrioh@gmail.com', '글쎄...', '글쎄는 글쎄.. 글쎄로군');
INSERT INTO diary (write_day, write_user_id, title, content) VALUES ('20190428', 'jerrioh@gmail.com', '4월 28일의 일기', '별로 특별한게 없는 날');
INSERT INTO diary (write_day, write_user_id, title, content) VALUES ('20190430', 'jerrioh@gmail.com', '오늘의 일기', '역시 별로 특별한게 없는 날');
INSERT INTO diary (write_day, write_user_id, title, content) VALUES ('20190501', 'jerrioh@gmail.com', '', '역시 쓸 말이 없다.');
INSERT INTO diary (write_day, write_user_id, title, content) VALUES ('20190502', 'jerrioh@gmail.com', '', '다른 복수개의 단말에서 동시에 일기를 쓴다면? 먼저 업로드 된 쪽이 임자인가? 12시에 업로드가 몰리는 현상은 없을까?');

INSERT INTO letter (write_day, write_user_id, read_user_id, title, content) VALUES ('20190510', 'jerrioh@naver.com', 'jerrioh@gmail.com', '나에게로의 편지', 'ㅅㅂ');
INSERT INTO letter (write_day, write_user_id, read_user_id, title, content) VALUES ('20190511', 'jerrioh@naver.com', 'jerrioh@gmail.com', '나에게로의 편지2', '난 천재');
