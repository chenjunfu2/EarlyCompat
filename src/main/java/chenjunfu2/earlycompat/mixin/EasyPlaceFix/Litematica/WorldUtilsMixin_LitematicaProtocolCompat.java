package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.addExtraProtocolBit;
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
	private static void replaceExtraProtocol(BlockPos pos, BlockState state, Vec3d hitVecIn, CallbackInfoReturnable<Vec3d> cir)
	{
		if(!(state.getBlock() instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.REPLACE)
		{
			return;//如果不是替换模式，那么什么也不做
		}
		
		int protocolValue = blockProtocolStateAdapter.earlycompat$toProtocolValue(0, state);
		cir.setReturnValue(encodeExtraProtocolRawValue(protocolValue, hitVecIn));
		cir.cancel();
	}
	
	
	@ModifyVariable
	(
		method = "Lfi/dy/masa/litematica/util/WorldUtils;applyCarpetProtocolHitVec(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
		at = @At
		(
			value = "INVOKE_ASSIGN",
			target = "Lfi/dy/masa/litematica/util/WorldUtils;applySlabOrStairHitVecY(DLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)D"
		),
		name = "protocolValue"
	)
	private static int addExtraProtocol(int protocolValue, @Local(name = "state") BlockState state)
	{
		if(!(state.getBlock() instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return protocolValue;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.ADDED)
		{
			return protocolValue;//如果不是添加模式，那么什么也不做
		}
		
		//添加新值
		int newProtocolValue = addExtraProtocolBit(blockProtocolStateAdapter.earlycompat$toProtocolValue(protocolValue, state));
		return newProtocolValue;
	}
}
