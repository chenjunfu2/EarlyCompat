package chenjunfu2.earlycompat.mixin.accessor;

import carpetextra.CarpetExtraSettings;
import chenjunfu2.earlycompat.accessor.CarpetExtraSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CarpetExtraSettingsAccessor.class)
public abstract class CarpetExtraSettingsAccessorMixin_CarpetExtraProtocolCompat
{
	@Overwrite(remap = false)
	public static boolean getAccurateBlockPlacement()
	{
		return CarpetExtraSettings.accurateBlockPlacement;
	}
}
