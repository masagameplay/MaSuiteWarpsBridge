package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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

public class List implements CommandExecutor {

    private MaSuiteWarps plugin;

    public List(MaSuiteWarps p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (plugin.checkCooldown(cs, plugin)) return;

            Player p = (Player) cs;
            if (args.length == 0) {
                try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(b)) {
                    out.writeUTF("ListWarps");
                    out.writeUTF(plugin.checkPermissions(p));
                    out.writeUTF(p.getName());
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                plugin.in_command.remove(cs);
            }
        });

        return true;
    }
}
