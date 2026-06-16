package chenjunfu2.earlycompat.mixin;

import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.encodeExtraProtocolRawValue;

@Mixin(WorldUtils.class)
public abstract class WorldUtilsMixin_LitematicaVanillaCompat
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
			int note = state.get(NoteBlock.NOTE);
			cir.setReturnValue(encodeExtraProtocolRawValue(note, hitVecIn));
			cir.cancel();
		}
	}
}
