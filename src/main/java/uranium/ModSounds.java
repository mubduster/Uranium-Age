package uranium;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    private ModSounds(){

    }

    public static final SoundEvent REACTOR_HUM = registerSound("reactor_hum");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(UraniumAge.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize(){
        UraniumAge.LOGGER.info("Sounds for "+ UraniumAge.MOD_ID +" have been registered!");
    }
}