package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int facingOrdinal = fromState.get(ComparatorBlock.FACING).ordinal();
		boolean isPowered = fromState.get(ComparatorBlock.POWERED);
		boolean isSubMod = fromState.get(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT;
		int bits =
			(facingOrdinal & 0b0000_0011) |
			(isPowered ? 0b0000_0100 : 0b0000_0000) |
			(isSubMod  ? 0b0000_1000 : 0b0000_0000);
		return  protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int facingOrdinal = (extraProtocolValue & 0b0000_0011);
		boolean isPowered = (extraProtocolValue & 0b0000_0100) == 0b0000_0100;
		boolean isSubMod = (extraProtocolValue & 0b0000_1000) == 0b0000_1000;
		return fromState
			.with(ComparatorBlock.FACING, Direction.values()[facingOrdinal])
			.with(ComparatorBlock.POWERED, isPowered)
			.with(ComparatorBlock.MODE, isSubMod ? ComparatorMode.SUBTRACT : ComparatorMode.COMPARE);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
