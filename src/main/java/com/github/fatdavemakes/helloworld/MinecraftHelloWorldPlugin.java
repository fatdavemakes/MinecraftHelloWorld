package com.github.fatdavemakes.minecrafthelloworld;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import org.bukkit.entity.Player;
import java.util.LinkedList;



public final class MinecraftHelloWorldPlugin extends JavaPlugin implements Listener {

    //Read from config file to see if plugin should start automatically
    private boolean autostart;
    
    // Read from config file, how many ticks between the scheduled task(just prints a message)
    private long updateinterval;
    
    // Variable we will store the task in that we run periodically.
    private BukkitTask periodictask = null;
    
    // Called when the plugin is loaded/enabled
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        getMinecraftHelloWorldConfig();
        
        Bukkit.getPluginManager().registerEvents(this, this);
        // Registering the commands, see the MinecraftHelloWorldCommands class for implementation
        this.getCommand("helloworld").setExecutor(new MinecraftHelloWorldCommands(this));
        autostart();      


    }
    
    // Function for autostarting, called from the enable function and the reload function.
    private void autostart() {
        if(autostart) {
            startMinecraftHelloWorld(null);
        } else {
            getLogger().info("helloworld NOT STARTING as autostart is false, run '/helloworld start'");
        }  
    }
    
    // Read the config values into local variables. Can be called whenever new config info needs to be loaded
    private void getMinecraftHelloWorldConfig() {
        reloadConfig();
        updateinterval = getConfig().getLong("periodic-update-tick-interval",(10*20)); // Gets the value from config and if not set defaults to 10 * 20 i.e. 10 seconds
        autostart = getConfig().getBoolean("autostart");
    }
    
    // When the plugin is disabled, just stop it. Usually happens on server shutdown.
    // In theory should probably take the command name away etc but overkill for this example.
    
    @Override
    public void onDisable() {
        stopMinecraftHelloWorld(null);
    }
    
    // The functionality for starting the plugin, called from the autostart function and also from within the MinecraftHelloWorldCommands
    // On large scale plugins with lots of commands integrating all the functionality into the top Plugin class would not be ideal
    // but core functionlaity like start/stop makes sense and for an example keeping them in one class is nice and simple
    public void startMinecraftHelloWorld(CommandSender sender) {
        if(periodictask != null) { //If we are already running we shouldn't start again, using null as a simple flag. Notice that stop will reset this to null.
            getLogger().warning("helloworld is already running so cannot be started - reload to update with new config");
            if(sender != null) {
                sender.sendMessage("helloworld is already running so cannot be started - reload to update with new config");
            }
            return; // Return prevents the running of the task below.
        }
        
        // Create the actual task, passes in the updateinterval read from the config or created above and passes the
        // this.runPeriodicTask() as the function to call.
        periodictask = Bukkit.getScheduler().runTaskTimer(this, () -> this.runPeriodicTask(), 1L, updateinterval);
    }

    // Stops... not much to say
    public void stopMinecraftHelloWorld(CommandSender sender) {

        
        if(sender != null) {
            sender.sendMessage("helloworld Stopped");
        }
        
        periodictask.cancel();
        periodictask=null;
    }
    
    // A status command is usually used for checking the plugin is running and giving any useful info. IN THIS
    // CASE we are just printing a message. Shows example of casting a sender to a player this is useful for handling commands
    public void statusMinecraftHelloWorld(CommandSender sender) {
        if(sender != null) {
            Player p = (Player)sender;// Player is an interface of CommandSender so we can cast it and use it to get the player name.
            String player_name = p.getName(); 
            sender.sendMessage("helloworld Status, Hello" + player_name);
        }
    }
    
    //Reload is updating the config. We stop, get the new config and then call autostart to decide if we should start without a manual command
    public void reloadMinecraftHelloWorld(CommandSender sender) {
        getLogger().info("helloworld - reload triggered");
        stopMinecraftHelloWorld(null);
        getMinecraftHelloWorldConfig();
        autostart();
        if(sender != null) {
            sender.sendMessage("helloworld - reloaded");
        }
    }
    
    // Make a default config if one isn't set
    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
        // perform configuration upgrade - the config file in the plugin is V1 so that all the upgfrades can be coded
        if (getConfig().getInt("config-version") != 2) { 
            // default of regenerating ever 5 seconds
            getConfig().set("autostart", false);
            getConfig().set("config-version", 2);
            saveConfig();
        }
    }

    // Will just send a message to everyone on the server, not great but shows that it is running.
    public void runPeriodicTask() {
        Bukkit.broadcastMessage("This is helloworld, just saying HELLO!");
    }

}
