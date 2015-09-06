package org.jcarvajal.minigame.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jcarvajal.minigame.infrastructure.ScoreRepository;
import org.jcarvajal.minigame.infrastructure.entities.Score;
import org.junit.Before;
import org.junit.Test;

public class ScoreServiceImplTest {
	private static final int LEVEL_ID = 1;
	private static final int USER_ID = 1;
	
	private ScoreServiceImpl service;
	
	private ScoreRepository mockRepository;
	
	@Before
	public void setup() {
		this.mockRepository = mock(ScoreRepository.class);
		
		this.service = new ScoreServiceImpl();
		this.service.setScoreRepository(mockRepository);
	}
	
	/**
	 * When a new score.
	 * Then score is saved.
	 */
	@Test
	public void updateScore_whenNewScore_thenScoreIsSaved() {
		whenUpdateScore(100);
		thenScoreIsSavedInRepository(100);
	}
	
	/**
	 * When an existing score.
	 * Then score is updated if the previous score was worse.
	 */
	@Test
	public void updateScore_whenExistingScore_thenScoreIsUpdated() {
		givenExistingScore(100);
		whenUpdateScore(200);
		thenScoreIsUpdatedInRepository(200);
	}
	
	/**
	 * When an existing score.
	 * Then score is updated if the previous score was worse.
	 */
	@Test
	public void updateScore_whenExistingScore_thenScoreIsNotUpdated() {
		givenExistingScore(100);
		whenUpdateScore(50);
		thenScoreIsNotUpdatedInRepository();
	}
	
	@Test
	public void getHighScoreListByLevel_thenRepositoryIsInvoked() {
		whenGetHighScoreListByLevel();
		thenRepositoryGetHighScoreListInvoked();
	}
	
	private void givenExistingScore(int scoreValue) {
		Score score = new Score();
		score.setLevelId(LEVEL_ID);
		score.setUserId(USER_ID);
		score.setScore(scoreValue);
		
		when(this.mockRepository.getScore(eq(LEVEL_ID), eq(USER_ID))).thenReturn(score);
	}
	
	private void whenGetHighScoreListByLevel() {
		this.service.getHighScoreListByLevel(LEVEL_ID);
	}
	
	private void whenUpdateScore(int score) {
		this.service.updateScore(LEVEL_ID, USER_ID, score);
	}
	
	private void thenScoreIsUpdatedInRepository(final int scoreValue) {
		verify(this.mockRepository, times(1)).updateScore(argThat(new ScoreMatcher(scoreValue)));
	}
	
	private void thenScoreIsNotUpdatedInRepository() {
		verify(this.mockRepository, times(0)).updateScore(any(Score.class));
	}
	
	private void thenScoreIsSavedInRepository(final int scoreValue) {
		verify(this.mockRepository, times(1)).saveScore(argThat(new ScoreMatcher(scoreValue)));
	}
	
	private void thenRepositoryGetHighScoreListInvoked() {
		verify(this.mockRepository, times(1)).getHighScoreListByLevel(eq(LEVEL_ID));
	}
	
	private class ScoreMatcher extends BaseMatcher<Score> {
		private final int score;
		
		private ScoreMatcher(int score) {
			this.score = score;
		}
		
		public boolean matches(Object arg) {
			if (arg instanceof Score) {
				Score score = (Score) arg;
				assertEquals(LEVEL_ID, score.getLevelId());
				assertEquals(USER_ID, score.getUserId());
				assertEquals(this.score, score.getScore());
				return true;
			}
			
			return false;
		}

		public void describeTo(Description arg0) {
			
		}
	}
}
