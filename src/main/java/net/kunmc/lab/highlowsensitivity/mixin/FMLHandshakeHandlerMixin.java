package net.kunmc.lab.highlowsensitivity.mixin;

import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.FMLHandshakeMessages;
import net.minecraftforge.fml.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(FMLHandshakeHandler.class)
public class FMLHandshakeHandlerMixin {
    @Inject(method = "handleClientModListOnServer", at = @At(value = "RETURN"), remap = false)
    private void handleClientModListOnServer(FMLHandshakeMessages.C2SModListReply clientModList, Supplier<NetworkEvent.Context> c, CallbackInfo ci) {

        //  nm.disconnectedReason = new StringTextComponent("F.C.O.H");
        //   System.out.println(rm);
        //    System.out.println("test");
    }
}
