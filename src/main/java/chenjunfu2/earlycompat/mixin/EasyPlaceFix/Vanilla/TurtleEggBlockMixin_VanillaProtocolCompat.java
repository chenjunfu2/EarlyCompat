package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		boolean isTurtleEgg = ctx.stateClient.isOf((TurtleEggBlock)(Object)this);
		int curEggs = isTurtleEgg ? ctx.stateClient.get(TurtleEggBlock.EGGS) : 0;
		int targetEggs = ctx.stateSchematic.get(TurtleEggBlock.EGGS);
		
		if(targetEggs > curEggs)
		{
			ctx.loopCount = targetEggs - curEggs;
		}
		else
		{
			ctx.loopCount = 0;
		}
	}
	
	@Override
	public int earlycompat$toProtocolValueLoop(LoopContext ctx)
	{
		return (ctx.stateSchematic.get(TurtleEggBlock.EGGS) - 1) & 0b0011;//2bit 1~4 -> 0~3
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int maxEggs = (extraProtocolValue & 0b0011) + 1;
		if(fromState.get(TurtleEggBlock.EGGS) > maxEggs)//获取一下当前自动生成的下一级
		{
			return null;
		}

		return fromState;
	}
	
	
	//下面无用
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		return 0;
	}

	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
