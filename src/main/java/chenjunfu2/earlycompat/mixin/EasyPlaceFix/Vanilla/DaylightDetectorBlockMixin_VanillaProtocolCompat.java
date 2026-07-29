package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DaylightDetectorBlock.class)
public abstract class DaylightDetectorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter//让目标类实现此接口
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		boolean isInverted = fromState.get(DaylightDetectorBlock.INVERTED);
		return isInverted ? 0b0001 : 0b0000;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		boolean isInverted = (extraProtocolValue & 0b0001) == 0b0001;//0bit
		return fromState.with(DaylightDetectorBlock.INVERTED, isInverted);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
