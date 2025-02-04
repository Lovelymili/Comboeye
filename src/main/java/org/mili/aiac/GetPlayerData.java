package org.mili.aiac;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.command.ConsoleCommandSender;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;
import static org.mili.aiac.Global.BaseURL;
import static org.mili.aiac.Global.key;
import static org.mili.aiac.Global.playerVal;
import static org.mili.aiac.Global.v1;
import static org.mili.aiac.Global.v2;
import static org.mili.aiac.Global.v3;
import static org.mili.aiac.Global.t1;
import static org.mili.aiac.Global.t2;
import static org.mili.aiac.Global.t3;
import static org.mili.aiac.Global.warn_text;
import static org.mili.aiac.Global.kick_text;
import static org.mili.aiac.Global.ban_text;


public class GetPlayerData implements Listener {

    private final HashMap<UUID, Long[]> clickTimes = new HashMap<>();
    private final Map<Player, Integer> attackCounts = new HashMap<>();
    private final Map<Player, Long> lastAttackTimes = new HashMap<>();
    private long lastCheckTime = System.currentTimeMillis();

    @EventHandler
    public void ShouldCheck(EntityDamageByEntityEvent event) throws IOException {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();
            long currentTime = System.currentTimeMillis();
            long lastAttackTime = lastAttackTimes.getOrDefault(player, 0L);

            if (currentTime - lastCheckTime >= 1000) {
                for (Player p : attackCounts.keySet()) {
                    int count = attackCounts.getOrDefault(p, 0);
                    attackCounts.put(p, 0);
                    if(count >= 3)
                    {
                      int result = 0;
                        result = GetData(p,entity,count);
                        if(result !=0) {
                            UpdateVal(p, result);
                        }
                    }
                }
                lastCheckTime = currentTime;
            }
        }
    }
    public int GetCheatLevel(PlayerAttackData data) throws IOException {
        String distance = String.valueOf(data.distance);
        String cps = String.valueOf(data.cps);
        String yaw = String.valueOf(data.yaw);
        String pitch = String.valueOf(data.pitch);
        String yaw_a = String.valueOf(data.yaw_a);
        String pitch_a = String.valueOf(data.pitch_a);
        String count = String.valueOf(data.count);
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet(
                BaseURL +
                        "?key="+ key +
                        "&distance=" + distance +
                        "&cps=" + cps +
                        "&yaw=" + yaw +
                        "&pitch=" + pitch +
                        "&yaw_a" + yaw_a +
                        "pitch_a" + pitch_a +
                        "count" + count

        );
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {
            HttpEntity entity = response.getEntity();
            String result = null;
            try {
                result = EntityUtils.toString(entity);
                switch(result){
                    case "0":
                        return 0;
                    case "1":
                        return 1;
                    case "2":
                        return 2;
                    case "3":
                        return 3;
                    default:
                        getLogger().warning("Comboeye:AntiCheat Server went some wrong!These are fallback:"+result);

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EntityUtils.consume(entity);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            response.close();
        }
        getLogger().warning("Comboeye:Plugin can't connect to AntiCheat Server! Try to check your Internet connect!");
        return 0;

    }
    public int GetData(Player p1,Entity entity,int count) throws IOException {
        double distance = GetDistance(p1,entity);
        double CPS = GetCPS(p1);
        PlayerAttackData it = new PlayerAttackData();
        PlayerRotation rotations = new PlayerRotation();
        rotations.update(p1);
        it.cps = (double) CPS;
        it.distance = distance;
        it.yaw = p1.getLocation().getYaw();
        it.pitch = p1.getLocation().getPitch();
        it.yaw_a = rotations.getYawAcceleration(p1);
        it.pitch_a = rotations.getPitchAcceleration(p1);
        it.count = count;
        return GetCheatLevel(it);
    }

    public double GetDistance(Player p1, Entity entity) {
        ProcessData ProcessData = new ProcessData();
        return ProcessData.ProcessDistance(p1, entity);
    }

    public int GetCPS(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (!clickTimes.containsKey(playerId)) {
            return 0;
        }

        Long[] times = clickTimes.get(playerId);
        int cps = 0;

        for (Long time : times) {
            if (time != null && currentTime - time <= 1000) {
                cps++;
            }
        }

        return cps;
    }
    @EventHandler
    public int onPlayerClick(PlayerAnimationEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        long currentTime = System.currentTimeMillis();

        clickTimes.putIfAbsent(playerId, new Long[20]);
        Long[] times = clickTimes.get(playerId);

        System.arraycopy(times, 1, times, 0, times.length - 1);
        times[times.length - 1] = currentTime;

        int cps = 0;
        for (Long time : times) {
            if (time != null && currentTime - time <= 1000) {
                cps++;

            }
        }
        return cps;
    }

    public void UpdateVal(Player p,int result)
    {
        int plus_Value =0;
        switch (result)
        {
            case 1:
                plus_Value = v1;
            case 2:
                plus_Value = v2;
            case 3:
                plus_Value = v3;
        }
        String playerId = p.getUniqueId().toString();

        if (playerVal.containsKey(playerId)) {
            int currentVal = playerVal.get(playerId);
            if(currentVal+ plus_Value >= t3)
            {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console,"ban"+" "+p.getName()+" "+ban_text);
                playerVal.put(playerId,0);
            }
            else if (currentVal+ plus_Value >= t2 )
            {
                p.kickPlayer(kick_text);
                playerVal.put(playerId, 0);
            }
            else if (currentVal+ plus_Value >= t1) {
                p.sendMessage(warn_text);
                playerVal.put(playerId, currentVal );
            }
        } else {
            playerVal.put(playerId, plus_Value);
        }
    }
    private static class PlayerAttackData {
        private double distance,cps,yaw,pitch,yaw_a,pitch_a,count;
    }


}