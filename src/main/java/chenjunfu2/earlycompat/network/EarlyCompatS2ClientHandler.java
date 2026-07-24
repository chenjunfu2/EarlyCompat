package chenjunfu2.earlycompat.network;

import chenjunfu2.earlycompat.EarlyCompat;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class EarlyCompatS2ClientHandler
{
	private static NbtCompound serverExtraProtocolInfo = null;
	
    public static void handle(EarlyCompatPacket packet, PacketHandlerS2C.Context ctx)
	{
		ClientPlayerEntity player = ctx.getPlayer();
		
        switch (packet.getPacketId())
		{
            case EarlyCompatNetwork.S2C.HI_ACK://接收到S2C HI_ACK
			{
				serverExtraProtocolInfo = packet.getNbt();
                break;
			}
        }
    }
	
	public static void onGameJoin(MinecraftClient client, ClientPlayNetworkHandler networkHandler)
	{
		sendHi(networkHandler);
	}
	
	public static void onPlayerRespawn(MinecraftClient client, ClientPlayNetworkHandler networkHandler)
	{
		sendHi(networkHandler);
	}
	
	public static void onDisconnect(MinecraftClient client)
	{
		serverExtraProtocolInfo = null;
	}

    private static void sendHi(ClientPlayNetworkHandler networkHandler)
	{
        // reset
        serverExtraProtocolInfo = null;
		networkHandler.sendPacket(EarlyCompatNetwork.createC2S(
            EarlyCompatNetwork.C2S.HI,//发送C2S
            nbt -> nbt.putString("mod_version", EarlyCompat.VERSION)
        ));
    }

    public static boolean isServerSupportsExtraProtocol()
	{
        return serverExtraProtocolInfo != null;
    }
	
	public static NbtCompound getServerExtraProtocolInfo()
	{
        return serverExtraProtocolInfo;
    }
}
