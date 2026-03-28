package com.mgen256.al.items;

import com.mgen256.al.LogProvider;

public class ModItemCore implements ModItemTrait {
    private final String name;
    private final LogProvider logger;

    public ModItemCore(String name, LogProvider logger) {
        this.name = name;
        this.logger = logger;
    }

    @Override
    public String getRegName() {
        return name;
    }

    @Override
    public void log(String message) {
        logger.log(message);
    }
}
