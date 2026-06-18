package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.WallMountLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WallMountedBlock.class)
public abstract class WallMountedBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int faceOridinal = fromState.get(WallMountedBlock.FACE).ordinal();
		int bits = ((faceOridinal & 0b0000_0011) << 4);
		return protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int faceOridinal = ((extraProtocolValue | 0b0011_0000) >>> 4) % 3;//0~2
		return fromState.with(WallMountedBlock.FACE, WallMountLocation.values()[faceOridinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}
