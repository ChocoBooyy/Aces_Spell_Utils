package net.acetheeldritchking.aces_spell_utils.entity.mobs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IKeepInventoryEntity {
    public default double keepInventoryDetectionRange()
    {
        return 64;
    }

    public List<UUID> playerIDs = new ArrayList<>();
    int range = 128;

    public default void setParticipantsFromServerPlayers(List<? extends ServerPlayer> players)
    {
        playerIDs.clear();
        for (ServerPlayer player : players)
        {
            playerIDs.add(player.getUUID());
        }
    }

    public default ServerPlayer getParticipantsFromServer(ServerLevel serverLevel)
    {
        int playerIDList = playerIDs.size();
        ServerPlayer contextPlayer = null;
        for (int i = 0; i < playerIDList; i++)
        {
            UUID serverPlayerID;
            if (!playerIDs.isEmpty())
            {
                serverPlayerID = playerIDs.get(i);
                Player playerLookup = serverLevel.getPlayerByUUID(serverPlayerID);
                if (playerLookup instanceof ServerPlayer serverPlayer)
                {
                    contextPlayer = serverPlayer;
                }
            }
        }

        return contextPlayer;
    }
}
