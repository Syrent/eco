package com.willfp.eco.core.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.recipe.recipes.CraftingRecipe;
import com.willfp.eco.core.recipe.recipes.ShapedCraftingRecipe;
import com.willfp.eco.util.NamespacedKeyUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utility class to manage and register crafting recipes.
 */
public final class Recipes {
    /**
     * Registry of all recipes.
     */
    private static final BiMap<NamespacedKey, CraftingRecipe> RECIPES = HashBiMap.create();


    /**
     * Register a recipe.
     *
     * @param recipe The recipe.
     */
    public static void register(@NotNull final CraftingRecipe recipe) {
        RECIPES.forcePut(recipe.getKey(), recipe);
    }

    /**
     * Get recipe matching matrix.
     *
     * @param matrix The matrix to test.
     * @return The match, or null if not found.
     */
    @Nullable
    public static CraftingRecipe getMatch(@NotNull final ItemStack[] matrix) {
        return RECIPES.values().stream().filter(recipe -> recipe.test(matrix)).findFirst().orElse(null);
    }

    /**
     * Get recipe by key.
     *
     * @param key The key.
     * @return The recipe, or null if not found.
     */
    @Nullable
    public static CraftingRecipe getRecipe(@NotNull final NamespacedKey key) {
        CraftingRecipe recipe = RECIPES.get(key);
        if (recipe != null) {
            return recipe;
        }

        if (key.getKey().contains("_displayed")) {
            NamespacedKey otherKey = NamespacedKeyUtils.create(
                    key.getNamespace(),
                    key.getKey().replace("_displayed", "")
            );

            return RECIPES.get(otherKey);
        }

        return null;
    }

    /**
     * Create and register recipe.
     *
     * @param plugin        The plugin.
     * @param key           The key.
     * @param output        The output.
     * @param recipeStrings The recipe.
     * @return The recipe.
     */
    public static CraftingRecipe createAndRegisterRecipe(@NotNull final EcoPlugin plugin,
                                                         @NotNull final String key,
                                                         @NotNull final ItemStack output,
                                                         @NotNull final List<String> recipeStrings) {
        return createAndRegisterRecipe(plugin, key, output, recipeStrings, null);
    }

    /**
     * Create and register recipe.
     *
     * @param plugin        The plugin.
     * @param key           The key.
     * @param output        The output.
     * @param recipeStrings The recipe.
     * @param permission    The permission.
     * @return The recipe.
     */
    @Nullable
    public static CraftingRecipe createAndRegisterRecipe(@NotNull final EcoPlugin plugin,
                                                         @NotNull final String key,
                                                         @NotNull final ItemStack output,
                                                         @NotNull final List<String> recipeStrings,
                                                         @Nullable final String permission) {
        ShapedCraftingRecipe.Builder builder = ShapedCraftingRecipe.builder(plugin, key)
                .setOutput(output)
                .setPermission(permission);

        for (int i = 0; i < 9; i++) {
            builder.setRecipePart(i, Items.lookup(recipeStrings.get(i)));
        }

        if (builder.isAir()) {
            Bukkit.getLogger().warning("RECIPE ERROR! " + plugin.getName() + ":" + key + " consists only");
            Bukkit.getLogger().warning("of air or invalid items! Please change that or disable this recipe.");
            return null;
        }

        ShapedCraftingRecipe recipe = builder.build();
        recipe.register();

        return recipe;
    }

    private Recipes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
