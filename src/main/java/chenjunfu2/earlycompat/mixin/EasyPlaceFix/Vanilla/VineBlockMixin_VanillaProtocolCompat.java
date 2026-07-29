package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(VineBlock.class)
public abstract class VineBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Unique
	private static final Direction[] earlycompat$DIRECTIONS =
		Arrays.stream(Direction.values())
          .filter(d -> d != Direction.DOWN)
          .toArray(Direction[]::new);
	
	@Unique
	private boolean earlycompat$hasDirection(BlockState state, Direction direction)
	{
		if(!state.isOf((VineBlock)(Object)this))
		{
			return false;
		}
		
		BooleanProperty booleanProperty = VineBlock.getFacingProperty(direction);
        return state.contains(booleanProperty) && (Boolean)state.get(booleanProperty);
	}
	
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		List<Integer> requireDirection = new ArrayList<>();
		ctx.data = requireDirection;
		
		for(int i = 0; i < earlycompat$DIRECTIONS.length; ++i)
		{
			var direction = earlycompat$DIRECTIONS[i];
			
			if (earlycompat$hasDirection(ctx.stateSchematic, direction) &&
				!earlycompat$hasDirection(ctx.stateClient,direction))//投影有但是世界缺失，那么计数
			{
				requireDirection.add(i);
            	++ctx.loopCount;
        	}
		}
	}
	
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
	
	@Invoker("shouldHaveSide")
	public abstract boolean earlycompat_shadow$shouldHaveSide(BlockView world, BlockPos pos, Direction side);
	
	@Nullable
	public BlockState withDirection(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		//获取prop
    	BooleanProperty property = VineBlock.getFacingProperty(direction);
    	boolean isVine = state.isOf((VineBlock)(Object)this);
	
    	// 如果已经是藤蔓且已有该方向的面，无法重复添加
    	if (isVine && state.get(property))
		{
    	    return null;
    	}
	
    	// 检查该方向是否适合附着（完整方块面或藤蔓向下延伸的特殊规则）
    	if (!earlycompat_shadow$shouldHaveSide(world, pos, direction))
		{
    	    return null;
    	}
	
    	// 确定基础状态：若当前位置已是藤蔓就沿用，否则使用默认状态
    	BlockState baseState = isVine ? state : ((VineBlock)(Object)this).getDefaultState();
    	return baseState.with(property, true);
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int index = extraProtocolValue & 0b0111;
		if(index >= earlycompat$DIRECTIONS.length)
		{
			return null;
		}
		
		World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockWorldState = world.getBlockState(blockPos);

		return withDirection(blockWorldState, world, blockPos, earlycompat$DIRECTIONS[index]);
	}
	
	
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
