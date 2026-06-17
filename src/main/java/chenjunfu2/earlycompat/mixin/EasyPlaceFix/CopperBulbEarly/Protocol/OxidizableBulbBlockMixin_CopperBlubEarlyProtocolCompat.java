package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CopperBulbEarly.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import com.chenjunfu2.block.OxidizableBulbBlock;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OxidizableBulbBlock.class)
public abstract class OxidizableBulbBlockMixin_CopperBlubEarlyProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int lit = fromState.get(OxidizableBulbBlock.LIT) ? 0b0001 : 0b0000;//bit0
		int powered = fromState.get(OxidizableBulbBlock.POWERED) ? 0b0010 : 0b0000;//bit1
		return lit | powered;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		//两个bit，bit0是lit，bit1是powered，都是bool值
		boolean lit = (extraProtocolValue & 0b0001) == 0b0001;//bit0
		boolean powered = (extraProtocolValue & 0b0010) == 0b0010;//bit1
		return fromState.with(OxidizableBulbBlock.LIT, lit).with(OxidizableBulbBlock.POWERED, powered);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
