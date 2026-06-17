package chenjunfu2.earlycompat.mixin.Vanilla;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin_VanillaBugFix extends BlockEntity
{
	public JukeboxBlockEntityMixin_VanillaBugFix(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	@Shadow
	@Final
	private DefaultedList<ItemStack> inventory;
	
	@Invoker("stopPlaying")
	public abstract void earlycompat_shadow$stopPlaying();
	
	@Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;getBoolean(Ljava/lang/String;)Z"))
	void readNbtBugFix(NbtCompound nbt, CallbackInfo ci)
	{
		if(!nbt.contains("RecordItem", NbtElement.COMPOUND_TYPE))//相当于原先的else，如果不存在，那么清空（与1.21代码同步）
		{
			if(((JukeboxBlockEntity)(Object)this).isPlayingRecord())//如果正在播放
			{
				earlycompat_shadow$stopPlaying();//先停止播放
			}
			inventory.set(0, ItemStack.EMPTY);
		}
	}
}
