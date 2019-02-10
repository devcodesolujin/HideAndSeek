package me.devcode.hideandseek;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.devcode.hideandseek.listeners.CancelListeners;
import me.devcode.hideandseek.listeners.DeathListener;
import me.devcode.hideandseek.listeners.PlayerInteractListener;
import me.devcode.hideandseek.listeners.PlayerJoinListener;
import me.devcode.hideandseek.listeners.PlayerMoveListener;
import me.devcode.hideandseek.listeners.PlayerQuitListener;
import me.devcode.hideandseek.listeners.ShopListener;
import me.devcode.hideandseek.listeners.TauntListeners;
import me.devcode.hideandseek.mysql.AsyncMySQL;
import me.devcode.hideandseek.mysql.MySQLMethods;
import me.devcode.hideandseek.mysql.MySQLStats;
import me.devcode.hideandseek.mysql.MySQLUtils;
import me.devcode.hideandseek.utils.CountdownHandler;
import me.devcode.hideandseek.utils.GameStatus;
import me.devcode.hideandseek.utils.GameUtils;
import me.devcode.hideandseek.utils.MessageUtils;
import me.devcode.hideandseek.utils.PlayerUtils;
import me.devcode.hideandseek.utils.ShopUtils;
import me.devcode.hideandseek.utils.TeleportUtils;
import me.devcode.hideandseek.utils.TitleApi;

@Getter
@Setter
public class HideAndSeek extends JavaPlugin implements Listener {
    @Getter
    private static HideAndSeek instance;

    private MessageUtils messageUtils;
    private GameStatus gameStatus;
    private PlayerUtils playerUtils;
    private TitleApi titleApi;
    private CountdownHandler countdownHandler;
    private GameUtils gameUtils;

    private int minPlayers, maxPlayers;

    private AsyncMySQL mysql;
    private MySQLStats stats;
    private MySQLUtils mySQLUtils;
    private MySQLMethods mySQLMethods;
    private ShopUtils shopUtils;
    private TeleportUtils teleportUtils;

    private File mysqlFile = new File("plugins/HideAndSeek", "mysql.yml");
    private FileConfiguration mysqlCfg = null;
    private File ingameFile = new File("plugins/HideAndSeek", "ingame.yml");
    private FileConfiguration ingameCfg = null;

    private boolean mysqlActivated = false;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        instance = this;
        if(!mysqlFile.exists()) {
            mysqlCfg = loadFile("mysql.yml");
        }else{
            mysqlCfg = YamlConfiguration.loadConfiguration(mysqlFile);
        }
        if(!ingameFile.exists()) {
            ingameCfg = loadFile("ingame.yml");
        }else{
            ingameCfg = YamlConfiguration.loadConfiguration(ingameFile);
        }
        minPlayers = ingameCfg.getInt("Game.MinPlayers");
        maxPlayers = ingameCfg.getInt("Game.MaxPlayers");
        connectMySQL();
        messageUtils = new MessageUtils();
        gameStatus = GameStatus.LOBBY;
        playerUtils = new PlayerUtils();
        titleApi = new TitleApi();
        stats = new MySQLStats();
        mySQLUtils = new MySQLUtils();
        mySQLMethods = new MySQLMethods();
        shopUtils = new ShopUtils();
        countdownHandler = new CountdownHandler();
        countdownHandler.startLobbyCountdown();
        teleportUtils = new TeleportUtils();
        gameUtils = new GameUtils();
        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        ArrayList<Listener> list = new ArrayList<>();
        list.add(new PlayerJoinListener());
        list.add(new ShopListener());
        list.add(new PlayerMoveListener());
        list.add(new PlayerInteractListener());
        list.add(new CancelListeners());
        list.add(new DeathListener());
        list.add(new TauntListeners());
        list.add(new PlayerQuitListener());
        PluginManager pluginManager = getServer().getPluginManager();
        list.forEach(listener ->
                pluginManager.registerEvents(listener, this));
    }

    private final HashMap<String, CommandExecutor> commands = new HashMap<>();
    private final String commandPackage = "me.devcode.hideandseek.commands";

    private void register(String key, CommandExecutor val){
        commands.put(key, val);
        getCommand(key).setExecutor(val);
    }
    private void registerCommands(){
        getDescription().getCommands().entrySet().stream().map(Map.Entry::getKey)
                .forEach( commandName ->{
                    try {
                        CommandExecutor commandExecutor = (CommandExecutor) Class.forName(commandPackage + "." + StringUtils.capitalize(commandName)).getConstructor().newInstance();
                        register(commandName, commandExecutor);
                    } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                });
    }

    private void connectMySQL() {
        if(!mysqlCfg.getBoolean("MySQL.Activated"))
            return;
        mysqlActivated = true;
        mysql = new AsyncMySQL(this, mysqlCfg.getString("MySQL.Host"), mysqlCfg.getInt("MySQL.Port"), mysqlCfg.getString("MySQL.User"), mysqlCfg.getString("MySQL.Password"), mysqlCfg.getString("MySQL.Database"));
        mysql.update("CREATE TABLE IF NOT EXISTS HideAndSeek(UUID varchar(64), KILLS int, DEATHS int, WINS int, GAMES int, BLOCKS TEXT, COINS int);");
    }

    @Override
    public void onDisable() {
        if(mysqlActivated)
        mysql.getMySQL().closeConnection();
    }

    @SneakyThrows
    public FileConfiguration loadFile(String file) {
        File t = new File(this.getDataFolder(), file);
        System.out.println("Writing new file: " + t.getAbsolutePath());

        t.createNewFile();
        FileWriter out = new FileWriter(t);
        InputStream is = getClass().getResourceAsStream("/" + file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            out.write(line + "\n");
        }
        out.flush();
        is.close();
        isr.close();
        br.close();
        out.close();
    return YamlConfiguration.loadConfiguration(t);
    }

}
