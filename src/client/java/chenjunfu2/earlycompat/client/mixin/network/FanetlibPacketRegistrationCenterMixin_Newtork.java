package chenjunfu2.earlycompat.client.mixin.network;

import chenjunfu2.earlycompat.client.network.EarlyCompatS2ClientHandler;
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
public class FanetlibPacketRegistrationCenterMixin_Newtork
{
	@Inject(method = "s2c", at = @At("HEAD"), remap = false)
    private static void register(CallbackInfo ci)
    {
		FanetlibPackets.registerS2C(//client
			EarlyCompatNetwork.PACKET_TYPE,
	    	PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
	    	(packet, ctx) -> EarlyCompatS2ClientHandler.handle(packet, ctx.getPlayer())
	    );
    }
}
