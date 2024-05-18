package com.github.fatdavemakes.minecrafthelloworld;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MinecraftHelloWorldCommands implements CommandExecutor {

    private MinecraftHelloWorldPlugin plugin; // Local variable where we store a copy of the plugin which is given in the constructor
    
    public MinecraftHelloWorldCommands (MinecraftHelloWorldPlugin pl) {
        plugin = pl;
    }
    
    
    // A very simple function that just gets the commands and parses them. THe
    // plugin registers helloworld as a key word with and tells the server that
    // the command helloworld should come here so this only parses the sub commands.
    // This is done in the onEnable of MinecraftHelloWorldPlugin
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(args.length != 0) {
            // we have a sub command
            switch (args[0]) {
                case "start": {
                    sender.sendMessage("HelloWorld - starting");
                    plugin.startMinecraftHelloWorld(sender);
                    break;
                }
                case "stop": {
                    sender.sendMessage("HelloWorld - stopping");
                    plugin.stopMinecraftHelloWorld(sender);
                    break;
                }
                case "reload": {
                    sender.sendMessage("HelloWorld - reloading");
                    plugin.reloadMinecraftHelloWorld(sender);
                    break;
                }
                case "status": {
                    sender.sendMessage("HelloWorld - status");
                    plugin.statusMinecraftHelloWorld(sender);
                    break;
                }
                default: {
                    sender.sendMessage("HelloWorld - INVALID SUBCOMMAND");
                }
            }
        } else {
            sender.sendMessage("HelloWorld - requires subcommands");
        }
        
        return false;
    }
    
}
