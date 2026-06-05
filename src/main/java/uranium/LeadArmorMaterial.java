package uranium;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class LeadArmorMaterial {
    public static final int BASE_DURABILITY = 13;

    public static final ResourceKey<EquipmentAsset> LEAD_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, "lead_armor"));

    public static final TagKey<Item> REPAIRS_LEAD_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, "repairs_lead_armor"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                ArmorType.HELMET, 2,
                ArmorType.CHESTPLATE, 5,
                ArmorType.LEGGINGS, 4,
                ArmorType.BOOTS, 2
            ),
            5,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            REPAIRS_LEAD_ARMOR,
            LEAD_ARMOR_MATERIAL_KEY
    );

}
