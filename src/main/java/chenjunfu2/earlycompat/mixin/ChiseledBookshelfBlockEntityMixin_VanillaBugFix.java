package chenjunfu2.earlycompat.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChiseledBookshelfBlockEntity.class)
public class ChiseledBookshelfBlockEntityMixin_VanillaBugFix extends BlockEntity
{
	public ChiseledBookshelfBlockEntityMixin_VanillaBugFix(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	@Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At(value = "RETURN"))
	void removeStackMarkDirtyFix(int slot, int amount, CallbackInfoReturnable<ItemStack> cir)
	{
		this.markDirty();
	}
	
	@Inject(method = "setStack(ILnet/minecraft/item/ItemStack;)V", at = @At(value = "RETURN"))
	void setStackMarkDirtyFix(int slot, ItemStack stack, CallbackInfo ci)
	{
		this.markDirty();
	}
}
