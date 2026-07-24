package chenjunfu2.earlycompat;

import chenjunfu2.earlycompat.network.EarlyCompatC2ServerHandler;
import chenjunfu2.earlycompat.network.EarlyCompatNetwork;
import chenjunfu2.earlycompat.network.EarlyCompatPacket;
import chenjunfu2.earlycompat.network.EarlyCompatS2ClientHandler;
import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents;
import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;
import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarlyCompat implements ModInitializer
{
	public static final String MOD_ID = "earlycompat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
    public static PacketHandlerS2C<EarlyCompatPacket> packetS2ClientHandler = null;
	
	public static boolean IS_SERVER_ENV = false;
	public static boolean IS_CLIENT_ENV = false;
	public static String VERSION = "NaN";
	
	@Override
	public void onInitialize()
	{
		var loaderInstance = FabricLoader.getInstance();
		
		//设置信息
		IS_SERVER_ENV = loaderInstance.getEnvironmentType() == EnvType.SERVER;
		IS_CLIENT_ENV = loaderInstance.getEnvironmentType() == EnvType.CLIENT;
		
		VERSION = loaderInstance.getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
		
		//注册
		FanetlibPackets.registerC2S(//server
			EarlyCompatNetwork.PACKET_TYPE,
			PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
			(packet, ctx) -> EarlyCompatC2ServerHandler.handle(packet, ctx.getPlayer())
		);
		
		FanetlibPackets.registerS2C(//client
			EarlyCompatNetwork.PACKET_TYPE,
	    	PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
	    	(packet, ctx) ->
			{
				if(packetS2ClientHandler != null)
				{
					packetS2ClientHandler.handle(packet, ctx);
				}
			}
	    );
		
		EarlyCompat.packetS2ClientHandler = (packet, ctx) -> EarlyCompatS2ClientHandler.handle(packet, ctx.getPlayer());
		
		if (IS_CLIENT_ENV)
		{
			FanetlibClientEvents.registerGameJoinListener(
        		(client, networkHandler) -> EarlyCompatS2ClientHandler.sendHi(networkHandler)
        	);
        	FanetlibClientEvents.registerPlayerRespawnListener(
        		(client, networkHandler) -> EarlyCompatS2ClientHandler.sendHi(networkHandler)
        	);
		}
		
		
	}
}