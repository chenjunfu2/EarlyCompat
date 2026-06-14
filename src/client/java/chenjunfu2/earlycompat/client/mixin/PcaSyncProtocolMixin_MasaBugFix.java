package chenjunfu2.earlycompat.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import com.plusls.MasaGadget.util.PcaSyncProtocol;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PcaSyncProtocol.class)
@Environment(EnvType.CLIENT)
public abstract class PcaSyncProtocolMixin_MasaBugFix
{
	@Inject
	(
		method = "updateBlockEntityHandler(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/network/PacketByteBuf;Lnet/fabricmc/fabric/api/networking/v1/PacketSender;)V",
		at =
		@At
		(
			value = "INVOKE",
			target = "Ltop/hendrixshen/magiclib/api/compat/minecraft/world/level/block/BlockEntityCompat;of(Lnet/minecraft/block/entity/BlockEntity;)Ltop/hendrixshen/magiclib/api/compat/minecraft/world/level/block/BlockEntityCompat;",
			shift = At.Shift.BEFORE
		)
	)
	private static void updateBlockEntityHandlerBugFix(net.minecraft.client.MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender, CallbackInfo ci, @Local(name = "blockEntity") BlockEntity blockEntity)
	{
		//先清理物品栏
		if (blockEntity instanceof Inventory inventory)
		{
			inventory.clear();
		}
	}
	
}
