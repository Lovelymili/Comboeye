package org.mili.aiac;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProcessData {
    public double[] ProcessDistance(Player p1, Entity entity) {
        Location Location = p1.getLocation();
        BoundingBox BoundingBox = getBoundingBox(entity);
        double dx = calculateAxisDistance(Location.getX(), BoundingBox.getMinX(), BoundingBox.getMaxX());
        double dy = calculateAxisDistance(Location.getY(), BoundingBox.getMinY(), BoundingBox.getMaxY());
        double dz = calculateAxisDistance(Location.getZ(), BoundingBox.getMinZ(), BoundingBox.getMaxZ());
        return new double[]{dx,dy,dz};

    }
    private static double calculateAxisDistance(double point, double min, double max) {
        if (point < min) {
            return min - point;
        } else if (point > max) {
            return point - max;
        } else {
            return 0.0;
        }
    }
    public BoundingBox getBoundingBox(Entity entity) {
        Vector location = entity.getLocation().toVector();
        double width = entity.getWidth();
        double height = entity.getHeight();
        double length = width;
        Vector offsetMin = new Vector(-width / 2, 0, -length / 2);
        Vector offsetMax = new Vector(width / 2, height, length / 2);
        Vector min = location.clone().add(offsetMin);
        Vector max = location.clone().add(offsetMax);
        return new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }



}

