package io.github.muricans.prisontools.util;

public class Cooldown {
    private boolean hasCooldown;
    private long timeLeft;

    public Cooldown(boolean hasCooldown, long timeLeft) {
        this.hasCooldown = hasCooldown;
        this.timeLeft = timeLeft;
    }

    public Cooldown(boolean hasCooldown) {
        this.hasCooldown = hasCooldown;
    }

    public boolean hasCooldown() {
        return hasCooldown;
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}
