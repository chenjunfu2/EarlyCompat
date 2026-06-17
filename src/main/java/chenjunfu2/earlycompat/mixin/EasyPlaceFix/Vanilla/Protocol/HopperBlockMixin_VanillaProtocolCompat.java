package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		boolean isEnabled = fromState.get(HopperBlock.ENABLED);
		int bits = (isEnabled ? 0b0001_0000 : 0b0000_0000);
		return protocolValue | bits;//添加模式，从原始协议值中添加自定义内容
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		boolean isEnabled = (extraProtocolValue & 0b0001_0000) == 0b0001_0000;
		return fromState.with(HopperBlock.ENABLED, isEnabled);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}
