package com.pao.escaperoom.model;

public class GameResult {
    private PlayerProfile player;
    private GameMap map;
    private Difficulty difficulty;

    private boolean isWin;
    private long timeTakenSeconds;

    public GameResult(PlayerProfile player, long timeTakenSeconds, boolean isWin, GameMap map, Difficulty difficulty) {
        this.player = player;
        this.timeTakenSeconds = timeTakenSeconds;
        this.isWin = isWin;
        this.map = map;
        this.difficulty = difficulty;
    }

    // aici e vorba de niste puncte pe care le capat pe parcursul seisunii?
    // ma gandesc
    public double calculatePoints(){
        if(!isWin){
            return 0.0;
        }

        long timeLimitSeconds = difficulty.getTimeLimitMinutes() * 60L;
        long secondsLeft = timeLimitSeconds - timeTakenSeconds;

        double basePoints = 1000.0;
        double timeBonus = secondsLeft * 5.0;
        double total = (basePoints + timeBonus)* difficulty.getMultiplier();

        return total;

    }
}
