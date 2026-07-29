package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;


@Mixin(MultifaceGrowthBlock.class)
public class MultifaceGrowthBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Shadow
	@Final
	protected static Direction[] DIRECTIONS;
	
	@Unique
	private boolean earlycompat$hasDirection(BlockState state, Direction direction)
	{
		if(!state.isOf((MultifaceGrowthBlock)(Object)this))
		{
			return false;
		}
		
		return MultifaceGrowthBlock.hasDirection(state, direction);
	}
	
	//客户端编码
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		List<Integer> requireDirection = new ArrayList<>();
		ctx.data = requireDirection;
		
		for(int i = 0; i < DIRECTIONS.length; ++i)
		{
			var direction = DIRECTIONS[i];
			if (earlycompat$hasDirection(ctx.stateSchematic, direction) &&
				!earlycompat$hasDirection(ctx.stateClient, direction))//投影有但是世界缺失，那么计数
			{
				requireDirection.add(i);
            	++ctx.loopCount;
        	}
		}
	}
	
	//客户端编码
	@Override
	public int earlycompat$toProtocolValueLoop(LoopContext ctx)
	{
		List<Integer> requireDirection = (List<Integer>)ctx.data;
		
		if(ctx.loopIndex >= requireDirection.size())
		{
			return 0b0111;//7->服务端返回null，忽略
		}
		
		return requireDirection.get(ctx.loopIndex) & 0b0111;//3bit
	}
	
	//服务端解析
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int index = extraProtocolValue & 0b0111;
		if(index >= DIRECTIONS.length)
		{
			return null;
		}
		
		World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockWorldState = world.getBlockState(blockPos);

		return ((MultifaceGrowthBlock)fromState.getBlock()).withDirection(blockWorldState, world, blockPos, DIRECTIONS[index]);
	}
	
	
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)//用不到
	{
		return 0;
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()//用不到
	{
		return ProtocolType.REPLACE;
	}
}
