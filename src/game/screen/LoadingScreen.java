package com.jung.game.screen;

import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Graphics.PixmapFormat;
import com.jung.framework.intf.Screen;
import com.jung.game.main.Assets;
import com.jung.game.main.Settings;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.logo = g.newPixmap("logo.png", PixmapFormat.ARGB4444);
		Assets.mainMenu = g.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
		Assets.buttons = g.newPixmap("buttons.png", PixmapFormat.ARGB4444);
		Assets.help1 = g.newPixmap("help1.png", PixmapFormat.ARGB4444);
		Assets.help2 = g.newPixmap("help2.png", PixmapFormat.ARGB4444);
		Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB4444);
		Assets.ready = g.newPixmap("ready.png", PixmapFormat.ARGB4444);
		Assets.pause = g.newPixmap("pause.png", PixmapFormat.ARGB4444);
		Assets.gameOver = g.newPixmap("gameover.png", PixmapFormat.ARGB4444);
		Assets.headUp = g.newPixmap("headup.png", PixmapFormat.ARGB4444);
		Assets.headLeft = g.newPixmap("headleft.png", PixmapFormat.ARGB4444);
		Assets.headDown = g.newPixmap("headdown.png", PixmapFormat.ARGB4444);
		Assets.headRight = g.newPixmap("headright.png", PixmapFormat.ARGB4444);
		Assets.tail = g.newPixmap("tail.png", PixmapFormat.ARGB4444);
		Assets.star1 = g.newPixmap("star1.png", PixmapFormat.ARGB4444);
		Assets.star2 = g.newPixmap("star2.png", PixmapFormat.ARGB4444);
		Assets.star3 = g.newPixmap("star3.png", PixmapFormat.ARGB4444);
		Assets.star4 = g.newPixmap("star4.png", PixmapFormat.ARGB4444);
		Assets.grass = g.newPixmap("grass.png", PixmapFormat.ARGB4444);
		Assets.eat = game.getAudio().newSound("Pickup_Coin3.wav");
		Assets.bitten = game.getAudio().newSound("bitten.wav");
		Assets.bgm = game.getAudio().newMusic("odyssey.mp3");
		Settings.load(game.getFileIO());
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub

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
