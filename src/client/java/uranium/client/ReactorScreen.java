package uranium.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import uranium.ReactorMenu;

public class ReactorScreen extends AbstractContainerScreen<ReactorMenu> {
    private static final Identifier CONTAINER_TEXTURE_INACTIVE = Identifier.fromNamespaceAndPath("uranium-age","textures/gui/container/reactor_menu_disabled.png");
    private static final Identifier CONTAINER_TEXTURE_ACTIVE = Identifier.fromNamespaceAndPath("uranium-age","textures/gui/container/reactor_menu_enabled.png");

    public ReactorScreen(ReactorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 9;
    }

    @Override
    public void extractBackground (GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta){
        super.extractBackground(graphics, mouseX, mouseY, delta);
        Identifier CONTAINER_TEXTURE = this.menu.isActive() ? CONTAINER_TEXTURE_ACTIVE : CONTAINER_TEXTURE_INACTIVE;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
