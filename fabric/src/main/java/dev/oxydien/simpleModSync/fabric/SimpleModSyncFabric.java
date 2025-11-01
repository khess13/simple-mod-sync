package dev.oxydien.simpleModSync.fabric;

import dev.oxydien.simpleModSync.SimpleModSync;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class SimpleModSyncFabric extends SimpleModSync implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleModSync.init(this);
        super.onInitialize();
    }

    @Override
    public Path getInstanceDir() {
        return FabricLoader.getInstance().getGameDir();
    }
}
