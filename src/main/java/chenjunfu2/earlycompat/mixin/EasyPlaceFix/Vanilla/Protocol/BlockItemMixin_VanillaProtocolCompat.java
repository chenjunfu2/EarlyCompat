package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla.Protocol;

import chenjunfu2.earlycompat.network.EarlyCompatC2ServerHandler;
import chenjunfu2.earlycompat.util.ItemStackProtocolDataAdapter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin_VanillaProtocolCompat
{
	@WrapOperation
	(
		method = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getStack()Lnet/minecraft/item/ItemStack;")
	)
	ItemStack replaceItemStack(ItemPlacementContext instance, Operation<ItemStack> original, @Local(name = "blockState") BlockState blockState, @Local(name = "playerEntity") PlayerEntity playerEntity)
	{
		ItemStack stack = original.call(instance);//先调用
		
		if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity) ||
			!(EarlyCompatC2ServerHandler.isExtraProtocolPlayer(serverPlayerEntity)))//玩家没有扩展协议或不是服务端
		{
			return stack;//原封不动返回
		}
		
		if(!(blockState.getBlock() instanceof ItemStackProtocolDataAdapter itemStackProtocolDataAdapter))
		{
			return stack;//原封不动返回
		}
		
		double relativeHitZ = getRelativeHitZ(instance.getHitPos(),instance.getBlockPos());
		if(!isProtocol(relativeHitZ))
		{
			return stack;//原封不动返回
		}
		
		//获取协议值
		int protocolAdditionValue = decodeProtocolValueFromHitDim(relativeHitZ);
		
		return itemStackProtocolDataAdapter.earlycompat$fromProtocolValueAddition(protocolAdditionValue, stack);
	}

}
