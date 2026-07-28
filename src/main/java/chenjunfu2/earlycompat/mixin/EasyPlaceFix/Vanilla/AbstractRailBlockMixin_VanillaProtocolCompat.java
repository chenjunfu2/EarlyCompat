package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.accessor.PlaceStateAccessor;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin_VanillaProtocolCompat
{
	@Inject
	(
		method = "Lnet/minecraft/block/AbstractRailBlock;updateBlockState(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;",
		at = @At("HEAD"),
		cancellable = true
	)
    private void cancelUpdate(World world, BlockPos pos, BlockState state, boolean forceUpdate, CallbackInfoReturnable<BlockState> cir)
	{
    	//不执行更新，当前必须当前是轻松放置的状态
		if(!(state.getBlock().asItem() instanceof PlaceStateAccessor blockItemPlaceStateAccessor))
		{
			return;//首先必须要是PlaceStateAccessor
		}
		
		//然后获取轻松放置状态
		if(!blockItemPlaceStateAccessor.earlycompat$isEasyPlaceState())
		{
			return;//当前不是轻松放置，跳过
		}
		
		//直接返回原始state，啥都不做，跳过更新
		cir.setReturnValue(state);
		cir.cancel();
    }
}
