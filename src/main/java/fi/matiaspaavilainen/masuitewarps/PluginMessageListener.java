package fi.matiaspaavilainen.masuitewarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
     private MaSuiteWarps plugin;

    PluginMessageListener(MaSuiteWarps p) {
        plugin = p;
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
