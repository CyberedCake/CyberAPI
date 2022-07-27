package net.cybercake.cyberapi.spigot.server;

import net.cybercake.cyberapi.spigot.server.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class CyberAPIListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        String args = (event.getMessage().substring(command.length()).isEmpty() ? null : (event.getMessage().substring((command + " ").length())));
        String fakeCommand = CommandManager.getFakeCommands().get(command.substring(1)); // substring because we don't want the slash
        if(fakeCommand == null) return;
        event.setCancelled(true);
        event.getPlayer().performCommand(fakeCommand.replace("remove:", "") + (args == null ? "" : " " + args));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent event) {
        event.getCommands().removeAll(CommandManager.getFakeCommands().values()
                .stream()
                .filter(command -> command.startsWith("remove:"))
                .map(command -> command.substring("remove:".length()))
                .toList()
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTabCompleteEvent(TabCompleteEvent event) {
        String command = event.getBuffer().split(" ")[0];
        String argsTogether = event.getBuffer().substring((command + " ").length());
        String[] args = argsTogether.split(" ");
        if(argsTogether.endsWith(" ")) {
            char[] characters = argsTogether.toCharArray();
            int totalIncrease = 0;
            for(int i = characters.length-1; i>0; i--) {
                if(Character.isSpaceChar(characters[i])) break;
                totalIncrease++;
            }
            List<String> newArgs = new ArrayList<>(List.of(args));
            newArgs.add(" ".repeat(totalIncrease));
            args = newArgs.toArray(String[]::new);
        }
        String fakeCommandActual = CommandManager.getFakeCommands().get(command.substring(1));
        if(fakeCommandActual == null) return;
        PluginCommand pluginCommand = Bukkit.getPluginCommand(fakeCommandActual);
        if(pluginCommand == null) return;
        List<String> newCompletions = pluginCommand.tabComplete(event.getSender(), command, args);
        if(newCompletions == null) return;
        event.setCompletions(newCompletions);
    }

}
