package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.config.LitematicaEarlyCompatConfigs;
import fi.dy.masa.litematica.config.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Configs.class)
@Environment(EnvType.CLIENT)
public abstract class ConfigsMixin_LitematicaProtocolCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/config/Configs;loadFromFile()V",
		at = @At(value = "TAIL")
	)
	private static void loadEarlyCompatConfigsFromFile(CallbackInfo ci)
	{
		LitematicaEarlyCompatConfigs.loadFromFile();
	}
	
	@Inject
	(
		method = "Lfi/dy/masa/litematica/config/Configs;saveToFile()V",
		at = @At(value = "TAIL")
	)
	private static void saveEarlyCompatConfigsToFile(CallbackInfo ci)
	{
		LitematicaEarlyCompatConfigs.saveToFile();
	}
}
