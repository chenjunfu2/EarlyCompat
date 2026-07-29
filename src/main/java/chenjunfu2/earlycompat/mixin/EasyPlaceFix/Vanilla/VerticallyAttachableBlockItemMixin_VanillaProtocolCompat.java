package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.accessor.CarpetExtraSettingsAccessor;
import chenjunfu2.earlycompat.accessor.VerticallyAttachableBlockItemAccessor;
import chenjunfu2.earlycompat.network.EarlyCompatC2ServerHandler;
import chenjunfu2.earlycompat.util.BlockPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.EarlyCompat.IS_SERVER_ENV;

@Mixin(VerticallyAttachableBlockItem.class)
public abstract class VerticallyAttachableBlockItemMixin_VanillaProtocolCompat extends BlockItem implements VerticallyAttachableBlockItemAccessor
{
	@Final
	@Shadow
	protected Block wallBlock;
	@Final
	@Shadow
	private Direction verticalAttachmentDirection;
	
	public VerticallyAttachableBlockItemMixin_VanillaProtocolCompat(Block block, Settings settings)
	{
		super(block, settings);
	}
	
	@Override
	public Block esrlycompat$getWallBlock()
	{
		return wallBlock;
	}
	
	@Inject
	(
		method = "Lnet/minecraft/item/VerticallyAttachableBlockItem;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
		cancellable = true,
		at = @At(value = "HEAD")
	)
	void getAlternatePlacement(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir)
	{
		if(IS_SERVER_ENV)//不是客户端
		{
			if (!(context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity) ||
				!EarlyCompatC2ServerHandler.isExtraProtocolPlayer(serverPlayerEntity) ||
				!CarpetExtraSettingsAccessor.getAccurateBlockPlacement())//玩家没有扩展协议或carpet规则被关闭
			{
				return;//啥都不做
			}
		}
		
		BlockState tryAlternative = BlockPlacer.alternativeBlockPlacement(this.getBlock(), wallBlock, verticalAttachmentDirection, context);
		if(tryAlternative == null)
		{
			return;//啥都不做
		}
		
		if(!this.canPlace(context, tryAlternative))
		{
			tryAlternative = null;//禁止放置
		}
		
		cir.setReturnValue(tryAlternative);
		cir.cancel();
	}
}
