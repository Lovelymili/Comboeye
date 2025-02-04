package org.mili.aiac;

import org.bukkit.entity.Player;

public class PlayerRotation {
    private double lastYaw = 0;
    private double lastPitch = 0;
    private long lastTime = 0;

    private double yawAcceleration = 0;
    private double pitchAcceleration = 0;

    // 更新玩家的角加速度
    public void update(Player player) {
        long currentTime = System.currentTimeMillis();  // 获取当前时间（毫秒）
        float currentYaw = player.getLocation().getYaw();  // 获取当前 yaw 角度
        float currentPitch = player.getLocation().getPitch();  // 获取当前 pitch 角度

        if (lastTime != 0) {  // 确保不是第一次计算
            long deltaTime = currentTime - lastTime;  // 计算时间差，单位：毫秒
            double deltaYaw = currentYaw - lastYaw;  // 计算 yaw 角度变化量
            double deltaPitch = currentPitch - lastPitch;  // 计算 pitch 角度变化量

            // 将时间差转换为秒
            deltaTime /= 1000.0;

            // 计算角速度（单位：°/秒）
            double yawVelocity = deltaYaw / deltaTime;
            double pitchVelocity = deltaPitch / deltaTime;

            // 计算角加速度（单位：°/秒²）
            if (deltaTime > 0) {
                yawAcceleration = yawVelocity / deltaTime;
                pitchAcceleration = pitchVelocity / deltaTime;
            }
        }

        // 更新上次的 yaw、pitch 和时间
        lastYaw = currentYaw;
        lastPitch = currentPitch;
        lastTime = currentTime;
    }

    // 获取 yaw 角加速度
    public double getYawAcceleration() {
        return yawAcceleration;
    }

    // 获取 pitch 角加速度
    public double getPitchAcceleration() {
        return pitchAcceleration;
    }
}

