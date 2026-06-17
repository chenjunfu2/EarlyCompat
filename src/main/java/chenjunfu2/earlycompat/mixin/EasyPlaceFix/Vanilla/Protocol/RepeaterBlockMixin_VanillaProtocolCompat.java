package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RepeaterBlock.class)
public abstract class RepeaterBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		
		
		
		
		
		return 0;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		return null;
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()//中继器因为需要的状态内容太多，必须替换掉原先的处理流程，原始处理流程浪费的太多了
	{
		return ProtocolType.REPLACE;
	}
}
