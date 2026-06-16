package chenjunfu2.earlycompat.mixin;

import carpetextra.utils.BlockPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPlacer.class)
public abstract class BlockPlacerMixin_CarpetExtraVanillaCompat
{
	@Inject
	(
		method = "alternativeBlockPlacement",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
			ordinal = 0
		)
	)
	private static void tryNewProtocol(Block block, ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir)
	{
	
	}

	
}
