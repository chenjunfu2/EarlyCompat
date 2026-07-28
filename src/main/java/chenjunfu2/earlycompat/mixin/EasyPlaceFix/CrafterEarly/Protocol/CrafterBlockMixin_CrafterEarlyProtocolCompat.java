package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CrafterEarly.Protocol;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.ItemStackProtocolDataAdapter;
import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(CrafterBlock.class)
public abstract class CrafterBlockMixin_CrafterEarlyProtocolCompat implements BlockProtocolStateAdapter, ItemStackProtocolDataAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		int orientationOrdinal = fromState.get(Properties.ORIENTATION).ordinal();
		return orientationOrdinal & 0b0000_1111;
	}
	
	@Override
	public @NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState)
	{
		//低4bit存储12个方向
		int orientationOrdinal = (extraProtocolValue & 0b0000_1111) % 12;//0~11
		return fromState.with(Properties.ORIENTATION, JigsawOrientation.values()[orientationOrdinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
	
	@Override
	public int earlycompat$toProtocolValueAddition(ItemStack fromStack)
	{
		NbtCompound tagBlockEntity = fromStack.getSubNbt("BlockEntityTag");
		if(tagBlockEntity == null)
		{
			return 0;//全不锁
		}
		
		//9个bit存储9个槽位锁定状态
		if(!tagBlockEntity.contains("disabled_slots", NbtElement.INT_ARRAY_TYPE))
		{
			return 0;//全不锁
		}
		int[] dis_slots = tagBlockEntity.getIntArray("disabled_slots");

		int bits = 0;
		int mask = 1;
		for(int slot_idx : dis_slots)
		{
			if(slot_idx > -1 && slot_idx < 9)
			{
				bits |= (mask << slot_idx);
			}
		}
		
		return bits & 0b0001_1111_1111;//9bit
	}
	
	@Override
	public @NotNull ItemStack earlycompat$fromProtocolValueAddition(int extraProtocolValue, ItemStack fromStack)
	{
		int dis_count = Integer.bitCount(extraProtocolValue & 0b0001_1111_1111);//9bit
		if(dis_count == 0)
		{
			return fromStack;//啥都没有
		}
		
		//务必拷贝返回，禁止修改原对象
		NbtCompound tagBlockEntity = fromStack.getSubNbt("BlockEntityTag");//尝试获取be
		if(tagBlockEntity != null && tagBlockEntity.contains("disabled_slots"))
		{
			return fromStack;//已有数据，不可覆盖，回退
		}
		
		//拷贝并修改
		ItemStack stackCopy = fromStack.copy();
		tagBlockEntity = stackCopy.getSubNbt("BlockEntityTag");
		if(tagBlockEntity == null)
		{
			NbtCompound nbt = stackCopy.getNbt();
			if(nbt == null)
			{
				nbt = new NbtCompound();
				stackCopy.setNbt(nbt);
			}
			
			tagBlockEntity = new NbtCompound();
			nbt.put("BlockEntityTag", tagBlockEntity);
		}
		
		int[] dis_slots = new int[dis_count];
		
		int slot_idx = 0;
		int mask = 1;
		for(int i = 0; i < 9; ++i)
		{
			if((extraProtocolValue & mask) == mask)
			{
				dis_slots[slot_idx++] = i;
			}
			mask <<= 1;
		}
		
		//插入并返回
		tagBlockEntity.putIntArray("disabled_slots", dis_slots);
		return stackCopy;
	}
}
