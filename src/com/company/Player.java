package com.company;

public class Player implements Runnable{

    private Game game;
    private String name;
    private PingPongPlayerType type;

    public Player(Game game, String name, PingPongPlayerType type) {
        this.game = game;
        this.name = name;
        this.type = type;

        switch (type) {
            case PINGER:
                if (game.getPingerName() == null) {
                    game.setPingerName(name);
                }
                break;

            case PONGER:
                if (game.getPongerName() == null) {
                    game.setPongerName(name);
                }
                break;

            default:
                System.out.println("No such type");
        }
    }

    @Override
    public void run() {
        switch (type) {
            case PINGER:
                game.ping();
                break;

            case PONGER:
                game.pong();
                break;

            default:
                System.out.println("No such type");
        }
    }
}
