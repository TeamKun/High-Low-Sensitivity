package net.kunmc.lab.highlowsensitivity.mixin;


import net.kunmc.lab.highlowsensitivity.data.SensitivityManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.storage.FolderName;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "saveAllChunks", at = @At("HEAD"), cancellable = true)
    private void saveAllChunks(boolean p_213211_1_, boolean p_213211_2_, boolean p_213211_3_, CallbackInfoReturnable<Boolean> cir) {
        SensitivityManager.getInstance().saveFile(((MinecraftServer) (Object) this).getWorldPath(FolderName.ROOT).resolve("data").toFile());
    }

    @Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;",ordinal = 1), cancellable = true)
    private void stopServer(CallbackInfo ci) {
        SensitivityManager.getInstance().unload();
    }

    @Inject(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;setLevel(Lnet/minecraft/world/server/ServerWorld;)V"), cancellable = true)
    private void createLevels(IChunkStatusListener p_240787_1_, CallbackInfo ci) {
        SensitivityManager.getInstance().load(((MinecraftServer) (Object) this).getWorldPath(FolderName.ROOT).resolve("data").toFile());
    }
}
