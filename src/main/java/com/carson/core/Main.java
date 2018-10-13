package com.carson.core;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0){
            throw new IllegalArgumentException("you need to specify an argument");
        }
        switch(args[0]){
            case "desktop":
                run("/home/carson/java/files/xkcd/");
                break;
            case "server":
                run("/home/carson/xkcd/files");
                break;
            default:
                run(args[0]);
        }
    }


    private static void run(String dir){
        String token;

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
        Handler handle = new Handler(dir);
        client.getDispatcher().registerListener(handle);
        client.login();
    }
}
