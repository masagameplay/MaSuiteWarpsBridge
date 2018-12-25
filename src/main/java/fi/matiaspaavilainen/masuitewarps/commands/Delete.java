package fi.matiaspaavilainen.masuitewarps.commands;

import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Delete implements CommandExecutor {

    private MaSuiteWarps plugin;

    public Delete(MaSuiteWarps p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (plugin.in_command.contains(cs)) { // this function is not really necessary, but safety first
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.config.getMessages().getString("on-active-command")));
                return;
            }

            plugin.in_command.add(cs);

            Player p = (Player) cs;
            if (args.length == 1) {
                try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(b)) {
                    out.writeUTF("DelWarp");
                    out.writeUTF(p.getName());
                    out.writeUTF(args[0]);
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                    MaSuiteWarps.warpNames.remove(args[0]);
                    MaSuiteWarps.warps.forEach(warp -> {
                        if (warp.getName().equalsIgnoreCase(args[0])) {
                            MaSuiteWarps.warps.remove(warp);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.config.getSyntaxes().getString("warp.delete")));
            }
            plugin.in_command.remove(cs);

        });

        return true;
    }
}
