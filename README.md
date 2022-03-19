# GroupLib

GroupLib is a lightweight ItemGroup Library for Fabric 1.18.2+. GroupLib allows Mod Developers to further customize their ItemGroups.

## Setup

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
	        modImplementation "com.github.Safrodev:GroupLib:VERSION"
	}
```
Replace ``VERSION`` with the latest (or preferred) version of the lib. You can find the versions here: https://github.com/Safrodev/GroupLib/releases

## Examples

### ItemGroup Class

Example 1
````java
public class ExampleGroup extends ExtendedItemGroup {

    public ExampleGroup() {
        super("example");
    }
    
    public boolean hasSearchBar() {
        return true;
    }
    
    public String getSearchTabTexture() {
        return "example_texture.png";
    }
}
````

Example 2
````java
public class ExampleGroup2 extends ExtendedItemGroup {

    public ExampleGroup(String name) {
        super(name);
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

### ItemGroup Registry
````java
public class ExampleMod extends ModInitializer {
    public static final ExtendedItemGroup EXAMPLE_GROUP = new ExampleGroup();
    public static final ExtendedItemGroup EXAMPLE_GROUP2 = new ExampleGroup2("example2");
    
    @Override
    public void onInitialize() {}
}
````
