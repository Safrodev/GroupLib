# GroupLib

GroupLib is a lightweight ItemGroup Library for Fabric 1.18.2+. GroupLib allows Mod Developers to further customize their ItemGroups.

# Setup

Add the following to your mod's ``build.gradle`` file:
```
repositories {
	maven {
		url 'https://jitpack.io'
	}
}
```
```
dependencies {
	        modImplementation include("com.github.Safrodev:GroupLib:VERSION")
	}
```
Replace ``VERSION`` with the latest (or preferred) version of the lib. You can find the versions here: https://github.com/Safrodev/GroupLib/releases

# Examples

## ItemGroup Class

### Example 1

This example creates a simple ItemGroup with an Apple icon. It contains a search bar and a custom ItemGroup texture.

````java
public class ExampleGroup extends ExtendedItemGroup {

    public ExampleGroup() {
        super("example");
    }
    
    public ItemStack createIcon() {
    	return new ItemStack(Items.APPLE);
    }
    
    public boolean hasSearchBar() {
        return true;
    }
    
    public String getSearchTabTexture() {
        return "example_texture.png";
    }
}
````

### Example 2

In this example, an ItemGroup with a Spyglass icon is created. It contains a search bar with a width of 100 and a lime green title color.

````java
public class ExampleGroup2 extends ExtendedItemGroup {

    public ExampleGroup(String name) {
        super(name);
    }
    
    public ItemStack createIcon() {
    	return new ItemStack(Items.SPYGLASS);
    }
    
    public boolean hasSearchBar() {
        return true;
    }

    public int getSearchBarLength() {
        return 100;
    }
    
    public int getTitleColor() {
        return 2817792;
    }
}
````
See ``ExtendedItemGroup`` for the full list of available methods and constructors.

## ItemGroup Registry
````java
public class ExampleMod extends ModInitializer {
    public static final ExtendedItemGroup EXAMPLE_GROUP = new ExampleGroup();
    public static final ExtendedItemGroup EXAMPLE_GROUP2 = new ExampleGroup2("example2");
    
    @Override
    public void onInitialize() {}
}
````

# Questions & Issues

If you have a question, issue, or any other thought please let me know by opening an Issue or on my [Discord](https://discord.gg/muAnYRGXrq).
