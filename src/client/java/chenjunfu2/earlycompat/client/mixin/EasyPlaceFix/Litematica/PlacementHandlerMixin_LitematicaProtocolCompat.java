package chenjunfu2.earlycompat.client.mixin.EasyPlaceFix.Litematica;

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

import static chenjunfu2.earlycompat.EarlyCompat.isExtraProtocolServerEnabled;
import static chenjunfu2.earlycompat.client.EarlyCompatClient.isExtraProtocolClientEnabled;
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
		if(!isExtraProtocolClientEnabled)//未开启扩展协议
		{
			return;
		}
		
		Block block = state.getBlock();
		double relativeHitX = getRelativeHitX(context.getHitVec(), context.getPos());
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValueFromHitX(relativeHitX);
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
		
		int extraProtocolValue = extraProtocolValueToRawProtocolValue(protocolValue);
		cir.setReturnValue(blockProtocolStateAdapter.earlycompat$fromProtocolValue(extraProtocolValue, state));
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
		if(!isExtraProtocolClientEnabled)//未开启扩展协议
		{
			return protocolValue;
		}
		
		return removeExtraProtocolBit(protocolValue);//防止ADDED模式下自定义扩展bit对原始逻辑的影响，所有模式下协议值都从原始浮点内读出，此处修改不影响自定义协议处理效果
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
		if(!isExtraProtocolClientEnabled)//未开启扩展协议
		{
			return state;
		}
		
		Block block = state.getBlock();
		double relativeHitX = getRelativeHitX(context.getHitVec(), context.getPos());
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValueFromHitX(relativeHitX);
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
