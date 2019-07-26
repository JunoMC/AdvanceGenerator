package xyz.juno.advancegenerator.main.listener;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import xyz.juno.advancegenerator.main.AdvanceGenerator;
import xyz.juno.advancegenerator.main.cmds.CmdsInterface.CmdsAPI;
import xyz.juno.advancegenerator.main.settings.SettingsInterface.Settings;

public class BlockBreak implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		FileConfiguration LOCATION = AdvanceGenerator.loc;
		File FILE_LOCATION = AdvanceGenerator.locf;
		Block b = e.getBlock();
		
		if (LOCATION.getConfigurationSection("Location") != null) {
			for (String UID_LOCATION : LOCATION.getConfigurationSection("Location").getKeys(false)) {
				
				Location BlockLocation = b.getLocation();
				
				String LocationWorld = LOCATION.getString("Location." + UID_LOCATION + ".location.world");
				double LocationX = LOCATION.getDouble("Location." + UID_LOCATION + ".location.x");
				double LocationY = LOCATION.getDouble("Location." + UID_LOCATION + ".location.y");
				double LocationZ = LOCATION.getDouble("Location." + UID_LOCATION + ".location.z");
				
				Location BreakLocation = new Location(Bukkit.getWorld(LocationWorld), LocationX, LocationY, LocationZ);
				
				double BlockX = b.getLocation().getX();
				double BlockY = b.getLocation().getY();
				double BlockZ = b.getLocation().getZ();
				
				if (BlockLocation.getWorld().getName().equals(BreakLocation.getWorld().getName())) {
					if (BlockX == BreakLocation.getX() && BlockY == BreakLocation.getY() && BlockZ == BreakLocation.getZ()) {
						if (AdvanceGenerator.getInstance().getConfig().getBoolean("hook-ASkyBlock")) {
							
							if (Bukkit.getPluginManager().getPlugin("ASkyBlock") == null) {
								CmdsAPI.sender(e.getPlayer()).sendPath(Settings.NOT_FOUND_ASKYBLOCK_PLUGIN.getPath());
								return;
							}
							
							if (AdvanceGenerator.getInstance().getConfig().getBoolean("can-not-break-in-another-island")) {
								if (String.valueOf(e.getPlayer().getUniqueId()).matches(String.valueOf(ASkyBlockAPI.getInstance().getOwner(b.getLocation()))) || ASkyBlockAPI.getInstance().inTeam(ASkyBlockAPI.getInstance().getOwner(b.getLocation()))) {
									if (!AdvanceGenerator.getInstance().getConfig().getBoolean("Configuration.break.drop-default-item")) {
										AdvanceGenerator.getInstance().getConfig().getStringList("Configuration.break.game-mode-allow").forEach(game_mode -> {
											b.setType(Material.AIR);
											if (CmdsAPI.sender(e.getPlayer()).toPlayer().getGameMode().name().toUpperCase().equals(game_mode.toUpperCase())) {
												ItemStack drop = LOCATION.getItemStack("Location." + UID_LOCATION + ".type").clone();
												drop.setAmount(1);
												b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0, 0.5), drop);
											}
										});
									}
									
									AdvanceGenerator.ALL_HOLOGRAM.remove(UID_LOCATION).delete();
									AdvanceGenerator.taskManager.removeInTask(e.getBlock().getLocation());
									
									LOCATION.set("Location." + UID_LOCATION, null);
									
									try {
										LOCATION.save(FILE_LOCATION);
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								} else {
									e.setCancelled(true);
									CmdsAPI.sender(e.getPlayer()).sendPath(Settings.CAN_NOT_BREAK_IN_ANOTHER_ISLAND.getPath());
								}
							}
						} else {
							if (!AdvanceGenerator.getInstance().getConfig().getBoolean("Configuration.break.drop-default-item")) {
								AdvanceGenerator.getInstance().getConfig().getStringList("Configuration.break.game-mode-allow").forEach(game_mode -> {
									b.setType(Material.AIR);
									if (CmdsAPI.sender(e.getPlayer()).toPlayer().getGameMode().name().toUpperCase().equals(game_mode.toUpperCase())) {
										ItemStack drop = LOCATION.getItemStack("Location." + UID_LOCATION + ".type").clone();
										drop.setAmount(1);
										b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0, 0.5), drop);
									}
								});
							}
							
							AdvanceGenerator.ALL_HOLOGRAM.remove(UID_LOCATION).delete();
							AdvanceGenerator.taskManager.removeTask(e.getBlock().getLocation());
							
							LOCATION.set("Location." + UID_LOCATION, null);
							
							try {
								LOCATION.save(FILE_LOCATION);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}