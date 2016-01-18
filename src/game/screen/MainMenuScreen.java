package com.jung.game.screen;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.jung.framework.impl.AndroidGame;
import com.jung.framework.intf.BannerListener;
import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Input.TouchEvent;
import com.jung.framework.intf.Screen;
import com.jung.game.main.Assets;

public class MainMenuScreen extends Screen {
	BannerListener bannerListener;
	
	public MainMenuScreen(Game game) {
		super(game);
		bannerListener = (BannerListener) game;
		bannerListener.showBanner(false);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 160, 505, 480, 60)) {
					game.setScreen(new GameScreen(game));
					return;
				}
				if (inBounds(event, 160, 505 + 100, 480, 60)) {
					game.setScreen(new HighscoreScreen(game));
					return;
				}
				if (inBounds(event, 160, 505 + 200, 480, 60)) {
					game.setScreen(new HelpScreen(game));
					return;
				}
				if (inBounds(event, 160, 505 + 295, 480, 65)) {
					Uri uri = Uri.parse("market://details?id=" + ((AndroidGame)game).getPackageName());
					Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
					try {
						((AndroidGame)game).startActivity(goToMarket);
						} catch (ActivityNotFoundException e) {
							((AndroidGame)game).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ((AndroidGame)game).getPackageName())));
						}
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clear(0x22B24C);
		g.drawPixmap(Assets.logo, 80, 150);
		g.drawPixmap(Assets.mainMenu, 160, 505);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	public boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "MainMenuScreen";
	}

}
