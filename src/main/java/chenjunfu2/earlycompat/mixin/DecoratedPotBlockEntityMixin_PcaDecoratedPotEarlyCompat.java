package chenjunfu2.earlycompat.mixin;

import com.plusls.carpet.PluslsCarpetAdditionReference;
import com.plusls.carpet.PluslsCarpetAdditionSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin_PcaDecoratedPotEarlyCompat extends BlockEntity
{
	public DecoratedPotBlockEntityMixin_PcaDecoratedPotEarlyCompat(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	@Override
	@Intrinsic
	public void markDirty()// 正常情况下 DecoratedPotEarly 会进行实现
	{
		super.markDirty();
	}
	
	@SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference", "target"})
	@Inject(method = "markDirty()V", at = @At(value = "RETURN"))
	private void PcaCallback(CallbackInfo info)
	{
		if (PluslsCarpetAdditionSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this))
		{
			PluslsCarpetAdditionReference.getLogger().debug("update DecoratedPotBlockEntity: {}", this.pos);
		}
	}
}