package com.carson.core;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {

        String token = "";

        try {
            token = Files.readAllLines(new File("key").toPath()).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        IDiscordClient client = new ClientBuilder()
                .withToken(token)
                .setMaxReconnectAttempts(9999)
                .setMaxMessageCacheCount(-1)
                .build();

        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
        Handler handle = new Handler();
        client.getDispatcher().registerListener(handle);
        client.login();
    }
}
