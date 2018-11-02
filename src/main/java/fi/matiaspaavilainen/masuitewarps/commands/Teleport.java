package fi.matiaspaavilainen.masuitewarps.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitewarps.Countdown;
import fi.matiaspaavilainen.masuitewarps.MaSuiteWarps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (plugin.in_command.contains(cs)) { // this function is not really necessary, but safety first
				cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.config.getMessages().getString("on-active-command")));
				return;
			}

			plugin.in_command.add(cs);

			if (args.length == 1) {

				Player p = (Player) cs;

				if (checkWarp(cs, args[0])) {
					if (checkCooldown(p)) {
						if (plugin.getConfig().getInt("warmup") > 0) {
							MaSuiteWarps.warmups.add(p.getUniqueId());
							cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.config.getMessages().getString("teleportation-started").replace("%time%",
											String.valueOf(plugin.getConfig().getInt("warmup")))));
							new Countdown(plugin.getConfig().getInt("warmup"), plugin) {
								@Override
								public void count(int current) {
									if (current == 0) {
										if (MaSuiteWarps.warmups.contains(p.getUniqueId())) {
											send(args, p, plugin);
											MaSuiteWarps.warmups.remove(p.getUniqueId());
										}
									}
								}
							}.start();
							return;
						} else {
							if (checkWarp(cs, args[0])) {

								send(args, p, plugin);
								return;
							}
						}
					}
				}

			} else if (args.length == 2) {
				if (cs.hasPermission("masuitewarps.warp.others")) {
					if (checkWarp(cs, args[0])) {
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("WarpPlayerCommand");
						out.writeUTF(args[1]);
						out.writeUTF("console");
						out.writeUTF(args[0]);

						Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
						return;
					}
				} else {
					cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.config.getMessages().getString("no-permission")));
					return;
				}
			} else {
				cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.config.getSyntaxes().getString("warp.teleport")));
				return;
			}

			plugin.in_command.remove(cs);

		});
		return true;
	}

	private void send(String[] args, Player p, Plugin plugin) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("WarpCommand");
		if (p.hasPermission("masuitewarps.warp.hidden")) {
			out.writeUTF("HIDDEN");
		} else {
			out.writeUTF("-------");
		}
		out.writeUTF(p.getName());
		out.writeUTF(args[0]);
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	private Boolean checkWarp(CommandSender p, String name) {
		if (MaSuiteWarps.warps.contains(name.toLowerCase())) {
			return true;
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					plugin.config.getMessages().getString("warp-not-found")));
			return false;
		}
	}

	private Boolean checkCooldown(Player p) {
		if (plugin.getConfig().getInt("cooldown") > 0) {
			if (MaSuiteWarps.cooldowns.containsKey(p.getUniqueId())) {
				if (System.currentTimeMillis()
						- MaSuiteWarps.cooldowns.get(p.getUniqueId()) > plugin.getConfig().getInt("cooldown") * 1000) {
					MaSuiteWarps.cooldowns.remove(p.getUniqueId());
					return true;
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages()
							.getString("in-cooldown").replace("%time%", plugin.getConfig().getString("cooldown"))));
					return false;
				}
			} else {
				return true;
			}
		}
		return true;
	}
}
