package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SeaPickleBlock.class)
public abstract class SeaPickleBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		boolean isSeaPickle = ctx.stateClient.isOf((SeaPickleBlock)(Object)this);
		int curPickles = isSeaPickle ? ctx.stateClient.get(SeaPickleBlock.PICKLES) : 0;
		int targetPickles = ctx.stateSchematic.get(SeaPickleBlock.PICKLES);
		
		if(targetPickles > curPickles)
		{
			ctx.loopCount = targetPickles - curPickles;
		}
		else
		{
			ctx.loopCount = 0;
		}
	}
	
	@Override
	public int earlycompat$toProtocolValueLoop(LoopContext ctx)
	{
		return (ctx.stateSchematic.get(SeaPickleBlock.PICKLES) - 1) & 0b0011;//2bit 1~4 -> 0~3
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int maxPickles = (extraProtocolValue & 0b0011) + 1;
		if(fromState.get(SeaPickleBlock.PICKLES) > maxPickles)//获取一下当前自动生成的下一级
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
