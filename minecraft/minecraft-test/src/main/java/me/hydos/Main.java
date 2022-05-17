package me.hydos;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.freezeRegistries();
		Registry.register(Registry.ITEM, new Identifier("eg"), new Item(new Item.Settings().fireproof()));
	}
}
