package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.api.datapack.provider.FileVerifier;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Run some final verfications at the end.
 */
public class PostProcessVerification implements DataProvider {

    private final FileVerifier verifier;
    private final List<Consumer<FileVerifier>> verifications = new ArrayList<>();

    public PostProcessVerification(FileVerifier verifier) {
        this.verifier = verifier;
    }

    public void add(Consumer<FileVerifier> consumer) {
        this.verifications.add(consumer);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf(this.verifications.stream().map(cons -> CompletableFuture.runAsync(() -> cons.accept(this.verifier)))
                .toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Verifier";
    }
}
