package xyz.juno.advancegenerator.main.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.juno.advancegenerator.main.AdvanceGenerator;

public interface CmdsInterface {
	
	boolean isHas(String permisison);
	boolean isLength(String[] a, int length);
	void send(String message);
	void sendPath(String path);
	boolean isArgumentSimilar(String args, String regex);
	boolean isPlayer();
	Player toPlayer();
	boolean isMaxLength(String[] a, int length);
	boolean isMinLength(String[] a, int length);
	boolean isMinMaxLength(String[] a, int min, int max);

	class CmdsUtils implements CmdsInterface {
		public CommandSender sender;
		
		public CmdsUtils(CommandSender sender) {
			this.sender = sender;
		}
		
		@Override
		public boolean isPlayer() {
			return sender instanceof Player ? true : false;
		}
		
		@Override
		public boolean isHas(String permission) {
			return ((Player)sender).hasPermission(permission) ? true : false;
		}
		
		@Override
		public boolean isLength(String[] a, int length) {
			return a.length == length ? true : false;
		}

		@Override
		public void send(String message) {
			sender.sendMessage(message);
		}

		@Override
		public boolean isArgumentSimilar(String args, String regex) {
			return args.toLowerCase().matches(regex) ? true : false;
		}

		@Override
		public Player toPlayer() {
			return (Player)sender;
		}

		@Override
		public void sendPath(String path) {
			AdvanceGenerator AG = AdvanceGenerator.getInstance();
			String pf = AG.getConfig().getString("Prefix");
			
			String var = AdvanceGenerator.Color(pf + AG.getConfig().getString(path));
			sender.sendMessage(var);
		}

		@Override
		public boolean isMaxLength(String[] a, int length) {
			return a.length < length ? true : false;
		}

		@Override
		public boolean isMinLength(String[] a, int length) {
			return a.length > length ? true : false;
		}

		@Override
		public boolean isMinMaxLength(String[] a, int min, int max) {
			return (a.length > min && a.length < max) ? true : false;
		}
	}

	class CmdsAPI {
		private static CmdsUtils utils;
		
		public static CmdsUtils sender(CommandSender sender) {
			utils = new CmdsUtils(sender);
			return utils;
		}
	}
}