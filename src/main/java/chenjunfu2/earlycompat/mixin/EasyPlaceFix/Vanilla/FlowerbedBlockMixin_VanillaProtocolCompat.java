package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.MultiStageBlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerbedBlock.class)
public abstract class FlowerbedBlockMixin_VanillaProtocolCompat implements MultiStageBlockProtocolStateAdapter, BlockProtocolStateAdapter
{
	@Override
	public void earlycompat$setLoopCount(LoopContext ctx)
	{
		boolean isFlowerbed = ctx.stateClient.isOf((FlowerbedBlock)(Object)this);
		int curAmount = isFlowerbed ? ctx.stateClient.get(FlowerbedBlock.FLOWER_AMOUNT) : 0;
		int targetAmount = ctx.stateSchematic.get(FlowerbedBlock.FLOWER_AMOUNT);
		
		if(targetAmount > curAmount)
		{
			ctx.loopCount = targetAmount - curAmount;
		}
		else
		{
			ctx.loopCount = 0;
		}
	}
	
	@Override
	public int earlycompat$toProtocolValueLoop(LoopContext ctx)
	{
		int facingOridinal = ctx.stateSchematic.get(FlowerbedBlock.FACING).ordinal() - 2;// 2~5 -> 0~3
		int maxAmount = ctx.stateSchematic.get(FlowerbedBlock.FLOWER_AMOUNT) - 1;// 1~4 -> 0~3
		
		int bits =
			((facingOridinal & 0b0011) << 2) |//hi 2bit
			((maxAmount & 0b0011));//lo 2bit
		
		return bits;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int facingOridinal = ((extraProtocolValue & 0b1100) >> 2) + 2;
		int maxAmount = (extraProtocolValue & 0b0011) + 1;
		
		World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockWorldState = world.getBlockState(blockPos);
		
		BlockState newState = fromState;
		
		//如果当前放置的方块位置不是目标方块，那么应用首次放置转向，否则忽略转向
		if(!blockWorldState.isOf((FlowerbedBlock)(Object)this))
		{
			newState = newState.with(FlowerbedBlock.FACING, Direction.values()[facingOridinal]);
		}
		
		if(newState.get(FlowerbedBlock.FLOWER_AMOUNT) > maxAmount)//获取一下当前自动生成的下一级
		{
			return null;
		}

		return newState;
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
