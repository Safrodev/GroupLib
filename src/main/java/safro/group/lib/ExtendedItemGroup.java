package safro.group.lib;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public abstract class ExtendedItemGroup extends ItemGroup {

    public ExtendedItemGroup(String id) {
        super(GroupLib.createIndex(), id);
    }

    public ExtendedItemGroup(Identifier id) {
        super(GroupLib.createIndex(), String.format("%s.%s", id.getNamespace(), id.getPath()));
    }

    public ExtendedItemGroup(int index, String id) {
        super(index, id);
    }

    /**
     * @return Whether the ItemGroup should have a search bar
     */
    public boolean hasSearchBar() {
        return false;
    }

    /**
     * Called if the tab has a search bar, used for custom search bar textures
     * @return Returns a texture for the ItemGroup
     */
    public String getSearchTabTexture() {
        return "item_search.png";
    }

    /**
     * @return Returns the length of the Search Bar (if the group has one)
     */
    public int getSearchBarLength() {
        return 89;
    }

    /**
     * @return Returns the left x-offset of the Search Bar (if the group has one)
     */
    public int getSearchBarOffset() {
        return 82;
    }

    /**
     * Used to set the color of the Display Title
     * @return Returns an integer for a decimal color value
     */
    public int getTitleColor() {
        return 4210752;
    }

    /**
     * Used to set the x-position of the Display Title
     * @return Returns a float for the x-pos
     */
    public float getTitleX() {
        return 8.0F;
    }

    /**
     * Used to set the y-position of the Display Title
     * @return Returns a float for the y-pos
     */
    public float getTitleY() {
        return 6.0F;
    }

    @Override
    public String getTexture() {
        if (hasSearchBar()) {
            return getSearchTabTexture();
        }
        return super.getTexture();
    }
}
