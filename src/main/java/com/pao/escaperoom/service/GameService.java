package com.pao.escaperoom.service;

import com.pao.escaperoom.commands.CommandParser;
import com.pao.escaperoom.model.*;

import java.util.Scanner;

public class GameService {
    private static GameService instance;
    private final Scanner scanner;
    private final CommandParser commandParser;

    private GameService() {
        this.commandParser = new CommandParser();
        this.scanner = new Scanner(System.in);
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void startNewGame(PlayerProfile player, GameMap map, Difficulty difficulty) {
        System.out.println("\n=========================================");
        System.out.println(" LOADING MISSION: " + map.getName().toUpperCase());
        System.out.println(" DIFFICULTY: " + difficulty.name());
        System.out.println("=========================================\n");

        GameSession session = new GameSession(player, map, difficulty);
        session.start();

        System.out.println(map.getDescription());
        System.out.println("\nYou wake up in the " + session.getCurrentLocation().getName() + ".");
        System.out.println("Type 'help' to see what you can do.\n");

        runGame(session);
    }

    private void runGame(GameSession session) {
        while (!session.isGameOver()) {
            if (session.hasTimeExpired()) {
                System.out.println("\n TIME IS UP!");
                System.out.println("The room fills with sleeping gas. You failed to escape.");
                endGame(session, false);
                break;
            }

            System.out.println("\n>");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("\nYou surrendered. The guards drag you away.");
                endGame(session, false);
                break;
            }

            String output = commandParser.parseAndExecute(session, input);
            System.out.println(output);

            if (session.isWon()) {
                System.out.println("\nMISSION ACCOMPLISHED!");
                System.out.println("Time taken: " + session.getElapsedSeconds() + " seconds");
                endGame(session, true);
                break;
            }
        }
    }

    private void endGame(GameSession session, boolean hasEscaped) {
        GameResult result = session.finishGame(hasEscaped);
        result.setPlayerId(session.getPlayer().getId());

        System.out.println("\n=========================================");
        System.out.println("               GAME OVER                 ");
        System.out.println("=========================================");
        System.out.println(result.toString());

        session.getPlayer().addGameResult(result);
        LeaderboardService.getInstance().addResult(result);
    }
}
