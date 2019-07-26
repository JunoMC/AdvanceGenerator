package xyz.juno.advancegenerator.main.cmds;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.juno.advancegenerator.main.AdvanceGenerator;
import xyz.juno.advancegenerator.main.cmds.CmdsInterface.CmdsAPI;
import xyz.juno.advancegenerator.main.settings.SettingsInterface.Settings;

public class CmdsList implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command c, String label, String[] a) {
		
		Arguments HELP = Arguments.HELP;
		Arguments INFO = Arguments.INFO;
		Arguments RELOAD = Arguments.RELOAD;
		Arguments SET = Arguments.SET;
		Arguments GIVE = Arguments.GIVE;
		Arguments REMOVE = Arguments.REMOVE;
		
		if (CmdsAPI.sender(sender).isLength(a, 0)) {
			if (CmdsAPI.sender(sender).isPlayer()) {
				if (CmdsAPI.sender(sender).isHas(HELP.getPermission())) {
					for (String help : AdvanceGenerator.getInstance().getConfig().getStringList("messages.help")) {
						CmdsAPI.sender(sender).send(AdvanceGenerator.Color(help.replace("{lenh}", label)));
					}
				} else {
					CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
				}
			} else {
				for (String help : AdvanceGenerator.getInstance().getConfig().getStringList("messages.help")) {
					CmdsAPI.sender(sender).send(AdvanceGenerator.Color(help.replace("{lenh}", label)));
				}
			}
		}
		
