package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		boolean isLit = fromState.get(CampfireBlock.LIT);
		int bits = (isLit ? 0b0001_0000 : 0b0000_0000);
		return protocolValue | bits;//添加模式，从原始协议值中添加自定义内容
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		boolean isLit = (extraProtocolValue & 0b0001_0000) == 0b0001_0000;
		return fromState.with(CampfireBlock.LIT, isLit);
	}
	
	@Override
	public @NotNull BlockProtocolStateAdapter.ProtocolType earlycompat$getProtocolType()
	{
		return BlockProtocolStateAdapter.ProtocolType.ADDED;
	}
}
