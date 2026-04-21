package com.pao.escaperoom.service;

import com.pao.escaperoom.model.Direction;
import com.pao.escaperoom.model.GameMap;
import com.pao.escaperoom.model.Location;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.model.ClueItem;
import com.pao.escaperoom.model.ToolItem;
import com.pao.escaperoom.model.DoorObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapService {

    private static MapService instance;
    private Map<String, GameMap> maps;

    private MapService(){
        this.maps = new HashMap<>();
        initializeMaps();
    }

    public static MapService getInstance(){
        if(instance == null){
            instance = new MapService();
        }
        return instance;
    }

    private void initializeMaps() {
        // --- LOCAȚII ---
        Location cell = new Location("Prison Cell", "O celulă umedă și rece. Pereții sunt plini de mușchi.");
        Location hallway = new Location("Dark Hallway", "Un coridor lung luminat de torțe care pârâie.");
        Location freedom = new Location("Freedom", "Ești în afara castelului. Aerul nopții este proaspăt.");

        // --- OBIECTE ȘI PUZZLE ---
        // 1. O notă în celulă
        ClueItem note = new ClueItem("Bilet vechi", "O bucată de hârtie îngălbenită.", "Cheia de aur este singura care te scoate de aici!");
        cell.addObject(note);

        // 2. Cheia pentru prima ușă (pusă direct pe jos în celulă pentru testare rapidă)
        ToolItem rustyKey = new ToolItem("Cheie Ruginită", "O cheie veche din fier.", true);
        cell.addObject(rustyKey);

        // 3. Cheia de aur pentru ieșirea finală (pusă în Hallway)
        ToolItem goldenKey = new ToolItem("Cheie de Aur", "O cheie strălucitoare și grea.", true);
        hallway.addObject(goldenKey);

        // --- UȘI ---
        // Ușa dintre Cell și Hallway (încuiată cu rusty key)
        DoorObject cellDoor = new DoorObject("Ușa Celulei", "O ușă grea din fier.", true, "Cheie Ruginită", "Dark Hallway", false);
        cell.addObject(cellDoor);

        // Ușa de ieșire finală (încuiată cu golden key)
        DoorObject exitDoor = new DoorObject("Ieșirea Castelului", "O poartă masivă din lemn de stejar.", true, "Cheie de Aur", "Freedom", true);
        hallway.addObject(exitDoor);

        // --- CONEXIUNI ---
        cell.addExit(Direction.NORTH, hallway);
        hallway.addExit(Direction.SOUTH, cell);
        hallway.addExit(Direction.NORTH, freedom); // Direcția spre libertate

        // --- INSTANȚIERE HĂRȚI ---
        maps.put("The Dungeon", new GameMap("The Dungeon", "Evadează din închisoarea medievală.", List.of(cell, hallway, freedom), cell));
    }

    public GameMap getMapByName(String name){
        return maps.get(name);
    }

    public List<GameMap> getAllMaps(){
        return new ArrayList<>(maps.values());
    }
}
