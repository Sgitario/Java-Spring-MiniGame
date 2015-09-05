package org.jcarvajal.minigame.application;

import static org.mockito.Mockito.*;

import org.jcarvajal.minigame.service.ScoreService;
import org.jcarvajal.minigame.service.SessionService;
import org.jcarvajal.minigame.service.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

public class LevelControllerTest {
	
	private static final int USER_ID = 1;
	private static final int LEVEL_ID = 10;
	private static final int SCORE = 100;
	private static final String SESSION_KEY = "TEST";
	
	private SessionService mockSessionService;
	private ScoreService mockScoreService;
	
	private LevelController controller;
	
	@Before
	public void setup() {
		mockSessionService = mock(SessionService.class);
		mockScoreService = mock(ScoreService.class);
		
		controller = new LevelController();
		controller.setScoreService(mockScoreService);
		controller.setSessionService(mockSessionService);
	}
	
	@Test
	public void addScore_whenContainsSession_thenUpdateScoreIsInvoked() 
			throws UserNotFoundException {
		givenContainsSession();
		whenAddScore();
		thenUpdateScoreInvoked();
	}
	
	@Test
	public void addScore_whenNotContainsSession_thenUpdateScoreIsNotInvoked() 
			throws UserNotFoundException {
		whenAddScore();
		thenUpdateScoreNotInvoked();
	}
	
	@Test
	public void highScoreList_thenServiceIsInvoked() {
		whenHighScoreList();
		thenHighScoreServiceInvoked();
	}
	
	private void givenContainsSession() throws UserNotFoundException {
		when(mockSessionService.containsSessionKey(SESSION_KEY)).thenReturn(true);
		when(mockSessionService.getUserIdBySessionKey(SESSION_KEY)).thenReturn(USER_ID);
	}
	
	private void whenAddScore() throws UserNotFoundException {
		controller.addScore(LEVEL_ID, SESSION_KEY, SCORE);
	}
	
	private void whenHighScoreList() {
		controller.highScoreList(LEVEL_ID);
	}
	
	private void thenUpdateScoreInvoked() {
		verify(mockScoreService, times(1)).updateScore(LEVEL_ID, USER_ID, SCORE);
	}
	
	private void thenUpdateScoreNotInvoked() {
		verify(mockScoreService, times(0)).updateScore(anyInt(), anyInt(), anyInt());
	}
	
	private void thenHighScoreServiceInvoked() {
		verify(mockScoreService, times(1)).getHighScoreListByLevel(LEVEL_ID);
	}
}
