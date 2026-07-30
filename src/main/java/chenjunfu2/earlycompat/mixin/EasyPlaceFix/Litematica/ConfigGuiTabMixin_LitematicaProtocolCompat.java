package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import fi.dy.masa.litematica.gui.GuiConfigs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static chenjunfu2.earlycompat.accessor.ConfigGuiTabAccessor.nameEARLY_COMPAT_TAB_KEY;
import static chenjunfu2.earlycompat.accessor.ConfigGuiTabAccessor.EARLY_COMPAT_TAB_KEY;

@Mixin(GuiConfigs.ConfigGuiTab.class)
@Environment(EnvType.CLIENT)
public abstract class ConfigGuiTabMixin_LitematicaProtocolCompat
{
	@Shadow(remap = false)
	@Final
	@Mutable
	private static GuiConfigs.ConfigGuiTab[] $VALUES;
	
	@Invoker("<init>")
	private static GuiConfigs.ConfigGuiTab earlycompat_shadow$createGuiTab(String name, int ordinal, String translationKey)
	{
		throw new AssertionError("This will be replaced by Mixin");
	}

	@Inject
	(
		method = "<clinit>",
		at = @At(value = "RETURN")
	)
	private static void onStaticInit(CallbackInfo ci)
	{
		EARLY_COMPAT_TAB_KEY = earlycompat_shadow$createGuiTab(nameEARLY_COMPAT_TAB_KEY, $VALUES.length, "EarlyCompat");//创建新枚举常量，序号用当前数组长度（新序号正好等于原数量）
		GuiConfigs.ConfigGuiTab[] newValues = Arrays.copyOf($VALUES, $VALUES.length + 1);
		newValues[newValues.length - 1] = EARLY_COMPAT_TAB_KEY;
		$VALUES = newValues;
	}
}
