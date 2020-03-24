package io.github.muricans.prisontools.util;

import java.util.HashMap;
import java.util.UUID;

public class Util {

    public static Cooldown hasCooldown(HashMap<UUID, Long> cooldowns, UUID player, int time) {
        if(cooldowns.containsKey(player)) {
            long left = ((cooldowns.get(player) / 1000) + time) - (System.currentTimeMillis() / 1000);
            if(left > 0) {
                return new Cooldown(true, left);
            } else {
                cooldowns.remove(player);
                return new Cooldown(false);
            }
        }
        return new Cooldown(false);
    }

}