		if (CmdsAPI.sender(sender).isLength(a, 1)) {
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], HELP.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(HELP.getPermission())) {
						for (String help : AdvanceGenerator.getInstance().getConfig().getStringList("messages.help")) {
							CmdsAPI.sender(sender).send(AdvanceGenerator.Color(help.replace("{lenh}", label)));
						}
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					for (String help : AdvanceGenerator.getInstance().getConfig().getStringList("messages.help")) {
						CmdsAPI.sender(sender).send(AdvanceGenerator.Color(help.replace("{lenh}", label)));
					}
				}
			}
			
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], RELOAD.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(RELOAD.getPermission())) {
						try {
							AdvanceGenerator.getInstance().reloadConfig();
							AdvanceGenerator.getInstance().createFile();
						} catch (Exception e) {
							CmdsAPI.sender(sender).sendPath(Settings.RELOAD_ERROR.getPath());
						} finally {
							CmdsAPI.sender(sender).sendPath(Settings.RELOAD_SUCCESS.getPath());
						}
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					try {
						AdvanceGenerator.getInstance().reloadConfig();
						AdvanceGenerator.getInstance().createFile();
					} catch (Exception e) {
						CmdsAPI.sender(sender).sendPath(Settings.RELOAD_ERROR.getPath());
					} finally {
						CmdsAPI.sender(sender).sendPath(Settings.RELOAD_SUCCESS.getPath());
					}
				}
			}
			
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], INFO.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(INFO.getPermission())) {
						CmdsAPI.sender(sender).send(AdvanceGenerator.Color("&m                     "));
						
						TextComponent s1 = new TextComponent(AdvanceGenerator.Color("&fAuthor: "));
						
						TextComponent s2 = new TextComponent(AdvanceGenerator.Color("&eJuno"));
						s2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.facebook.com/profile.php?id=100033827385372"));
			            s2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AdvanceGenerator.Color("&bGo to Juno's facebook")).create()));
						
						ArrayList<TextComponent> array = new ArrayList<TextComponent>();
						array.add(s1);
						array.add(s2);
						
						TextComponent message = new TextComponent("");
						
						for (TextComponent total : array) {
							message.addExtra(total);
						}
						
						CmdsAPI.sender(sender).toPlayer().spigot().sendMessage(message);
						CmdsAPI.sender(sender).send(AdvanceGenerator.Color("&fVersion: 1.7-release"));
						
						CmdsAPI.sender(sender).send(AdvanceGenerator.Color("&m                     "));
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					CmdsAPI.sender(sender).sendPath(Settings.MUST_BE_PLAYER.getPath());
				}
			}
		}
		
		if (CmdsAPI.sender(sender).isMinMaxLength(a, 0, 5)) {
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], SET.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(SET.getPermission())) {
						if (CmdsAPI.sender(sender).isMaxLength(a, 4)) {
							CmdsAPI.sender(sender).sendPath(Settings.USAGE_COMMAND_SET.getPath());
						} else {
							if (CmdsAPI.sender(sender).isLength(a, 4)) {
								String name = a[1];
								String type = a[2];
								String time = a[3];
								
								if(CmdsAPI.sender(sender).toPlayer().getItemInHand() == null || 
										CmdsAPI.sender(sender).toPlayer().getItemInHand().getType() == Material.AIR) {
									CmdsAPI.sender(sender).sendPath(Settings.NO_ITEM_IN_HAND.getPath());
								} else {
									if (Material.matchMaterial(type) == null) {
										CmdsAPI.sender(sender).sendPath(Settings.TYPE_NOT_FOUND.getPath());
									} else {
										if (!CmdsAPI.sender(sender).toPlayer().getItemInHand().getType().isBlock()) {
											CmdsAPI.sender(sender).sendPath(Settings.ITEM_IN_HAND_MUST_BE_BLOCK.getPath());
										} else {
											try {
												int t = Integer.parseInt(time);
												ItemStack newItem = CmdsAPI.sender(sender).toPlayer().getItemInHand().clone();
												newItem.setAmount(1);
												AdvanceGenerator.fc.set("List." + name + ".material", newItem);
												AdvanceGenerator.fc.set("List." + name + ".time", t);
												AdvanceGenerator.fc.set("List." + name + ".type", type);
												
												try {
													AdvanceGenerator.fc.save(AdvanceGenerator.f);
												} catch (IOException e) {
													e.printStackTrace();
												} finally {
													CmdsAPI.sender(sender).sendPath(Settings.SET_SUCCESS.getPath());
												}
											} catch (NumberFormatException ex) {
												CmdsAPI.sender(sender).sendPath(Settings.IS_NOT_NUMBER.getPath());
											}
										}
									}
								}
							}
						}
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					CmdsAPI.sender(sender).sendPath(Settings.MUST_BE_PLAYER.getPath());
				}
			}
			
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], GIVE.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(GIVE.getPermission())) {
						if (CmdsAPI.sender(sender).isMaxLength(a, 4)) {
							CmdsAPI.sender(sender).sendPath(Settings.USAGE_COMMAND_GIVE.getPath());
						} else {
							if (CmdsAPI.sender(sender).isLength(a, 4)) {
								String player = a[1];
								String type = a[2];
								String amount = a[3];
								
								if (Bukkit.getPlayer(player) == null) {
									CmdsAPI.sender(sender).sendPath(Settings.PLAYER_NOT_FOUND.getPath());
								} else {
									if (AdvanceGenerator.fc.getItemStack("List." + type + ".material") == null) {
										CmdsAPI.sender(sender).sendPath(Settings.NAME_NOT_FOUND.getPath());
									} else {
										if (Bukkit.getPlayer(player) != null) {
											try {
												int i = Integer.parseInt(amount);
												ItemStack item = AdvanceGenerator.fc.getItemStack("List." + type + ".material");
												item.setAmount(i);
												CmdsAPI.sender(Bukkit.getPlayer(player)).toPlayer().getInventory().addItem(item);
											} catch (NumberFormatException ex) {
												CmdsAPI.sender(sender).sendPath(Settings.IS_NOT_NUMBER.getPath());
											}
										} else {
											CmdsAPI.sender(sender).sendPath(Settings.NOT_FOUND_PLAYER.getPath());
										}
									}
								}
							}
						}
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					CmdsAPI.sender(sender).sendPath(Settings.MUST_BE_PLAYER.getPath());
				}
			}
		}
		
		if (CmdsAPI.sender(sender).isMinMaxLength(a, 0, 3)) {
			if (CmdsAPI.sender(sender).isArgumentSimilar(a[0], REMOVE.getArgument())) {
				if (CmdsAPI.sender(sender).isPlayer()) {
					if (CmdsAPI.sender(sender).isHas(REMOVE.getPermission())) {
						if (CmdsAPI.sender(sender).isMaxLength(a, 2)) {
							CmdsAPI.sender(sender).sendPath(Settings.USAGE_COMMAND_REMOVE.getPath());
						} else {
							if (CmdsAPI.sender(sender).isLength(a, 2)) {
								String type = a[1];
								if (AdvanceGenerator.getInstance().getConfig().get("List." + type) != null) {
									try {
										AdvanceGenerator.fc.set("List." + type, null);
										AdvanceGenerator.fc.save(AdvanceGenerator.f);
									} catch (IOException e) {
										e.printStackTrace();
									} finally {
										CmdsAPI.sender(sender).sendPath(Settings.REMOVE_SUCCESS.getPath());
									}
								} else {
									CmdsAPI.sender(sender).sendPath(Settings.NAME_NOT_FOUND.getPath());
								}
							}
						}
					} else {
						CmdsAPI.sender(sender).sendPath(Settings.NO_PERMISSION.getPath());
					}
				} else {
					if (CmdsAPI.sender(sender).isMaxLength(a, 2)) {
						CmdsAPI.sender(sender).sendPath(Settings.USAGE_COMMAND_REMOVE.getPath());
					} else {
						if (CmdsAPI.sender(sender).isLength(a, 2)) {
							String type = a[1];
							if (AdvanceGenerator.fc.get("List." + type) != null) {
								try {
									AdvanceGenerator.fc.set("List." + type, null);
									AdvanceGenerator.fc.save(AdvanceGenerator.f);
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									CmdsAPI.sender(sender).sendPath(Settings.REMOVE_SUCCESS.getPath());
								}
							} else {
								CmdsAPI.sender(sender).sendPath(Settings.NAME_NOT_FOUND.getPath());
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}