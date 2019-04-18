package org.jerrioh.diary.controller.diary;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.Diary;
import org.jerrioh.diary.domain.Diary.DiaryPk;
import org.jerrioh.diary.service.DiaryService;
import org.jerrioh.security.authentication.CompleteToken;
import org.jerrioh.security.authentication.JwtToken;
import org.jerrioh.security.authentication.SigninToken;
import org.jerrioh.security.provider.JwtAuthenticationProvider;
import org.jerrioh.security.provider.SigninAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DiaryController.class)
public class DiaryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JwtAuthenticationProvider jwtAuthenticationProvider;
	
	@MockBean
	private SigninAuthenticationProvider signinAuthenticationProvider;

	@MockBean
	private DiaryService diaryService;
	
	@Before
	public void setup() {
		Account account = new Account();
		account.setUserId("jerrioh@gmail.com");
		account.setPasswordEnc("");
		Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
		
		when(jwtAuthenticationProvider.authenticate(Mockito.any(JwtToken.class))).thenReturn(new CompleteToken(account, authorities));
		when(jwtAuthenticationProvider.authenticate(Mockito.any(SigninToken.class))).thenReturn(new CompleteToken(account, authorities));
	}

	@Test
	public void read() throws Exception {
		String writeDay = "20190430";
		String writeUserId = "jerrioh@gmail.com";
		
		Diary diary = new Diary();
		DiaryPk diaryPk = new DiaryPk();
		diaryPk.setWriteDay(writeDay);
		diaryPk.setWriteUserId(writeUserId);
		diary.setDiaryPk(diaryPk);
		diary.setTitle("Genius OJW");
		diary.setContent("역시는 역시 역시군");
		when(diaryService.read(writeUserId, writeDay)).thenReturn(diary);
		
		this.mockMvc.perform(get("/diary/20194030").header("token", ""))
					.andDo(print())
					.andExpect(status().isOk());
	}

}
