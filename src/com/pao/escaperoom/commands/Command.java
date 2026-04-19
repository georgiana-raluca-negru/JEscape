package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;

public interface Command {
    void execute(GameSession session);
}
