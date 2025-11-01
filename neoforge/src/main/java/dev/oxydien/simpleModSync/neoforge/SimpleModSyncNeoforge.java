package dev.oxydien.simpleModSync.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import dev.oxydien.simpleModSync.SimpleModSync;
import java.nio.file.Path;

@Mod(SimpleModSync.MOD_ID)
public class SimpleModSyncNeoforge extends SimpleModSync {

    public SimpleModSyncNeoforge(IEventBus eventBus) {
        SimpleModSync.init(this);
        super.onInitialize();
    }

    @Override
    public Path getInstanceDir() {
        return FMLPaths.GAMEDIR.get();
    }
}