package org.antarcticgardens.newage.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Set;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Redirect(
            method = "configurePackRepository",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V")
            )
    )
    private static <E> boolean add(Set<E> set, E e) {
        if (e instanceof String s) {
            if (s.equals("create_new_age:create_new_age_monkey_edition"))
                return false;
        }

        set.add(e);
        return true;
    }
}
