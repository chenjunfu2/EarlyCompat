package chenjunfu2.earlycompat.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class EarlyCompatPacket
{
    private final int id;
    private final NbtCompound nbt;

    public EarlyCompatPacket(int id, NbtCompound nbt)
	{
        this.id = id;
        this.nbt = nbt;
    }

    // 从网络字节流反序列化
    public EarlyCompatPacket(PacketByteBuf buf)
	{
        this(buf.readVarInt(), buf.readNbt());
    }

    // 序列化到网络字节流
    public void write(PacketByteBuf buf)
	{
        buf.writeVarInt(this.id);
        buf.writeNbt(this.nbt);
    }

    public int getPacketId()
	{
		return this.id;
	}
	
    public NbtCompound getNbt()
	{
		return this.nbt;
	}
}