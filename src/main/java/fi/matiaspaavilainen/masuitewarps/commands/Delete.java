package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        Player p = (Player) cs;
        if (args.length == 1) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("DelWarp");
            out.writeUTF(p.getName());
            out.writeUTF(args[0]);
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }else{
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getSyntaxes().getString("warp.delete")));
        }
        return false;
    }
}
