package com.railway.thread;

public class NotificationTask implements Runnable {

    private final String message;

    public NotificationTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {

        try {

            System.out.println(
                    "Notification processing by thread: "
                            + Thread.currentThread().getName());

            // Simulate notification sending delay
            Thread.sleep(2000);

            System.out.println();
            System.out.println("=================================");
            System.out.println("       NOTIFICATION");
            System.out.println("=================================");

            System.out.println(message);

            System.out.println("=================================");

            System.out.println(
                    "Notification sent successfully by: "
                            + Thread.currentThread().getName());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Notification thread interrupted!");
        }
    }
}