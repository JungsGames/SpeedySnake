package com.jung.game.main;

import com.jung.framework.impl.AndroidGame;
import com.jung.framework.intf.Screen;
import com.jung.game.screen.LoadingScreen;

public class SnakeGame extends AndroidGame {
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

}
