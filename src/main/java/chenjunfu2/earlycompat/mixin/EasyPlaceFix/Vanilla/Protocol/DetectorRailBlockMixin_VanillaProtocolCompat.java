package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.enums.RailShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DetectorRailBlock.class)
public class DetectorRailBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int shapeOrdinal = fromState.get(DetectorRailBlock.SHAPE).ordinal();
		return shapeOrdinal & 0b0000_1111;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int shapeOrdinal = (extraProtocolValue & 0b0000_1111) % 10;//0~9 10种状态
		return fromState
			.with(DetectorRailBlock.SHAPE, RailShape.values()[shapeOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
