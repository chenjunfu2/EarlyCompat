package chenjunfu2.earlycompat.mixin;

import carpetextra.utils.BlockPlacer;
import com.chenjunfu2.block.BulbBlock;
import com.chenjunfu2.block.OxidizableBulbBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockPlacer.class)
public class BlockPlacerMixin_CarpetExtraCopperBulbEarlyCompat
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
	private static void tryExtraProtocol
	(
		Block block,
		ItemPlacementContext context,
		CallbackInfoReturnable<BlockState> cir,
		@Local(name = "state") BlockState state,
		@Local(name = "relativeHitX") double relativeHitX
	)
	{
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValue(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return;//不是扩展协议
		}
		int extraProtocolValue = decodeExtraProtocolRawValue(protocolValue);
		
		
		//只处理扩展协议内已知的方块
		if(block instanceof OxidizableBulbBlock)
		{
			//两个bit，bit0是lit，bit1是powered，都是bool值
			boolean lit = (extraProtocolValue & 0b0001) == 0b0001;//bit0
			boolean powered = (extraProtocolValue & 0b0010) == 0b0010;//bit1
			cir.setReturnValue(state.with(OxidizableBulbBlock.LIT, lit).with(OxidizableBulbBlock.POWERED, powered));
			cir.cancel();//完成返回
		}
		else if(block instanceof BulbBlock)
		{
			//两个bit，bit0是lit，bit1是powered，都是bool值
			boolean lit = (extraProtocolValue & 0b0001) == 0b0001;//bit0
			boolean powered = (extraProtocolValue & 0b0010) == 0b0010;//bit1
			cir.setReturnValue(state.with(BulbBlock.LIT, lit).with(BulbBlock.POWERED, powered));
			cir.cancel();//完成返回
		}
		
		//不是已知方块，跳过处理，有可能是其它mixin的协议
	}
}
