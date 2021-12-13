package com.willfp.eco.core.items.args;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Parse skull textures.
 *
 * @deprecated Moved to internals.
 */
@Deprecated(since = "6.16.0", forRemoval = true)
public class TextureArgParser implements LookupArgParser {
    /**
     * Instantiate arg parser.
     */
    public TextureArgParser() {
        Bukkit.getLogger().severe("Instantiation of class marked for removal! (" + this.getClass().getName() + ")");
    }

    @Override
    public @Nullable Predicate<ItemStack> parseArguments(@NotNull final String[] args,
                                                         @NotNull final ItemMeta meta) {
        return null;
    }
}
