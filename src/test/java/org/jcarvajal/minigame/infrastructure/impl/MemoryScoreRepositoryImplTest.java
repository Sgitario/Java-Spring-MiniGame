package org.jcarvajal.minigame.infrastructure.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jcarvajal.minigame.entities.Score;
import org.junit.Before;
import org.junit.Test;

public class MemoryScoreRepositoryImplTest {
	private MemoryScoreRepositoryImpl repository;
	
	private int currentLevelId = 1;
	private List<Score> expected;
	private List<Score> actual;
	private Score actualScore;
	
	@Before
	public void setup() {
		expected = new ArrayList<Score>();
		
		this.repository = new MemoryScoreRepositoryImpl();
	}
	
	@Test
	public void save_whenSaveScore_thenScoreIsReturned() {
		givenLevelId(1);
		givenScore(10, 20);
		whenSave();
		whenRetrieveScore(1, 10);
		thenScoreIs(20);
	}
	
	@Test
	public void duplicate_whenUpdateScore_thenScoreIsReturned() {
		givenLevelId(1);
		givenScore(10, 10);
		whenSave();
		givenScore(10, 20);
		whenUpdate();
		whenRetrieveScore(1, 10);
		thenScoreIs(20);
	}
	
	@Test
	public void sort_whenSaveScore_thenScoreIsReturned() {
		givenLevelId(1);
		givenScore(8, 10);
		givenScore(10, 20);
		givenScore(5, 18);
		whenSave();
		whenRetrieveScores(1);
		thenScoreAtIndexIs(0, 10, 20);
		thenScoreAtIndexIs(1, 5, 18);
		thenScoreAtIndexIs(2, 8, 10);
	}
	
	@Test
	public void sort_whenLimitExceeded_thenLowestScoreIsDeleted() {
		givenLimit(3);
		givenLevelId(1);
		givenScore(8, 10); // Should be deleted
		givenScore(10, 20);
		givenScore(5, 18);
		givenScore(6, 19);
		whenSave();
		whenRetrieveScores(1);
		thenScoreAtIndexIs(0, 10, 20);
		thenScoreAtIndexIs(1, 6, 19);
		thenScoreAtIndexIs(2, 5, 18);
	}
	
	private void givenLimit(int limit) {
		this.repository.setLimitScores("" + limit);
	}

	private void givenScore(int userId, int score) {
		expected.add(new Score(this.currentLevelId, userId, score));
	}

	private void givenLevelId(int level) {
		this.currentLevelId = level;
	}
	
	private void whenRetrieveScores(int levelId) {
		this.actual = new ArrayList<Score>(repository.getScoreByLevelId(levelId));
	}
	
	private void whenRetrieveScore(int levelId, int userId) {
		this.actualScore = repository.getScore(levelId, userId);
	}
	
	private void whenSave() {
		for (Score score : expected) {
			repository.saveScore(score);
		}
		
		expected.clear();
	}
	
	private void whenUpdate() {
		for (Score score : expected) {
			repository.updateScore(score);
		}
		
		expected.clear();
	}
	
	private void thenScoreIs(int score) {
		assertNotNull(this.actualScore);
		assertEquals(score, this.actualScore.getScore());
	}
	
	private void thenScoreAtIndexIs(int index, int userId, int score) {
		assertNotNull(this.actual);
		assertTrue(index < this.actual.size());
		Score found = this.actual.get(index);
		assertNotNull(found);
		assertEquals(userId, found.getUserId());
		assertEquals(score, found.getScore());
	}
}
