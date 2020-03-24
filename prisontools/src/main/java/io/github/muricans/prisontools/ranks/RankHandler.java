package io.github.muricans.prisontools.ranks;

public interface RankHandler {

    boolean isDefault();
    String getName();
    double getPrice();
    int getBorderSize();
    void rankup(String playerUUID);

}
