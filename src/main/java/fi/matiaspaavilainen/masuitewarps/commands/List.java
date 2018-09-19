package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        Player p = (Player) cs;
        if (args.length == 1) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ListWarps");
            out.writeUTF(p.getName());
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }
        return false;
    }
}
