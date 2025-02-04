package org.mili.aiac;

public class Math {
    public double GetDistance(player p1, player p2) {
        Location Location = p1.getLocation();
        BoundingBox BoundingBox = p2.getBoundingBox();
        double dx = calculateAxisDistance(Location.getX(), BoundingBox.getMinX(), BoundingBox.getMaxX());
        double dy = calculateAxisDistance(Location.getY(), BoundingBox.getMinY(), BoundingBox.getMaxY());
        double dz = calculateAxisDistance(Location.getZ(), BoundingBox.getMinZ(), BoundingBox.getMaxZ());
        return Math.sqrt(dx * dx + dy * dy + dz * dz);

    }
    private static double calculateAxisDistance(double point, double min, double max) {
        if (point < min) {
            return min - point; // 点在左侧
        } else if (point > max) {
            return point - max; // 点在右侧
        } else {
            return 0.0; // 点在范围内
        }
    }
}
