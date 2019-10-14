package com.gadarts.parashoot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Main;
import com.gadarts.parashoot.model.MentorsFactory;
import com.gadarts.parashoot.model.MentorsManager;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;

import static com.gadarts.parashoot.model.MentorsManager.MentorState.FINISHED;

/**
 * Created by Gad on 28/10/2016.
 */

public abstract class BasicMenuScreen extends BasicScreen {

    protected Timer timer = new Timer();
    private Main.MenuType nextScreen;
    private Main.MenuType backScreen;
    private MentorsFactory mentorsFactory;
    private Queue<MentorSchedule> mentorsScheduledQueue = new Queue<MentorSchedule>();

    @Override
    public void show() {
        super.show();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(menuStage);
        Gdx.input.setInputProcessor(multiplexer);
        getLoadingDoors().toFront();
    }

    @Override
    public void dispose() {
        super.dispose();
        mentorsFactory.dispose();
    }

    protected void scheduleMentor(String name, final MentorTask task) {
        MentorsManager mentorsManager = Main.getMentorsManager();
        if (mentorsManager.getMentorState(name) == MentorsManager.MentorState.READY) {
            mentorsScheduledQueue.addLast(new MentorSchedule(task));
            if (mentorsScheduledQueue.size == 1) {
                doNextMentor(Rules.Menu.Mentors.INITIAL_DELAY);
            }
        }
    }

    private void doNextMentor(float delay) {
        if (mentorsScheduledQueue.size > 0) {
            mentorsScheduledQueue.first().schedule(delay);
        }
    }

    public void invokeAdditionalInfo(HashMap<String, Integer> additionalInfo) {
    }

    public MentorsFactory getMentorsFactory() {
        return mentorsFactory;
    }

    public void setNextScreen(Main.MenuType nextScreen) {
        this.nextScreen = nextScreen;
    }

    public void setBackScreen(Main.MenuType backScreen) {
        this.backScreen = backScreen;
    }

    public Main.MenuType getNextScreen() {
        return nextScreen;
    }

    public Main.MenuType getBackScreen() {
        return backScreen;
    }

    public void setMentorsFactory(MentorsFactory mentorsFactory) {
        this.mentorsFactory = mentorsFactory;
    }

    protected void showMentor(String name, String message, SequenceAction actions, Skin skin) {
        if (Main.getInstance().getScreen() == BasicMenuScreen.this) {
            initializeMentor(name, message);
            Image pointMentor = getMentorsFactory().createMentor(actions, skin);
            menuStage.addActor(pointMentor);
        }
    }

    private void initializeMentor(String name, String message) {
        Main.getSoundPlayer().playSound(SFX.Menu.SYSTEM_CONFIRM);
        menuStage.addActor(mentorsFactory.createMentor(message));
        Main.getMentorsManager().setMentorState(name, FINISHED);
    }

    protected void showMentor(String name, String message, Actor pointOn, Skin skin,
                              BasicMenuScreen screen) {
        if (Main.getInstance().getScreen() == screen) {
            initializeMentor(name, message);
            if (pointOn != null) {
                Image pointMentor = mentorsFactory.createMentor(pointOn, skin);
                menuStage.addActor(pointMentor);
            }
        }
    }

    protected class MentorTask extends Timer.Task {
        @Override
        public void run() {
            mentorsScheduledQueue.removeFirst();
            doNextMentor(Rules.Menu.Mentors.WAIT_TIME);
        }
    }

    private class MentorSchedule {
        private final MentorTask task;

        public MentorSchedule(MentorTask task) {
            this.task = task;
        }

        public void schedule(float interval) {
            if (Main.getInstance().getScreen() instanceof BasicMenuScreen) {
                timer.scheduleTask(task, interval);
            }
        }
    }
}
