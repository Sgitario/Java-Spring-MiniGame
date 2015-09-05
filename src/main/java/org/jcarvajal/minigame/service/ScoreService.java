package org.jcarvajal.minigame.service;

import java.util.Collection;

import org.jcarvajal.minigame.infrastructure.entities.Score;

/**
 * Score services with operations for:
 * - Update score.
 * - Get high score per level id.
 * @author JoseCH
 *
 */
public interface ScoreService {
	/**
	 * Update score for the level id, user id.
	 * 
	 * @param levelId
	 * @param userId
	 * @param score
	 */
	void updateScore(int levelId, int userId, int score);

	/**
	 * Get the high score list by level.
	 * 
	 * @param levelId
	 * @return
	 */
	Collection<Score> getHighScoreListByLevel(int levelId);

}
