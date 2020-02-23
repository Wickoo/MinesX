package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoreCMD implements CommandExecutor, TabExecutor {

    private MinesX plugin;
    private MinesHandler handler;

    private List<CommandManager> commands;

    public CoreCMD(MinesX plugin, MinesHandler handler) {
        this.plugin = plugin;
        this.handler = handler;

        commands = new ArrayList<>();
        commands.add(new ResetCMD(handler));
        commands.add(new CreateCMD(handler));
        commands.add(new ViewCMD(handler));
        commands.add(new WandCMD(handler));
        commands.add(new ResetAllCMD(handler));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command basecommand, String label, String[] args) {

        Player player = (Player) sender;

        switch (args.length) {

            default:
                player.sendMessage(Util.col("&c&lIncorrect Usage! &7See /minesx help for proper syntax"));
                break;

            case 0:

                String version = Bukkit.getVersion().substring(Bukkit.getVersion().lastIndexOf(':') + 1).replace(')', ' ');
                player.sendMessage(Util.col("&a&lMinesX &7by author &bWick_\n&aPlugin Version: &7" +
                        plugin.getDescription().getVersion() + "\n&aCommand: &7/minesx <command>" + "\n&aMinecraft Version:&7" + version));
                return true;

            case 1:
            case 2:
            case 3:

                String subcommand = args[0];

                for (CommandManager command : commands) {

                    if (command.getName().equalsIgnoreCase(subcommand)) {

                        if(!player.hasPermission(command.getPermission())) {

                            player.sendMessage(Util.col("&c&lERROR! &7Insufficient permissions"));
                            return true;

                        }

                        command.executeCommand((Player) sender, args);
                        return true;

                    }

                }

                player.sendMessage(Util.col("&c&lERROR! &7Unknown command &c/minesx " + args[0] ));
                return true;

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("minesx")) {

            ArrayList<String> autoCompletes = new ArrayList<>();

            if (args.length == 1) {

                for (CommandManager commandManager : commands) {

                    autoCompletes.add(commandManager.getName());

                }

                return autoCompletes;

            }

        }

        return null;
    }

    public List<CommandManager> getCommands () {
        return commands;
    }

}
