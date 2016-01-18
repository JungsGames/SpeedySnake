package com.jung.game.model;

import java.util.Random;

import android.util.Log;

public class World {
	static final int WORLD_WIDTH = 10;
	static final int WORLD_HEIGHT = 15;
	static final int SCORE_INCREMENT = 10;
	static final float TICK_INITIAL = 0.26f;
	static final float TICK_DECREMENT = 0.02f;
	
	public Snake snake;
	public Star star;
	public boolean gameOver = false;
	public int score = 0;
	public int scoreBound = 50;
	
	boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	Random random = new Random();
	float tickTime = 0;
	float tick = TICK_INITIAL;
	
	public World() {
		snake = new Snake();
		placeStar();
	}
	
	private void placeStar() {
		for (int x=0; x < WORLD_WIDTH; x++) {
			for (int y=0;y < WORLD_HEIGHT; y++) {
				fields[x][y] = false;
			}
		}
		
		int len = snake.parts.size();
		for (int i=0;i<len;i++) {
			SnakePart part = snake.parts.get(i);
			fields[part.x][part.y] = true;
		}
		
		int starX = random.nextInt(WORLD_WIDTH);
		int starY = random.nextInt(WORLD_HEIGHT);
		while (true) {
			if (fields[starX][starY] == false)
				break;
			starX += 1;
			if (starX >= WORLD_WIDTH) {
				starX = 0;
				starY += 1;
				if(starY >= WORLD_HEIGHT) {
					starY = 0;
				}
			}
		}
		star = new Star(starX, starY, random.nextInt(4));
	}
	
	public void update(float deltaTime) {
		if (gameOver)
			return;
		
		tickTime += deltaTime;
		
		while (tickTime > tick) {
			tickTime -= tick;
			snake.advance();
			if (snake.checkBitten()) {
				gameOver = true;
				return;
			}
			
			SnakePart head = snake.parts.get(0);
			if (head.x == star.x && head.y == star.y) {
				score += SCORE_INCREMENT;
				snake.eat();
				if (snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
					gameOver = true;
					return;
				} else {
					placeStar();
				}
				
				if (score % scoreBound == 0 && tick - TICK_DECREMENT > 0.2) {
					tick -= TICK_DECREMENT;
					if (scoreBound >= 450)
						scoreBound += 200;
					else if (scoreBound >= 150)
						scoreBound += 150;
					else
						scoreBound += 50;
					Log.d("Game", Integer.toString(scoreBound));
					Log.d("Game", Float.toString(tick));
				}
			}
		}	
	}
}
