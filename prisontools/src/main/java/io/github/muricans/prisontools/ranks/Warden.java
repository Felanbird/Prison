package io.github.muricans.prisontools.ranks;

public class Warden implements RankHandler {

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public String getName() {
        return "Warden";
    }

    @Override
    public double getPrice() {
        return 1000000;
    }

    @Override
    public int getBorderSize() {
        return 125;
    }

    @Override
    public void rankup(String playerUUID) {

    }
}
