package chenjunfu2.earlycompat.util;

import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;

public interface BlockProtocolStateAdapter
{
	int earlycompat$toProtocolValue(BlockState fromState);
	@NotNull BlockState earlycompat$fromProtocolValue(int protocolValue, BlockState fromState);
}
