package uranium;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.swing.*;
import java.util.function.Function;

public class ModItems {
    public static <T extends Item>T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
     ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, name));

     T item = itemFactory.apply(settings.setId(itemKey));

     Registry.register(BuiltInRegistries.ITEM, itemKey, item);

     return item;
    }

    public static final Item URANIUM_INGOT = register(
            "uranium_ingot",
            Item::new,
            new Item.Properties()
    );
    public static final Item RAW_URANIUM = register(
            "raw_uranium",
            Item::new,
            new Item.Properties()
    );
    public static final Item BORON_INGOT = register(
            "boron_ingot",
            Item::new,
            new Item.Properties()
    );
    public static final Item BORON_ROD = register(
            "boron_rod",
            Item::new,
            new Item.Properties()
    );
    public static final Item LEAD_DUST = register(
            "lead_dust",
            Item::new,
            new Item.Properties()
    );
    public static final Item LEAD_DUST_CLUMP = register(
            "lead_dust_clump",
            Item::new,
            new Item.Properties()
    );
    public static final Item LEAD_INGOT = register(
            "lead_ingot",
            Item::new,
            new Item.Properties()
    );

    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, "creative_tab")
    );
    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.RAW_URANIUM))
            .title(Component.translatable("creativeTab.uranium-age"))
            .displayItems((prams, output) -> {
                output.accept(ModBlocks.URANIUM_ORE);
                output.accept(ModBlocks.DEEPSLATE_URANIUM_ORE);
                output.accept(ModItems.RAW_URANIUM);
                output.accept(ModBlocks.RAW_URANIUM_BLOCK);
                output.accept(ModItems.URANIUM_INGOT);
                output.accept(ModBlocks.URANIUM_BLOCK);
                output.accept(ModBlocks.TARNISHED_URANIUM_BLOCK);
                output.accept(ModBlocks.BORON_ORE);
                output.accept(ModBlocks.DEEPSLATE_BORON_ORE);
                output.accept(ModItems.BORON_INGOT);
                output.accept(ModBlocks.BORON_BLOCK);
                output.accept(ModItems.BORON_ROD);
                output.accept(ModItems.LEAD_DUST);
                output.accept(ModItems.LEAD_DUST_CLUMP);
                output.accept(ModItems.LEAD_INGOT);
                output.accept(ModBlocks.LEAD_ORE);
                output.accept(ModBlocks.DEEPSLATE_LEAD_ORE);
                output.accept(ModBlocks.LEAD_BLOCK);
            })
            .build();

    public static void initialize(){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
    }
}
