package chenjunfu2.earlycompat.mixin.Litematica;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.encodeExtraProtocolRawValue;

@Mixin(WorldUtils.class)
public abstract class WorldUtilsMixin_LitematicaProtocolCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/util/WorldUtils;applyCarpetProtocolHitVec(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
		cancellable = true,
		at = @At(value = "HEAD")
	)
	private static void addExtraProtocol(BlockPos pos, BlockState state, Vec3d hitVecIn, CallbackInfoReturnable<Vec3d> cir)
	{
		if(!(state.getBlock() instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		int protocolValue = blockProtocolStateAdapter.earlycompat$toProtocolValue(state);
		cir.setReturnValue(encodeExtraProtocolRawValue(protocolValue, hitVecIn));
		cir.cancel();
	}
}
