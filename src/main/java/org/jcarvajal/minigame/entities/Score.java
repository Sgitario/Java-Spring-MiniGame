package org.jcarvajal.minigame.entities;

public class Score implements Comparable<Score> {
	private int userId;
	private int levelId;
	private int score;
	
	public Score() {
		
	}
	
	public Score(int levelId, int userId, int scoreValue) {
		this.levelId = levelId;
		this.userId = userId;
		this.score = scoreValue;
	}

	public int getLevelId() {
		return levelId;
	}
	
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public int compareTo(Score o) {
		int compare = 0;
		if (o.getUserId() != this.getUserId()) {
			if (this.getScore() < o.getScore()) {
				compare = 1;
			} else if (this.getScore() > o.getScore()) {
				compare = -1;
			}
		}
		
		return compare;
	}
}
