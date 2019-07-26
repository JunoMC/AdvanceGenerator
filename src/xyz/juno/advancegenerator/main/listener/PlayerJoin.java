package xyz.juno.advancegenerator.main.listener;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.juno.advancegenerator.main.AdvanceGenerator;
import xyz.juno.advancegenerator.main.cmds.CmdsInterface.CmdsAPI;
import xyz.juno.advancegenerator.main.updater.Updater;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if (AdvanceGenerator.getInstance().getConfig().getBoolean("updater.enable") && AdvanceGenerator.getInstance().getConfig().getBoolean("updater.broadcast-to-staff-player")) {
			if (CmdsAPI.sender(p).isHas("ag.updater")) {
				if (Updater.hasUpdate()) {
					new BukkitRunnable() {
						@Override
						public void run() {
							p.sendMessage(AdvanceGenerator.Color("&f&m                                   "));
							p.sendMessage(AdvanceGenerator.Color("&7[&c&l►&7] &aCó phiên bản mới! &f(&a↑ &e" + Updater.verString() + "&f)"));
							TextComponent link = new TextComponent(AdvanceGenerator.Color("&c&l&oDOWNLOAD"));
							link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraftvn.net/resources/advancedgenerator.1785/history"));
							link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AdvanceGenerator.Color("&bẤn vào để mở đường dẫn!")).create()));
							
							String s = AdvanceGenerator.Color("&7[&c&l►&7] &cTải ngay: ");
							
							ArrayList<TextComponent> text = new ArrayList<TextComponent>();
							
							text.add(new TextComponent(s));
							text.add(link);
							
							TextComponent bound = new TextComponent("");
							
							for (TextComponent ex : text) {
								bound.addExtra(ex);
							}
							
							p.spigot().sendMessage(bound);
							
							p.sendMessage(AdvanceGenerator.Color("&f&m                                   "));
						}
					}.runTaskLaterAsynchronously(AdvanceGenerator.getInstance(), 20L);
					
				}
			}
		}
	}
}