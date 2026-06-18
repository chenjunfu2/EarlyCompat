package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.accessor.CarpetExtraSettingsAccessor;
import chenjunfu2.earlycompat.util.BlockPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.EarlyCompat.isExtraProtocolServerEnabled;

@Mixin(VerticallyAttachableBlockItem.class)
public class VerticallyAttachableBlockItemMixin_VanillaProtocolCompat extends BlockItem
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
	
	@Inject
	(
		method = "Lnet/minecraft/item/VerticallyAttachableBlockItem;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
		cancellable = true,
		at = @At(value = "HEAD")
	)
	void getAlternatePlacement(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir)
	{
		if(!isExtraProtocolServerEnabled || ! CarpetExtraSettingsAccessor.getAccurateBlockPlacement())
		{
			return;//啥都不做
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
