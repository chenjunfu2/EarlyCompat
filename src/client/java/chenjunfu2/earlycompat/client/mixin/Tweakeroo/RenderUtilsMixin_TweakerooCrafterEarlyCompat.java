package chenjunfu2.earlycompat.client.mixin.Tweakeroo;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.InventoryOverlay;
import fi.dy.masa.tweakeroo.renderer.RenderUtils;
import net.chenjunfu2.block.entity.CrafterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderUtils.class)
@Environment(EnvType.CLIENT)
public abstract class RenderUtilsMixin_TweakerooCrafterEarlyCompat
{
	@Unique
	private static final Identifier earlycompat$CRAFTER_DISABLED_SLOT_TEXTURE = new Identifier("textures/gui/container/crafter/disabled_slot.png");
	
	@Unique
	private static BlockEntity earlycompat$LookBlockEntity = null;
	
	@ModifyVariable
	(
		method = "Lfi/dy/masa/tweakeroo/renderer/RenderUtils;renderInventoryOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "STORE"
		),
		name = "type"
	)
	private static InventoryOverlay.InventoryRenderType renderInventoryOverlayChiseledBookshelfCompat(InventoryOverlay.InventoryRenderType type, @Local(name = "world") World world, @Local(name = "trace")HitResult trace)
	{
		if(trace.getType() != HitResult.Type.BLOCK)
		{
			earlycompat$LookBlockEntity = null;//重置
			return type;
		}
		
		BlockPos lookPos = ((BlockHitResult)trace).getBlockPos();
		earlycompat$LookBlockEntity = world.getWorldChunk(lookPos).getBlockEntity(lookPos);
		
		if(earlycompat$LookBlockEntity instanceof CrafterBlockEntity)
		{
			return InventoryOverlay.InventoryRenderType.DISPENSER;//修改为投掷器样式
		}
		else
		{
			return type;
		}
	}
	
	@Inject
	(
		method = "Lfi/dy/masa/tweakeroo/renderer/RenderUtils;renderInventoryOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "INVOKE",
			target = "Lfi/dy/masa/malilib/render/InventoryOverlay;renderInventoryBackground(Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;IIIILnet/minecraft/client/MinecraftClient;)V",
			ordinal = 1,
			shift = At.Shift.AFTER
		)
	)
	private static void renderInventoryOverlayCrafterEarlyCompat//在绘制背景后面，开始绘制遮罩，然后才是物品
	(
		MinecraftClient mc,
		DrawContext drawContext,
		CallbackInfo ci,
		@Local(name = "xInv") int xInv,
		@Local(name = "yInv") int yInv
	)
	{
		//绘制合成器锁定槽位
		if(earlycompat$LookBlockEntity instanceof CrafterBlockEntity crafter)
		{
			//绘制禁用槽位
			for (int i = 0; i < crafter.size(); i++)
			{
				int row = i / 3, col = i % 3;
				int sx = xInv + 7 + col * 18;
				int sy = yInv + 7 + row * 18;
				if (crafter.isSlotDisabled(i))
				{
					drawContext.drawTexture(earlycompat$CRAFTER_DISABLED_SLOT_TEXTURE, sx, sy, 0, 0, 18, 18);
				}
			}
		}
	}

	
}
