package uranium;

import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorBlockEntity extends BlockEntity implements ImplementedContainer, MenuProvider {
    public ReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.Reactor_Block_Entity, pos, state);
    }

    public static final int CONTAINER_SIZE = 3 * 3 ;
    private final NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public boolean stillValid(Player player){
        return Container.stillValidBlockEntity(this, player);
    }


    @Override
    @NonNull
    public Component getDisplayName(){
        return Component.translatable("block.uranium-age.reactor_block");
    }
    @Override
    public @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player){
        return new ReactorMenu(containerId, inventory, this, this.dataAccess);
    }

    private boolean active = false;

    private boolean wasActive = false;

    private int fuelTime = 0;

    public static java.util.function.Consumer<BlockPos> START_SOUND;
    public static java.util.function.Consumer<BlockPos> STOP_SOUND;

    public final ContainerData dataAccess = new ContainerData(){
        @Override
        public int get(int index){
            return index == 0 ? (active ? 1:0) : 0;
        }
        @Override
        public void set(int index, int value){
            if (index == 0) active = value == 1;
        }
        public int getCount(){
            return 1;
        }
    };

    public static void clientTick(Level level, BlockPos pos, BlockState state, ReactorBlockEntity entity){
        boolean active = state.getValue(ReactorBlock.ACTIVE);
        if (active && !entity.wasActive && START_SOUND != null){
            START_SOUND.accept(pos);
        } else if (!active && entity.wasActive && STOP_SOUND != null) {
            STOP_SOUND.accept(pos);
        }
        entity.wasActive = active;
    }
    public static void tick(Level level, BlockPos pos, BlockState state, ReactorBlockEntity entity){
        boolean hasAll = !entity.getItem(0).isEmpty() && !entity.getItem(1).isEmpty() && !entity.getItem(2).isEmpty();



        boolean currentlyActive = state.getValue(ReactorBlock.ACTIVE);
        boolean currentlyGlowing= state.getValue(ReactorBlock.GLOWING);

        BlockPos above = pos.above();

        if (hasAll && !currentlyActive){
            ItemStack uraniumStack = entity.getItem(1);

            if (uraniumStack.is(ModItems.URANIUM_INGOT)){
                entity.fuelTime = 900*20;
            } else if (uraniumStack.getItem() instanceof BlockItem bi && bi.getBlock() == ModBlocks.URANIUM_BLOCK){
                entity.fuelTime = 3600*20;
            }
            entity.getItem(0).shrink(1);
            entity.getItem(1).shrink(1);
            entity.setChanged();
        }

        boolean hadFuel = entity.fuelTime < 0;

        if (hasAll && entity.fuelTime == 0 && !currentlyActive){
            ItemStack uraniumStack = entity.getItem(1);
            entity.getItem(0).shrink(1);
            entity.getItem(1).shrink(1);
            entity.getItem(2).shrink(1);
            entity.setChanged();
            if (uraniumStack.is(ModItems.URANIUM_INGOT)){
                entity.fuelTime = 900*20;
            } else if (uraniumStack.getItem() instanceof BlockItem bi && bi.getBlock() == ModBlocks.URANIUM_BLOCK){
                entity.fuelTime = 3600*20;
            }
        }

        if (entity.fuelTime > 0){
            entity.fuelTime--;
        }

        boolean isRunning = entity.fuelTime > 0;

        if (hadFuel && !isRunning){
            entity.getItem(2).shrink(1);
            entity.setChanged();
        }

        boolean submerged = isRunning && Direction.stream()
                .filter(d -> d != Direction.UP && d != Direction.DOWN)
                .allMatch(d -> level.getFluidState(pos.relative(d)).is(FluidTags.WATER));

        if (isRunning != currentlyActive || submerged != currentlyGlowing){
            System.out.println("submerged "+submerged+" currentlyGlowing "+currentlyGlowing);
            level.setBlock(pos, state
                    .setValue(ReactorBlock.ACTIVE, isRunning)
                    .setValue(ReactorBlock.GLOWING, submerged),
                    Block.UPDATE_ALL);
            level.updateNeighborsAt(pos.above(), level.getBlockState(pos).getBlock());
        }

        entity.active = isRunning;

        if (level.getBlockEntity(above) instanceof AbstractFurnaceBlockEntity furnace){
            if (isRunning){
                furnace.litTimeRemaining = 200;
                furnace.litTotalTime= 200;
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input){
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
        fuelTime = input.getIntOr("fuelTime", 0);
    }
    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putInt("fuelTime", fuelTime);
    }
}
