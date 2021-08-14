package me.claycoding.pocketgambler.util;

public class ConsoleMessages {

    public static String nullGuiWarning(String pluginName, String name){
        return new StringBuilder().append("[").append(pluginName).append("]").
                append(Color.colorize(" &cWARNING: inventory attempting to obtain is null: ")).append(Color.strip(name)).toString();
    }

    public static String couldNotSaveFileWarning(String pluginName, String name){
        return new StringBuilder().append("[").append(pluginName).append("]").
                append(Color.colorize(" &cWARNING: could not save the config file: ")).append(Color.strip(name)).toString();
    }

    public static String nullDataWarning(String name){
        return new StringBuilder().append(Color.colorize("&cWarning: the following is null: ")).append(Color.strip(name)).toString();
    }

    public static String needsInstanceOfPlayer(){
        return new StringBuilder().append(Color.colorize("&4Error: execute command as a player!")).toString();
    }

    public static String doNotHavePermission(){
        return new StringBuilder().append(Color.colorize("&4You do not have permission to use that command!")).toString();
    }

}
