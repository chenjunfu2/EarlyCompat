package chenjunfu2.earlycompat.mixin;

import carpetextra.utils.BlockPlacer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockPlacer.class)
public abstract class BlockPlacerMixin_CarpetExtraVanillaCompat
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
		if(block instanceof NoteBlock)
		{
			//低5bit（0~31）存储note
			int note = (extraProtocolValue & 0b0001_1111) % 25;// 0~24
			cir.setReturnValue(state.with(NoteBlock.NOTE, note));
			cir.cancel();//完成返回
		}
		
		//不是已知方块，跳过处理，有可能是其它mixin的协议
	}
}
