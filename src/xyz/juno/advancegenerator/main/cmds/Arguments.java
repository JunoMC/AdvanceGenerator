package xyz.juno.advancegenerator.main.cmds;

public enum Arguments {
	HELP("(help|\\?)", "ag.help"),
	INFO("(info)", "ag.info"),
	RELOAD("(reload|rl)", "ag.reload"),
	SET("(set|settype)", "ag.set"),
	GIVE("(give)", "ag.give"),
	REMOVE("(delete|remove)", "ag.remove");
	
	private String regex;
	private String permission;
	
	private Arguments(String regex, String permission) {
		this.regex = regex;
		this.permission = permission;
	}
	
	public String getArgument() {
		return regex;
	}
	
	public String getPermission() {
		return permission;
	}
}