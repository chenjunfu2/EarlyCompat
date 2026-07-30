package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.accessor.PlaceStateAccessor;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import static chenjunfu2.earlycompat.config.EarlyCompatConfigs.EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE;

@Mixin(RailBlock.class)
public abstract class RailBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int shapeOrdinal = fromState.get(RailBlock.SHAPE).ordinal();
		boolean noUpdate = EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE();
		
		int bits =
			(shapeOrdinal & 0b0000_1111) |
			(noUpdate ? 0b0001_0000 : 0b0000_0000);
		
		return bits;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int shapeOrdinal = (extraProtocolValue & 0b0000_1111) % 10;//0~9 10种状态
		boolean noUpdate = (extraProtocolValue & 0b0001_0000) == 0b0001_0000;
		
		if(noUpdate && (fromState.getBlock().asItem() instanceof PlaceStateAccessor placeStateAccessor))
		{
			placeStateAccessor.earlycompat$setPlaceFlag(PlaceStateAccessor.easyPlaceRailBlockNoShapeUpdate);
		}
		
		return fromState
			.with(RailBlock.SHAPE, RailShape.values()[shapeOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
