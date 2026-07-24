package chenjunfu2.earlycompat.mixin.network;

import chenjunfu2.earlycompat.EarlyCompat;
import chenjunfu2.earlycompat.network.EarlyCompatC2ServerHandler;
import chenjunfu2.earlycompat.network.EarlyCompatNetwork;
import chenjunfu2.earlycompat.network.EarlyCompatPacket;
import me.fallenbreath.fanetlib.api.packet.FanetlibPacketRegistrationCenter;
import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;
import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FanetlibPacketRegistrationCenter.class)
public class FanetlibPacketRegistrationCenterMixin_Server_Network
{
	@Inject(method = "c2s", at = @At("HEAD"), remap = false)
    private static void register(CallbackInfo ci)
    {
		EarlyCompat.LOGGER.info("MAIN mixin register called!");
        FanetlibPackets.registerC2S(//server
			EarlyCompatNetwork.PACKET_TYPE,
			PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
			(packet, ctx) -> EarlyCompatC2ServerHandler.handle(packet, ctx.getPlayer())
		);
    }
}
