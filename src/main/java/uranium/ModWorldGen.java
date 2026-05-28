package uranium;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldGen {
    public static final ResourceKey<PlacedFeature> URANIUM_CAVE_BLOCK_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("uranium-age", "uranium_cave_block_placed")
    );
    public static final ResourceKey<PlacedFeature> BORON_CAVE_BLOCK_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("uranium-age", "boron_cave_block_placed")
    );
    public static void initialize() {}
}
