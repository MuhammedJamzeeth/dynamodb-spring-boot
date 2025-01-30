package com.dynamodb.main.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignalListener {
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 65;

    public static void listen() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Received SIGTERM - Sending `Connection: close` header on all responses, exiting in {} seconds", SHUTDOWN_TIMEOUT_SECONDS);
            ConnectionCloseFilter.setSendConnectionClose(true);
            try {
                Thread.sleep(SHUTDOWN_TIMEOUT_SECONDS * 1000);
            } catch (InterruptedException e) {
                // It's ok... we'll just exit a little early
            }
            log.info("Exiting");
            System.exit(0);
        }));
    }
}
