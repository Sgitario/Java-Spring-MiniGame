package org.jcarvajal.minigame;

import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.*;

import java.util.UUID;

import org.jcarvajal.framework.rest.RestServer;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.utils.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainIntegrationTest {
	
	private static final String URL = "http://localhost:8081/";
	
	private int currentUserId;
	private String currentSessionKey;
	private String actualHighestScores;
	private RestServer server;
	
	@Before
	public void setup() throws OnRestInitializationException {		
		server = new RestServer();
		server.start();
	}
	
	@After
	public void after() {
		server.stop();
		
		currentUserId = -1;
		currentSessionKey = null;
		actualHighestScores = null;
	}
	
	@Test
	public void login_whenUserIsNotLogged_thenSessionIsNotNull() {
		whenUserLogin(100);
		thenSessionKeyIsNotNull();
	}
	
	@Test
	public void login_whenUserIsLogged_thenSessionDoesNotChange() {
		String sessionKey = whenUserLogin(100);
		whenUserLogin(100);
		thenSessionKeyEquals(sessionKey);
	}
	
	@Test
	public void postScore_whenUserNotLogged_thenScoreNotSaved() {
		givenRandomSessionKey();
		whenPostScore(1, 500);
		whenRetrieveScores(1);
		thenScoresAreEmpty();
	}
	
	@Test
	public void postScore_whenUserLogged_thenScoreSaved() {
		whenUserLogin(100);
		whenPostScore(1, 500);
		whenRetrieveScores(1);
		thenScoresAre("100=500");
	}
	
	@Test
	public void postScore_whenUserAddLowerScore_thenScoreNotUpdated() {
		whenUserLogin(100);
		whenPostScore(1, 500);
		whenPostScore(1, 200);
		whenRetrieveScores(1);
		thenScoresAre("100=500");
	}
	
	@Test
	public void postScore_whenUserAddHigherScore_thenScoreUpdated() {
		whenUserLogin(100);
		whenPostScore(1, 500);
		whenPostScore(1, 800);
		whenRetrieveScores(1);
		thenScoresAre("100=800");
	}
	
	@Test
	public void postScore_whenManyUsers_thenScoreUpdated() {
		for (int user = 0; user < 20; user++) {
			whenUserLogin(user);
			whenPostScore(1, user + 10);
		}
		
		whenRetrieveScores(1);
		thenScoresAre("19=29,18=28,17=27,16=26,15=25,14=24,13=23,12=22,11=21,10=20,9=19,8=18,7=17,6=16,5=15");
	}
	
	private void givenRandomSessionKey() {
		currentSessionKey = UUID.randomUUID().toString();
	}
	
	private void whenRetrieveScores(int levelId) {
		actualHighestScores = get(URL + levelId + "/highscorelist")
				.asString();
	}
	
	private void whenPostScore(int levelId, int score) {
		given()
			.body(score)
			.post(URL + levelId + "/score?sessionkey=" + currentSessionKey)
			.then().statusCode(200);
	}

	private String whenUserLogin(int userId) {
		currentUserId = userId;
		currentSessionKey = get(URL + currentUserId + "/login").asString();
		return currentSessionKey;
	}
	
	private void thenSessionKeyIsNotNull() {
		assertNotNull(currentSessionKey);
	}
	
	private void thenSessionKeyEquals(String sessionKey) {
		assertEquals(sessionKey, currentSessionKey);
	}
	
	private void thenScoresAreEmpty() {
		assertFalse(StringUtils.isNotEmpty(actualHighestScores));
	}
	
	private void thenScoresAre(String expected) {
		assertEquals(expected, actualHighestScores);
	}
}
