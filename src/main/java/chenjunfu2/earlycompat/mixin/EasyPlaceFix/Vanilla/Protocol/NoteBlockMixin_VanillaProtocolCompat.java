package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter//让目标类实现此接口
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int note = fromState.get(NoteBlock.NOTE);
		return note;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int note = (extraProtocolValue & 0b0001_1111) % 25;// 0~24
		return fromState.with(NoteBlock.NOTE, note);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}
