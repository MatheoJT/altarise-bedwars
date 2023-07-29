package net.altarise.bw.game;

import org.bukkit.Location;

public class Generator {

    public enum GeneratorType{DIAMOND,EMERALD}

    private final GeneratorType type;
    private final Location location;

    public int count;
    public int starCount;




    public Generator(GeneratorType type, Location location) {
        this.type = type;
        this.location = location;
        this.count = 0;
        this.starCount = 0;
    }

    public GeneratorType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }








}
