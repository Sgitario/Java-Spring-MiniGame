package org.jcarvajal.minigame.service.impl;

import java.util.Collection;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.minigame.infrastructure.ScoreRepository;
import org.jcarvajal.minigame.infrastructure.entities.Score;
import org.jcarvajal.minigame.service.ScoreService;

/**
 * Score service implementation.
 * @author JoseCH
 *
 */
public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreRepository scoreRepository;
	
	public void setScoreRepository(ScoreRepository scoreRepository) {
		this.scoreRepository = scoreRepository;
	}
	
	public void updateScore(int levelId, int userId, int scoreValue) {
		Score score = scoreRepository.getScore(levelId, userId);
		if (score == null) {
			score = new Score(levelId, userId, scoreValue);
			scoreRepository.saveScore(score);
		} else {
			synchronized(score) {
				if (score.getScore() < scoreValue) {
					score.setScore(scoreValue);
					scoreRepository.updateScore(score);
				}
			}
		}
	}
	
	/**
	 * Delegate this operation to the repository bcause simplicity, 
	 * but this logic should be implemented in the service.
	 */
	public Collection<Score> getHighScoreListByLevel(int levelId) {
		return scoreRepository.getScoreByLevelId(levelId);
	}
}
