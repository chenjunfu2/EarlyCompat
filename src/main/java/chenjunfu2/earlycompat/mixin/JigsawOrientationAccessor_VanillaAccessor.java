package chenjunfu2.earlycompat.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.enums.JigsawOrientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JigsawOrientation.class)
public class JigsawOrientationAccessor_VanillaAccessor
{
	@Accessor("BY_INDEX")
	static Int2ObjectMap<JigsawOrientation> getBY_INDEX()
	{
		throw new AssertionError();
	}
}
