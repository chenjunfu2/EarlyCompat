package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PoweredRailBlock.class)//相当于同时修复activator_rail
public class PoweredRailBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int shapeOrdinal = fromState.get(PoweredRailBlock.SHAPE).ordinal();
		return shapeOrdinal & 0b0000_0111;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int shapeOrdinal = (extraProtocolValue & 0b0000_0111) % 10;//0~5 6种状态
		return fromState
			.with(PoweredRailBlock.SHAPE, RailShape.values()[shapeOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
