package org.jerrioh.diary.controller.diary;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.jerrioh.diary.controller.account.AccountDiaryController;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountDiary;
import org.jerrioh.diary.domain.repo.AccountDiaryRepository;
import org.jerrioh.security.authentication.after.CompleteAccountToken;
import org.jerrioh.security.authentication.before.AccountJwtToken;
import org.jerrioh.security.authentication.before.AccountSigninToken;
import org.jerrioh.security.provider.AccountJwtAuthenticationProvider;
import org.jerrioh.security.provider.AccountSigninAuthenticationProvider;
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
@WebMvcTest(AccountDiaryController.class)
public class DiaryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountJwtAuthenticationProvider jwtAuthenticationProvider;
	
	@MockBean
	private AccountSigninAuthenticationProvider signinAuthenticationProvider;

	@MockBean
	private AccountDiaryRepository diaryRepository;
	
	@Before
	public void setup() {
		Account account = new Account();
		account.setAccountEmail("jerrioh@gmail.com");
		account.setPasswordEnc("");
		Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
		
		when(jwtAuthenticationProvider.authenticate(Mockito.any(AccountJwtToken.class))).thenReturn(new CompleteAccountToken(account, authorities));
		when(jwtAuthenticationProvider.authenticate(Mockito.any(AccountSigninToken.class))).thenReturn(new CompleteAccountToken(account, authorities));
	}

	@Test
	public void read() throws Exception {
		String diaryDate = "20190430";
		String accountEmail = "jerrioh@gmail.com";

		AccountDiary diary = new AccountDiary();
		diary.setDiaryDate(diaryDate);
		diary.setAccountEmail(accountEmail);
		diary.setTitle("Genius OJW");
		diary.setContent("역시는 역시 역시군");
		
		when(diaryRepository.findByAccountEmailAndDiaryDate(accountEmail, diaryDate)).thenReturn(diary);
		
		this.mockMvc.perform(get("/diary/20194030").header("token", ""))
					.andDo(print())
					.andExpect(status().isOk());
	}

}
