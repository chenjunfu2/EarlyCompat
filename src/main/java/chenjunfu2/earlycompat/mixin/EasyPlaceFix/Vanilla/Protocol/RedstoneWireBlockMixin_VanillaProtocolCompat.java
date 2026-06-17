package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.enums.WireConnection;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter//让目标类实现此接口
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		boolean isDot =
			fromState.get(RedstoneWireBlock.WIRE_CONNECTION_SOUTH)==WireConnection.NONE &&
			fromState.get(RedstoneWireBlock.WIRE_CONNECTION_WEST)==WireConnection.NONE &&
			fromState.get(RedstoneWireBlock.WIRE_CONNECTION_NORTH)==WireConnection.NONE &&
			fromState.get(RedstoneWireBlock.WIRE_CONNECTION_EAST)==WireConnection.NONE;
		return isDot ? 0b0001 : 0b0000;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		boolean isDot = (extraProtocolValue & 0b0001) == 0b0001;//0bit
		return isDot
			? fromState
				.with(RedstoneWireBlock.WIRE_CONNECTION_EAST, WireConnection.NONE)
				.with(RedstoneWireBlock.WIRE_CONNECTION_WEST, WireConnection.NONE)
				.with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.NONE)
				.with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.NONE)
			: fromState;
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}