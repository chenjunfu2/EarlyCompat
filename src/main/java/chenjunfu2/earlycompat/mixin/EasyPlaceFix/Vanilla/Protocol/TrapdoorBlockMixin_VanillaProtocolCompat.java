package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int halfOrdinal = fromState.get(TrapdoorBlock.HALF).ordinal();
		boolean isOpen = fromState.get(TrapdoorBlock.OPEN);
		int bits =
			(halfOrdinal & 0b0000_0001) << 4 |
			(isOpen ? 0b0010_0000 : 0b0000_0000);
		return protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int halfOrdinal = (extraProtocolValue & 0b0001_0000) >>> 4;//0~1
		boolean isOpen = (extraProtocolValue & 0b0010_0000) == 0b0010_0000;
		return fromState
			.with(TrapdoorBlock.HALF, BlockHalf.values()[halfOrdinal])
			.with(TrapdoorBlock.OPEN, isOpen);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}
