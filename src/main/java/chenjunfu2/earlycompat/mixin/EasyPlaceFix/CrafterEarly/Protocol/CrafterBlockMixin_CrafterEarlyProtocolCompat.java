package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CrafterEarly.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrafterBlock.class)
public abstract class CrafterBlockMixin_CrafterEarlyProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		JigsawOrientation orientation = fromState.get(Properties.ORIENTATION);
		int orientationOrdinal = orientation.ordinal();
		return orientationOrdinal & 0b0000_1111;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		//低4bit存储12个方向
		int orientationOrdinal = (extraProtocolValue & 0b0000_1111) % 12;//0~11
		return fromState.with(Properties.ORIENTATION, JigsawOrientation.values()[orientationOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
