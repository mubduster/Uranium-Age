package uranium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import uranium.ModSounds;
import uranium.ReactorBlock;
import uranium.client.mixin.GuiAccessor;

public class ReactorHumSoundInstance extends AbstractTickableSoundInstance {
    private final BlockPos pos;

    private int subtitleTimer = 0;
    private static final int SUBTITLE_INTERVAL = 30;

    public ReactorHumSoundInstance(BlockPos pos) {
        super(ModSounds.REACTOR_HUM, SoundSource.BLOCKS, RandomSource.create());
        this.pos = pos;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.looping = true;
        this.volume = 1.5f;
    }

    @Override
    public void tick(){
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || !(client.level.getBlockState(pos).getBlock() instanceof ReactorBlock) || !client.level.getBlockState(pos).getValue(ReactorBlock.ACTIVE)){
            this.stop();
            return;
        }

        subtitleTimer++;
        if(subtitleTimer >= SUBTITLE_INTERVAL){
            subtitleTimer = 0;

            SoundManager soundManager = client.getSoundManager();
            WeighedSoundEvents weighedEvent = soundManager.getSoundEvent(ModSounds.REACTOR_HUM.location());
            if(weighedEvent != null){
                SubtitleOverlay overlay = ((GuiAccessor) client.gui).getSubtitleOverlay();
                overlay.onPlaySound(this, weighedEvent, this.getSound().getAttenuationDistance());
            }
        }
    }
}
