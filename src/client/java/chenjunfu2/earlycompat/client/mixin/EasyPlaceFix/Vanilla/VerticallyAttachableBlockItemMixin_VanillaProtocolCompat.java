package chenjunfu2.earlycompat.client.mixin.EasyPlaceFix.Vanilla;

import net.minecraft.block.Block;
import net.minecraft.item.VerticallyAttachableBlockItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VerticallyAttachableBlockItem.class)
public class VerticallyAttachableBlockItemMixin_VanillaProtocolCompat implements VerticallyAttachableBlockItemAccessor
{
	@Final
	@Shadow
	protected Block wallBlock;
	
	@Override
	public Block esrlycompat$getWallBlock()
	{
		return wallBlock;
	}
}
