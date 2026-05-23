package uranium;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

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


    public static void initialize(){
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> {
            creativeTab.accept(ModItems.RAW_URANIUM);
            creativeTab.accept(ModItems.URANIUM_INGOT);
        });
    }
}
