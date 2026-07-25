package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.network.EarlyCompatS2ClientHandler;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.util.PlacementHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(PlacementHandler.class)
@Environment(EnvType.CLIENT)
public class PlacementHandlerMixin_LitematicaProtocolCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/util/PlacementHandler;applyPlacementProtocolV2(Lnet/minecraft/block/BlockState;Lfi/dy/masa/litematica/util/PlacementHandler$UseContext;)Lnet/minecraft/block/BlockState;",
		cancellable = true,
		at = @At
		(
			value = "INVOKE",
			target = "Lfi/dy/masa/malilib/util/BlockUtils;getFirstDirectionProperty(Lnet/minecraft/block/BlockState;)Lnet/minecraft/state/property/DirectionProperty;",
			ordinal = 0
		)
	)
	private static void replaceExtraProtocol(BlockState state, PlacementHandler.UseContext context, CallbackInfoReturnable<BlockState> cir)
	{
		if(!EarlyCompatS2ClientHandler.isServerSupportsExtraProtocol())//服务端不是拓展协议端
		{
			return;
		}
		
		Block block = state.getBlock();
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		double relativeHitX = getRelativeHitX(context.getHitVec(), context.getPos());
		
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
		
		// litematica的ServerPlayNetworkHandlerMixin已经拦截掉了hitpos的所有维度，重放差值为0，所以可以直接利用别的，比如z
		if(blockProtocolStateAdapter.earlycompat$useProtocolAddition())//使用z扩展，因为z总是自己的，所以完全不需要进行扩展协议验证和摘除
		{
			double relativeHitZ = context.getHitVec().z - (double)context.getPos().getZ();
			int rawAdditionProtocolValue = decodeProtocolValueFromHitDim(relativeHitZ);
			newState = blockProtocolStateAdapter.earlycompat$fromProtocolValueAddition(rawAdditionProtocolValue, newState);
		}
		
		cir.setReturnValue(newState);
		cir.cancel();
	}
	
	
	@ModifyVariable
	(
		method = "Lfi/dy/masa/litematica/util/PlacementHandler;applyPlacementProtocolV2(Lnet/minecraft/block/BlockState;Lfi/dy/masa/litematica/util/PlacementHandler$UseContext;)Lnet/minecraft/block/BlockState;",
		at = @At
		(
			value = "INVOKE_ASSIGN",
			target = "Lfi/dy/masa/malilib/util/BlockUtils;getFirstDirectionProperty(Lnet/minecraft/block/BlockState;)Lnet/minecraft/state/property/DirectionProperty;",
			ordinal = 0
		),
		name = "protocolValue"
	)
	private static int replaceExtraProtocolValue
	(
		int protocolValue
	)
	{
		if(!EarlyCompatS2ClientHandler.isServerSupportsExtraProtocol())//服务端不是拓展协议端
		{
			return protocolValue;
		}
		
		//注意投影这里的处理时，原始值没有丢弃最低位，要和carpet extra一样必须先右移才是协议值，清理extra扩展位后复原
		return removeExtraProtocolBit(protocolValue >>> 1) << 1;//防止ADDED模式下自定义扩展bit对原始逻辑的影响，所有模式下协议值都从原始浮点内读出，此处修改不影响自定义协议处理效果
	}
	
	@ModifyVariable
	(
		method = "Lfi/dy/masa/litematica/util/PlacementHandler;applyPlacementProtocolV2(Lnet/minecraft/block/BlockState;Lfi/dy/masa/litematica/util/PlacementHandler$UseContext;)Lnet/minecraft/block/BlockState;",
		at = @At
		(
			value = "RETURN",
			ordinal = 2,
			shift = At.Shift.BY,
			by = -1//前移一条指令，在ALOAD 0之前才能修改state
		),
		name = "state"
	)
	private static BlockState addExtraProtocol
	(
		BlockState state,
		@Local(name = "context") PlacementHandler.UseContext context
	)
	{
		if(!EarlyCompatS2ClientHandler.isServerSupportsExtraProtocol())//服务端不是拓展协议端
		{
			return state;
		}
		
		Block block = state.getBlock();
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return state;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		double relativeHitX = getRelativeHitX(context.getHitVec(), context.getPos());
		
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
		
		// litematica的ServerPlayNetworkHandlerMixin已经拦截掉了hitpos的所有维度，重放差值为0，所以可以直接利用别的，比如z
		if(blockProtocolStateAdapter.earlycompat$useProtocolAddition())//使用z扩展，因为z总是自己的，所以完全不需要进行扩展协议验证和摘除
		{
			double relativeHitZ = context.getHitVec().z - (double)context.getPos().getZ();
			int rawAdditionProtocolValue = decodeProtocolValueFromHitDim(relativeHitZ);
			newState = blockProtocolStateAdapter.earlycompat$fromProtocolValueAddition(rawAdditionProtocolValue, newState);
		}
		
		return newState;
	}

}
