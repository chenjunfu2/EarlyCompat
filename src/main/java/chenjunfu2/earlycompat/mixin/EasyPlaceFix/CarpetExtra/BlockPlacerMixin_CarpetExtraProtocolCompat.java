package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CarpetExtra;

import carpetextra.utils.BlockPlacer;
import chenjunfu2.earlycompat.network.EarlyCompatC2ServerHandler;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockPlacer.class)
public abstract class BlockPlacerMixin_CarpetExtraProtocolCompat
{
	@Inject
	(
		method = "Lcarpetextra/utils/BlockPlacer;alternativeBlockPlacement(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
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
		if(!EarlyCompatC2ServerHandler.isExtraProtocolPlayer((ServerPlayerEntity)context.getPlayer()))//玩家没有扩展协议
		{
			return;
		}
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValueFromHitDim(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return;//不是扩展协议
		}

		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.REPLACE)
		{
			return;//如果不是替换模式，那么什么也不做
		}
		
		int rawProtocolValue = extraProtocolValueToRawProtocolValue(protocolValue);
		BlockState newState = blockProtocolStateAdapter.earlycompat$fromProtocolValue(rawProtocolValue, state);
		
		// carpet的ServerPlayNetworkHandlerMixin已经拦截掉了hitpos的所有维度，重放差值为0，所以可以直接利用别的，比如z
		if(blockProtocolStateAdapter.earlycompat$useProtocolAddition())//使用z扩展，因为z总是自己的，所以完全不需要进行扩展协议验证和摘除
		{
			double relativeHitZ = context.getHitPos().z - (double)context.getBlockPos().getZ();
			int rawAdditionProtocolValue = decodeProtocolValueFromHitDim(relativeHitZ);
			newState = blockProtocolStateAdapter.earlycompat$fromProtocolValueAddition(rawAdditionProtocolValue, newState);
		}

		cir.setReturnValue(newState);
		cir.cancel();
	}
	
	@ModifyVariable
	(
		method = "Lcarpetextra/utils/BlockPlacer;alternativeBlockPlacement(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
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
		int protocolValue,
		@Local(name = "context") ItemPlacementContext context
	)
	{
		if(!EarlyCompatC2ServerHandler.isExtraProtocolPlayer((ServerPlayerEntity)context.getPlayer()))//玩家没有扩展协议
		{
			return protocolValue;
		}
		
		return removeExtraProtocolBit(protocolValue);//防止ADDED模式下自定义扩展bit对原始逻辑的影响，所有模式下协议值都从原始浮点内读出，此处修改不影响自定义协议处理效果
	}
	
	@ModifyVariable
	(
		method = "Lcarpetextra/utils/BlockPlacer;alternativeBlockPlacement(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
		at = @At
		(
			value = "RETURN",
			ordinal = 2,
			shift = At.Shift.BY,
			by = -1//前移一条指令，在ALOAD 6之前才能修改state
		),
		name = "state"
	)
	private static BlockState addExtraProtocol
	(
		BlockState state,
		@Local(name = "block") Block block,
		@Local(name = "context") ItemPlacementContext context,
		@Local(name = "relativeHitX") double relativeHitX
	)
	{
		if(!EarlyCompatC2ServerHandler.isExtraProtocolPlayer((ServerPlayerEntity)context.getPlayer()))//玩家没有扩展协议
		{
			return state;
		}
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return state;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValueFromHitDim(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return state;//不是扩展协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() != BlockProtocolStateAdapter.ProtocolType.ADDED)
		{
			return state;//如果不是添加模式，那么什么也不做
		}
		
		BlockState newState = blockProtocolStateAdapter.earlycompat$fromProtocolValue(protocolValue, state);//使用原值，不解包
		
		// carpet的ServerPlayNetworkHandlerMixin已经拦截掉了hitpos的所有维度，重放差值为0，所以可以直接利用别的，比如z
		if(blockProtocolStateAdapter.earlycompat$useProtocolAddition())//使用z扩展，因为z总是自己的，所以完全不需要进行扩展协议验证和摘除
		{
			double relativeHitZ = context.getHitPos().z - (double)context.getBlockPos().getZ();
			int rawAdditionProtocolValue = decodeProtocolValueFromHitDim(relativeHitZ);
			newState = blockProtocolStateAdapter.earlycompat$fromProtocolValueAddition(rawAdditionProtocolValue, newState);
		}
		
		return newState;
	}
}
