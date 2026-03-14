package dev.oxydien.simpleModSync.ui.widgets;

import dev.oxydien.simpleModSync.ui.ProgressHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class TotalSyncProgress extends AbstractWidget {
    private final ProgressHelper progressHelper;

    public TotalSyncProgress(int x, int y, int width, int height, ProgressHelper progressHelper) {
        super(x, y, width, height, Component.empty());
        this.progressHelper = progressHelper;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        float progress = this.progressHelper.getOverallProgress();

        guiGraphics.fill(0, 0, (int) (((float) this.width) * progress), this.height, 0xAFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
