package chenjunfu2.earlycompat.network;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;

import java.util.function.Consumer;

public class EarlyCompatNetwork
{
// 通道名：用你的 MOD_ID + 路径
      private static final PacketId<EarlyCompatPacket> PACKET_TYPE =
          PacketId.of("earlycompat", "network");

      public static class C2S
	  {
          public static final int HI = 0;
      }

      public static class S2C
	  {
          public static final int HI = 0;
      }

      // 创建 C2S 包
      public static CustomPayloadC2SPacket createC2S(int packetId, Consumer<NbtCompound> builder)
	  {
          NbtCompound nbt = new NbtCompound();
          builder.accept(nbt);
          return FanetlibPackets.createC2S(PACKET_TYPE, new EarlyCompatPacket(packetId, nbt));
      }

      // 创建 S2C 包
      public static CustomPayloadS2CPacket createS2C(int packetId, Consumer<NbtCompound> builder)
	  {
          NbtCompound nbt = new NbtCompound();
          builder.accept(nbt);
          return FanetlibPackets.createS2C(PACKET_TYPE, new EarlyCompatPacket(packetId, nbt));
      }

      // 注册（由 Mixin 钩子调用）
      public static void initPackets() {
          FanetlibPackets.registerDual(
			  PACKET_TYPE,
              PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
              (p, c) -> EarlyCompatC2SHandler.handle(p, c.getPlayer()),
              (p, c) -> EarlyCompatS2CHandler.handle(p, c.getPlayer())
          );
      }
}
