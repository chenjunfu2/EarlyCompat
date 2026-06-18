package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.enums.WallMountLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeverBlock.class)
public class LeverBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int faceOridinal = fromState.get(LeverBlock.FACE).ordinal();
		boolean isPowered = fromState.get(LeverBlock.POWERED);
		int bits =
			((faceOridinal & 0b0000_0011) << 4) |
			(isPowered ? 0b0100_0000 : 0b0000_0000);
		return protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int faceOridinal = ((extraProtocolValue & 0b0011_0000) >>> 4) % 3;//0~2
		boolean isPowered = (extraProtocolValue & 0b0100_0000) == 0b0100_0000;
		return fromState
			.with(LeverBlock.FACE, WallMountLocation.values()[faceOridinal])
			.with(LeverBlock.POWERED, isPowered);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}
