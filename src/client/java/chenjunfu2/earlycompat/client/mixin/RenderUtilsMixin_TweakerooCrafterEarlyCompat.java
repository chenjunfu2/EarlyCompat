package chenjunfu2.earlycompat.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.tweakeroo.renderer.RenderUtils;
import net.chenjunfu2.block.entity.CrafterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderUtils.class)
@Environment(EnvType.CLIENT)
public abstract class RenderUtilsMixin_TweakerooCrafterEarlyCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/tweakeroo/renderer/RenderUtils;renderInventoryOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "INVOKE",
			target = "Lfi/dy/masa/malilib/render/InventoryOverlay;renderInventoryBackground(Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;IIIILnet/minecraft/client/MinecraftClient;)V",
			ordinal = 1,
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	private static void renderInventoryOverlayCrafterEarlyCompat(MinecraftClient mc, DrawContext drawContext, CallbackInfo ci, @Local(name = "world") World world, @Local(name = "trace") HitResult trace)
	{
		if(trace.getType() != HitResult.Type.BLOCK)
		{
			return;//啥都不做
		}
		
		BlockPos lookPos = ((BlockHitResult)trace).getBlockPos();
		BlockEntity lookBlockEntity = world.getWorldChunk(lookPos).getBlockEntity(lookPos);
		if(lookBlockEntity == null)
		{
			return;//啥都不做
		}
		
		//绘制合成器界面，并跳过剩下的绘制
		if(lookBlockEntity instanceof CrafterBlockEntity)
		{
			
		
		
		
		
		
			ci.cancel();
		}
	}

	
}
