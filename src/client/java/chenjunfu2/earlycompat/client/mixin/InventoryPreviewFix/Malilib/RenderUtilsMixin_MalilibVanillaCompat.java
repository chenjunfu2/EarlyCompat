package chenjunfu2.earlycompat.client.mixin.InventoryPreviewFix.Malilib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.malilib.render.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderUtils.class)
@Environment(EnvType.CLIENT)
public abstract class RenderUtilsMixin_MalilibVanillaCompat
{
	@WrapOperation
	(
		method = "Lfi/dy/masa/malilib/render/RenderUtils;renderShulkerBoxPreview(Lnet/minecraft/item/ItemStack;IIZLnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "INVOKE",
			target = "Lfi/dy/masa/malilib/util/InventoryUtils;getStoredItems(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/collection/DefaultedList;"
		)
	)
	private static DefaultedList<ItemStack> modifyInventoryToCrafterSimpleInventory(ItemStack stackIn, int slotCount, Operation<DefaultedList<ItemStack>> original)
	{
		//如果是书架，那么强制slotCount为6
        if (stackIn.getItem() instanceof BlockItem blockItem)
		{
			if (blockItem.getBlock() instanceof ChiseledBookshelfBlock)
			{
				return original.call(stackIn, 6);//slotCount使用6强制覆盖
			}
		}
		
		return original.call(stackIn, slotCount);
	}
}
