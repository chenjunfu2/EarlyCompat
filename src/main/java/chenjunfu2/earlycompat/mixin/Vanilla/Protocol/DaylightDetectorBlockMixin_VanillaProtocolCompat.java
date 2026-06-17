package chenjunfu2.earlycompat.mixin.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DaylightDetectorBlock.class)
public abstract class DaylightDetectorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter//让目标类实现此接口
{
	@Override
	public int earlycompat$toProtocolValue(BlockState fromState)
	{
		boolean isInverted = fromState.get(DaylightDetectorBlock.INVERTED);
		return isInverted ? 0b0001 : 0b0000;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int protocolValue, BlockState fromState)
	{
		boolean isInverted = (protocolValue & 0b0001) == 0b0001;//0bit
		return fromState.with(DaylightDetectorBlock.INVERTED, isInverted);
	}
}
