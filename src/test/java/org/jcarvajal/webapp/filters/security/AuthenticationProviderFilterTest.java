package org.jcarvajal.webapp.filters.security;

import java.util.UUID;

import org.jcarvajal.webapp.model.AccessToken;
import org.jcarvajal.webapp.model.User;
import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthenticationProviderFilterTest {
	private AuthenticationProviderFilter filter;
	
	private UserService mockUserService;
	private RequestContext mockRequestContext;
	
	private User expectedUser;
	
	@Before
	public void setup() {
		mockUserService = Mockito.mock(UserService.class);
		mockRequestContext = Mockito.mock(RequestContext.class);
		
		filter = new AuthenticationProviderFilter(mockUserService);
	}
	
	@Test
	public void doFilter_whenAccessTokenNotFound_thenPrincipalIsNull() {
		givenNotExistingAccessToken();
		whenDoFilter();
		thenPrincipalNotInvoked();
	}
	
	@Test
	public void doFilter_whenAccessTokenFound_thenPrincipalIsSet() {
		givenExistingAccessToken();
		whenDoFilter();
		thenPrincipalInvoked();
	}
	
	private void givenNotExistingAccessToken() {
		Mockito.when(mockUserService.getAccessToken(Mockito.anyString())).thenReturn(null);
	}
	
	private void givenExistingAccessToken() {
		expectedUser = new User();
		expectedUser.setUsername(UUID.randomUUID().toString());
		expectedUser.setRole(UUID.randomUUID().toString());
		expectedUser.setPassword(UUID.randomUUID().toString());
		AccessToken accessToken = new AccessToken(expectedUser);
		Mockito.when(mockUserService.getAccessToken(Mockito.anyString())).thenReturn(accessToken);
	}
	
	private void whenDoFilter() {
		filter.doFilter(mockRequestContext);
	}
	
	private void thenPrincipalNotInvoked() {
		Mockito.verify(mockRequestContext, Mockito.never())
			.setPrincipal(Mockito.anyString(), Mockito.anyString());
	}
	
	private void thenPrincipalInvoked() {
		Mockito.verify(mockRequestContext, Mockito.times(1))
			.setPrincipal(expectedUser.getUsername(), expectedUser.getRole());
	}
}
