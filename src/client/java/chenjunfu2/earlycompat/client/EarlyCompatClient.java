package chenjunfu2.earlycompat.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class EarlyCompatClient implements ClientModInitializer
{
	public static boolean isExtraProtocolClientEnabled = true;
	
	@Override
	public void onInitializeClient()
	{
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
		{
			var rootCommand = CommandManager.literal("earlycompat")
			.requires(source -> source.hasPermissionLevel(2))
			.then(CommandManager.literal("set")
			.then(
				CommandManager.argument("ExtraProtocolServerEnabled", BoolArgumentType.bool())
				.executes
				(context ->
					{
						isExtraProtocolClientEnabled = BoolArgumentType.getBool(context, "ExtraProtocolClientEnabled");
						context.getSource().
							sendFeedback(
								() -> Text.literal("set ExtraProtocolServerEnabled " + (isExtraProtocolClientEnabled ? "true" : "false")),
								true
							);
						return 1;
					}
				)
			));
			
			dispatcher.register(rootCommand);
		});
		
	}
}