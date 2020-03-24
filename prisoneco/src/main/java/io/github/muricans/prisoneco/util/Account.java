package io.github.muricans.prisoneco.util;

import java.util.UUID;

public class Account {
    private String name;
    private String id;
    public Account() {}

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return UUID.fromString(id.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5"));
    }

    public String toString() {
        return "Account [ name: " + name + ", id: " + id + " ]";
    }
}
