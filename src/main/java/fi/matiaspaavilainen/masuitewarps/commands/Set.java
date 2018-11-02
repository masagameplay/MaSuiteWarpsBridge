package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Set implements CommandExecutor {

	private MaSuiteWarps plugin;

	public Set(MaSuiteWarps p) {
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
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("SetWarp");
				out.writeInt(2);
				out.writeUTF(p.getName());
				out.writeUTF(args[0]);
				Location loc = p.getLocation();
				out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
						+ loc.getYaw() + ":" + loc.getPitch());
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
			} else if (args.length == 2) {
				if (args[1].equalsIgnoreCase("hidden") || args[1].equalsIgnoreCase("global")) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("SetWarp");
					out.writeInt(3);
					out.writeUTF(p.getName());
					out.writeUTF(args[0]);
					Location loc = p.getLocation();
					out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
							+ loc.getYaw() + ":" + loc.getPitch());
					out.writeUTF(args[1]);
					p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				}
			} else {
				cs.sendMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.config.getSyntaxes().getString("warp.set")));
			}

			plugin.in_command.remove(cs);

		});

		return true;
	}
}
