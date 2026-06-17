package chenjunfu2.earlycompat.client.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.client.mixin.EasyPlaceFix.Vanilla.VerticallyAttachableBlockItemAccessor;
import chenjunfu2.earlycompat.util.BlockPlacer;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.WorldUtils;
import net.fabricmc.api.EnvType;import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.addExtraProtocolBit;
import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.encodeExtraProtocolRawValue;
import static chenjunfu2.earlycompat.client.EarlyCompatClient.isExtraProtocolClientEnabled;

@Mixin(WorldUtils.class)
@Environment(EnvType.CLIENT)
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
		if(!isExtraProtocolClientEnabled)//未开启扩展协议
		{
			return;
		}
		
		Block block = state.getBlock();
		
		//如果不是附着方块并且不是协议方块，那么跳出
		//如果是附着方块，但不是协议方块，并且是墙上方块，使用默认协议
		
		//附着方块默认行为
		int wallProtocolValue = 0;
		if(block.asItem() instanceof VerticallyAttachableBlockItem verticallyAttachableBlockItem &&
			block.equals(((VerticallyAttachableBlockItemAccessor)verticallyAttachableBlockItem).esrlycompat$getWallBlock()))
		{
			DirectionProperty dir = BlockPlacer.getFirstDirectionProperty(state);
			if(dir != null)
			{
				int facingIndex = state.get(dir).ordinal() - 2;//2 based index
				wallProtocolValue = ((facingIndex & 0b0000_0011) << 1);
			}
			
			wallProtocolValue |= 0b0000_0001;
		}
		
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			if((wallProtocolValue & 0b0000_0001) == 0b0000_0001)//最低为至少为1
			{
				cir.setReturnValue(encodeExtraProtocolRawValue(wallProtocolValue, hitVecIn));
				cir.cancel();
			}
			return;
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.REPLACE)
		{
			return;//如果不是替换模式，那么什么也不做
		}
		
		int protocolValue = blockProtocolStateAdapter.earlycompat$toProtocolValue(wallProtocolValue, state);
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
		if(!isExtraProtocolClientEnabled)//未开启扩展协议
		{
			return protocolValue;
		}
		
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
