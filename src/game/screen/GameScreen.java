package com.jung.game.screen;

import java.util.List;
import java.util.Random;

import android.graphics.Color;

import com.jung.framework.intf.BannerListener;
import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Input.KeyEvent;
import com.jung.framework.intf.Input.TouchEvent;
import com.jung.framework.intf.InterstitialListener;
import com.jung.framework.intf.Pixmap;
import com.jung.framework.intf.Screen;
import com.jung.game.main.Assets;
import com.jung.game.main.Settings;
import com.jung.game.model.Snake;
import com.jung.game.model.SnakePart;
import com.jung.game.model.Star;
import com.jung.game.model.World;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Pause, GameOver
	}

	// Declare Google Admob variables for advertisement
	InterstitialListener interstitialListener;
	BannerListener bannerListener;

	Random rand;
	GameState state = GameState.Ready;
	World world;
	// Accumulate deltaTime for fixed-time-step simulation
	float tickTime = 0;
	int oldScore = 0;
	String score = "0";
	private int oldTouchY;
	private int oldTouchX;
	// Allow TOUCH_DRAGGED to work nicely
	// If removed, the risk of turning backwards by rapid touch events increases
	private boolean isTouched = false;
	private boolean moveUpFlag = true;
	private boolean moveLeftFlag = false;
	private boolean moveDownFlag = false;
	private boolean moveRightFlag = false;
	// Decreases chances of Snake head turning backwards
	// by having a delay on each touch event
	private final float TOUCH_DELAY = 0.12f;

	public GameScreen(Game game) {
		super(game);
		world = new World();
		rand = new Random();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();

		if (state == GameState.Ready)
			updateReady(keyEvents, touchEvents);
		if (state == GameState.Running)
			// Music starts
			updateRunning(keyEvents, touchEvents, deltaTime);
		if (state == GameState.Pause)
			// Music pauses
			updatePaused(keyEvents, touchEvents);
		if (state == GameState.GameOver)
			// Music pauses
			updateGameOver(keyEvents, touchEvents);
	}

	private void updateReady(List<KeyEvent> keyEvents,
			List<TouchEvent> touchEvents) {

		int len = keyEvents.size();
		for (int i = 0; i < len; i++) {
			KeyEvent keyEvent = keyEvents.get(i);
			if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
				Assets.bgm.setLooping(true);
				bannerListener = (BannerListener) game;
				bannerListener.showBanner(true);
				state = GameState.Pause;
			}
		}
		if (touchEvents.size() > 0) {
			if (Settings.soundEnabled) {
				Assets.bgm.play();
				Assets.bgm.setLooping(true);
			}
			state = GameState.Running;
		}

	}

	private void updateRunning(List<KeyEvent> keyEvents,
			List<TouchEvent> touchEvents, float deltaTime) {

		tickTime += deltaTime;

		int len = touchEvents.size();

		if (tickTime > TOUCH_DELAY) {

			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);

				if (event.type == TouchEvent.TOUCH_DOWN) {
					oldTouchX = event.x;
					oldTouchY = event.y;
					isTouched = true;
				} else if (event.type == TouchEvent.TOUCH_DRAGGED) {
					if (event.x - oldTouchX < -40 && isTouched) {
						if (moveRightFlag) {
						} else {
							moveLeftFlag = true;
							moveRightFlag = false;
							moveUpFlag = false;
							moveDownFlag = false;
							isTouched = false;
							tickTime = 0;
							world.snake.turnLeft();
						}
					} else if (event.x - oldTouchX > 40 && isTouched) {
						if (moveLeftFlag) {
						} else {
							moveLeftFlag = false;
							moveRightFlag = true;
							moveUpFlag = false;
							moveDownFlag = false;
							isTouched = false;
							tickTime = 0;
							world.snake.turnRight();
						}
					} else if (event.y - oldTouchY < -40 && isTouched) {
						if (moveDownFlag) {
						} else {
							moveLeftFlag = false;
							moveRightFlag = false;
							moveUpFlag = true;
							moveDownFlag = false;
							isTouched = false;
							tickTime = 0;
							world.snake.turnUp();
						}
					} else if (event.y - oldTouchY > 40 && isTouched) {
						if (moveUpFlag) {
						} else {
							moveLeftFlag = false;
							moveRightFlag = false;
							moveUpFlag = false;
							moveDownFlag = true;
							isTouched = false;
							tickTime = 0;
							world.snake.turnDown();
						}
					}
				} else {
					if (event.x < 110 && event.y > 1280 - 88) {
						Settings.soundEnabled = !Settings.soundEnabled;
						if (Settings.soundEnabled)
							Assets.bgm.play();
						else
							Assets.bgm.pause();
					} else if (event.x > 700 && event.y > 1280 - 90) {
						if (Settings.soundEnabled)
							Assets.bgm.pause();
						state = GameState.Pause;
						bannerListener = (BannerListener) game;
						bannerListener.showBanner(true);
						return;
					}
				}
			}
		}
		world.update(deltaTime);
		if (world.gameOver) {
			if (Settings.soundEnabled) {
				Assets.bitten.play(0.4f);
				Assets.bgm.pause();
			}
			state = GameState.GameOver;
			bannerListener = (BannerListener) game;
			bannerListener.showBanner(true);
		}
		if (oldScore != world.score) {
			oldScore = world.score;
			score = "" + oldScore;
			if (Settings.soundEnabled)
				Assets.eat.play(0.5f);
		}
		int keyLength = keyEvents.size();
		for (int i = 0; i < keyLength; i++) {
			KeyEvent keyEvent = keyEvents.get(i);
			if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
				bannerListener = (BannerListener) game;
				bannerListener.showBanner(true);
				if (Settings.soundEnabled)
					Assets.bgm.pause();
				state = GameState.Pause;
			}
		}
	}

	private void updatePaused(List<KeyEvent> keyEvents,
			List<TouchEvent> touchEvents) {

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 255 && event.x <= 545) {
					if (event.y > 500 && event.y <= 590) {
						state = GameState.Running;
						if (Settings.soundEnabled)
							Assets.bgm.play();
						bannerListener = (BannerListener) game;
						bannerListener.showBanner(false);
						return;
					}
					if (event.y > 590 && event.y < 680) {
						Assets.bgm.seekTo(0);
						if (rand.nextInt(10) == 0) {
							interstitialListener = (InterstitialListener) game;
							interstitialListener.showInterstitial();
						}
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}

	private void updateGameOver(List<KeyEvent> keyEvents,
			List<TouchEvent> touchEvents) {

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 700 && event.y > 1180) {
					Assets.bgm.seekTo(0);
					if (rand.nextInt(10) == 0) {
						interstitialListener = (InterstitialListener) game;
						interstitialListener.showInterstitial();
					}
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.clear(0x22B24C);
		g.drawPixmap(Assets.grass, 400, 30, 0, 0, 19, 25);
		g.drawPixmap(Assets.grass, 650, 260, 20, 0, 19, 25);
		g.drawPixmap(Assets.grass, 215, 635, 0, 0, 19, 25);
		g.drawPixmap(Assets.grass, 60, 720, 20, 0, 19, 25);
		g.drawPixmap(Assets.grass, 212, 942, 0, 0, 19, 25);
		g.drawPixmap(Assets.grass, 670, 1090, 20, 0, 19, 25);
		drawWorld(world);
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Pause)
			drawPauseUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

		drawText(g, score, g.getWidth() / 2 - score.length() * 30 / 2,
				g.getHeight() - 63);

	}

	private void drawWorld(World world) {
		Graphics g = game.getGraphics();
		Snake snake = world.snake;
		SnakePart head = snake.parts.get(0);
		Star star = world.star;

		Pixmap starPixmap = null;
		if (star.type == Star.TYPE_1)
			starPixmap = Assets.star1;
		if (star.type == Star.TYPE_2)
			starPixmap = Assets.star2;
		if (star.type == Star.TYPE_3)
			starPixmap = Assets.star3;
		if (star.type == Star.TYPE_4)
			starPixmap = Assets.star4;
		int x = star.x * 80 + 20;
		int y = star.y * 80 + 20;
		g.drawPixmap(starPixmap, x, y);

		int len = snake.parts.size();
		for (int i = 1; i < len; i++) {
			SnakePart part = snake.parts.get(i);
			x = part.x * 80;
			y = part.y * 80;
			g.drawPixmap(Assets.tail, x, y);
		}

		Pixmap headPixmap = null;
		if (snake.direction == Snake.UP)
			headPixmap = Assets.headUp;
		if (snake.direction == Snake.LEFT)
			headPixmap = Assets.headLeft;
		if (snake.direction == Snake.DOWN)
			headPixmap = Assets.headDown;
		if (snake.direction == Snake.RIGHT)
			headPixmap = Assets.headRight;
		x = head.x * 80 + 40;// question
		y = head.y * 80 + 40;
		g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2,
				y - headPixmap.getHeight() / 2);
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.ready, 245, 225);
		g.drawLine(0, 1190, 800, 1190, Color.WHITE);
	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.buttons, g.getWidth() - 100, g.getHeight() - 90, 0,
				110, 100, 90);
		if (Settings.soundEnabled)
			g.drawPixmap(Assets.buttons, 0, g.getHeight() - 88, 0, 0, 110, 88);
		else
			g.drawPixmap(Assets.buttons, 4, g.getHeight() - 88, 120, 0, 63, 88);
		g.drawLine(0, 1190, 800, 1190, Color.WHITE);
	}

	private void drawPauseUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.pause, 255, 500);
		g.drawLine(0, 1190, 800, 1190, Color.WHITE);
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.gameOver, 180, 550);
		g.drawPixmap(Assets.buttons, 700, 1185, 100, 110, 100, 90);
		g.drawLine(0, 1190, 800, 1190, Color.WHITE);
	}

	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
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
		if (state == GameState.Running)
			state = GameState.Pause;
		if (world.gameOver) {
			Settings.addScore(world.score);
			Settings.save(game.getFileIO());
		}
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public String toString() {
		return "GameScreen";
	}

}
