package fi.matiaspaavilainen.masuitewarps;

import fi.matiaspaavilainen.masuitewarps.commands.Delete;
import fi.matiaspaavilainen.masuitewarps.commands.List;
import fi.matiaspaavilainen.masuitewarps.commands.Set;
import fi.matiaspaavilainen.masuitewarps.commands.Teleport;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class MaSuiteWarps extends JavaPlugin implements Listener {

    public static HashSet<UUID> warmups = new HashSet<>();
    public static HashSet<String> warps = new HashSet<>();
    public static HashMap<UUID, Long> cooldowns = new HashMap<>();
    public Config config = new Config(this);
    public final java.util.List<CommandSender> in_command = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new WarpMessageListener(this));
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Sign(this), this);
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
                if (e.getFrom() != e.getTo()) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMessages().getString("teleportation-cancelled")));
                    warmups.remove(e.getPlayer().getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (warps.isEmpty()) {
            requestWarps();
        }
    }

    private void requestWarps() {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("RequestWarps");
            getServer().getScheduler().runTaskTimerAsynchronously(this, () -> getServer().sendPluginMessage(this, "BungeeCord", b.toByteArray()), 0, 3000);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public String checkPermissions(Player p) {
        StringJoiner types = new StringJoiner("");
        if (p.hasPermission("masuitewarps.list.global")) {
            types.add("GLOBAL");
        }
        if (p.hasPermission("masuitewarps.list.server")) {
            types.add("SERVER");
        }
        if (p.hasPermission("masuitewarps.list.hidden")) {
            types.add("HIDDEN");
        }
        return types.toString();
    }

    public boolean checkCooldown(CommandSender cs, MaSuiteWarps plugin) {
        if (plugin.in_command.contains(cs)) {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.config.getMessages().getString("on-active-command")));
            return true;
        }

        plugin.in_command.add(cs);
        return false;
    }
}
