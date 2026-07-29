package chenjunfu2.earlycompat.mixin.InventoryPreviewFix.Malilib;

import chenjunfu2.earlycompat.util.CrafterSimpleInventory;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.InventoryOverlay;
import net.chenjunfu2.block.CrafterBlock;
import net.chenjunfu2.block.entity.CrafterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryOverlay.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryOverlayMixin_MalilibCrafterEarlyCompat
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
		if(inv instanceof CrafterBlockEntity)
		{
			return InventoryOverlay.InventoryRenderType.DISPENSER;
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
			if (block instanceof CrafterBlock)
			{
				return InventoryOverlay.InventoryRenderType.DISPENSER;
			}
		}
		
		return original;
	}
	
	@Unique
	private static final Identifier earlycompat$CRAFTER_DISABLED_SLOT_TEXTURE = new Identifier("textures/gui/container/crafter/disabled_slot.png");
	
	@Inject
	(
		method = "Lfi/dy/masa/malilib/render/InventoryOverlay;renderInventoryStacks(Lfi/dy/masa/malilib/render/InventoryOverlay$InventoryRenderType;Lnet/minecraft/inventory/Inventory;IIIIILnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/DrawContext;)V",
		at = @At
		(
			value = "HEAD"
		)
	)
	private static void drawCrafterDisableSlots(InventoryOverlay.InventoryRenderType type, Inventory inv, int startX, int startY, int slotsPerRow, int startSlot, int maxSlots, MinecraftClient mc, DrawContext drawContext, CallbackInfo ci)
	{
		//绘制合成器锁定槽位
		if(inv instanceof CrafterBlockEntity crafter)
		{
			//绘制禁用槽位
			for (int i = 0; i < crafter.size(); i++)
			{
				int row = i / 3;
				int col = i % 3;
				int sx = startX - 1 + col * 18;
				int sy = startY - 1 + row * 18;
				if (crafter.isSlotDisabled(i))
				{
					drawContext.drawTexture(earlycompat$CRAFTER_DISABLED_SLOT_TEXTURE, sx, sy, 0, 0, 18, 18);
				}
			}
		}
		else if(inv instanceof CrafterSimpleInventory crafterInv)
		{
			for (int i = 0; i < crafterInv.size(); i++)
			{
				int row = i / 3;
				int col = i % 3;
				int sx = startX - 1 + col * 18;
				int sy = startY - 1 + row * 18;
				if (crafterInv.isSlotDisabled(i))
				{
					drawContext.drawTexture(earlycompat$CRAFTER_DISABLED_SLOT_TEXTURE, sx, sy, 0, 0, 18, 18);
				}
			}
		}
	}
}
