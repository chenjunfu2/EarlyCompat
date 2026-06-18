package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int facingOrdinal = fromState.get(DoorBlock.FACING).ordinal() - 2;
		int hingeOrdinal = fromState.get(DoorBlock.HINGE).ordinal();
		boolean isOpen = fromState.get(DoorBlock.OPEN);
		int bits =
			(facingOrdinal & 0b0000_0011) |
				(hingeOrdinal & 0b0000_0001) << 2 |
				(isOpen ? 0b0000_1000 : 0b0000_0000);
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int facingOrdinal = (extraProtocolValue & 0b0000_0011) + 2;
		int hingeOrdinal = (extraProtocolValue & 0b0000_0100) >>> 2;//0~1
		boolean isOpen = (extraProtocolValue & 0b0000_1000) == 0b0000_1000;
		return fromState
			.with(DoorBlock.FACING, Direction.values()[facingOrdinal])
			.with(DoorBlock.HINGE, DoorHinge.values()[hingeOrdinal])
			.with(DoorBlock.OPEN, isOpen);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
