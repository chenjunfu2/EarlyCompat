package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int facingOrdinal = fromState.get(TrapdoorBlock.FACING).ordinal() - 2;
		int halfOrdinal = fromState.get(TrapdoorBlock.HALF).ordinal();
		boolean isOpen = fromState.get(TrapdoorBlock.OPEN);
		int bits =
			(facingOrdinal & 0b0000_0011) |
			(halfOrdinal & 0b0000_0001) << 2 |
			(isOpen ? 0b0000_1000 : 0b0000_0000);
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int facingOrdinal = (extraProtocolValue & 0b0000_0011) + 2;
		int halfOrdinal = (extraProtocolValue & 0b0000_0100) >>> 2;//0~1
		boolean isOpen = (extraProtocolValue & 0b0000_1000) == 0b0000_1000;
		return fromState
			.with(TrapdoorBlock.FACING, Direction.values()[facingOrdinal])
			.with(TrapdoorBlock.HALF, BlockHalf.values()[halfOrdinal])
			.with(TrapdoorBlock.OPEN, isOpen);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
