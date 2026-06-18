package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HangingSignBlock.class)
public abstract class HangingSignBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int rotation = fromState.get(HangingSignBlock.ROTATION);
		boolean isAttached = fromState.get(HangingSignBlock.ATTACHED);
		int bits =
			(rotation & 0b0000_1111) | //0~15 共16种旋转
			(isAttached ? 0b0001_0000 : 0b0000_0000);
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int rotation = (extraProtocolValue & 0b0000_1111);
		boolean isAttached = (extraProtocolValue & 0b0001_0000) == 0b0001_0000;
		return fromState
			.with(HangingSignBlock.ROTATION, rotation)
			.with(HangingSignBlock.ATTACHED, isAttached);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
