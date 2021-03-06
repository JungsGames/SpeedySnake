package com.jung.game.screen;

import java.util.List;

import com.jung.framework.intf.Game;
import com.jung.framework.intf.Graphics;
import com.jung.framework.intf.Input.TouchEvent;
import com.jung.framework.intf.Screen;
import com.jung.game.main.Assets;

public class HelpScreen extends Screen {      
    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if (event.x < 100 && event.y > 1165) {
            		game.setScreen(new MainMenuScreen(game));
            		return;
            	}
                if(event.x > 700 && event.y > 1165 ) {
                    game.setScreen(new HelpScreen2(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.clear(0x22B24C);
        g.drawPixmap(Assets.help1, 225, 100);
        g.drawPixmap(Assets.buttons, 0, 1165, 0, 220, 100, 100);
        g.drawPixmap(Assets.buttons, 700, 1165, 100, 220, 100, 100);
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
}
