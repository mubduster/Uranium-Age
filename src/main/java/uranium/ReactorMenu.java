package uranium;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;


public class ReactorMenu extends AbstractContainerMenu {
   private static final int SLOT_COUNT = 3;

   private static final int CONTAINER_START = 0;
   private static final int CONTAINER_END = SLOT_COUNT;
   private static final int INVENTORY_START = SLOT_COUNT;
   private static final int INVENTORY_END = SLOT_COUNT + Inventory.INVENTORY_SIZE;

   private static final int SLOT_START_X = 56;
   private static final int SLOT_START_Y = 17;
   private static final int INVENTORY_START_X = 8;
   private static final int INVENTORY_START_Y = 84;

   private final Container container;

   private final ContainerData data;

   public boolean isActive(){
       return data.get(0) == 1;
   }

   public ReactorMenu(final int containerId, final Inventory inventory) {
       this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(1));
   }

   public ReactorMenu(final int containerId, final Inventory inventory, final Container container, ContainerData data) {
       super(ModMenuType.REACTOR_BLOCK, containerId);
       checkContainerSize(container, SLOT_COUNT);
       this.container = container;
       this.data = data;

       container.startOpen(inventory.player);

       this.addReactorSlots();

       this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);

       this.addDataSlots(data);
   }

   private void addReactorSlots(){
       this.addSlot(new SlotFilter(this.container, 0, SLOT_START_X, SLOT_START_Y, stack -> stack.is(ModItems.BORON_ROD)));
       this.addSlot(new SlotFilter(this.container, 1, SLOT_START_X, SLOT_START_Y + 36, stack -> stack.is(ModItems.URANIUM_INGOT) || (stack.getItem() instanceof BlockItem bi && bi.getBlock() == ModBlocks.URANIUM_BLOCK)));
       this.addSlot(new SlotFilter(this.container, 2, SLOT_START_X + 80, SLOT_START_Y, stack -> stack.is(ModItems.BORON_INGOT)));
   };

   @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
       Slot slot = this.slots.get(slotIndex);

       if (!slot.hasItem()) {
           return ItemStack.EMPTY;
       }

       ItemStack stack = slot.getItem();
       ItemStack clicked = stack.copy();

       if (slotIndex < CONTAINER_END) {
           if (!this.moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, true)) {
               return ItemStack.EMPTY;
           }
       }else {
           if (!this.moveItemStackTo(stack, CONTAINER_START, CONTAINER_END, false)){
               return ItemStack.EMPTY;
           }
       }

       if (stack.isEmpty()){
           slot.setByPlayer(ItemStack.EMPTY);
       }else{
           slot.setChanged();
       }
       return clicked;
   }

   @Override
    public boolean stillValid(Player player){
       return this.container.stillValid(player);
   }

   @Override
    public void removed(Player player) {
       super.removed(player);
       this.container.stopOpen(player);
   }
}
