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

			if (plugin.in_command.contains(cs)) { // this function is not really necessary, but safety first
				cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.config.getMessages().getString("on-active-command")));
				return;
			}

			plugin.in_command.add(cs);

			Player p = (Player) cs;
			if (args.length == 0) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("ListWarps");
				out.writeUTF(plugin.checkPermissions(p));
				out.writeUTF(p.getName());
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
			}
			plugin.in_command.remove(cs);

		});

		return true;
	}
}
