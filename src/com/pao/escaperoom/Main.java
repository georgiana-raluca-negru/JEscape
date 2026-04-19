package com.pao.escaperoom;
import com.pao.escaperoom.model.*;


public class Main {
    public static void main(String[] args){
        ToolItem crowbar = new ToolItem("Crowbar", "A heavy crowbar.", false);
        ToolItem rustyKey = new ToolItem("Rusty Key", "An old key that looks fragile.", true);
        ToolItem battery = new ToolItem("Battery", "A standard AA battery.", true);
        ClueItem diary = new ClueItem("Diary", "A dusty old diary.", "Written inside it is said that the gold is hidden in the safe.");

        ContainerObject cabinet = new ContainerObject("Cabinet", "A tall wooden cabinet.", false, "");
        DoorObject woodernDoor = new DoorObject("Wooden Door", "A thick door blocking the way.", true, "Rusty Key", "Exit");
        ContainerObject smallSafe = new ContainerObject("Small Safe", "A heavy metal safe.", true, "Crowbar");

        cabinet.addInside(rustyKey);
        cabinet.addInside(smallSafe);

        System.out.println("--- Test 1: Inspecting Objects ---");
        System.out.println("Look at Crowbar: " + crowbar.examine());
        System.out.println("Look at Diary: " + diary.examine());
        System.out.println();

        System.out.println("--- Test 2: Inspecting Containers ---");
        System.out.println("Look at Safe (Locked): " + smallSafe.examine());
        System.out.println("Look at Cabinet (Unlocked with items): " + cabinet.examine());
        System.out.println();

        System.out.println("--- Test 3: Using Wrong Tools ---");
        System.out.println("Action: use Battery on Wooden Door");
        System.out.println("Result: " + diary.useOn(cabinet));
        System.out.println();

        // TEST 5: Interaction - Container in Container (Unlocking the safe)
        System.out.println("--- Test 5: Unlocking the Safe ---");
        System.out.println("Action: use Crowbar on Small Safe");
        System.out.println("Result: " + crowbar.useOn(smallSafe));
        System.out.println("Look at Safe (After unlock): " + smallSafe.examine());
        System.out.println();

        System.out.println("--- Test 6: Unlocking the Door ---");
        System.out.println("Action: use Rusy Key on Door");
        System.out.println("Result: " + rustyKey.useOn(woodernDoor));
        System.out.println("Look at Safe (After unlock): " + woodernDoor.examine());
        System.out.println();

    }
}