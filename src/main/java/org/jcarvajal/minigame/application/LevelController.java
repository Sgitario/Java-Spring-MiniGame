package org.jcarvajal.minigame.application;

import java.util.Collection;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.rest.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.controllers.annotations.RequestBody;
import org.jcarvajal.framework.rest.controllers.annotations.RequestMapping;
import org.jcarvajal.framework.rest.controllers.annotations.RequestParam;
import org.jcarvajal.framework.rest.controllers.annotations.ResponseMapping;
import org.jcarvajal.framework.rest.models.RequestMethod;
import org.jcarvajal.minigame.entities.Score;
import org.jcarvajal.minigame.exceptions.UserNotFoundException;
import org.jcarvajal.minigame.service.ScoreService;
import org.jcarvajal.minigame.service.SessionService;

/**
 * Controller for Level operations:
 * - Post a new score per user/level.
 * - Get the highest scores per level.
 * 
 * @author JoseCH
 *
 */
public class LevelController {
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private ScoreService scoreService;
	
	public void setScoreService(ScoreService scoreService) {
		this.scoreService = scoreService;
	}
	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	/**
	 * This method can be called several times per user and level and does not return anything. 
	 * Only requests with valid session keys shall be processed.
	 * 
	 * @param levelId
	 * @param sessionKey
	 * @param score
	 * @throws UserNotFoundException 
	 */
	@RequestMapping(url = "/{levelId}/score", method = RequestMethod.POST)
	public void addScore(@PathVariable(name = "levelId") int levelId,
			@RequestParam(attr = "sessionkey") String sessionKey, @RequestBody int score) 
					throws UserNotFoundException {
		if (sessionService.containsSessionKey(sessionKey)) {
			int userId = sessionService.getUserIdBySessionKey(sessionKey);
			scoreService.updateScore(levelId, userId, score);
		}
	}
	
	/**
	 * Retrieves the high scores for a specific level. The result is a comma 
	 * separated list in descending score order. Because of memory reasons no more 
	 * than 15 scores are to be returned for each level. Only the highest score counts. 
	 * ie: an user id can only appear at most once in the list. If a user hasn't 
	 * submitted a score for the level, no score is present for that user. 
	 * A request for a high score list of a level without any scores submitted shall be an empty string.
	 * 
	 * @param levelId
	 * @return
	 */
	@RequestMapping(url = "/{levelId}/highscorelist", method = RequestMethod.GET)
	@ResponseMapping(map = "{userId}={score}")
	public Collection<Score> highScoreList(@PathVariable(name = "levelId") int levelId) {
		return scoreService.getHighScoreListByLevel(levelId);
	}
}
