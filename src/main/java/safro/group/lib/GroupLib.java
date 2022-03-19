package safro.group.lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;

public class GroupLib implements ModInitializer {

	@Override
	public void onInitialize() {}

	/**
	 * Creates a new ItemGroup index
	 * @return - Returns an index integer
	 */
	public static int createIndex() {
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		return ItemGroup.GROUPS.length - 1;
	}
}
