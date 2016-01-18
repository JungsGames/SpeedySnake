package com.jung.game.screen;

import java.util.List;

import com.jung.framework.intf.BannerListener;
import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Screen;
import com.jung.framework.intf.Input.TouchEvent;
import com.jung.game.main.Assets;
import com.jung.game.main.Settings;

public class HighscoreScreen extends Screen {
	String lines[] = new String[5];
	BannerListener bannerListener;

	public HighscoreScreen(Game game) {
		super(game);
		
		for (int i=0; i<5; i++) {
			lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		bannerListener = (BannerListener) game;
		bannerListener.showBanner(true);
		
		int len = touchEvents.size();
		for (int i=0; i<len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x < 100 && event.y > 1165) {
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g= game.getGraphics();
		
		g.clear(0x22B24C);
		g.drawPixmap(Assets.mainMenu, 160, 250, 0, 100, 480, 60);
		
		int y = 500;
		for (int i=0; i<5; i++) {
			drawText(g, lines[i], 320, y);
			y += 75;
		}
		
		g.drawPixmap(Assets.buttons, 0, 1165, 0, 220, 100, 100);
	}
	
	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		for (int i=0; i<len; i++) {
			char character = line.charAt(i);
			
			if (character == ' ') {
				x += 30;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			if (character == '.') {
				srcX = 300;
				srcWidth = 10;
			} else {
				srcX = (character - '0') * 30;
				srcWidth = 30;
			}
			
			g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 43);
			x += srcWidth + 5;
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	

}
