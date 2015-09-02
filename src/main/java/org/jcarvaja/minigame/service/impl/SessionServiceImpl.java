package org.jcarvaja.minigame.service.impl;

import org.jcarvaja.minigame.infrastructure.SessionRepository;
import org.jcarvaja.minigame.service.SessionService;
import org.jcarvajal.marti.annotations.Autowired;

public class SessionServiceImpl implements SessionService {
	@Autowired
	private SessionRepository sessionRepository;
}
