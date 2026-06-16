package chenjunfu2.earlycompat.mixin;

import carpetextra.utils.BlockPlacer;
import com.llamalad7.mixinextras.sugar.Local;
import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.getExtraProtocolRawValue;
import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.isExtraProtocol;

@Mixin(BlockPlacer.class)
public class BlockPlacerMixin_CarpetExtraCrafterEarlyCompat
{
	@Inject
	(
		method = "alternativeBlockPlacement",
		cancellable = true,
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
			ordinal = 0
		)
	)
	private static void tryExtraProtocol
	(
		Block block,
		ItemPlacementContext context,
		CallbackInfoReturnable<BlockState> cir,
		@Local(name = "state") BlockState state,
		@Local(name = "protocolValue") int protocolValue
	)
	{
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		if (!isExtraProtocol(protocolValue))
		{
			return;//不是扩展协议
		}
		int extraProtocolValue = getExtraProtocolRawValue(protocolValue);
		
		//只处理扩展协议内已知的方块
		if(block instanceof CrafterBlock)
		{
			//低4bit存储12个方向
			int orientationIndex = (extraProtocolValue & 0b0000_1111) % 12;//0~11
			cir.setReturnValue(state.with(Properties.ORIENTATION, JigsawOrientationAccessor_VanillaAccessor.getBY_INDEX().get(orientationIndex)));
			cir.cancel();//完成返回
		}
		
		//不是已知方块，跳过处理，有可能是其它mixin的协议
	}
}

