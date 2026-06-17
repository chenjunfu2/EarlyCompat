package chenjunfu2.earlycompat;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarlyCompat implements ModInitializer
{
	public static final String MOD_ID = "earlycompat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static boolean isExtraProtocolServerEnabled = true;
	
	@Override
	public void onInitialize()
	{
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
		{
			var rootCommand = CommandManager.literal("earlycompat")
				.requires(source -> source.hasPermissionLevel(2));
			
			rootCommand.then(CommandManager.literal("set"))
			.then(
				CommandManager.argument("ExtraProtocolServerEnabled", BoolArgumentType.bool())
				.executes
				(context ->
					{
						isExtraProtocolServerEnabled = BoolArgumentType.getBool(context, "ExtraProtocolServerEnabled");
						context.getSource().
							sendFeedback(
								() -> Text.literal("set ExtraProtocolServerEnabled " + (isExtraProtocolServerEnabled ? "true" : "false")),
								true
							);
						return 1;
					}
				)
			);
		});
	}
}