package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.config.EarlyCompatConfigs;
import chenjunfu2.earlycompat.config.LitematicaEarlyCompatConfigs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EarlyCompatConfigs.class)
@Environment(EnvType.CLIENT)
public class EarlyCompatConfigsMixin_LitematicaProtocolCompat
{
	@Overwrite(remap = false)
	public static boolean EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE()
	{
		return LitematicaEarlyCompatConfigs.EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE.getBooleanValue();
	}
}
