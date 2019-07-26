package xyz.juno.advancegenerator.main.settings;

import xyz.juno.advancegenerator.main.AdvanceGenerator;

public interface SettingsInterface {
	String destroy(boolean prefix);
	
	public enum Settings {
		MUST_BE_PLAYER("messages.must-be-player", true),
		NO_PERMISSION("messages.no-permission", true),
		RELOAD_SUCCESS("messages.reload-success", true),
		RELOAD_ERROR("messages.reload-error", true),
		USAGE_COMMAND_SET("messages.usage.command-set", true),
		NO_ITEM_IN_HAND("messages.no-item-in-hand" ,true),
		SET_SUCCESS("messages.set-success", true),
		USAGE_COMMAND_GIVE("messages.usage.command-give", true),
		REMOVE_SUCCESS("messages.remove-success", true),
		ITEM_IN_HAND_MUST_BE_BLOCK("messages.item-in-hand-must-be-block", true),
		USAGE_COMMAND_REMOVE("messages.usage.command-remove", true),
		PLAYER_NOT_FOUND("messages.player-not-found", true),
		TYPE_NOT_FOUND("messages.type-not-found", true),
		NAME_NOT_FOUND("messages.name-not-found", true),
		JUST_PLACE_ON_ISLAND("messages.just-place-on-island", true),
		DONT_HAVE_ISLAND("messages.dont-have-island", true),
		NOT_FOUND_PLAYER("messages.not-found-player", true),
		NOT_FOUND_ASKYBLOCK_PLUGIN("messages.not-found-askyblock", true),
		IS_NOT_NUMBER("messages.is-not-number", true),
		CAN_NOT_BREAK_IN_ANOTHER_ISLAND("messages.can-not-break-in-another-island", true);
		
		private String path;
		private boolean prefix;
		
		private Settings(String path, boolean prefix) {
			this.path = path;
			this.prefix = prefix;
		}
		
		public String getPath() {
			return path;
		}
		
		public boolean isPrefix() {
			return prefix;
		}
	}
	
	class SettingsUtils implements SettingsInterface {
		public String str;
		
		public SettingsUtils(String str) {
			this.str = str;
		}
		
		@Override
		public String destroy(boolean prefix) {
			String var = "";
			AdvanceGenerator AG = AdvanceGenerator.getInstance();
			String pf = AG.getConfig().getString("Prefix");
			
			if (prefix) {
				var = AdvanceGenerator.Color(pf + AG.getConfig().getString(str));
			} else {
				var = AdvanceGenerator.Color(AG.getConfig().getString(str));
			}
			
			return var;
		}
		
	}

	class SettingAPI {
		private static SettingsUtils st;
		
		public static SettingsUtils getPath(String str) {
			st = new SettingsUtils(str);
			return st;
		}
	}
}