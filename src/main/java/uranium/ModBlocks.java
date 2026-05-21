package uranium;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory , BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem){
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name){
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, name));
    }
    private static ResourceKey<Item> keyOfItem(String name){
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, name));
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //create block
    public static final Block URANIUM_BLOCK = register(
            "uranium_block",
            Block::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.COPPER)
                    .strength(2.0f, 8f),
            true
    );
    public static final Block URANIUM_ORE = register(
            "uranium_ore",
            Block::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 6),
            true
    );
    public static final Block TARNISHED_URANIUM_BLOCK = register(
            "tarnished_uranium_block",
            Block::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.CALCITE)
                    .strength(1.3f, 4.0f),
            true
    );
    public static final Block YELLOW_CAKE_URANIUM_BLOCK = register(
            "yellow_cake_uranium_block",
            Block::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.BASALT)
                    .strength(0.8f, 2.0f),
            true
    );

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Add item to inventory and initialize the file
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> {
            creativeTab.accept(ModBlocks.URANIUM_BLOCK.asItem());
            creativeTab.accept(ModBlocks.TARNISHED_URANIUM_BLOCK.asItem());
            creativeTab.accept(ModBlocks.YELLOW_CAKE_URANIUM_BLOCK.asItem());
            creativeTab.accept(ModBlocks.URANIUM_ORE.asItem());
        });
    }
}
