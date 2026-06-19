package uranium;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;

public class ModItems {
    public static <T extends Item>T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------
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
    public static final Item LEAD_HELMET = register(
            "lead_helmet",
            Item::new,
            new Item.Properties().humanoidArmor(LeadArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(LeadArmorMaterial.BASE_DURABILITY))
    );
    public static final Item LEAD_CHESTPLATE = register(
            "lead_chestplate",
            Item::new,
            new Item.Properties().humanoidArmor(LeadArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(LeadArmorMaterial.BASE_DURABILITY))
    );
    public static final Item LEAD_LEGGINGS = register(
            "lead_leggings",
            Item::new,
            new Item.Properties().humanoidArmor(LeadArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(LeadArmorMaterial.BASE_DURABILITY))
    );
    public static final Item LEAD_BOOTS = register(
            "lead_boots",
            Item::new,
            new Item.Properties().humanoidArmor(LeadArmorMaterial.INSTANCE, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(LeadArmorMaterial.BASE_DURABILITY))
    );

    public static final Item PORTABLE_URANIUM_REACTOR = register(
            "portable_uranium_reactor",
            Item::new,
            new Item.Properties()
    );
    public static final Item PORTABLE_URANIUM_BLOCK_REACTOR = register(
            "portable_uranium_block_reactor",
            Item::new,
            new Item.Properties()
    );
    public static final Item PORTABLE_REACTOR_BODY = register(
            "portable_reactor_body",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_BLOCK_OUTER_CONCRETE_BODY = register(
            "reactor_block_outer_concrete_body",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_BLOCK_BODY = register(
            "reactor_block_body",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_BLOCK_CONTROLS = register(
            "reactor_block_controls",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_CORE_SMALL = register(
            "reactor_core_small",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_CORE_LARGE = register(
            "reactor_core_large",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_BOILER_SMALL = register(
            "reactor_boiler_small",
            Item::new,
            new Item.Properties()
    );
    public static final Item REACTOR_BOILER_LARGE =register(
            "reactor_boiler_large",
            Item::new,
            new Item.Properties()
    );
//---------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------------------------------------------------
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
                output.accept(ModItems.LEAD_HELMET);
                output.accept(ModItems.LEAD_CHESTPLATE);
                output.accept(ModItems.LEAD_LEGGINGS);
                output.accept(ModItems.LEAD_BOOTS);
                output.accept(ModItems.PORTABLE_URANIUM_REACTOR);
                output.accept(ModItems.PORTABLE_URANIUM_BLOCK_REACTOR);
                output.accept(ModBlocks.REACTOR_BLOCK);
                output.accept(ModItems.REACTOR_BLOCK_CONTROLS);
                output.accept(ModItems.PORTABLE_REACTOR_BODY);
                output.accept(ModItems.REACTOR_BLOCK_OUTER_CONCRETE_BODY);
                output.accept(ModItems.REACTOR_BLOCK_BODY);
                output.accept(ModItems.REACTOR_CORE_SMALL);
                output.accept(ModItems.REACTOR_CORE_LARGE);
                output.accept(ModItems.REACTOR_BOILER_SMALL);
                output.accept(ModItems.REACTOR_BOILER_LARGE);
            })
            .build();
//-------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void initialize(){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);

        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.PORTABLE_URANIUM_REACTOR, 500 * 20);
            builder.add(ModItems.PORTABLE_URANIUM_BLOCK_REACTOR, 1400 * 20);
        });

    }
}
