package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MushroomBlock.class)
public abstract class MushroomBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int bits =
			(fromState.get(MushroomBlock.NORTH)	? 0b0000_0001 : 0b0000_0000) |
			(fromState.get(MushroomBlock.EAST)	? 0b0000_0010 : 0b0000_0000) |
			(fromState.get(MushroomBlock.SOUTH)	? 0b0000_0100 : 0b0000_0000) |
			(fromState.get(MushroomBlock.WEST)	? 0b0000_1000 : 0b0000_0000) |
			(fromState.get(MushroomBlock.UP)	? 0b0001_0000 : 0b0000_0000) |
			(fromState.get(MushroomBlock.DOWN)	? 0b0010_0000 : 0b0000_0000);
			
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		return fromState
				.with(MushroomBlock.NORTH,	((extraProtocolValue & 0b0000_0001) == 0b0000_0001))
				.with(MushroomBlock.EAST,	((extraProtocolValue & 0b0000_0010) == 0b0000_0010))
				.with(MushroomBlock.SOUTH,	((extraProtocolValue & 0b0000_0100) == 0b0000_0100))
				.with(MushroomBlock.WEST,	((extraProtocolValue & 0b0000_1000) == 0b0000_1000))
				.with(MushroomBlock.UP,		((extraProtocolValue & 0b0001_0000) == 0b0001_0000))
				.with(MushroomBlock.DOWN,	((extraProtocolValue & 0b0010_0000) == 0b0010_0000));
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
