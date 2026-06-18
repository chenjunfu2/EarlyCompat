package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int hingeOrdinal = fromState.get(DoorBlock.HINGE).ordinal();
		boolean isOpen = fromState.get(DoorBlock.OPEN);
		int bits =
			(hingeOrdinal & 0b0000_0001) << 4 |
				(isOpen ? 0b0010_0000 : 0b0000_0000);
		return protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int hingeOrdinal = (extraProtocolValue & 0b0001_0000) >>> 4;//0~1
		boolean isOpen = (extraProtocolValue & 0b0010_0000) == 0b0010_0000;
		return fromState
			.with(DoorBlock.HINGE, DoorHinge.values()[hingeOrdinal])
			.with(DoorBlock.OPEN, isOpen);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}
