package chenjunfu2.earlycompat.client.mixin;

import chenjunfu2.earlycompat.EarlyCompat;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import com.plusls.MasaGadget.util.PcaSyncProtocol;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PcaSyncProtocol.class)
@Environment(EnvType.CLIENT)
public abstract class PcaSyncProtocolMixin_MasaBugFix
{
	@ModifyVariable
	(
		method = "updateBlockEntityHandler(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/network/PacketByteBuf;Lnet/fabricmc/fabric/api/networking/v1/PacketSender;)V",
		at =
		@At
		(
			value = "INVOKE",
			target = "Ltop/hendrixshen/magiclib/api/compat/minecraft/world/level/block/BlockEntityCompat;of(Lnet/minecraft/block/entity/BlockEntity;)Ltop/hendrixshen/magiclib/api/compat/minecraft/world/level/block/BlockEntityCompat;",
			shift = At.Shift.BEFORE
		),
		name = "tag"
	)
	private static NbtCompound updateBlockEntityHandlerBugFix(NbtCompound tag, @Local(name = "blockEntity") BlockEntity blockEntity)
	{
		//清理物品栏
		if (blockEntity instanceof Inventory inventory)
		{
			if(blockEntity instanceof DecoratedPotBlockEntity && EarlyCompat.isDecoratedPotEarlyAvailable)
			{
				if (!tag.contains("item", 10))
				{
					tag.put("item", new NbtCompound());//插入空段以清空容器
				}
			}
			else if(blockEntity instanceof JukeboxBlockEntity)
			{
				if (!tag.contains("RecordItem", 10))
				{
					tag.put("RecordItem", new NbtCompound());//插入空段以清空容器
				}
			}
		}
		
		return tag;
	}
}
