package xyz.juno.advancegenerator.main.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskManager {
	private HashMap<Location, Task> tasks = new HashMap<>();
    private JavaPlugin plugin;

    public TaskManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addTask(String uid, Block b, ItemStack item, int time) {
            tasks.put(b.getLocation(), new Task(uid, b, item, time, plugin, true));
    }

    public void removeTask(Location loc) {
    	tasks.remove(loc).getBukkitTask().cancel();
    }
    
    public void removeInTask(Location uid) {
		Iterator<Entry<Location, Task>> iterator = tasks.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<Location, Task> next = iterator.next();
			Location str = next.getKey();
			if (str.getWorld().getName().matches(uid.getWorld().getName()) && str.getX() == uid.getX() && str.getY() == uid.getY() && str.getZ() == uid.getZ()) {
				tasks.remove(str).getBukkitTask().cancel();
			}
		}
    }
    
    public HashMap<Location, Task> getHashMap() {
    	return tasks;
    }
}