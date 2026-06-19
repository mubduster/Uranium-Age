package uranium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uranium.ModItems;
import uranium.UraniumAge;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void handleReactorFuel(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity entity, CallbackInfo ci) {
        if (level == null || level.isClientSide()) return;

        ContainerData furnaceData = ((AbstractFurnaceBlockEntityAccessor) entity).getContainerData();
        int litTime = furnaceData.get(0);
        ItemStack fuelStack = entity.getItem(1);

        AbstractFurnaceBlockEntityMixin access = (AbstractFurnaceBlockEntityMixin)(Object)entity;
        boolean isBurning = litTime > 0;
        
        if ((!isBurning || litTime <= 1) && canAcceptFuel(entity)){
            if (fuelStack.is(ModItems.PORTABLE_URANIUM_REACTOR)){
                int energyLeft = fuelStack.getOrDefault(UraniumAge.REACTOR_ENERGY, 500 * 20);
                if (energyLeft > 0){
                    int dynamicBurn = Math.min(energyLeft, 100);

                    furnaceData.set(0, dynamicBurn);
                    furnaceData.set(1, dynamicBurn);

                    int newEnergy = energyLeft - dynamicBurn;
                    if (newEnergy <= 0) {
                        entity.setItem(1, new ItemStack(ModItems.PORTABLE_REACTOR_BODY));
                    }else {
                        fuelStack.set(UraniumAge.REACTOR_ENERGY, newEnergy);
                    }

                    entity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }else if (fuelStack.is(ModItems.PORTABLE_URANIUM_BLOCK_REACTOR)){
                int energyLeft = fuelStack.getOrDefault(UraniumAge.REACTOR_ENERGY, 1400 * 20);
                if (energyLeft > 0){
                    int dynamicBurn = Math.min(energyLeft, 200);

                    furnaceData.set(0, dynamicBurn);
                    furnaceData.set(1, dynamicBurn);
                    int newEnergy = energyLeft - dynamicBurn;
                    if(newEnergy <= 0) {
                        entity.setItem(1, new ItemStack(ModItems.PORTABLE_REACTOR_BODY));
                    }else {
                        fuelStack.set(UraniumAge.REACTOR_ENERGY, newEnergy);
                    }

                    entity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }

    }

    @Unique
    private static boolean canAcceptFuel(AbstractFurnaceBlockEntity entity) {
        ItemStack input = entity.getItem(1);
        return !input.isEmpty();
    }
}
