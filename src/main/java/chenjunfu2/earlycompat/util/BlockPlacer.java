package chenjunfu2.earlycompat.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.decodeProtocolValue;

public class BlockPlacer
{
	public static @Nullable BlockState alternativeBlockPlacement(Block block, Block wallBlock, Direction verticalAttachmentDirection, ItemPlacementContext context)
	{
		Vec3d hitPos = context.getHitPos();
		BlockPos blockPos = context.getBlockPos();
		double relativeHitX = hitPos.x - (double)blockPos.getX();
		if(relativeHitX < (double)2.0F)//不是协议值
		{
			return null;
		}
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValue(relativeHitX);//注意，特殊路径非Extra协议，全部bit都可利用
		
		BlockState blockState = null;
		boolean isWallType = (protocolValue & 0b0000_0001) == 0b0000_0001;
		protocolValue >>>= 1;//判断完毕，丢弃
		if(isWallType)
		{
			BlockState blockState2 = wallBlock.getPlacementState(context);
			if(blockState2 == null)
			{
				return null;
			}
			
			//默认协议
			DirectionProperty directionProperty = getFirstDirectionProperty(blockState2);
			if(directionProperty != null)
			{
				int facingIndex = (protocolValue & 0b0000_0011) + 2;//6方向，facing从2开始
				Direction facing = Direction.values()[facingIndex];
				blockState2 = blockState2.with(directionProperty, facing);
			}
			
			if(wallBlock instanceof BlockProtocolStateAdapter wallBlockProtocolStateAdapter)
			{
				blockState = wallBlockProtocolStateAdapter.earlycompat$fromProtocolValue(protocolValue, blockState2);
			}
			else
			{
				blockState = blockState2;
			}
		}
		else
		{
			if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
			{
				return null;
			}
			
			BlockState blockState2 = block.getPlacementState(context);
			if(blockState2 == null)
			{
				return null;
			}
			
			blockState = blockProtocolStateAdapter.earlycompat$fromProtocolValue(protocolValue, blockState2);
		}
		
		return blockState;
	}
	
	public static @Nullable DirectionProperty getFirstDirectionProperty(BlockState state)
	{
		for(Property<?> prop : state.getProperties())
		{
			if (prop instanceof DirectionProperty)
			{
				return (DirectionProperty)prop;
			}
		}
		
		return null;
	}
}
