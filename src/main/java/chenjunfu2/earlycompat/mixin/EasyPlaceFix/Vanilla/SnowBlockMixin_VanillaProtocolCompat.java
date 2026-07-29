package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		boolean isSnow = ctx.stateClient.isOf((SnowBlock)(Object)this);
		int curLayers = isSnow ? ctx.stateClient.get(SnowBlock.LAYERS) : 0;
		int targetLayers = ctx.stateSchematic.get(SnowBlock.LAYERS);
		
		if(targetLayers > curLayers)
		{
			ctx.loopCount = targetLayers - curLayers;
		}
		else
		{
			ctx.loopCount = 0;
		}
	}
	
	@Override
	public int earlycompat$toProtocolValueLoop(LoopContext ctx)
	{
		return (ctx.stateSchematic.get(SnowBlock.LAYERS) - 1) & 0b0111;//3bit 1~8 -> 0~7
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int maxLayers = (extraProtocolValue & 0b0111) + 1;
		if(fromState.get(SnowBlock.LAYERS) > maxLayers)//获取一下当前自动生成的下一级
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
