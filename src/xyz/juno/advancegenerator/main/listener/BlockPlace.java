package xyz.juno.advancegenerator.main.listener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.wasteofplastic.askyblock.ASkyBlockAPI;

import xyz.juno.advancegenerator.main.AdvanceGenerator;
import xyz.juno.advancegenerator.main.cmds.CmdsInterface.CmdsAPI;
import xyz.juno.advancegenerator.main.settings.SettingsInterface.Settings;
import xyz.juno.advancegenerator.main.task.TaskManager;

public class BlockPlace implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		//Player p = e.getPlayer();
		
		FileConfiguration DATA = AdvanceGenerator.fc;
		
		FileConfiguration LOCATION = AdvanceGenerator.loc;
		File FILE_LOCATION = AdvanceGenerator.locf;
		
		TaskManager TASK_MANAGER = AdvanceGenerator.taskManager;
		
		//Location PLAYER_LOCATION = p.getLocation();
		
		if (e.canBuild()) {
			if (DATA.getConfigurationSection("List") == null) {
				return;
			}
			for (String random : DATA.getConfigurationSection("List").getKeys(false)) {
				if (item.isSimilar(DATA.getItemStack("List." + random + ".material"))) {
					if (AdvanceGenerator.getInstance().getConfig().getBoolean("Configuration.hook-ASkyBlock")) {
						if (Bukkit.getPluginManager().getPlugin("ASkyBlock") != null) {
							if (AdvanceGenerator.getInstance().getConfig().getBoolean("Configuration.Place-require.has-island")) {
								if (!ASkyBlockAPI.getInstance().hasIsland(e.getPlayer().getUniqueId())) {
									e.setCancelled(true);
									CmdsAPI.sender(e.getPlayer()).sendPath(Settings.DONT_HAVE_ISLAND.getPath());
									return;
								}
							}
							if (AdvanceGenerator.getInstance().getConfig().getBoolean("Configuration.Place-require.is-on-island")) {
								if (!ASkyBlockAPI.getInstance().playerIsOnIsland(e.getPlayer())) {
									e.setCancelled(true);
									CmdsAPI.sender(e.getPlayer()).sendPath(Settings.JUST_PLACE_ON_ISLAND.getPath());
									return;
								}
							}
							
							Block b = e.getBlockPlaced();
							
							UUID uid = UUID.randomUUID();
							
							LOCATION.set("Location." + uid + ".location.world", b.getLocation().getWorld().getName());
							LOCATION.set("Location." + uid + ".location.x", b.getLocation().getX());
							LOCATION.set("Location." + uid + ".location.y", b.getLocation().getY());
							LOCATION.set("Location." + uid + ".location.z", b.getLocation().getZ());
							LOCATION.set("Location." + uid + ".type", DATA.getItemStack("List." + random + ".material"));
							LOCATION.set("Location." + uid + ".resource", Material.matchMaterial(DATA.getString("List." + random + ".type")).name());
							LOCATION.set("Location." + uid + ".time", DATA.getInt("List." + random + ".time"));
							
							try {
								LOCATION.save(FILE_LOCATION);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
								
							Hologram x = HologramsAPI.createHologram(AdvanceGenerator.getInstance(), b.getLocation().add(0.5, 3, 0.5));
							
							x.appendTextLine(AdvanceGenerator.Color("&aMÁY SẢN XUẤT"));
							x.appendTextLine(AdvanceGenerator.Color("&e" + Material.matchMaterial(DATA.getString("List." + random + ".type")).name().toUpperCase()));
							TextLine timing = x.appendTextLine(AdvanceGenerator.Color("&bSản xuất trong &c<time> &bgiây"));
							x.appendItemLine(item);
							
							AdvanceGenerator.TEXT_LINE.put(x, timing);
							AdvanceGenerator.ALL_HOLOGRAM.put(String.valueOf(uid), x);
							
							TASK_MANAGER.addTask(String.valueOf(uid), b, new ItemStack(Material.matchMaterial(DATA.getString("List." + random + ".type"))), DATA.getInt("List." + random + ".time"));
						
						} else {
							CmdsAPI.sender(e.getPlayer()).sendPath(Settings.NOT_FOUND_ASKYBLOCK_PLUGIN.getPath());
						}
					} else {
						Block b = e.getBlockPlaced();
						
						UUID uid = UUID.randomUUID();
						
						LOCATION.set("Location." + uid + ".location.world", b.getLocation().getWorld().getName());
						LOCATION.set("Location." + uid + ".location.x", b.getLocation().getX());
						LOCATION.set("Location." + uid + ".location.y", b.getLocation().getY());
						LOCATION.set("Location." + uid + ".location.z", b.getLocation().getZ());
						LOCATION.set("Location." + uid + ".type", item);
						LOCATION.set("Location." + uid + ".resource", Material.matchMaterial(DATA.getString("List." + random + ".type")).name());
						LOCATION.set("Location." + uid + ".time", DATA.getInt("List." + random + ".time"));
						
						try {
							LOCATION.save(FILE_LOCATION);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
							
						Hologram x = HologramsAPI.createHologram(AdvanceGenerator.getInstance(), b.getLocation().add(0.5, 3, 0.5));
						
						x.appendTextLine(AdvanceGenerator.Color("&aMÁY SẢN XUẤT"));
						x.appendTextLine(AdvanceGenerator.Color("&e" + Material.matchMaterial(DATA.getString("List." + random + ".type")).name().toUpperCase()));
						TextLine timing = x.appendTextLine(AdvanceGenerator.Color("&bSản xuất trong &c<time> &bgiây"));
						x.appendItemLine(item);
						
						AdvanceGenerator.TEXT_LINE.put(x, timing);
						AdvanceGenerator.ALL_HOLOGRAM.put(String.valueOf(uid), x);
						
						TASK_MANAGER.addTask(String.valueOf(uid), b, new ItemStack(Material.matchMaterial(DATA.getString("List." + random + ".type"))), DATA.getInt("List." + random + ".time"));
					}
				}
			}
		}
	}
}