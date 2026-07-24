package chenjunfu2.earlycompat.mixin.network;

import chenjunfu2.earlycompat.network.EarlyCompatNetwork;
import me.fallenbreath.fanetlib.api.packet.FanetlibPacketRegistrationCenter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FanetlibPacketRegistrationCenter.class)
public class FanetlibPacketRegistrationCenterMixin_Newtork
{
	@Inject(method = "common", at = @At("HEAD"), remap = false)
    private static void register(CallbackInfo ci)
    {
        EarlyCompatNetwork.initPackets();
    }
}
