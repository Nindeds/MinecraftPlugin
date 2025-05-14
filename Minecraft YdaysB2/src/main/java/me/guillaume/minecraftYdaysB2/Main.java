package me.guillaume.minecraftYdaysB2;

import me.guillaume.minecraftYdaysB2.Listener.PlayerInteractListener;
import me.guillaume.minecraftYdaysB2.Listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Minecraft YdaysB2 is enabled! LOL");


        //Listener
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

    }


    @Override
    public void onDisable() {
        System.out.println("Minecraft YdaysB2 is disabled!");
    }
}
