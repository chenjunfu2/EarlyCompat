package chenjunfu2.earlycompat.util;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemStackProtocolDataAdapter
{
	int earlycompat$toProtocolValueAddition(ItemStack fromStack);//禁止修改原对象
	@NotNull ItemStack earlycompat$fromProtocolValueAddition(int extraProtocolValue, ItemStack fromStack);//务必拷贝返回，禁止修改原对象
}
