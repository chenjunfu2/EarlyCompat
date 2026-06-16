package chenjunfu2.earlycompat.mixin;

import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.state.property.EnumProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CrafterBlock.class)
public abstract class CrafterBlockAccessor_CrafterEarlyAccessor
{
	@Accessor("ORIENTATION")
	static EnumProperty<JigsawOrientation> getORIENTATION()
	{
		throw new AssertionError();
	}
}
