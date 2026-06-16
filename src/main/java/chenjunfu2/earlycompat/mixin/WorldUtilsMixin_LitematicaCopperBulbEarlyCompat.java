package chenjunfu2.earlycompat.mixin;

import com.chenjunfu2.block.OxidizableBulbBlock;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.encodeExtraProtocolRawValue;

@Mixin(WorldUtils.class)
public class WorldUtilsMixin_LitematicaCopperBulbEarlyCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/util/WorldUtils;applyCarpetProtocolHitVec(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
		cancellable = true,
		at = @At(value = "HEAD")
	)
	private static void addExtraProtocol(BlockPos pos, BlockState state, Vec3d hitVecIn, CallbackInfoReturnable<Vec3d> cir)
	{
		if(state.getBlock() instanceof NoteBlock)
		{
			int lit = state.get(OxidizableBulbBlock.LIT) ? 0b0001 : 0b0000;//bit0
			int powered = state.get(OxidizableBulbBlock.POWERED) ? 0b0010 : 0b0000;//bit1
			cir.setReturnValue(encodeExtraProtocolRawValue(lit | powered, hitVecIn));
			cir.cancel();
		}
	}
}
