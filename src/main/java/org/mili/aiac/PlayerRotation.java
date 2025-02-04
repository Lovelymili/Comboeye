package org.mili.aiac;

import org.bukkit.entity.Player;

public class PlayerRotation {
    private double lastYaw = 0;
    private double lastPitch = 0;
    private long lastTime = 0;

    private double yawAcceleration = 0;
    private double pitchAcceleration = 0;

    public void update(Player player) {
        long currentTime = System.currentTimeMillis();
        float currentYaw = player.getLocation().getYaw();
        float currentPitch = player.getLocation().getPitch();

        if (lastTime != 0) {
            long deltaTime = currentTime - lastTime;
            double deltaYaw = currentYaw - lastYaw;
            double deltaPitch = currentPitch - lastPitch;

            deltaTime /= (long) 1000.0;

            double yawVelocity = deltaYaw / deltaTime;
            double pitchVelocity = deltaPitch / deltaTime;

            if (deltaTime > 0) {
                yawAcceleration = yawVelocity / deltaTime;
                pitchAcceleration = pitchVelocity / deltaTime;
            }
        }

        lastYaw = currentYaw;
        lastPitch = currentPitch;
        lastTime = currentTime;
    }

    public double getYawAcceleration(Player p1) {
        update(p1);
        return yawAcceleration;
    }

    public double getPitchAcceleration(Player p1) {
        update(p1);
        return pitchAcceleration;
    }
}

