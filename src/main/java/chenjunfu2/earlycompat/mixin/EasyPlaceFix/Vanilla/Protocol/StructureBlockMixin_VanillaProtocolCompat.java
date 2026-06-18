package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.enums.StructureBlockMode;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureBlock.class)
public abstract class StructureBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int modeOrdinal = fromState.get(StructureBlock.MODE).ordinal();
		int bits = (modeOrdinal & 0b0011);//只有4个
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int modeOrdinal = (extraProtocolValue & 0b0011);//0~3
		return fromState.with(StructureBlock.MODE, StructureBlockMode.values()[modeOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
