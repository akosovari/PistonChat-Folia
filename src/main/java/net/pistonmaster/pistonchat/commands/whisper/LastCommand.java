package net.pistonmaster.pistonchat.commands.whisper;

import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonchat.PistonChat;
import net.pistonmaster.pistonchat.tools.CommonTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LastCommand implements CommandExecutor, TabExecutor {
    private final PistonChat plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Optional<CommandSender> lastSentTo = plugin.getCacheTool().getLastSentTo(sender);
        Optional<CommandSender> lastMessagedOf = plugin.getCacheTool().getLastMessagedOf(sender);

        if (lastSentTo.isPresent()) {
            if (plugin.getIgnoreTool().isIgnored(sender, lastSentTo.get())) {
                if (plugin.getConfig().getBoolean("onlyhidepms")) {
                    plugin.getCommonTool().sendSender(sender, CommonTool.mergeArgs(args, 0), lastSentTo.get());
                } else {
                    plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "source-ignored");
                }
            } else if (!plugin.getConfig().getBoolean("allowpmignored") && plugin.getIgnoreTool().isIgnored(lastSentTo.get(), sender)) {
                plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "target-ignored");
            } else {
                if (args.length > 0) {
                    plugin.getCommonTool().sendWhisperTo(sender, CommonTool.mergeArgs(args, 0), lastSentTo.get());
                } else {
                    return false;
                }
            }
        } else if (lastMessagedOf.isPresent()) {
            if (plugin.getIgnoreTool().isIgnored(sender, lastMessagedOf.get())) {
                if (plugin.getConfig().getBoolean("onlyhidepms")) {
                    plugin.getCommonTool().sendSender(sender, CommonTool.mergeArgs(args, 0), lastMessagedOf.get());
                } else {
                    plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "source-ignored");
                }
            } else if (!plugin.getConfig().getBoolean("allowpmignored") && plugin.getIgnoreTool().isIgnored(lastMessagedOf.get(), sender)) {
                plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "target-ignored");
            } else {
                if (args.length > 0) {
                    plugin.getCommonTool().sendWhisperTo(sender, CommonTool.mergeArgs(args, 0), lastMessagedOf.get());
                } else {
                    return false;
                }
            }
        } else {
            plugin.getCommonTool().sendLanguageMessage(plugin.getAdventure(), sender, "notonline");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
