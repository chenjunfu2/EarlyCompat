package chenjunfu2.earlycompat.mixin.PcaProtocolFix.Masa;

import com.llamalad7.mixinextras.sugar.Local;
import com.plusls.MasaGadget.util.InventoryPreviewSyncDataUtil;
import com.plusls.MasaGadget.util.PcaSyncProtocol;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPreviewSyncDataUtil.class)
public abstract class InventoryPreviewSyncDataUtilMixin_MasaVanillaCompat
{
	@Inject
	(
		method = "onHitCallback(Lnet/minecraft/util/hit/HitResult;ZZ)V",
		cancellable = true,
		at =
		@At
		(
			value = "INVOKE_ASSIGN",
			target = "Ltop/hendrixshen/magiclib/util/collect/ValueContainer;orElse(Ljava/lang/Object;)Ljava/lang/Object;"
		)
	)
	private static void addNewSyncBlockEntity(HitResult hitResult, boolean oldStatus, boolean stateChanged, CallbackInfo ci, @Local(name = "pos")BlockPos pos, @Local(name = "blockEntity") Object blockEntity)
	{
		if(blockEntity == null)
		{
			ci.cancel();
		}
		
		if(blockEntity instanceof JukeboxBlockEntity || blockEntity instanceof ChiseledBookshelfBlockEntity)
		{
			PcaSyncProtocol.syncBlockEntity(pos);
			ci.cancel();
		}
		
		//其它情况，继续执行
	}
}
