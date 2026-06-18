package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StairsBlock.class)
public abstract class StairsBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int facingOrdinal = fromState.get(StairsBlock.FACING).ordinal() - 2;//0~3 2bit
		int halfOrdinal = fromState.get(StairsBlock.HALF).ordinal();//0~1 1bit
		int shapeOrdinal = fromState.get(StairsBlock.SHAPE).ordinal();//0~4 3bit
		int bits =
			(facingOrdinal & 0b0000_0011) |
			(halfOrdinal & 0b0000_0001) << 2 |
			(shapeOrdinal & 0b0000_0111) << 3;
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int facingOrdinal = (extraProtocolValue & 0b0000_0011) + 2;//0~3 2bit
		int halfOrdinal = (extraProtocolValue & 0b0000_0100) >>> 2;//0~1 1bit
		int shapeOrdinal = (extraProtocolValue & 0b0011_1000) >>> 3;//0~4 3bit
		return fromState
			.with(StairsBlock.FACING, Direction.values()[facingOrdinal])
			.with(StairsBlock.HALF, BlockHalf.values()[halfOrdinal])
			.with(StairsBlock.SHAPE, StairShape.values()[shapeOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
