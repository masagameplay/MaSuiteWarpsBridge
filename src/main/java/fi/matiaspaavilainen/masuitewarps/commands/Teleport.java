package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.Countdown;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {

    private MaSuiteWarps plugin;

    public Teleport(MaSuiteWarps p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            return false;
        }
        if (args.length == 1) {
            Player p = (Player) cs;
            if (checkWarp(cs, args[0])) {
                if (plugin.getConfig().getInt("warmup") > 0) {
                    MaSuiteWarps.warmups.add(p.getUniqueId());
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("teleportation-started").replace("%time%", String.valueOf(plugin.getConfig().getInt("warmup")))));
                    new Countdown(plugin.getConfig().getInt("warmup"), plugin) {
                        @Override
                        public void count(int current) {
                            if (current == 0) {
                                if (MaSuiteWarps.warmups.contains(p.getUniqueId())) {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    out.writeUTF("WarpCommand");
                                    out.writeUTF(p.getName());
                                    out.writeUTF(args[0]);
                                    p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                                    MaSuiteWarps.warmups.remove(p.getUniqueId());
                                }
                            }
                        }
                    }.start();
                } else {
                    if (MaSuiteWarps.warps.contains(args[0])) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("WarpCommand");
                        out.writeUTF(p.getName());
                        out.writeUTF(args[0]);
                        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                    }
                }
            }
            return true;
        } else if (args.length == 2) {
            if (checkWarp(cs, args[0])) {
                Player p = (Player) cs;
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("WarpPlayerCommand");
                out.writeUTF(args[1]);
                out.writeUTF(p.getName());
                out.writeUTF(args[0]);

                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
            return true;
        } else {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getSyntaxes().getString("warp.teleport")));
        }
        return false;
    }

    private Boolean checkWarp(CommandSender p, String name) {
        if (MaSuiteWarps.warps.contains(name.toLowerCase())) {
            return true;
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("warp-not-found")));
            return false;
        }
    }
}
