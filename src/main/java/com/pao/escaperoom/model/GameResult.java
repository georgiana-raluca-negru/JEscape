package com.pao.escaperoom.model;

public final class GameResult implements Comparable<GameResult> {
    private final String playerName;
    private final String mapName;
    private final Difficulty difficulty;

    private final boolean isWin;
    private final long timeTakenSeconds;

    public GameResult(String playerName, String mapName, Difficulty difficulty, boolean isWin, long timeTakenSeconds) {
        this.playerName = playerName;
        this.mapName = mapName;
        this.difficulty = difficulty;
        this.isWin = isWin;
        this.timeTakenSeconds = timeTakenSeconds;
    }

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

    @Override
    public int compareTo(GameResult other) {
        double myPoints = this.calculatePoints();
        double otherPoints = other.calculatePoints();

        int result = Double.compare(otherPoints, myPoints);

        if (result == 0) {
            return this.playerName.compareTo(other.playerName);
        }
        return result;
    }

    @Override
    public String toString() {
        String statusIcon = isWin ? " ESCAPED" : " FAILED ";

        long minutes = timeTakenSeconds / 60;
        long seconds = timeTakenSeconds % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        return String.format("%s | Map: %-20s | Diff: %-6s | Time: %s | Score: %7.2f",
                statusIcon,
                "\"" + mapName + "\"",
                difficulty.name(),
                timeFormatted,
                calculatePoints()
        );
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMapName() {
        return mapName;
    }

    public boolean isWin() {
        return isWin;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }
}
