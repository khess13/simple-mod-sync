package dev.oxydien.simpleModSync.ui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LinearScrollLayout extends AbstractScrollArea {
    private final List<AbstractWidget> children = new ArrayList<>();
    private final Layout layout;

    public LinearScrollLayout(int width, int height) {
        super(0, 0, width, height, CommonComponents.EMPTY);
        this.layout = new LinearLayout(width, height, LinearLayout.Orientation.VERTICAL);
        layout.visitWidgets(this::addWidget);
    }

    public void addWidget(AbstractWidget widget) {
        this.children.add(widget);
    }

    protected int contentHeight() {
        return this.layout.getHeight();
    }

    protected double scrollRate() {
        return 10.0F;
    }

    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        gui.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);
        gui.pose().pushPose();
        gui.pose().translate(0.0F, -this.scrollAmount(), 0.0F);

        for(AbstractWidget abstractwidget : this.children) {
            abstractwidget.render(gui, mouseX, mouseY, delta);
        }

        gui.pose().popPose();
        gui.disableScissor();
        this.renderScrollbar(gui);
    }

    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    public @NotNull ScreenRectangle getBorderForArrowNavigation(@NotNull ScreenDirection screenDirection) {
        return new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight());
    }

    public void setFocused(@Nullable GuiEventListener eventListener) {
        super.setFocused(false);
        if (eventListener != null) {
            ScreenRectangle screenrectangle = this.getRectangle();
            ScreenRectangle eventRectangle = eventListener.getRectangle();
            int i = (int)((double)eventRectangle.top() - this.scrollAmount() - (double)screenrectangle.top());
            int j = (int)((double)eventRectangle.bottom() - this.scrollAmount() - (double)screenrectangle.bottom());
            if (i < 0) {
                this.setScrollAmount(this.scrollAmount() + (double)i - (double)14.0F);
            } else if (j > 0) {
                this.setScrollAmount(this.scrollAmount() + (double)j + (double)14.0F);
            }
        }

    }

    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    public void setX(int x) {
        super.setX(x);
        this.layout.setX(x);
        this.layout.arrangeElements();
    }

    public void setY(int y) {
        super.setY(y);
        this.layout.setY(y);
        this.layout.arrangeElements();
    }

    public @NotNull Collection<? extends NarratableEntry> getNarratables() {
        return this.children;
    }

}
