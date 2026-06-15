package chenjunfu2.earlycompat;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarlyCompat implements ModInitializer
{
	public static final String MOD_ID = "earlycompat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static boolean isPluslsCarpetAdditionAvailable = false;
	public static boolean isMasaGadgetAvailable = false;
	
	public static boolean isDecoratedPotEarlyAvailable = false;
	public static boolean isCrafterEarlyAvailable = false;
	
	@Override
	public void onInitialize()
	{}
}