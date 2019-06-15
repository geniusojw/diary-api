package org.jerrioh.diary.controller.diary;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.account.AccountDiaryController;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountDiary;
import org.jerrioh.diary.domain.repo.AccountDiaryRepository;
import org.jerrioh.diary.domain.repo.AccountRepository;
import org.jerrioh.security.authentication.after.CompleteAccountToken;
import org.jerrioh.security.authentication.before.AccountJwtToken;
import org.jerrioh.security.provider.AccountJwtAuthenticationProvider;
import org.jerrioh.security.provider.AccountSigninAuthenticationProvider;
import org.jerrioh.security.provider.AuthorAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountDiaryController.class)
public class AccountDiaryControllerTest {

	@Autowired
	private MockMvc mockMvc;
    
	@MockBean
    private AccountSigninAuthenticationProvider accountSigninAuthenticationProvider;
    
	@MockBean
    private AuthorAuthenticationProvider authorAuthenticationProvider;
	
	@MockBean
    private AccountJwtAuthenticationProvider accountJwtAuthenticationProvider;
	
	@MockBean
	private AccountRepository accountRepository;
	
	@MockBean
	private AccountDiaryRepository accountDiaryRepository;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@Before
	public void setup() {
		Account account = new Account();
		account.setAccountEmail("jerrioh@gmail.com");
		account.setPasswordEnc("");
		Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
		
		when(authenticationManager.authenticate(Mockito.any(AccountJwtToken.class))).thenReturn(new CompleteAccountToken(account, authorities));
		
//		when(accountJwtAuthenticationProvider.authenticate(Mockito.any(AccountJwtToken.class))).thenReturn(new CompleteAccountToken(account, authorities));
//		when(accountSigninAuthenticationProvider.authenticate(Mockito.any(AccountSigninToken.class))).thenReturn(new CompleteAccountToken(account, authorities));
	}

	@Test
	public void read() throws Exception {
		String diaryDate = "20194030";
		String accountEmail = "jerrioh@gmail.com";

		AccountDiary diary = new AccountDiary();
		diary.setDiaryDate(diaryDate);
		diary.setAccountEmail(accountEmail);
		diary.setTitle("Genius OJW");
		diary.setContent("역시는 역시 역시군");
		
		when(accountDiaryRepository.findByAccountEmailAndDiaryDate(accountEmail, diaryDate)).thenReturn(diary);
		
//		String jwt = "eyJhbGciOiJIUzUxMiJ9"
//				+ ".eyJzdWIiOiJqZXJyaW9oQGdtYWlsLmNvbSIsImlhdCI6MTU2MDE4MzAxNywiZXhwIjoxNjQ2NTgzMDE2fQ"
//				+ ".fxyZDrUyVCJENZfWYzUQjBzYdaERIqYSZIjufPvQjNvePF_7O6RbhSp2khi1clDDlx5S5DUpQCPcUvwF1dguDw";
		
		this.mockMvc.perform(get("/account/diaries/20194030").header(OdHeaders.TOKEN, ""))
					.andDo(print())
					.andExpect(status().isOk());
	}

}
