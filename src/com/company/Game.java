package com.company;

import java.util.Random;

public class Game {
    private static final int MIN_SLEEP_TIME = 300;
    private static final int MAX_SLEEP_TIME = 1500;
    private static final int MAX_SHOT_TIME = 1000;
    private static final int WIN_POINTS = 10;
    private static final long MAX_GAME_TIME = 20_000;
    private static final long MAX_SET_TIME = 2000;

    private boolean isSetStarted = false;
    private boolean isSetFinished = false;
    private boolean isGameFinished = false;

    private long gameStartTime;
    private long setStartTime;

    private int pingerPoints;
    private int pongerPoints;

    private String pingerName;
    private String pongerName;

    public synchronized void ping() {
        if (isSetFinished) {
            return;
        }
        if (!isSetStarted) {
            isSetStarted = true;
            setStartTime = System.currentTimeMillis();
            System.out.println(pingerName + " starting the set.");
        } else {
            if (System.currentTimeMillis() - setStartTime > MAX_SET_TIME) {
                isSetFinished = true;
                if (checkEndGameConditions()) {
                    isGameFinished = true;
                }
                System.out.println("Set tied.");
            } else {
                boolean isShotMade = makeShot();
                if (!isShotMade) {
                    isSetFinished = true;
                    pongerPoints++;
                    if (checkEndGameConditions()) {
                        isGameFinished = true;
                    }
                }
                printShotMessage(isShotMade, pingerName);
            }
        }
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pong() {
        if (isSetFinished) {
            notify();
            return;
        }
        while (!isSetStarted){}

        if (System.currentTimeMillis() - setStartTime > MAX_SET_TIME) {
            isSetFinished = true;
            if (checkEndGameConditions()) {
                isGameFinished = true;
            }
            System.out.println("Set tied.");
        } else {
            boolean isShotMade = makeShot();
            if (!isShotMade) {
                isSetFinished = true;
                pingerPoints++;
                if (checkEndGameConditions()) {
                    isGameFinished = true;
                }
            }
            printShotMessage(isShotMade, pongerName);
        }
        notify();
    }

    private boolean checkEndGameConditions() {
        boolean ret = pingerPoints == WIN_POINTS || pongerPoints == WIN_POINTS;
        ret = ret || (System.currentTimeMillis() - gameStartTime > MAX_GAME_TIME);
        return ret;
    }

    private boolean makeShot() {
        long timeToSleep = getSleepTime();
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return timeToSleep <= MAX_SHOT_TIME;
    }

    private void printShotMessage(boolean isShotMade, String name) {
        System.out.println(name + (isShotMade ?
                " made successful shot" :
                " missed..."));
    }

    public void updateFlags() {
        isGameFinished = false;
        isSetStarted = false;
        isSetFinished = false;
    }

    public void printResult() {
        if (isGameFinished) {
            System.out.println("-------------------");
            System.out.println("Game is finished");
            printScore();
            printWinner();
            System.out.println("-------------------");
        }
    }

    public void printScore() {
        System.out.println("Score: " + pingerName + " " + pingerPoints + ":" + pongerPoints + " " + pongerName);
    }

    private void printWinner() {
        System.out.println(pingerPoints == pongerPoints ?
                "Game is tied" :
                (pingerPoints > pongerPoints ? pingerName : pongerName) + " won the game!");
    }

    public long getSleepTime() {
        return MIN_SLEEP_TIME + new Random().nextInt(MAX_SLEEP_TIME - MIN_SLEEP_TIME + 1);
    }

    public boolean isSetFinished() {
        return isSetFinished;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public String getPingerName() {
        return pingerName;
    }

    public void setPingerName(String pingerName) {
        this.pingerName = pingerName;
    }

    public String getPongerName() {
        return pongerName;
    }

    public void setStartTime() {
        this.gameStartTime = System.currentTimeMillis();
    }

    public void setPongerName(String pongerName) {
        this.pongerName = pongerName;
    }
}
