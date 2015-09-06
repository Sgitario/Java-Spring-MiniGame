package org.jcarvajal.minigame.infrastructure;

import java.util.Collection;

import org.jcarvajal.minigame.infrastructure.entities.Score;

/**
 * Score repository.
 * @author JoseCH
 *
 */
public interface ScoreRepository {

	/**
	 * Get a score by a level and user.
	 * @param levelId
	 * @param userId
	 * @return
	 */
	Score getScore(int levelId, int userId);
	
	/**
	 * Return the highest score list by level.
	 * @param levelId
	 * @return
	 */
	Collection<Score> getHighScoreListByLevel(int levelId);
	
	/**
	 * Save a score into the repository.
	 * @param score
	 */
	void saveScore(Score score);
	
	/**
	 * Update a score from the repository.
	 * @param score
	 */
	void updateScore(Score score);

}
