package net.pistonmaster.pistonchat.utils;

import com.github.puregero.multilib.MultiLib;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SoftIgnoreTool {
    private final Gson gson = new Gson();

    public SoftReturn softIgnorePlayer(Player player, Player ignored) {
        List<String> list = getStoredList(player);

        boolean contains = list.contains(ignored.getUniqueId().toString());
        if (contains) {
            list.remove(ignored.getUniqueId().toString());
        } else {
            list.add(ignored.getUniqueId().toString());
        }

        MultiLib.setData(player, "pistonchat_softignore", gson.toJson(list));

        return contains ? SoftReturn.UN_IGNORE : SoftReturn.IGNORE;
    }

    protected boolean isSoftIgnored(CommandSender chatter, Player receiver) {
        UUID chatterUUID = new UniqueSender(chatter).getUniqueId();
        return getStoredList(receiver).contains(chatterUUID.toString());
    }

    protected List<OfflinePlayer> getSoftIgnoredPlayers(Player player) {
        List<String> listUUID = getStoredList(player);
        List<OfflinePlayer> returnedPlayers = new ArrayList<>();

        for (String str : listUUID) {
            returnedPlayers.add(Bukkit.getOfflinePlayer(UUID.fromString(str)));
        }

        return returnedPlayers;
    }

    protected List<String> getStoredList(Player player) {
        String listData = MultiLib.getData(player, "pistonchat_softignore");
        return listData == null ? new ArrayList<>() : gson.<List<String>>fromJson(listData, List.class);
    }

    public enum SoftReturn {
        IGNORE, UN_IGNORE
    }
}
