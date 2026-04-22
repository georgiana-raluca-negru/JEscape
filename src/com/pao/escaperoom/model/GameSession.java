package com.pao.escaperoom.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.pao.escaperoom.exception.InvalidMoveException;

public class GameSession {
    private PlayerProfile player;
    private GameMap map;
    private Difficulty difficulty;

    private List<Item> inventory;
    private Location currentLocation;

    private LocalDateTime startTime;
    private boolean isGameOver;
    private boolean isWon;

    public GameSession(PlayerProfile player, GameMap map, Difficulty difficulty) {
        this.player = player;
        this.map = map;
        this.difficulty = difficulty;

        this.inventory = new ArrayList<>();
        this.currentLocation = map.getStartingLocation();

        this.isGameOver = false;
        this.isWon = false;
    }

    public void start(){

        startTime = LocalDateTime.now();
    }


    public  long getElapsedSeconds() {
        if (startTime == null){
            return 0;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        return Duration.between(startTime, currentTime).getSeconds();
    }

    public boolean hasTimeExpired() {
        long limitSeconds = difficulty.getTimeLimitMinutes() * 60L;
        return getElapsedSeconds() >= limitSeconds;
    }

    public void addItemToInventory(Item item) {
        if (!isGameOver()) {
            this.inventory.add(item);
        }
    }

    public void removeItemFromInventory(Item item) {
        this.inventory.remove(item);
    }

    public Item findItemByName(String objectName){
        if(objectName == null || inventory == null){
            return null;
        }

        for(Item object : inventory){
            if(object.getName().equalsIgnoreCase(objectName.trim())){
                return object;
            }
        }

        return null;
    }

    public String movePlayer(Direction direction) throws InvalidMoveException {
        Location nextLocation = currentLocation.getExit(direction);

        if(nextLocation == null){
            throw new InvalidMoveException("You cannot go " + direction.name().toLowerCase() + ". There is a dead end.");
        }

        this.currentLocation = nextLocation;

        return "You head " + direction.name().toLowerCase() + " into the " + currentLocation.getName() + ".\n\n";
    }

    public GameResult finishGame(boolean hasEscaped){
        this.isGameOver = true;
        this.isWon = hasEscaped;
        long timeTaken = getElapsedSeconds();

        return new GameResult(
                this.player.getUsername(),
                this.map.getName(),
                this.difficulty,
                hasEscaped,
                timeTaken
        );
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public GameMap getMap() {
        return map;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isGameOver() {
        return isGameOver || isWon || hasTimeExpired();
    }

    public boolean isWon() {
        return isWon;
    }

    public void setIsWon(boolean gameWon){ this.isWon = gameWon; }

    @Override
    public String toString() {
        return "GameSession{" +
                "player=" + player.getUsername() +
                ", map=" + map.getName() +
                ", difficulty=" + difficulty +
                ", currentLocation=" + currentLocation.getName() +
                ", isGameOver=" + isGameOver +
                ", inventorySize=" + inventory.size() +
                '}';
    }
}
