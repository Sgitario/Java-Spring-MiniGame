package org.jcarvajal.minigame.infrastructure;

import java.util.Collection;

import org.jcarvajal.minigame.infrastructure.entities.Score;

public interface ScoreRepository {

	Score getScore(int levelId, int userId);
	Collection<Score> getScoreByLevelId(int levelId);
	void saveScore(Score score);
	void updateScore(Score score);

}
