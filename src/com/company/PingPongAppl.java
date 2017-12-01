package com.company;

public class PingPongAppl {

    public static void main(String[] args) throws InterruptedException {

        Game game = new Game();

        do {
            game.updateFlags();
            game.setStartTime();
            while (!game.isSetFinished()) {
                Thread pingThread = new Thread(new Player(game, "Player 1", PingPongPlayerType.PINGER));
                Thread pongThread = new Thread(new Player(game, "Player 2", PingPongPlayerType.PONGER));

                pingThread.start();
                pongThread.start();

                pingThread.join();
                pongThread.join();
            }

            game.printScore();
            Thread.sleep(1000);

        } while (!game.isGameFinished());

        game.printResult();
    }
}
