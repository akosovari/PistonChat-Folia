package net.pistonmaster.pistonchat.commands.ignore;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatColor;
import net.pistonmaster.pistonchat.PistonChat;
import net.pistonmaster.pistonchat.tools.CommonTool;
import net.pistonmaster.pistonchat.utils.PlatformUtils;
import net.pistonmaster.pistonchat.tools.SoftIgnoreTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SoftIgnoreCommand implements CommandExecutor, TabExecutor {
    private final PistonChat plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                Optional<Player> ignored = PlatformUtils.getPlayer(args[0]);

                if (ignored.isPresent()) {
                    SoftIgnoreTool.SoftReturn type = plugin.getSoftignoreTool().softIgnorePlayer(player, ignored.get());

                    if (type == SoftIgnoreTool.SoftReturn.IGNORE) {
                        plugin.getCommonTool().sendLanguageMessageNoPrefix(plugin.getAdventure(), player,
                                "ignore",
                                CommonTool.getStrippedNameResolver(ignored.get()));
                    } else if (type == SoftIgnoreTool.SoftReturn.UN_IGNORE) {
                        plugin.getCommonTool().sendLanguageMessageNoPrefix(plugin.getAdventure(), player,
                                "unignore",
                                CommonTool.getStrippedNameResolver(ignored.get()));
                    }
                } else {
                    plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), player, "notonline");
                }
            } else {
                return false;
            }
        } else {
            plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "playeronly");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return null;
        } else {
            return Collections.emptyList();
        }
    }
}
