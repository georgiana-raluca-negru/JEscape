CREATE TABLE IF NOT EXISTS players (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL UNIQUE,
    email             VARCHAR(100) NOT NULL UNIQUE,
    title             VARCHAR(20)  NOT NULL DEFAULT 'ROOKIE',
    registration_date DATE         NOT NULL
);

-- starting_location_id este setat dupa ce avem locatiile create ( circular fk)
CREATE TABLE IF NOT EXISTS game_maps (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    name                 VARCHAR(100) NOT NULL UNIQUE,
    description          VARCHAR(500) NOT NULL,
    starting_location_id INT
);

CREATE TABLE IF NOT EXISTS locations (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    map_id      INT          NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    FOREIGN KEY (map_id) REFERENCES game_maps(id) ON DELETE CASCADE
);

ALTER TABLE game_maps
    ADD CONSTRAINT fk_starting_location
        FOREIGN KEY (starting_location_id) REFERENCES locations(id);

-- directia la care se afla locatia ( enum urile )
CREATE TABLE IF NOT EXISTS location_exits (
    from_location_id INT         NOT NULL,
    direction        VARCHAR(10) NOT NULL,
    to_location_id   INT         NOT NULL,
    PRIMARY KEY (from_location_id, direction),
    FOREIGN KEY (from_location_id) REFERENCES locations(id) ON DELETE CASCADE,
    FOREIGN KEY (to_location_id)   REFERENCES locations(id) ON DELETE CASCADE
);

-- DoorObject este mereu intro camera (logica separata)
CREATE TABLE IF NOT EXISTS door_objects (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,
    description        VARCHAR(500) NOT NULL,
    is_locked          BOOLEAN      NOT NULL,
    required_item_name VARCHAR(100),
    destination        VARCHAR(100) NOT NULL,
    is_final_exit      BOOLEAN      NOT NULL,
    location_id        INT          NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

-- ContainerObject: poate fi intr o locatie (location_id) sau intr un alt container (container_id)
CREATE TABLE IF NOT EXISTS container_objects (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,
    description        VARCHAR(500) NOT NULL,
    is_locked          BOOLEAN      NOT NULL,
    required_item_name VARCHAR(100),
    location_id        INT,
    container_id       INT,
    FOREIGN KEY (location_id)  REFERENCES locations(id)          ON DELETE CASCADE,
    FOREIGN KEY (container_id) REFERENCES container_objects(id)  ON DELETE CASCADE,
    CONSTRAINT chk_container_parent CHECK (
        (location_id IS NOT NULL AND container_id IS NULL) OR
        (location_id IS NULL     AND container_id IS NOT NULL)
    )
);

-- ToolItem: poate fi intro locatie sau intr un container
CREATE TABLE IF NOT EXISTS tool_items (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,
    description        VARCHAR(500) NOT NULL,
    is_consumed_on_use BOOLEAN      NOT NULL,
    location_id        INT,
    container_id       INT,
    FOREIGN KEY (location_id)  REFERENCES locations(id)         ON DELETE CASCADE,
    FOREIGN KEY (container_id) REFERENCES container_objects(id) ON DELETE CASCADE,
    CONSTRAINT chk_tool_parent CHECK (
        (location_id IS NOT NULL AND container_id IS NULL) OR
        (location_id IS NULL     AND container_id IS NOT NULL)
    )
);

-- ClueItem: poate fi intr o locatie sau intr un container
CREATE TABLE IF NOT EXISTS clue_items (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    description    VARCHAR(500) NOT NULL,
    hidden_message VARCHAR(500) NOT NULL,
    location_id    INT,
    container_id   INT,
    FOREIGN KEY (location_id)  REFERENCES locations(id)         ON DELETE CASCADE,
    FOREIGN KEY (container_id) REFERENCES container_objects(id) ON DELETE CASCADE,
    CONSTRAINT chk_clue_parent CHECK (
        (location_id IS NOT NULL AND container_id IS NULL) OR
        (location_id IS NULL     AND container_id IS NOT NULL)
    )
);

CREATE TABLE IF NOT EXISTS game_results (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    player_id          INT         NOT NULL,
    map_name           VARCHAR(100) NOT NULL,
    difficulty         VARCHAR(20)  NOT NULL,
    is_win             BOOLEAN      NOT NULL,
    time_taken_seconds BIGINT       NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);
