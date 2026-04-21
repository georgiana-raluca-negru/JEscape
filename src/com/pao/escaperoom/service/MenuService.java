package com.pao.escaperoom.service;

import com.pao.escaperoom.model.*;

import java.util.List;
import java.util.Scanner;

public class MenuService {
    private Scanner scanner;
    private PlayerService playerService;
    private GameService gameService;
    private MapService mapService;

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
            Difficulty difficuly = selectDifficulty();

            gameService.startNewGame(player, map, difficuly);

            System.out.println("\nReturning to Maint Menu...\n");
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
                    System.out.println("Invalid chocie.");
            }

            if(player != null){
                return player;
            }
        }
    }

    private PlayerProfile login(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine().trim();
        PlayerProfile player = playerService.findPlayerByName(username);

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
        if(playerService.addPlayer(newPlayer)){
            System.out.println("Profile creater successfully!");
            return  newPlayer;
        }
        else {
            System.out.println("Username is already taken. Try a different one.");
            return null;
        }
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
            System.out.println("1. EASY (60 min)");
            System.out.println("2. MEDIUM (45 min)");
            System.out.println("3. HARD (30 min)");
            System.out.print("Choose difficulty: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) return Difficulty.EASY;
            if (choice.equals("2")) return Difficulty.MEDIUM;
            if (choice.equals("3")) return Difficulty.HARD;

            System.out.println("Invalid selection.");
        }
    }
}
