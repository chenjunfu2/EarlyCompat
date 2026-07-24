package chenjunfu2.earlycompat.network;

import chenjunfu2.earlycompat.EarlyCompat;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class EarlyCompatS2CHandler
{
	private static NbtCompound serverExtraProtocolInfo = null;
	
    public static void handle(EarlyCompatPacket packet, ClientPlayerEntity player)
	{
        switch (packet.getPacketId())
		{
            case EarlyCompatNetwork.S2C.HI:
			{
				serverExtraProtocolInfo = packet.getNbt();
                break;
			}
        }
    }

    // 在登录/重生事件中调用
    public static void sendHi(ClientPlayNetworkHandler handler)
	{
        serverExtraProtocolInfo = null;  // reset
        handler.sendPacket(EarlyCompatNetwork.createC2S(
            EarlyCompatNetwork.C2S.HI,
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
