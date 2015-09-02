package org.jcarvaja.minigame.service.impl;

import org.jcarvaja.minigame.infrastructure.ScoreRepository;
import org.jcarvaja.minigame.service.ScoreService;
import org.jcarvajal.marti.annotations.Autowired;

public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreRepository scoreRepository;
}
