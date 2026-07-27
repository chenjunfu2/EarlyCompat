package chenjunfu2.earlycompat.util;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemStackProtocolDataAdapter
{
	int earlycompat$toProtocolValueAddition(ItemStack fromStack);
	@NotNull ItemStack earlycompat$fromProtocolValueAddition(int extraProtocolValue, ItemStack fromStack);
}
