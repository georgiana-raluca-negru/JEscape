package com.pao.escaperoom.service;

import com.pao.escaperoom.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapService {
    private static MapService instance;
    private final Map<String, GameMap> maps;

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

    public boolean addMap(GameMap newMap){
        if(newMap == null || newMap.getName() == null){
            return false;
        }

        if(maps.containsKey(newMap.getName())){
            return false;
        }

        maps.put(newMap.getName(), newMap);
        return true;
    }

    public GameMap getMapByName(String name){
        return maps.get(name);
    }

    public boolean deleteMap(String mapName){
        if(mapName == null){
            return false;
        }

        return maps.get(mapName) != null;
    }

    private void initializeMaps() {
        // ==========================================
        // MAP 1: SPACE STATION ALPHA (Sci-Fi)
        // ==========================================
        Location airlock = new Location("Airlock", "Red emergency lights are flashing. Oxygen levels are dropping.");
        Location controlRoom = new Location("Control Room", "A room full of dead monitors and floating debris.");
        Location escapePod = new Location("Escape Pod", "Safety at last. The stars await.");

        // Objects in Airlock
        ToolItem crowbar = new ToolItem("Heavy Crowbar", "A solid titanium crowbar.", false);
        ContainerObject locker = new ContainerObject("Rusty Locker", "A jammed crew locker.", true, "Heavy Crowbar");
        ToolItem accessCard = new ToolItem("Access Card", "A blue keycard belonging to the captain.", true);
        locker.addInside(accessCard); // Punem cardul ÎN dulap

        DoorObject blastDoor = new DoorObject("Blast Door", "A heavy automated door.", true, "Access Card", "Control Room", false);

        airlock.addObject(crowbar);
        airlock.addObject(locker);
        airlock.addObject(blastDoor);

        // Objects in Control Room
        ClueItem terminalLog = new ClueItem("Terminal Monitor", "A flickering screen.", "WARNING: Pod requires the Override Chip to launch.");
        ToolItem overrideChip = new ToolItem("Override Chip", "A small golden microchip.", true);
        DoorObject podHatch = new DoorObject("Pod Hatch", "The airlock to the escape pod.", true, "Override Chip", "Escape Pod", true);

        controlRoom.addObject(terminalLog);
        controlRoom.addObject(overrideChip);
        controlRoom.addObject(podHatch);

        // Connections
        airlock.addExit(Direction.NORTH, controlRoom);
        controlRoom.addExit(Direction.SOUTH, airlock);
        controlRoom.addExit(Direction.NORTH, escapePod);

        maps.put("Space Station Alpha", new GameMap("Space Station Alpha", "Survive the failing oxygen systems and escape the station.", List.of(airlock, controlRoom, escapePod), airlock));


        // ==========================================
        // MAP 2: THE HAUNTED MANOR (Horror)
        // ==========================================
        Location foyer = new Location("Dusty Foyer", "Cobwebs cover the chandelier. The air is freezing.");
        Location library = new Location("Dark Library", "Thousands of rotting books line the walls.");
        Location crypt = new Location("Family Crypt", "The exit out of this nightmare.");

        // Objects in Foyer
        ClueItem tornDiary = new ClueItem("Torn Diary", "A page ripped from a book.", "I hid the Brass Key in my coat pocket...");
        ContainerObject coatRack = new ContainerObject("Old Coat", "A moth-eaten winter coat hanging on a rack.", false, null); // Unlocked container
        ToolItem brassKey = new ToolItem("Brass Key", "An old, cold key.", true);
        coatRack.addInside(brassKey);

        DoorObject libraryDoor = new DoorObject("Wooden Door", "A door with scratches on the inside.", true, "Brass Key", "Dark Library", false);

        foyer.addObject(tornDiary);
        foyer.addObject(coatRack);
        foyer.addObject(libraryDoor);

        // Objects in Library
        ToolItem silverAmulet = new ToolItem("Silver Amulet", "It glows faintly in the dark.", false);
        ContainerObject cursedChest = new ContainerObject("Cursed Chest", "A wooden chest wrapped in chains.", true, "Silver Amulet");
        ToolItem cryptKey = new ToolItem("Skull Key", "A key shaped like a human skull.", true);
        cursedChest.addInside(cryptKey);

        DoorObject cryptGate = new DoorObject("Iron Gate", "A massive gate leading outside.", true, "Skull Key", "Family Crypt", true);

        library.addObject(silverAmulet);
        library.addObject(cursedChest);
        library.addObject(cryptGate);

        // Connections
        foyer.addExit(Direction.EAST, library);
        library.addExit(Direction.WEST, foyer);
        library.addExit(Direction.NORTH, crypt);

        maps.put("The Haunted Manor", new GameMap("The Haunted Manor", "Find your way out before the ghosts find you.", List.of(foyer, library, crypt), foyer));


        // ==========================================
        // MAP 3: COLD WAR BUNKER (Spy Thriller)
        // ==========================================
        Location barracks = new Location("Soldiers Barracks", "Rows of empty bunk beds. An alarm is blaring.");
        Location commsRoom = new Location("Comms Room", "Radio equipment and a large world map.");
        Location surface = new Location("The Surface", "Snowy mountains and freedom.");

        // Objects in Barracks
        ToolItem lockpick = new ToolItem("Lockpick Set", "A set of professional lockpicks.", true);
        DoorObject steelDoor = new DoorObject("Steel Door", "A military grade door.", true, "Lockpick Set", "Comms Room", false);

        barracks.addObject(lockpick);
        barracks.addObject(steelDoor);

        // Objects in Comms Room
        ClueItem memo = new ClueItem("Top Secret Memo", "A classified document.", "The launch code disk is in the safe. Do NOT lose it.");
        ToolItem stethoscope = new ToolItem("Stethoscope", "Used by medics, but good for cracking safes.", false);
        ContainerObject safe = new ContainerObject("Wall Safe", "A combination safe hidden behind a painting.", true, "Stethoscope");
        ToolItem launchCodes = new ToolItem("Launch Codes", "A floppy disk containing the codes.", true);
        safe.addInside(launchCodes);

        DoorObject blastHatch = new DoorObject("Blast Hatch", "A heavy hatch leading to the surface.", true, "Launch Codes", "The Surface", true);

        commsRoom.addObject(memo);
        commsRoom.addObject(stethoscope);
        commsRoom.addObject(safe);
        commsRoom.addObject(blastHatch);

        // Connections
        barracks.addExit(Direction.NORTH, commsRoom);
        commsRoom.addExit(Direction.SOUTH, barracks);
        commsRoom.addExit(Direction.NORTH, surface); // Folosim direcția UP!

        maps.put("Cold War Bunker", new GameMap("Cold War Bunker", "Steal the launch codes and escape the facility.", List.of(barracks, commsRoom, surface), barracks));
    }

    public List<GameMap> getAllMaps(){
        return new ArrayList<>(maps.values());
    }
}
