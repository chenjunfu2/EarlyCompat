package chenjunfu2.earlycompat.mixin.CopperBulbEarly.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.chenjunfu2.block.BulbBlock;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BulbBlock.class)
public abstract class BulbBlockMixin_CopperBlubEarlyProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(BlockState fromState)
	{
		int lit = fromState.get(BulbBlock.LIT) ? 0b0001 : 0b0000;//bit0
		int powered = fromState.get(BulbBlock.POWERED) ? 0b0010 : 0b0000;//bit1
		return lit | powered;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int protocolValue, BlockState fromState)
	{
		//两个bit，bit0是lit，bit1是powered，都是bool值
		boolean lit = (protocolValue & 0b0001) == 0b0001;//bit0
		boolean powered = (protocolValue & 0b0010) == 0b0010;//bit1
		return fromState.with(BulbBlock.LIT, lit).with(BulbBlock.POWERED, powered);
	}
}
