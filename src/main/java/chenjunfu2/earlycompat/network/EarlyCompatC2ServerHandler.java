package chenjunfu2.earlycompat.network;

import chenjunfu2.earlycompat.EarlyCompat;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class EarlyCompatC2ServerHandler
{
	private static final Map<ServerPlayerEntity, NbtCompound> extraProtocolPlayers = new HashMap<>();

    public static void handle(EarlyCompatPacket packet, ServerPlayerEntity player)
	{
		switch (packet.getPacketId())
		{
			case EarlyCompatNetwork.C2S.HI://接受到C2S
			{
				extraProtocolPlayers.put(player, packet.getNbt());
				 
				 // 回复握手
				player.networkHandler.sendPacket(EarlyCompatNetwork.createS2C(
            	    EarlyCompatNetwork.S2C.HI_ACK,//发送S2C
            	    nbt -> nbt.putString("mod_version", EarlyCompat.VERSION)
            	));
				break;
			}
		}
    }

    public static boolean isExtraProtocolPlayer(ServerPlayerEntity player)
	{
        return extraProtocolPlayers.containsKey(player);
    }

	//在玩家离开服务器后调用
    public static void onPlayerLeave(ServerPlayerEntity player)
	{
        extraProtocolPlayers.remove(player);
    }
}
