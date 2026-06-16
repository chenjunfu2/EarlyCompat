package chenjunfu2.earlycompat.mixin;

import fi.dy.masa.litematica.util.WorldUtils;
import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.encodeExtraProtocolRawValue;

@Mixin(WorldUtils.class)
public class WorldUtilsMixin_LitematicaCrafterEarlyCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/util/WorldUtils;applyCarpetProtocolHitVec(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
		cancellable = true,
		at = @At(value = "HEAD")
	)
	private static void addExtraProtocol(BlockPos pos, BlockState state, Vec3d hitVecIn, CallbackInfoReturnable<Vec3d> cir)
	{
		if(state.getBlock() instanceof CrafterBlock)
		{
			JigsawOrientation orientation = state.get(Properties.ORIENTATION);
			int orientationOrdinal = orientation.ordinal();
			cir.setReturnValue(encodeExtraProtocolRawValue(orientationOrdinal, hitVecIn));
			cir.cancel();
		}
	}
}
