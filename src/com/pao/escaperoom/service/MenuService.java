package com.pao.escaperoom.service;

import com.pao.escaperoom.exception.EmailTakenException;
import com.pao.escaperoom.exception.UsernameTakenException;
import com.pao.escaperoom.model.*;

import java.util.List;
import java.util.Scanner;

public class MenuService {
    private final Scanner scanner;
    private final PlayerService playerService;
    private final GameService gameService;
    private final MapService mapService;

    public MenuService(){
        this.scanner = new Scanner(System.in);
        this.playerService = PlayerService.getInstance();
        this.gameService = GameService.getInstance();
        this.mapService = MapService.getInstance();
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

            GameMap map = selectMap();
            Difficulty difficulty = selectDifficulty();

            gameService.startNewGame(player, map, difficulty);

            System.out.println("\nReturning to Main Menu...\n");
        }
    }

    private PlayerProfile authenticatePlayer(){
        while(true){
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Create New Profile");
            System.out.println("2. Login");
            System.out.println("3. Quit");
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
                    return null;
                default:
                    System.out.println("Invalid choice.");
            }

            if(player != null){
                return player;
            }
        }
    }

    private PlayerProfile login(){
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
