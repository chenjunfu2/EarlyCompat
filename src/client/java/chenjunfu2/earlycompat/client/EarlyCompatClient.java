package chenjunfu2.earlycompat.client;

import chenjunfu2.earlycompat.client.network.EarlyCompatS2ClientHandler;
import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents;
import net.fabricmc.api.ClientModInitializer;

public class EarlyCompatClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		FanetlibClientEvents.registerGameJoinListener(
        	(client, networkHandler) -> EarlyCompatS2ClientHandler.sendHi(networkHandler)
        );
        FanetlibClientEvents.registerPlayerRespawnListener(
        	(client, networkHandler) -> EarlyCompatS2ClientHandler.sendHi(networkHandler)
        );
	}
}