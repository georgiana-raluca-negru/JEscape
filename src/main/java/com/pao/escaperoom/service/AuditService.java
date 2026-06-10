package com.pao.escaperoom.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {
    private static AuditService instance;
    private static final String FILE_PATH = "audit.csv";
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String actionName) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(actionName + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
