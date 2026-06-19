package chenjunfu2.earlycompat.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import static chenjunfu2.earlycompat.EarlyCompat.isExtraProtocolClientEnabled;

public class EarlyCompatClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
		{
			var rootCommand = CommandManager.literal("cearlycompat")
			.then(CommandManager.literal("set")
			.then(CommandManager.literal("ExtraProtocolClientEnabled")
			.then(
				CommandManager.argument("ExtraProtocolClientEnabled", BoolArgumentType.bool())
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
			)));
			
			dispatcher.register(rootCommand);
		});
		
	}
}