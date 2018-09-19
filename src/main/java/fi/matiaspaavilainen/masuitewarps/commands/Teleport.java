package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;
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
        if (args.length == 1) {
            Player p = (Player) cs;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("WarpCommand");
            out.writeUTF(p.getName());
            out.writeUTF(args[0]);
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            return true;
        }
        return false;
    }
}
