package com.gadarts.parashoot.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Main;

public class ButtonClick extends ChangeListener {

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Main.getSoundPlayer().playSound(SFX.HUD.BUTTON_CLICK, false, false);
    }
}