-- 성격 BIG 5 요소 (신경성, 외향성, 개방성, 우호성, 성실성)
-- https://namu.wiki/w/Big5
INSERT INTO feedback_message (korean_description, english_description, factor_type, negative) VALUES
-- Neuroticism
('스트레스를 많이 받는다.', 'Experiences a lot of stress.', 0, 0),
('다른 것들에 대해 많이 걱정한다.', 'Worries about many different things.', 0, 0),
('쉽게 화가 난다.', 'Gets upset easily.', 0, 0),
('감정변화가 가파르다.', 'Experiences dramatic shifts in mood.', 0, 0),
('불안감을 자주 느낀다.', 'Feels anxious.', 0, 0),
('스트레스를 받으면 그걸 이겨내기 위해 노력한다.', 'Struggles to bounce back after stressful events.', 0, 0),

('감정적으로 흔들이지 않는다.', 'Emotionally stable.', 0, 1),
('스트레스를 잘 대처한다.', 'Deals well with stress.', 0, 1),
('슬프거나 우울한 느낌을 적게 받는다.', 'Rarely feels sad or depressed.', 0, 1),
('많이 걱정하지 않는다.', 'Doesn''t worry much.', 0, 1),
('매우 안정적이다.', 'Is very relaxed.', 0, 1),

-- Extraversion
('관심의 중심이 되는 것을 즐긴다.', 'Enjoys being the center of attention.', 1, 0),
('대화 하는 것을 좋아한다.', 'Likes to start conversations.', 1, 0),
('새로운 사람을 만나는 것을 즐긴다.', 'Enjoys meeting new people.', 1, 0),
('광범위한 친구와 지인이 있다.', 'Has a wide social circle of friends and acquaintances.', 1, 0),
('쉽게 새로운 친구를 만든다.', 'Finds it easy to make new friends.', 1, 0),
('다른 사람과 함께 있을 때 활력을 느낀다.', 'Feels energized when around other people.', 1, 0),
('생각하기 전에 말한다.', 'Say things before thinking about them.', 1, 0),

('혼자서 하는 것을 선호한다.', 'Prefers solitude.', 0, 0),
('많은 사람들을 만나야 할 때 지친 느낌을 받는다.', 'Feels exhausted when having to socialize a lot.', 1, 1),
('대화를 시작하는 것이 어려울 때가 있다.', 'Finds it difficult to start conversations.', 1, 1),
('가벼운 대화를 좋아하지 않는다.', 'Dislikes making small talk.', 1, 1),
('말하기 전에 신중하게 생각한다.', 'Carefully thinks things through before speaking.', 1, 1),
('관심의 중심이 되는 것을 싫어한다.', 'Dislikes being the center of attention.', 1, 1),

-- Openness
('창조적이다.', 'Very creative.', 2, 0),
('새로운 것을 좋아한다.', 'Open to trying new things.', 2, 0),
('새로운 도전에 몰두한다.', 'Focused on tackling new challenges.', 2, 0),
('추상적인 개념을 생각하는 것을 좋아한다.', 'Happy to think about abstract concepts.', 2, 0),

('변화를 싫어한다.', 'Dislikes change.', 2, 1),
('새로운 것을 즐기지 않는다.', 'Does not enjoy new things.', 2, 1),
('새로운 생각에 반대적이다.', 'Resists new ideas.', 2, 1),
('상상력이 풍부하지는 않다.', 'Not very imaginative.', 2, 1),
('추상적이거나 이론적인 개념을 싫어한다.', 'Dislikes abstract or theoretical concepts.', 2, 1),

-- Agreeableness
('다른 사람들에게 많은 관심을 가진다.', 'Has a great deal of interest in other people.', 3, 0),
('다른 사람들을 많이 신경한다.', 'Cares about others.', 3, 0),
('다른 사람들을 공감하고 걱정한다.', 'Feels empathy and concern for other people.', 3, 0),
('다른 사람들을 행복하는게 만드는 것을 좋아한다.', 'Enjoys helping and contributing to the happiness of other people.', 3, 0),
('도움이 필요한 사람을 돕는다.', 'Assists others who are in need of help.', 3, 0),

('다른 사람들에게는 거의 관심이 없다.', 'Takes little interest in others.', 3, 1),
('다른 사람이 어떻게 느끼는지 신경쓰지 않는다.', 'Doesn''t care about how other people feel.', 3, 1),
('다른 사람들의 문제에 거의 관심이 없다.', 'Has little interest in other people''s problems.', 3, 1),
('다른 사람에게 독설을 하기도 한다.', 'Insults and belittles others.', 3, 1),
('원하는 것을 얻기 위해 다른 사람들을 이용하기도 한다.', 'Manipulates others to get what they want.', 3, 1),

-- Conscientiousness
('준비성이 철저하다.', 'Spends time preparing.', 4, 0),
('중요한 일이 있으면 바로 처리한다.', 'Finishes important tasks right away.', 4, 0),
('세부적인 것에 주의를 기울인다.', 'Pays attention to detail.', 4, 0),
('정해진 일정을 좋아한다.', 'Enjoys having a set schedule.', 4, 0),

('구조와 일정을 싫어한다.', 'Dislikes structure and schedules.', 4, 1),
('주변 물건을 신경쓰지 않고 어지럽힌다.', 'Makes messes and doesn''t take care of things.', 4, 1),
('물건이 원래 있던 자리에 돌려놓지 않는다.', 'Fails to return things or put them back where they belong.', 4, 1),
('중요한 일을 미룬다.', 'Procrastinates important tasks.', 4, 1),
('필요하거나 할당 된 작업을 완료하지 못한다.', 'Fails to complete necessary or assigned tasks.', 4, 1)
;