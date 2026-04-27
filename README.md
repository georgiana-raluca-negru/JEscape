# Escape Room - Text-Based Adventure Engine

A complex, interactive text-based Escape Room game built in Java. This project serves as a robust game engine demonstrating advanced Object-Oriented Programming (OOP) principles, clean Domain-Driven Design (DDD), and secure in-memory data management.

---

## Game Actions and Commands
The game provides a rich set of **10 distinct actions** divided between the out-of-game menu flow and the in-game command engine. The `Main` class serves as the entry point to demonstrate all these actions in a continuous loop.

**Menu & Setup Actions:**
1. **`Register`**: Prompts the user to create a new profile.
2. **`Login`**: Authenticates an existing user via username or email.
3. **`Edit Profile`**: Allows the user to update their custom Title and Bio.
4. **`Select Map & Difficulty`**: Players can choose from multiple available Escape Room scenarios and set a difficulty level (EASY, MEDIUM, HARD), which scales the time limit and final score multiplier.
5. **`View Leaderboard`**: Displays the top global escape scores.

**In-Game Commands:**
6. **`move <direction>`**: Navigates the player between connected locations.
7. **`inspect <target>`**: Examines rooms, items, or containers to reveal hidden objects or clues.
8. **`take <item>`**: Picks up an item from the room or from inside an unlocked container.
9. **`drop <item>`**: Removes an item from the player's inventory and leaves it in the current room.
10. **`use <item> on <target>`**: Applies a tool/key to interact with lockable objects (doors, safes).
11. **`inventory`**: Displays all items currently carried by the player.
12. **`stats`**: Shows the active game session data (Time remaining, difficulty, current room).
13. **`help`**: Displays a dynamically generated list of all available commands and their syntax.
---

## Domain Models and Object Types
The engine is built upon **more than 8 core domain classes**, fully encapsulated with `private` attributes, appropriate `getters/setters`, and overridden `toString()` methods for logging and debugging. 

**Core Entities:**
* `PlayerProfile` (Holds user credentials, aesthetic titles, and personal history)
* `GameSession` (Tracks the active state of a player's current run)
* `GameResult` (Stores the historical data of a finished game)
* `Location` (Represents the nodes/rooms of the game map)
* `GameMap` (Aggregates locations and defines the starting point)

**Interactive Objects:**
* `DoorObject` (Manages transitions between locations and lock states)
* `ContainerObject` (Lockable entities that hold other items)
* `ToolItem` (Functional items used to interact with the environment)
* `ClueItem` (Objects that provide textual hints to the player)

**Enumerations (Enums):**
* **`Difficulty`**: Defines time limits and score multipliers (e.g., `EASY`, `MEDIUM`, `HARD`).
* **`Direction`**: Enforces strict navigational movement vectors (`NORTH`, `SOUTH`, `EAST`, `WEST`).
---

## Object-Oriented Architecture (Inheritance & Interfaces)
The project utilizes a deep, multi-level inheritance hierarchy to ensure code reusability and polymorphism:

* **Level 0 (Base Abstract Class):** `GameObject` (Defines `name` and `description`).
* **Level 1 (Abstract Branches):**  `Item` (Objects that can be picked up).
  * `LockableObject` (Objects requiring a specific item to unlock).
* **Level 2 (Concrete Implementations):**  `ToolItem`, `ClueItem` (extend `Item`).
  * `DoorObject`, `ContainerObject` (extend `LockableObject`).

Additionally, the engine uses the **`Storable` interface**, which is implemented by any object that can be placed inside a player's inventory or within a `ContainerObject`.

GameObject Hierarchy
```text
GameObject (abstract)
├── Item (abstract) ....................... [implements Storable]
│   ├── ToolItem
│   └── ClueItem
└── LockableObject (abstract) ..............[implements Interactable]
    ├── DoorObject
    └── ContainerObject ................... [implements Storable]
```

Command Hierarchy ( Command Design Pattern )
```text
Command (interface)
├── TakeCommand
├── DropCommand
├── MoveCommand
├── InspectCommand
├── UseCommand
└── HelpCommand
```

---

## Data Integrity: Immutability and Custom Exceptions
To maintain the security and integrity of the game's business logic, the project implements strict data protection:

* **Immutable Class:** The `GameResult` class is completely immutable. Once an escape room session ends, the score, time, and player data are finalized using `final` fields and no setters, preventing any score tampering.
* **Custom Checked Exceptions:** The engine defines and handles specific business-rule violations gracefully using:
  * `UsernameTakenException`
  * `EmailTakenException`
  * `InvalidMoveException`

---

## Data Structures and Collections
Memory management and relationships are handled using multiple collection types optimized for their specific use cases:

* **`Map<String, T>` (HashMap)**: Used extensively for $O(1)$ fast lookups in services (e.g., retrieving players by their username/email or loading maps by name).
* **`List<T>` (ArrayList)**: Used for dynamic, ordered data such as the player's `inventory` or the `visibleObjects` inside a room.
* **`TreeSet<T>` (Sorted Collection)**: Used in the `LeaderboardService` to automatically sort `GameResult` objects in descending order based on a custom `compareTo()` implementation.

---

## Core Services (Singleton & CRUD)
The business logic is decoupled from the models using the **Singleton Design Pattern**, ensuring only one instance of each service runs in memory. These services provide full **CRUD (Create, Read, Update, Delete)** operations:

### 1. `PlayerService`
* **Create**: Registers new users.
* **Read**: Lookups via `findPlayer` (smart search by email or username).
* **Update**: Modifies player metadata (Title, Bio).
* **Delete**: Safely removes users from all indexing maps to prevent data leaks.

### 2. `MapService`
* **Create**: Ingests new `GameMap` layouts into the engine.
* **Read**: Fetches specific maps or lists all available missions.
* **Update**: Replaces old map layouts with newly edited versions.
* **Delete**: Removes maps from the available mission list.

---

## Project Structure and Clean Code
The source code is modularly organized into standard Java packages, ensuring no file duplication and a clean separation of concerns. Null checks and defensive programming techniques are used throughout to prevent `NullPointerExceptions` (NPEs).

* `com.pao.escaperoom.model` - Domain entities and enums.
* `com.pao.escaperoom.service` - Singleton business logic and memory management.
* `com.pao.escaperoom.command` - User input processing logic.
* `com.pao.escaperoom.exception` - Custom error handling.

---

## Demonstration
Running the `Main` class will initialize the services with pre-loaded data (mock players and 3 distinct Escape Room maps). The console-based UI will guide the user through the entire flow: from account creation and profile editing to interacting with the environment, solving puzzles, and securing a spot on the global leaderboard.
