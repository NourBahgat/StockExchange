package com.example.demo2;

import java.time.LocalDateTime;

public class TimeStamp {
    private double price;
    private LocalDateTime timestamp;

    public TimeStamp(double price, LocalDateTime timestamp) {
        this.price = price;
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
