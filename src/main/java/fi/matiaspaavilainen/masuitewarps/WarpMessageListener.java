package fi.matiaspaavilainen.masuitewarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class WarpMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    WarpMessageListener(MaSuiteWarps p) {
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")){
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if(subchannel.equals("WarpPlayer")){
                Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                p.teleport(new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
            }
            if(subchannel.equals("ListWarpsForPlayers")){
                String w = in.readUTF().toLowerCase();
                String[] warps = w.split(":");
                MaSuiteWarps.warps.addAll(Arrays.asList(warps));
            }
            if(subchannel.equals("WarpCooldown")){
                Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                MaSuiteWarps.cooldowns.put(p.getUniqueId(), in.readLong());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
