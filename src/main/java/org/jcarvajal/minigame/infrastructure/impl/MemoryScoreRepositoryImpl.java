package org.jcarvajal.minigame.infrastructure.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jcarvajal.minigame.entities.Score;
import org.jcarvajal.minigame.infrastructure.ScoreRepository;

/**
 * Implementation of the ScoreRepository.
 * This class is using a HashMap to map Levels to Scores.
 * The scores are mapped to a Binary Search Tree (TreeMap) to sort by scores and 
 * has an unique user per score.
 * 
 * @author JoseCH
 *
 */
public class MemoryScoreRepositoryImpl implements ScoreRepository {
	
	private Map<Integer, TreeMap<Integer, Score>> scores 
		= new HashMap<Integer, TreeMap<Integer, Score>>();
	
	/**
	 * The complexity of this method is O(1) + O(logN).
	 */
	public Score getScore(int levelId, int userId) {
		Score score = null;
		if (scores.containsKey(levelId)) {
			TreeMap<Integer, Score> scoresByLevel = scores.get(levelId);
			if (scoresByLevel != null) {
				score = scoresByLevel.get(userId);
			}
		}
		
		return score;
	}
	
	public Collection<Score> getScoreByLevelId(int levelId) {
		Collection<Score> found = null;
		if (scores.containsKey(levelId)) {
			TreeMap<Integer, Score> scoresByLevel = scores.get(levelId);
			if (scoresByLevel != null) {
				found = scoresByLevel.values();
			}
		}
		
		return found;
	}
	
	public synchronized void saveScore(Score score) {
		if (score != null) {
			TreeMap<Integer, Score> scoresByLevel = scores.get(score.getLevelId());
			if (scoresByLevel == null) {
				scoresByLevel = new TreeMap<Integer, Score>();
				scores.put(score.getLevelId(), scoresByLevel);
			}
			
			scoresByLevel.put(score.getUserId(), score);
			
			// Limit number of items.
			if (scoresByLevel.size() > 15) {
				scoresByLevel.pollFirstEntry();
			}
		}
	}
	
	public void updateScore(Score score) {
		// Do nothing since we store scores in memory.
	}
}
