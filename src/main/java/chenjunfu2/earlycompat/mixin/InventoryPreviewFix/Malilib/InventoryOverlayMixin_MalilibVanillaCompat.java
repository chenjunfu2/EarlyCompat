package chenjunfu2.earlycompat.mixin.InventoryPreviewFix.Malilib;

import chenjunfu2.earlycompat.accessor.InventoryRenderTypeAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.InventoryOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryOverlay.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryOverlayMixin_MalilibVanillaCompat
{
	@ModifyReturnValue
	(
		method = "Lfi/dy/masa/malilib/render/InventoryOverlay;getInventoryType(Lnet/minecraft/inventory/Inventory;)Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;",
		at = @At
		(
			value = "RETURN",
			ordinal = 7
		)
	)
	private static InventoryOverlay.InventoryRenderType modifyInventoryTypeInv(InventoryOverlay.InventoryRenderType original, @Local(name = "inv") Inventory inv)
	{
		if(inv instanceof ChiseledBookshelfBlockEntity)
		{
			return InventoryRenderTypeAccessor.CHISELEDBOOKSHELF;
		}
		
		return original;
	}
	
	@ModifyReturnValue
	(
		method = "Lfi/dy/masa/malilib/render/InventoryOverlay;getInventoryType(Lnet/minecraft/item/ItemStack;)Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;",
		at = @At
		(
			value = "RETURN",
			ordinal = 5
		)
	)
	private static InventoryOverlay.InventoryRenderType modifyInventoryTypeStack(InventoryOverlay.InventoryRenderType original, @Local(name = "stack") ItemStack stack)
	{
		Item item = stack.getItem();
		if(item instanceof BlockItem blockItem)
		{
			Block block = blockItem.getBlock();
			if (block instanceof ChiseledBookshelfBlock)
			{
				return InventoryRenderTypeAccessor.CHISELEDBOOKSHELF;
			}
		}
		
		return original;
	}
	
	@ModifyReturnValue
	(
		method = "Lfi/dy/masa/malilib/render/InventoryOverlay;getInventoryPropsTemp(Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;I)Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryProperties;",
		at = @At
		(
			value = "RETURN",
			ordinal = 0
		),
		remap = false
	)
	private static InventoryOverlay.InventoryProperties modifyInventoryOverlay(InventoryOverlay.InventoryProperties original, @Local(name = "type") InventoryOverlay.InventoryRenderType type)
	{
		if(type == InventoryRenderTypeAccessor.CHISELEDBOOKSHELF)
		{
			original.slotsPerRow = 3;// 修改为一行 = 3 个
			original.width = 3 * 18 + 14;//重新设置宽度 = 68 像素
			original.height = 2 * 18 + 14;//重新设置高度 = 50 像素
		}
		
		return original;
	}
}
