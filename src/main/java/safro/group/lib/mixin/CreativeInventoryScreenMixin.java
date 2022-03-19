package safro.group.lib.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.search.SearchableContainer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.group.lib.ExtendedItemGroup;

import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    public CreativeInventoryScreenMixin(PlayerEntity player) {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), LiteralText.EMPTY);
    }

    @Shadow private static int selectedTab;

    @Shadow private boolean ignoreTypedCharacter;

    @Shadow private TextFieldWidget searchBox;

    @Shadow protected abstract void search();

    @Shadow protected abstract boolean isCreativeInventorySlot(@Nullable Slot slot);

    @Shadow private float scrollPosition;

    @Shadow protected abstract void searchForTags(String id);

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTypedSearchGroup(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ItemGroup selected = ItemGroup.GROUPS[selectedTab];
        if (!this.ignoreTypedCharacter) {
            if (selected instanceof ExtendedItemGroup group && group.hasSearchBar()) {
                String string = this.searchBox.getText();
                if (this.searchBox.charTyped(chr, modifiers)) {
                    if (!Objects.equals(string, this.searchBox.getText())) {
                        this.search();
                    }
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIndex()I", shift = At.Shift.BEFORE), cancellable = true)
    private void keyPressedSearchGroup(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ItemGroup selected = ItemGroup.GROUPS[selectedTab];
        if (selected instanceof ExtendedItemGroup group && group.hasSearchBar()) {
            boolean bl = !this.isCreativeInventorySlot(this.focusedSlot) || this.focusedSlot.hasStack();
            boolean bl2 = InputUtil.fromKeyCode(keyCode, scanCode).toInt().isPresent();
            if (bl && bl2 && this.handleHotbarKeyPressed(keyCode, scanCode)) {
                this.ignoreTypedCharacter = true;
                cir.setReturnValue(true);
            } else {
                String string = this.searchBox.getText();
                if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                    if (!Objects.equals(string, this.searchBox.getText())) {
                        this.search();
                    }

                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers));
                }
            }
        }
    }

    @Inject(method = "search", at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V", shift = At.Shift.AFTER), cancellable = true)
    private void customSearch(CallbackInfo ci) {
        ItemGroup selected = ItemGroup.GROUPS[selectedTab];
        if (selected instanceof ExtendedItemGroup group && group.hasSearchBar()) {
            String string = this.searchBox.getText();
            if (string.isEmpty()) {
                Iterator var2 = Registry.ITEM.iterator();

                while(var2.hasNext()) {
                    Item item = (Item)var2.next();
                    if (item.getGroup() == group) {
                        item.appendStacks(group, (this.handler).itemList);
                    }
                }
            } else {
                SearchableContainer<ItemStack> searchable;
                if (string.startsWith("#")) {
                    string = string.substring(1);
                    searchable = this.client.getSearchableContainer(SearchManager.ITEM_TAG);
                    this.searchForTags(string);
                } else {
                    searchable = this.client.getSearchableContainer(SearchManager.ITEM_TOOLTIP);
                }

                for (ItemStack stack : searchable.findAll(string.toLowerCase(Locale.ROOT))) {
                    if (stack.getItem().getGroup() == group) {
                        this.handler.itemList.add(stack);
                    }
                }
            }
            this.scrollPosition = 0.0F;
            handler.scrollItems(0.0F);
            ci.cancel();
        }
    }

    @Inject(method = "setSelectedTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setText(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void setSearchVisible(ItemGroup group, CallbackInfo ci) {
       if (group instanceof ExtendedItemGroup group1 && group1.hasSearchBar()) {
           this.searchBox.setVisible(true);
           this.searchBox.setFocusUnlocked(false);
           this.searchBox.setTextFieldFocused(true);
           if (selectedTab != group.getIndex()) {
               this.searchBox.setText("");
           }
           this.searchBox.setWidth(group1.getSearchBarLength());
           this.searchBox.x = this.x + (group1.getSearchBarOffset() + 89) - this.searchBox.getWidth();

           this.search();
       }
    }

    @Inject(method = "drawForeground", at = @At("HEAD"), cancellable = true)
    private void drawDisplayTitle(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        ItemGroup selected = ItemGroup.GROUPS[selectedTab];
        if (selected instanceof ExtendedItemGroup group && group.shouldRenderName()) {
            RenderSystem.disableBlend();
            this.textRenderer.draw(matrices, group.getDisplayName(), group.getTitleX(), group.getTitleY(), group.getTitleColor());
            ci.cancel();
        }
    }
}
