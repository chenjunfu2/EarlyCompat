package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.accessor.PlaceStateAccessor;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin_VanillaProtocolCompat implements PlaceStateAccessor
{
	@WrapOperation
	(
		method = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;postPlacement(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)Z")
	)
	boolean replaceItemStack(BlockItem instance, BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState state, Operation<Boolean> original,
		@Local(ordinal = 1) ItemPlacementContext itemPlacementContext,
		@Local(ordinal = 0) BlockState blockState)
	{
		if (!(player instanceof ServerPlayerEntity serverPlayerEntity) ||
			!(EarlyCompatC2ServerHandler.isExtraProtocolPlayer(serverPlayerEntity)))//玩家没有扩展协议或不是服务端
		{
			return original.call(instance, pos, world, player,stack, state);//原封不动返回
		}
		
		if(!(blockState.getBlock() instanceof ItemStackProtocolDataAdapter itemStackProtocolDataAdapter))
		{
			return original.call(instance, pos, world, player,stack, state);//原封不动返回
		}
		
		double relativeHitZ = getRelativeHitZ(itemPlacementContext.getHitPos(),itemPlacementContext.getBlockPos());
		if(!isProtocol(relativeHitZ))
		{
			return original.call(instance, pos, world, player,stack, state);//原封不动返回
		}
		
		//获取协议值
		int protocolAdditionValue = decodeProtocolValueFromHitDim(relativeHitZ);
		ItemStack newStack = itemStackProtocolDataAdapter.earlycompat$fromProtocolValueAddition(protocolAdditionValue, stack);
		
		//使用修改的stack调用
		return original.call(instance, pos, world, player, newStack, state);
	}
	
	@Unique
	private static boolean earlycompat$isEasyPlaceState = false;
	
	@Unique
	private static long earlycompat$placeProperty = 0;
	
	@Override
	public boolean earlycompat$isEasyPlaceState()
	{
		return earlycompat$isEasyPlaceState;
	}
	
	@Override
	public long earlycompat$placeProperty()
	{
		return earlycompat$placeProperty;
	}
	
	@Override
	public void earlycompat$placeProperty(long val)
	{
		earlycompat$placeProperty = val;
	}

	@Inject
	(
		method = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
		at = @At(value = "HEAD")
	)
	void setPlaceState(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir)
	{
		if(isProtocol(getRelativeHitX(context.getHitPos(), context.getBlockPos())))
		{
			earlycompat$isEasyPlaceState = true;
		}
	}
	
	@Inject
	(
		method = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
		at = @At(value = "RETURN")
	)
	void clearPlaceState(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir)
	{
		earlycompat$isEasyPlaceState = false;
		earlycompat$placeProperty = 0;
	}
	
}
