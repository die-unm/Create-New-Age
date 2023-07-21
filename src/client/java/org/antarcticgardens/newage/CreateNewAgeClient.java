package org.antarcticgardens.newage;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.antarcticgardens.newage.content.energiser.EnergiserBlockEntity;
import org.antarcticgardens.newage.renderers.energiser.EnergiserInstance;
import org.antarcticgardens.newage.renderers.energiser.EnergiserRenderer;

public class CreateNewAgeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(EnergiserBlockEntity.type, EnergiserRenderer::new);
		InstancedRenderRegistry.configure(EnergiserBlockEntity.type).factory(EnergiserInstance::new).apply();
	}
}