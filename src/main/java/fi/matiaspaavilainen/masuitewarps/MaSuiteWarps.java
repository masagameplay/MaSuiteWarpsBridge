package fi.matiaspaavilainen.masuitewarps;

import fi.matiaspaavilainen.masuitewarps.commands.Delete;
import fi.matiaspaavilainen.masuitewarps.commands.List;
import fi.matiaspaavilainen.masuitewarps.commands.Set;
import fi.matiaspaavilainen.masuitewarps.commands.Teleport;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteWarps extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        super.onEnable();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new WarpMessageListener(this));
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new Sign(this), this);
        registerCommands();
        new Config(this).createConfigs();
    }

    private void registerCommands() {
        getCommand("warp").setExecutor(new Teleport(this));
        getCommand("setwarp").setExecutor(new Set(this));
        getCommand("delwarp").setExecutor(new Delete(this));
        getCommand("warps").setExecutor(new List(this));
    }

}
