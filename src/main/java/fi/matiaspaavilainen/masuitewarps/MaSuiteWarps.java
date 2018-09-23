package fi.matiaspaavilainen.masuitewarps;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.commands.Delete;
import fi.matiaspaavilainen.masuitewarps.commands.List;
import fi.matiaspaavilainen.masuitewarps.commands.Set;
import fi.matiaspaavilainen.masuitewarps.commands.Teleport;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MaSuiteWarps extends JavaPlugin implements Listener {

    public static HashSet<UUID> warmups = new HashSet<>();
    public static HashSet<String> warps = new HashSet<>();
    public static HashMap<UUID, Long> cooldowns = new HashMap<>();
    public Config config = new Config(this);

    @Override
    public void onEnable() {
        super.onEnable();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new WarpMessageListener(this));
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new Sign(this), this);
        registerCommands();
        config.createConfigs();
        requestWarps();
    }

    private void registerCommands() {
        getCommand("warp").setExecutor(new Teleport(this));
        getCommand("setwarp").setExecutor(new Set(this));
        getCommand("delwarp").setExecutor(new Delete(this));
        getCommand("warps").setExecutor(new List(this));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (getConfig().getInt("warmup") > 0) {
            if (warmups.contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMessages().getString("teleportation-cancelled")));
                warmups.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    private void requestWarps() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RequestWarps");
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            System.out.println("[MaSuite] [Warps] Requesting list of warps");
            getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
        }, 0, 3000);

    }
}
