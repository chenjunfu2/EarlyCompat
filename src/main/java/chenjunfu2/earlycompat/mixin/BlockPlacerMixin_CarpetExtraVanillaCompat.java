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
			target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
			ordinal = 0
		)
	)
	private static void tryExtraProtocol
	(
		Block block,
		ItemPlacementContext context,
		CallbackInfoReturnable<BlockState> cir,
		@Local(name = "relativeHitX") double relativeHitX,
		@Local(name = "state") BlockState state,
		@Local(name = "protocolValue") int protocolValue
	)
	{
		//判断最低为是否为1，是的话则为扩展字节码
		boolean isExtraProtocol = (((int)relativeHitX - 2) & 0x01) == 0x01;
		if(!isExtraProtocol)
		{
			return;//不是扩展协议
		}
		
		//只处理扩展协议内已知的方块
		if(block instanceof NoteBlock)
		{
			//低5bit（0~31）存储note
			int note = protocolValue & 0b0001_1111;
			cir.setReturnValue(state.with(NoteBlock.NOTE, note % 25));// 0~24
			cir.cancel();//完成返回
		}
		
		//不是已知方块，跳过处理，有可能是其它mixin的协议
	}
}
