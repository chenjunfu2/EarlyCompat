package chenjunfu2.earlycompat.network;

import chenjunfu2.earlycompat.EarlyCompat;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerC2S;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class EarlyCompatC2ServerHandler
{
	private static final Map<ServerPlayerEntity, NbtCompound> extraProtocolPlayers = new HashMap<>();

    public static void handle(EarlyCompatPacket packet, PacketHandlerC2S.Context ctx)
	{
		ServerPlayerEntity player = ctx.getPlayer();
		
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
	
	public static void onPlayerJoin(MinecraftServer server, ServerPlayNetworkHandler networkHandler, ServerPlayerEntity player)
	{
		//wait hi packet
	}
	
	public static void onPlayerDisconnect(MinecraftServer server, ServerPlayNetworkHandler networkHandler, ServerPlayerEntity player)
	{
		extraProtocolPlayers.remove(player);
	}
}
