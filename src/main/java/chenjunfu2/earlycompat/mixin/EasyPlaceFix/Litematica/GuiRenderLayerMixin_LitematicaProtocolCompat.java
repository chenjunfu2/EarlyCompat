package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.accessor.ConfigGuiTabAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.gui.GuiConfigs;
import fi.dy.masa.litematica.gui.GuiRenderLayer;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderLayer.class)
@Environment(EnvType.CLIENT)
public abstract class GuiRenderLayerMixin_LitematicaProtocolCompat extends GuiRenderLayerEditBase
{
	@Invoker(value = "createTabButton", remap = false)
	public abstract int earlycompat_shadow$createTabButton(int x, int y, int width, GuiConfigs.ConfigGuiTab tab);
	
	@WrapOperation
	(
		method = "Lfi/dy/masa/litematica/gui/GuiRenderLayer;initGui()V",
		at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/gui/GuiRenderLayer;createTabButton(IIILfi/dy/masa/litematica/gui/GuiConfigs$ConfigGuiTab;)I", ordinal = 5),
		remap = false
	)
	public int initGui(GuiRenderLayer instance, int x, int y, int width, GuiConfigs.ConfigGuiTab tab, Operation<Integer> original, @Local(name = "x") int guiX, @Local(name = "y") int guiY)
	{
		int ret = original.call(instance,x,y,width,tab);
		guiX += ret;
		
		return earlycompat_shadow$createTabButton(guiX, guiY, -1, ConfigGuiTabAccessor.EARLY_COMPAT_TAB_KEY);
	}
	
}
