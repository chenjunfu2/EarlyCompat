package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin_VanillaProtocolCompat extends WallMountedBlock implements BlockProtocolStateAdapter
{
	public LeverBlockMixin_VanillaProtocolCompat(Settings settings)
	{
		super(settings);
	}
	
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int faceOridinal = fromState.get(LeverBlock.FACE).ordinal();
		boolean isPowered = fromState.get(LeverBlock.POWERED);
		int bits =
			((faceOridinal & 0b0000_0011) << 4) |
			(isPowered ? 0b0100_0000 : 0b0000_0000);
		return protocolValue | bits;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		int faceOridinal = ((extraProtocolValue & 0b0011_0000) >>> 4) % 3;//0~2
		boolean isPowered = (extraProtocolValue & 0b0100_0000) == 0b0100_0000;
		return fromState
			.with(LeverBlock.FACE, WallMountLocation.values()[faceOridinal])
			.with(LeverBlock.POWERED, isPowered);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
	
	@Override
	@Intrinsic
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack)
	{
		super.onPlaced(world, pos, state, placer, itemStack);
    }
	
	@SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference", "target"})
	@Inject
	(
          method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V",
          at = @At("HEAD")
    )
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci)
	{
        if (!world.isClient && state.get(LeverBlock.POWERED))//更新一下附着方块的临近
		{
            world.updateNeighborsAlways(pos.offset(getDirection(state).getOpposite()), (Block)(Object)this);
        }
    }
}
