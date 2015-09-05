package org.jcarvajal.minigame.infrastructure.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

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
	
	private static final Logger LOG = Logger.getLogger(
			MemoryScoreRepositoryImpl.class.getName());
	
	private Map<Integer, TreeSet<Score>> scores 
		= new ConcurrentHashMap<Integer, TreeSet<Score>>();
	private int limitScores = 15; // default 15 
	
	public void setLimitScores(String limitScores) {
		this.limitScores = Integer.valueOf(limitScores);
	}
	
	/**
	 * The complexity of this method is O(1) + O(logN).
	 */
	public Score getScore(int levelId, int userId) {
		Score score = null;
		if (scores.containsKey(levelId)) {
			TreeSet<Score> scoresByLevel = scores.get(levelId);
			if (scoresByLevel != null) {
				Score toFound = new Score();
				toFound.setLevelId(levelId);
				toFound.setUserId(userId);
				
				score = getScoreInternal(scoresByLevel, toFound);
			}
		}
		
		return score;
	}
	
	public Collection<Score> getScoreByLevelId(int levelId) {
		Collection<Score> found = null;
		if (scores.containsKey(levelId)) {
			TreeSet<Score> scoresByLevel = scores.get(levelId);
			if (scoresByLevel != null) {
				found = new ArrayList<Score>(scoresByLevel);
			}
		}
		
		return found;
	}
	
	public void saveScore(Score score) {
		if (score != null) {
			TreeSet<Score> scoresByLevel = initializeScoresByLevel(score.getLevelId()); 
			synchronized(scoresByLevel) {
				// Update
				scoresByLevel.add(score);
				
				// Limit number of items.
				resizeScoresByLevelIfNeeded(scoresByLevel);
			}
		}
	}
	
	/**
	 * Remove the score from the collection and add it back to sort it.
	 */
	public void updateScore(Score score) {
		if (score != null) {
			TreeSet<Score> scoresByLevel = initializeScoresByLevel(score.getLevelId()); 
			
			// Update
			synchronized(scoresByLevel) {
				scoresByLevel.remove(score);
				scoresByLevel.add(score); // sort
			}
		}
	}
	
	/**
	 * Find the score Complexity=(O(logN))
	 * @param scoresByLevel
	 * @param search
	 * @return
	 */
	private Score getScoreInternal(TreeSet<Score> scoresByLevel, Score search) {
		Score score = null;
		if (scoresByLevel != null) {			
			Score ceiling = scoresByLevel.ceiling(search);
			if (ceiling != null 
					&& ceiling.getUserId() == search.getUserId()) {
				score = ceiling;
			}
		}
		
		return score;
	}
	
	/**
	 * Initialize the list of scores by level if it was not created yet.
	 * @param levelId
	 * @return
	 */
	private TreeSet<Score> initializeScoresByLevel(int levelId) {
		TreeSet<Score> scoresByLevel = scores.get(levelId);
		if (scoresByLevel == null) {
			synchronized(this) { // only create one score per time.
				scoresByLevel = new TreeSet<Score>();
				scores.put(levelId, scoresByLevel);
			}
			
		}
		
		return scoresByLevel;
	}
	
	/**
	 * Delete the lowest scores.
	 * @param scoresByLevel
	 */
	private void resizeScoresByLevelIfNeeded(TreeSet<Score> scoresByLevel) {
		while (scoresByLevel.size() > limitScores) {
			Score deleted = scoresByLevel.pollLast();
			LOG.info(String.format("Deleted lowest score %s for level %s and user %s",
					deleted.getScore(), deleted.getLevelId(), deleted.getUserId()));
		}
	}
}
