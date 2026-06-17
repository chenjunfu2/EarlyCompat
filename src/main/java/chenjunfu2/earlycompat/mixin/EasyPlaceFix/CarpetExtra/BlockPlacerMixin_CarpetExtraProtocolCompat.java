package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CarpetExtra;

import carpetextra.utils.BlockPlacer;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.EarlyCompat.isExtraProtocolServerEnabled;
import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockPlacer.class)
public abstract class BlockPlacerMixin_CarpetExtraProtocolCompat
{
	@Inject
	(
		method = "alternativeBlockPlacement",
		cancellable = true,
		at = @At
		(
			value = "INVOKE",
			target = "Lcarpetextra/utils/BlockPlacer;getFirstDirectionProperty(Lnet/minecraft/block/BlockState;)Lnet/minecraft/state/property/DirectionProperty;",
			ordinal = 0
		)
	)
	private static void replaceExtraProtocol
	(
		Block block,
		ItemPlacementContext context,
		CallbackInfoReturnable<BlockState> cir,
		@Local(name = "state") BlockState state,
		@Local(name = "relativeHitX") double relativeHitX
	)
	{
		if(!isExtraProtocolServerEnabled)//未开启扩展协议
		{
			return;
		}
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValue(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return;//不是扩展协议
		}
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.REPLACE)
		{
			return;//如果不是替换模式，那么什么也不做
		}
		
		int extraProtocolValue = decodeExtraProtocolRawValue(protocolValue);
		cir.setReturnValue(blockProtocolStateAdapter.earlycompat$fromProtocolValue(extraProtocolValue, state));
		cir.cancel();
	}
	
	@ModifyVariable
	(
		method = "alternativeBlockPlacement",
		at = @At
			(
				value = "INVOKE_ASSIGN",
				target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
				ordinal = 0
			),
		name = "protocolValue"
	)
	private static int replaceExtraProtocolValue
	(
		int protocolValue
	)
	{
		if(!isExtraProtocolServerEnabled)//未开启扩展协议
		{
			return protocolValue;
		}
		
		return removeExtraProtocolBit(protocolValue);//防止ADDED模式下自定义扩展bit对原始逻辑的影响，所有模式下协议值都从原始浮点内读出，此处修改不影响自定义协议处理效果
	}
	
	@ModifyVariable
	(
		method = "alternativeBlockPlacement",
		at = @At
		(
			value = "RETURN",
			ordinal = 0
		),
		name = "state"
	)
	private static BlockState addExtraProtocol
	(
		BlockState state,
		@Local(name = "block") Block block,
		@Local(name = "relativeHitX") double relativeHitX
	)
	{
		if(!isExtraProtocolServerEnabled)//未开启扩展协议
		{
			return state;
		}
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValue(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return state;//不是扩展协议
		}
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return state;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.ADDED)
		{
			return state;//如果不是添加模式，那么什么也不做
		}
		
		BlockState newState = blockProtocolStateAdapter.earlycompat$fromProtocolValue(protocolValue, state);//使用原值，不解包
		return newState;
	}
}
