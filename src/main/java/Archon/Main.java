package Archon;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInvalidMoveEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerMoveEvent;

import java.text.DecimalFormat;


public class Main extends PluginBase implements Listener{

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Archon enabled");
        this.saveDefaultConfig();

    }

    @Override
    public void onDisable(){
        getLogger().info("Archon disabled");
    }

    Server server = this.getServer();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        DecimalFormat df = new DecimalFormat("#");
        String x = df.format(player.getPosition().x);
        String y = df.format(player.getPosition().y);
        String z = df.format(player.getPosition().z);
        String msg = TextFormat.colorize(this.getConfig().getString("Format")
                .replace("%x%", x)
                .replace("%y%", y)
                .replace("%z%", z)
                .replace("%n%", "\n")
                .replace("%playercount%", "" + this.getServer().getOnlinePlayers().size())
                .replace("%itemhand%", player.getInventory().getItemInHand().getName())
                .replace("%ping%", player.getPing() + "ms")
                .replace("%servername%", this.getServer().getName())
                .replace("%maxplayers%", Integer.toString(this.getServer().getMaxPlayers()))
        );

        String type = this.getConfig().getString("Type");

        if(type.toLowerCase().contains("popup"))
            player.sendPopup(msg);
        else
            player.sendActionBar(msg);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        player.sendMessage(TextFormat.AQUA + "Your XUID: ".concat(uuid)); //Il tuo UUID al join, non darlo a nessuno!
        player.sendMessage(TextFormat.RED + "Welcome back ".concat(name) + TextFormat.YELLOW + "!");
    }

    @EventHandler
    public void onCheat (PlayerInvalidMoveEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        String name = player.getName();
        event.setFormat(TextFormat.GRAY + "Player : §f".concat(name) + " : §e" + event.getMessage());
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = null;
        if ((sender instanceof Player)) {
            player = (Player)sender;
        }
        if (command.getName().equalsIgnoreCase("fly"))
        {
            if (player == null)
            {
                sender.sendMessage("You need to be a player!");
                return true;
            }
            if (player != null)
            {
                if (player.hasPermission("archon.fly")) {
                    if (!player.getAllowFlight())
                    {
                        player.sendMessage(TextFormat.GREEN + "Archon : " + "Fly Enabled");
                        player.setAllowFlight(true);
                    }
                    else
                    {
                        sender.sendMessage(TextFormat.RED + "Archon : " + "Fly Disabled");
                        player.setAllowFlight(false);
                    }
                }
            }
            else {
                sender.sendMessage(TextFormat.RED + "Archon : " + "No permission!");
            }
        }
        return true;
    }
}
