//package io.github.flemmli97.runecraftory.client.gui.widgets;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import io.github.flemmli97.runecraftory.common.registry.ModItems;
//import net.minecraft.client.gui.components.toasts.Toast;
//import net.minecraft.client.gui.components.toasts.ToastComponent;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.resources.sounds.SimpleSoundInstance;
//import net.minecraft.network.chat.Component;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.util.FormattedCharSequence;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class QuestToast implements Toast {
//
//    private static final int PAGE_TIME = 2000;
//
//    private final Component title;
//    private final Component subtitle;
//    private boolean playedSound;
//    private List<ToastPage> contents;
//    private int page;
//
//    public QuestToast(Component title, Component subtitle) {
//        this.title = title;
//        this.subtitle = subtitle;
//    }
//
//    @Override
//    public Visibility render(PoseStack poseStack, ToastComponent toastComponent, long timeSinceLastVisible) {
//        int xPadding = 8;
//        int yPadding = 6;
//        if (this.contents == null) {
//            List<FormattedCharSequence> title = toastComponent.getMinecraft().font.split(this.title, this.width() - 30 - xPadding);
//            List<FormattedCharSequence> subtitle = toastComponent.getMinecraft().font.split(this.subtitle, this.width() - 30 - xPadding);
//            this.contents = getToastPages(title, subtitle);
//            return Visibility.SHOW;
//        }
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        toastComponent.blit(poseStack, 0, 0, 0, 32, this.width(), this.height());
//        ToastPage content = this.contents.get(this.page);
//        toastComponent.getMinecraft().font.draw(poseStack, content.first(), 30, yPadding, 0);
//        if (content.second() != null)
//            toastComponent.getMinecraft().font.draw(poseStack, content.second(), 30, yPadding + 12, 0);
//        if (this.page + 1 < this.contents.size() && timeSinceLastVisible >= (long) PAGE_TIME * (this.page + 1)) {
//            this.page++;
//        }
//        if (!this.playedSound && timeSinceLastVisible > 0L) {
//            this.playedSound = true;
//            toastComponent.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F));
//        }
//        toastComponent.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModItems.QUEST_BOARD.get()), 8, 8);
//        return timeSinceLastVisible >= ((long) this.contents.size() * PAGE_TIME) + 3000 ? Visibility.HIDE : Visibility.SHOW;
//    }
//
//    private static @NotNull List<ToastPage> getToastPages(List<FormattedCharSequence> title, List<FormattedCharSequence> subtitle) {
//        List<ToastPage> pages = new ArrayList<>();
//        if (title.size() == 1 && subtitle.size() == 1) {
//            pages.add(new ToastPage(title.get(0), subtitle.get(0)));
//        } else {
//            for (int i = 0; i < title.size(); i += 2) {
//                if (i + 1 < title.size())
//                    pages.add(new ToastPage(title.get(i), title.get(i + 1)));
//                else
//                    pages.add(new ToastPage(title.get(i), null));
//            }
//            for (int i = 0; i < subtitle.size(); i += 2) {
//                if (i + 1 < subtitle.size())
//                    pages.add(new ToastPage(subtitle.get(i), subtitle.get(i + 1)));
//                else
//                    pages.add(new ToastPage(subtitle.get(i), null));
//            }
//        }
//        return pages;
//    }
//
//    @Override
//    public int width() {
//        return 160;
//    }
//
//    @Override
//    public int height() {
//        return 32;
//    }
//
//    record ToastPage(FormattedCharSequence first, FormattedCharSequence second) {
//
//    }
//}
