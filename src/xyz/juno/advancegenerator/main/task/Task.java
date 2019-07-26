package xyz.juno.advancegenerator.main.task;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import xyz.juno.advancegenerator.main.AdvanceGenerator;
import xyz.juno.advancegenerator.main.task.core.TaskChainTasks;

public class Task extends BukkitRunnable {
	private Block b;
	private ItemStack item;
    private BukkitTask task;
    private int time;
    private int run;
    private String uid;
    
    public Task(String uid, Block b, ItemStack item, int time, JavaPlugin plugin, boolean async) {
    	this.uid = uid;
        this.b = b;
        this.item = item;
        this.time = time;
        if (!async) {
            this.task = runTaskTimerAsynchronously(plugin, 0, 20);
        } else {
            this.task = runTaskTimer(plugin, 0, 20);
        }
    }
    
    public TaskManager getTaskManager() {
		return AdvanceGenerator.taskManager;
	}
    
    public void performGenerator(final Block b, final ItemStack item, final Location loc) {
    	AdvanceGenerator.newChain().sync(new TaskChainTasks.GenericTask() {
    		@Override
	        public void runGeneric() {
		    	run = 0;
		    	
		    	if (time == run) {
		    		b.getLocation().getWorld().dropItemNaturally(loc.add(0.5, 2, 0.5), item).setVelocity(new Vector(0, 0, 0));
		    		time = AdvanceGenerator.loc.getInt("Location." + uid + ".time");
		    	} else {
		    		time -= 1;
		    	}
		    	Hologram x = AdvanceGenerator.ALL_HOLOGRAM.get(uid);
		    	AdvanceGenerator.TEXT_LINE.get(x).setText(AdvanceGenerator.Color("&bSản xuất trong &c<time> &bgiây").replace("<time>", String.valueOf(time)));
    		}
    	}).execute();
	}

    @Override
    public void run() {
    	performGenerator(b, item, b.getLocation());
	}

    public BukkitTask getBukkitTask() {
        return task;
    }
}