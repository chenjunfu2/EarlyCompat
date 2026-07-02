package chenjunfu2.earlycompat.client.mixin.InventoryPreviewFix.Malilib;

import chenjunfu2.earlycompat.client.util.CrafterSimpleInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.RenderUtils;
import net.chenjunfu2.block.CrafterBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderUtils.class)
@Environment(EnvType.CLIENT)
public class RenderUtilsMixin_MalilibVanillaCompat
{
	@WrapOperation
	(
		method = "Lfi/dy/masa/malilib/render/RenderUtils;renderShulkerBoxPreview(Lnet/minecraft/item/ItemStack;IIZLnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "INVOKE",
			target = "Lfi/dy/masa/malilib/util/InventoryUtils;getAsInventory(Lnet/minecraft/util/collection/DefaultedList;)Lnet/minecraft/inventory/Inventory;"
		),
		remap = false
	)
	private static Inventory modifyInventoryToCrafterSimpleInventory(DefaultedList<ItemStack> items, Operation<Inventory> original, @Local(name = "stack") ItemStack stack)
	{
        if (!(stack.getItem() instanceof BlockItem blockItem))
		{
			return original.call(items);
		}

		if(!(blockItem.getBlock() instanceof CrafterBlock))
		{
			return original.call(items);
		}
		
		NbtCompound nbt = stack.getNbt();
		if(nbt == null)
		{
			return original.call(items);
		}
		
		return new CrafterSimpleInventory(items, nbt);
	}
}
