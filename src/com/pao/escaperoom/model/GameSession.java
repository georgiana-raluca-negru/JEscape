package com.pao.escaperoom.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameSession {
    // i need these to create e game session
    private PlayerProfile player;
    private GameMap map;
    private Difficulty difficulty;

    // player state ( inventory and where i am )
    private List<Item> inventory;
    private Location currentLocation;

    // the state of the session itself
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

    // pornesc jocul
    // practic o sa am oleaca de story pana sa incep si apoi dupa o sa mi pornesc timpul pentru sesiune
    public void start(){
        startTime = LocalDateTime.now();
    }

    // seconds spent gaming
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
        if (!isGameOver) {
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

    // change the cuurent location towards a direction
    public String movePlayer(Direction direction) {
        Location nextLocation = currentLocation.getExit(direction);

        if(nextLocation == null){
            return "You cannot go this way. There's a dead end.";
        }

        this.currentLocation = nextLocation;

        return "You have moved.";
    }

    public GameResult finishGame(boolean hasEscaped){
        this.isGameOver = true;
        long timeTaken = getElapsedSeconds();

        GameResult result = new GameResult(
                this.player.getUsername(),
                this.map.getName(),
                this.difficulty,
                hasEscaped,
                timeTaken
        );

        return result;
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
        return isGameOver;
    }

    public boolean isWon() {
        return isWon;
    }

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
