package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.accessor.PlaceStateAccessor;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.block.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin_VanillaProtocolCompat
{
	//@Inject
	//(
	//	method = "Lnet/minecraft/world/block/NeighborUpdater;replaceWithStateForNeighborUpdate(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;II)V",
	//	at = @At("HEAD"),
	//	cancellable = true
	//)
    //private static void executeShapeUpdate(WorldAccess world, Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth, CallbackInfo ci)
	//{
	//	//Block block = neighborState.getBlock();
	//	//if(!(block instanceof BlockProtocolStateAdapter protocolStateAdapter))
	//	//{
	//	//	return;
	//	//}
	//	//
	//	////执行更新，那么啥都没发生
	//	//if(protocolStateAdapter.earlycompat$executeShapeUpdate())
	//	//{
	//	//	return;
	//	//}
	//	//
	//	////不执行更新，当前必须当前是轻松放置的状态
	//	//if(!(block.asItem() instanceof PlaceStateAccessor blockItemPlaceStateAccessor))
	//	//{
	//	//	return;//首先必须要是PlaceStateAccessor
	//	//}
	//	//
	//	////然后获取轻松放置状态
	//	//if(!blockItemPlaceStateAccessor.earlycompat$isEasyPlaceState())
	//	//{
	//	//	return;//当前不是轻松放置，跳过
	//	//}
	//	//
	//	////是，那么取消更新
	//	//ci.cancel();
    //}
}
