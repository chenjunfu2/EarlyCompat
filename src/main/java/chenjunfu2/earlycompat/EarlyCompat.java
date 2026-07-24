package chenjunfu2.earlycompat;

import chenjunfu2.earlycompat.network.EarlyCompatNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarlyCompat implements ModInitializer
{
	public static final String MOD_ID = "earlycompat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean IS_SERVER_ENV = false;
	public static String VERSION = "NaN";
	
	@Override
	public void onInitialize()
	{
		var loaderInstance = FabricLoader.getInstance();
		
		//设置信息
		IS_SERVER_ENV = loaderInstance.getEnvironmentType() == EnvType.SERVER;
		VERSION = loaderInstance.getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
		
		//注册
		EarlyCompatNetwork.registerPackets();
		EarlyCompatNetwork.registerServerEvents();
		if(!IS_SERVER_ENV)
		{
			EarlyCompatNetwork.registerClientEvents();
		}
	}
}