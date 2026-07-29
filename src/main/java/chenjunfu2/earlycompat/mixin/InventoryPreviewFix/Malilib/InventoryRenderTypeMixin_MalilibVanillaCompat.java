package chenjunfu2.earlycompat.mixin.InventoryPreviewFix.Malilib;

import fi.dy.masa.malilib.render.InventoryOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static chenjunfu2.earlycompat.accessor.InventoryRenderTypeAccessor.CHISELEDBOOKSHELF;
import static chenjunfu2.earlycompat.accessor.InventoryRenderTypeAccessor.nameCHISELEDBOOKSHELF;

@Mixin(InventoryOverlay.InventoryRenderType.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryRenderTypeMixin_MalilibVanillaCompat
{
	@Shadow(remap = false)
	@Final
	@Mutable
	private static InventoryOverlay.InventoryRenderType[] $VALUES;
	
	@Invoker("<init>")
	private static InventoryOverlay.InventoryRenderType earlycompat_shadow$createVariant(String name, int ordinal)
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
		CHISELEDBOOKSHELF = earlycompat_shadow$createVariant(nameCHISELEDBOOKSHELF, $VALUES.length);//创建新枚举常量，序号用当前数组长度（新序号正好等于原数量）
		InventoryOverlay.InventoryRenderType[] newValues = Arrays.copyOf($VALUES, $VALUES.length + 1);
		newValues[newValues.length - 1] = CHISELEDBOOKSHELF;
		$VALUES = newValues;
	}
}
