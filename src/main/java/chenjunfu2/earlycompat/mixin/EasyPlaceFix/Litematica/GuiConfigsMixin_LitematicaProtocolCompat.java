package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.accessor.ConfigGuiTabAccessor;
import chenjunfu2.earlycompat.config.LitematicaEarlyCompatConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiConfigs;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiConfigs.class)
@Environment(EnvType.CLIENT)
public abstract class GuiConfigsMixin_LitematicaProtocolCompat extends GuiConfigsBase
{
	public GuiConfigsMixin_LitematicaProtocolCompat(int listX, int listY, String modId, Screen parent, String titleKey, Object... args)
	{
		super(listX, listY, modId, parent, titleKey, args);
	}
	
	@Invoker(value = "createButton", remap = false)
    public abstract int earlycompat_shadow$createButton(int x, int y, int width, GuiConfigs.ConfigGuiTab tab);
	
	@WrapOperation
	(
		method = "Lfi/dy/masa/litematica/gui/GuiConfigs;initGui()V",
		at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/gui/GuiConfigs;createButton(IIILfi/dy/masa/litematica/gui/GuiConfigs$ConfigGuiTab;)I", ordinal = 5),
		remap = false
	)
	public int initMyGui(GuiConfigs instance, int x, int y, int width, GuiConfigs.ConfigGuiTab tab, Operation<Integer> original, @Local(name = "x") int guiX, @Local(name = "y") int guiY)
	{
		int ret = original.call(instance,x,y,width,tab);
		guiX += ret;
		
		return earlycompat_shadow$createButton(guiX, guiY, -1, ConfigGuiTabAccessor.EARLY_COMPAT_TAB_KEY);
	}
	
	@Inject
	(
		method = "Lfi/dy/masa/litematica/gui/GuiConfigs;getConfigs()Ljava/util/List;",
		at = @At("HEAD"),
		cancellable = true,
		remap = false
	)
    private void getConfigs(CallbackInfoReturnable<List<ConfigOptionWrapper>> cir)
	{
        if (ConfigGuiTabAccessor.EARLY_COMPAT_TAB_KEY == DataManager.getConfigGuiTab())
		{
            cir.setReturnValue(ConfigOptionWrapper.createFor(LitematicaEarlyCompatConfigs.OPTIONS));
			cir.cancel();
        }
    }
}
