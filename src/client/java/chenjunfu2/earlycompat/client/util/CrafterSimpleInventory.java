package chenjunfu2.earlycompat.client.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;

public class CrafterSimpleInventory implements Inventory
{
	DefaultedList<ItemStack> inputStacks;
	protected final PropertyDelegate propertyDelegate;
	
	public CrafterSimpleInventory(DefaultedList<ItemStack> inputStacks, NbtCompound nbt)
	{
		this.inputStacks = inputStacks;
		this.propertyDelegate = new PropertyDelegate()
		{
            private final int[] disabledSlots = new int[9];
            private int triggered = 0;

            public int get(int index) {
                return index == 9 ? this.triggered : this.disabledSlots[index];
            }

            public void set(int index, int value)
			{
                if (index == 9)
				{
                    this.triggered = value;
                }
				else
				{
                    this.disabledSlots[index] = value;
                }

            }

            public int size()
			{
                return 10;
            }
        };
		
		
		int[] is = nbt.getIntArray("disabled_slots");

        for(int i = 0; i < 9; ++i)
		{
            this.propertyDelegate.set(i, 0);
        }

        for(int j : is)
		{
            if (this.canToggleSlot(j))
			{
                this.propertyDelegate.set(j, 1);
            }
        }

        this.propertyDelegate.set(9, nbt.getInt("triggered"));
	}
	
	private boolean canToggleSlot(int slot)
	{
        return slot > -1 && slot < 9 && ((ItemStack)this.inputStacks.get(slot)).isEmpty();
    }
	
    public boolean isSlotDisabled(int slot)
	{
        return slot >= 0 && slot < 9 && this.propertyDelegate.get(slot) == 1;
    }
	
	
	@Override
	public int size()
	{
        return 9;
    }
	
	@Override
	public boolean isEmpty()
	{
		return this.inputStacks.stream().allMatch(ItemStack::isEmpty);
	}
	
	@Override
	public ItemStack getStack(int slot)
	{
		return (ItemStack)this.inputStacks.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		return null;
	}
	
	@Override
	public ItemStack removeStack(int slot)
	{
		return null;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack)
	{
	
	}
	
	@Override
	public void markDirty()
	{
	
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return false;
	}
	
	@Override
	public void clear()
	{
	
	}
}
