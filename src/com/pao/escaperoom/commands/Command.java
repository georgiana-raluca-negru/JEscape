package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;

public interface Command {
    String execute(GameSession session, String[] args);
}
