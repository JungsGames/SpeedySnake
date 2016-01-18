package com.jung.game.model;

public class Star {
	public static final int TYPE_1 = 0;
	public static final int TYPE_2 = 1;
	public static final int TYPE_3 = 2;
	public static final int TYPE_4 = 3;
	public int x, y;
	public int type;
	
	public Star(int x, int y, int type) {
	this.x = x;
	this.y = y;
	this.type = type;
	}
}
