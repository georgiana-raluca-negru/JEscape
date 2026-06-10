package com.pao.escaperoom.service;

import com.pao.escaperoom.exception.EmailTakenException;
import com.pao.escaperoom.exception.UsernameTakenException;
import com.pao.escaperoom.model.*;

import java.util.List;
import java.util.Scanner;
import com.pao.escaperoom.service.AuditService;

public class MenuService {
    private final Scanner scanner;
    private final PlayerService playerService;
    private final GameService gameService;
    private final MapService mapService;
    private final LeaderboardService leaderboardService;

    public MenuService(){
        this.scanner = new Scanner(System.in);
        this.playerService = PlayerService.getInstance();
        this.gameService = GameService.getInstance();
        this.mapService = MapService.getInstance();
        this.leaderboardService = LeaderboardService.getInstance();
    }

    public void start(){
        System.out.println("=========================================");
        System.out.println("       WELCOME TO THE ESCAPE ROOM        ");
        System.out.println("=========================================");

        while(true){
            PlayerProfile player = authenticatePlayer();
            if(player == null){
                System.out.println("Exiting game. Goodbye!");
                break;
            }

            boolean loggedIn = true;
            while(loggedIn){
                loggedIn = playerDashboard(player);
            }
        }
    }

    private PlayerProfile authenticatePlayer(){
        while(true){
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Create New Profile");
            System.out.println("2. Login");
            System.out.println("3. View Global Leaderboard");
            System.out.println("4. Quit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();
            PlayerProfile player = null;
            switch (choice){
                case "1":
                    player = register();
                    break;
                case "2":
                    player = login();
                    break;
                case "3":
                    AuditService.getInstance().log("view_leaderboard");
                    leaderboardService.displayTopResults(10);
                    break;
                case "4":
                    return null;
                default:
                    System.out.println("Invalid choice.");
            }

            if(player != null){
                return player;
            }
        }
    }

    private boolean playerDashboard(PlayerProfile player){
        System.out.println("\n--- LOGGED IN AS: " + player.getUsername().toUpperCase() + " ---");
        System.out.println("Title: " + player.getTitle().getDisplayName());

        System.out.println("1. Play Escape Room");
        System.out.println("2. View My Game History");
        System.out.println("3. Edit Profile (Bio & Title)");
        System.out.println("4. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine().trim();

        switch (choice){
            case "1":
                GameMap map = selectMap();
                Difficulty difficulty = selectDifficulty();
                gameService.startNewGame(player, map, difficulty);
                System.out.println("\nReturning to Player Dashboard...\n");
                break;
            case "2":
                AuditService.getInstance().log("view_history");
                viewPersonalHistory(player);
                break;
            case "3":
                AuditService.getInstance().log("update_title");
                editProfile(player);
                break;
            case "4":
                AuditService.getInstance().log("logout");
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid choice");
        }
        return true;
    }

    private void viewPersonalHistory(PlayerProfile player) {
        System.out.println("\n--- MY ESCAPE HISTORY ---");
        List<GameResult> history = player.getGameHistory();

        if (history.isEmpty()) {
            System.out.println("You haven't played any games yet.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i).toString());
            }
        }
    }

    private void editProfile(PlayerProfile player){
        System.out.println("\n--- EDIT PROFILE ---");
        System.out.println("Current Title: " + player.getTitle().getDisplayName());

        System.out.println("Available Titles:");
        PlayerTitle[] availableTitles = PlayerTitle.values();

        for(int i=0; i < availableTitles.length; i++){
            System.out.println((i+1) + " " + availableTitles[i].getDisplayName());
        }
        System.out.println("0.Cancel");
        System.out.println("Choose your new title (enter number): ");

        String choice = scanner.nextLine().trim();

        try{
            int index = Integer.parseInt(choice);

            if(index == 0){
                System.out.println("Edit canceled. Returning to dashboard...");
                return;
            }

            if(index >=0 && index <= availableTitles.length){
                PlayerTitle selectedTitle = availableTitles[index -1 ];

                boolean success = playerService.updatePlayerTitle(player.getUsername(), selectedTitle);
                if(success){
                    System.out.println("Success! Your title is now: " + selectedTitle.getDisplayName());
                }
                else {
                    System.out.println("An error occured while updating the title .");
                }
            }
            else {
                System.out.println("Invalid chocie. Please choose a valid number.");
            }
        }
        catch(NumberFormatException e){
            System.out.println("Invalid input. PLease enter a number.");
        }
    }

    private PlayerProfile login(){
        AuditService.getInstance().log("login");
        System.out.println("Enter Username or Email: ");
        String input = scanner.nextLine().trim();
        PlayerProfile player = playerService.findPlayer(input);

        if(player != null){
            System.out.println("Welcome back, " + player.getUsername() + "!");
            return player;
        }
        else {
            System.out.println("Profile not found. Please try again or create a new one.");
            return null;
        }
    }

    private PlayerProfile register(){
        AuditService.getInstance().log("register");
        System.out.println("Choose your username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter email: ");
        String email = scanner.nextLine().trim();

        PlayerProfile newPlayer = new PlayerProfile(username, email);
        try{
            playerService.addPlayer(newPlayer);
        }
        catch (UsernameTakenException | EmailTakenException e){
            System.out.println(e.getMessage());
            System.out.println(" Please try again.");
            return null;
        }

        System.out.println("Profile created successfully!");
        return newPlayer;
    }

    private GameMap selectMap(){
        List<GameMap> maps = mapService.getAllMaps();
        while(true){
            System.out.println("\n--- SELECT MISSION ---");
            for(int i=0; i< maps.size(); i++){
                System.out.println((i+1) + ". " + maps.get(i).getName() + " - " + maps.get(i).getDescription());
            }
            System.out.println("Choose map number");
            String choice = scanner.nextLine().trim();

            try{
                int index = Integer.parseInt(choice) - 1;
                if(index >=0 && index < maps.size()){
                    return maps.get(index);
                }
                else {
                    System.out.println("Invalid map number. Try again");
                }
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private Difficulty selectDifficulty(){
        while(true){
            System.out.println("\n--- SELECT DIFFICULTY ---");
            System.out.println("1. EASY (20 min)");
            System.out.println("2. MEDIUM (10 min)");
            System.out.println("3. HARD (5 min)");
            System.out.print("Choose difficulty: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) return Difficulty.EASY;
            if (choice.equals("2")) return Difficulty.MEDIUM;
            if (choice.equals("3")) return Difficulty.HARD;

            System.out.println("Invalid selection.");
        }
    }
}
