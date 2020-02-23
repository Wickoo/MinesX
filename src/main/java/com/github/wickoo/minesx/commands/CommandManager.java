package com.github.wickoo.minesx.commands;

import org.bukkit.entity.Player;

public interface CommandManager {

    String getName ();
    String getPermission ();
    String getAdditionalArgs ();
    String getDescription();
    void executeCommand (Player player, String[] args);

}
