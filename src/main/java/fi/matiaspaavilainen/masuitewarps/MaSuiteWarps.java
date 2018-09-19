package fi.matiaspaavilainen.masuitewarps;

import fi.matiaspaavilainen.masuitewarps.commands.Teleport;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteWarps extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new WarpMessageListener(this));
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new Sign(this), this);
        registerCommands();
    }

    private void registerCommands(){
        getCommand("warp").setExecutor(new Teleport(this));
    }
}
