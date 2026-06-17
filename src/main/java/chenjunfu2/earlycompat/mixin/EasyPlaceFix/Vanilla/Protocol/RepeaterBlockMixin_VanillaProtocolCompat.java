package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RepeaterBlock.class)
public abstract class RepeaterBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int facingOrdinal = fromState.get(RepeaterBlock.FACING).ordinal() - 2;//2bit 0~3 默认方向有6个，中继器只能4向
		int delay = fromState.get(RepeaterBlock.DELAY) - 1;//2bit 0~3
		boolean isPowered = fromState.get(RepeaterBlock.POWERED);//1bit
		boolean isLocked = fromState.get(RepeaterBlock.LOCKED);//1bit
		int bits =
			(facingOrdinal & 0b0000_0011)|
			((delay & 0b0000_0011) << 2)|
			(isPowered ? 0b0001_0000 : 0b0000_0000)|
			(isLocked ? 0b0010_0000 : 0b0000_0000);
		return bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int facingOrdinal = (extraProtocolValue & 0b0000_0011) + 2;//2bit 0~3
		int delay = ((extraProtocolValue & 0b0000_1100) >>> 2) + 1;//2bit 0~3
		boolean isPowered = (extraProtocolValue & 0b0001_0000) == 0b0001_0000;//1bit
		boolean isLocked = (extraProtocolValue & 0b0010_0000) == 0b0010_0000;//1bit
		
		return fromState
			.with(RepeaterBlock.FACING, Direction.values()[facingOrdinal])
			.with(RepeaterBlock.DELAY, delay)
			.with(RepeaterBlock.POWERED, isPowered)
			.with(RepeaterBlock.LOCKED, isLocked);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()//中继器因为需要的状态内容太多，必须替换掉原先的处理流程，原始处理流程浪费的太多了
	{
		return ProtocolType.REPLACE;
	}
}
