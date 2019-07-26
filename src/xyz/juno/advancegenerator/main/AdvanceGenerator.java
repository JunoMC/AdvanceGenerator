package xyz.juno.advancegenerator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import xyz.juno.advancegenerator.main.cmds.CmdsList;
import xyz.juno.advancegenerator.main.listener.BlockBreak;
import xyz.juno.advancegenerator.main.listener.BlockPlace;
import xyz.juno.advancegenerator.main.listener.PlayerJoin;
import xyz.juno.advancegenerator.main.task.BukkitTaskChainFactory;
import xyz.juno.advancegenerator.main.task.TaskManager;
import xyz.juno.advancegenerator.main.task.core.TaskChain;
import xyz.juno.advancegenerator.main.task.core.TaskChainFactory;

public class AdvanceGenerator extends JavaPlugin implements Listener {
	private static AdvanceGenerator advance;
	public static File f, locf;
	public static FileConfiguration fc, loc;
	public static TaskManager taskManager;
	
	public static HashMap<Hologram, TextLine> TEXT_LINE = new HashMap();
	public static HashMap<String, Hologram> ALL_HOLOGRAM = new HashMap();
	private static TaskChainFactory taskChainFactory;
	
    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }
    
    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
	
	@Override
	public void onEnable() {
		if (!getDescription().getVersion().equals("1.7-release")) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		taskChainFactory = BukkitTaskChainFactory.create(this);
		
		advance = this;
		
		saveDefaultConfig();
		
		createFile();
		getCommand("ag").setExecutor(new CmdsList());
		
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
		taskManager = new TaskManager(this);
		onCountinue();
	}
	
	public void onCountinue() {
		if (loc.getConfigurationSection("Location") != null) {
			for (String random : loc.getConfigurationSection("Location").getKeys(false)) {
				String LocationWorld = loc.getString("Location." + random + ".location.world");
				double LocationX = loc.getDouble("Location." + random + ".location.x");
				double LocationY = loc.getDouble("Location." + random + ".location.y");
				double LocationZ = loc.getDouble("Location." + random + ".location.z");
				
				Hologram x = HologramsAPI.createHologram(getInstance(), new Location(Bukkit.getWorld(LocationWorld), LocationX, LocationY, LocationZ).add(0.5, 3, 0.5));
				
				x.appendTextLine(Color("&aMÁY SẢN XUẤT"));
				x.appendTextLine(Color("&e" + loc.getString("Location." + random + ".resource").toUpperCase()));
				TextLine timing = x.appendTextLine(AdvanceGenerator.Color("&bSản xuất trong &c<time> &bgiây"));
				x.appendItemLine(loc.getItemStack("Location." + random + ".type"));
				
				TEXT_LINE.put(x, timing);
				ALL_HOLOGRAM.put(random.toString(), x);
				taskManager.addTask(random, new Location(Bukkit.getWorld(LocationWorld), LocationX, LocationY, LocationZ).getBlock(), new ItemStack(Material.matchMaterial(loc.getString("Location." + random + ".resource"))), loc.getInt("Location." + random + ".time"));
			}
		}
	}
	
	@Override
	public void onDisable() {
		if (!ALL_HOLOGRAM.isEmpty()) {
			for (Entry<String, Hologram> entry : ALL_HOLOGRAM.entrySet()) {
				Hologram x = entry.getValue();
				if (x != null) {
					x.delete();
				}
			}
		}
	}
	
	public static AdvanceGenerator getInstance() {
		return advance;
	}
	
	public void createFile() {
		f = new File(getDataFolder(), "data.yml");
		fc = new YamlConfiguration();
		
		locf = new File(getDataFolder(), "location.yml");
		loc = new YamlConfiguration();
		
		if (!locf.exists()) {
			locf.getParentFile().mkdirs();
			try {
				locf.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				loc.load(locf);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			try {
				loc.load(locf);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fc.load(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fc.load(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String Color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}