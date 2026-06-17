package chenjunfu2.earlycompat.client.mixin.Tweakeroo;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.InventoryOverlay;
import fi.dy.masa.tweakeroo.renderer.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderUtils.class)
@Environment(EnvType.CLIENT)
public abstract class RenderUtilsMixin_TweakerooVanillaCompat
{
	@ModifyVariable
	(
		method = "Lfi/dy/masa/tweakeroo/renderer/RenderUtils;renderInventoryOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/DrawContext;)V",
		at = @At
			(
				value = "INVOKE_ASSIGN",
				target = "Lfi/dy/masa/malilib/render/InventoryOverlay;getInventoryPropsTemp(Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;I)Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryProperties;"
			),
		name = "props"
	)
	private static InventoryOverlay.InventoryProperties renderInventoryOverlayChiseledBookshelfCompat(InventoryOverlay.InventoryProperties props, @Local(name = "world") World world, @Local(name = "trace")HitResult trace)
	{
		if(trace.getType() != HitResult.Type.BLOCK)
		{
			return props;
		}
		
		BlockPos lookPos = ((BlockHitResult)trace).getBlockPos();
		BlockEntity lookBlockEntity = world.getWorldChunk(lookPos).getBlockEntity(lookPos);
		
		if(lookBlockEntity instanceof ChiseledBookshelfBlockEntity)
		{
			props.slotsPerRow = 3;// 修改为一行 = 3 个
			props.width = 3 * 18 + 14;//重新设置宽度 = 68 像素
			props.height = 2 * 18 + 14;//重新设置高度 = 50 像素
		}
		
		return props;
	}
}
