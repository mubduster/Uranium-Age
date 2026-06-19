package uranium;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SlotFilter extends Slot {
    private final Predicate<ItemStack> filter;

    public SlotFilter (Container container, int slot, int x, int y, Predicate<ItemStack> filter ) {
        super (container, slot, x, y);
        this.filter = filter;
    }

    @Override
    public boolean mayPlace (ItemStack stack){
        return filter.test(stack);
    }
}
