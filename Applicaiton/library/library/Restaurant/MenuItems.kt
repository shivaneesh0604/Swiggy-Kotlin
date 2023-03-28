package library.Restaurant

import library.enums.RestaurantManagerFunctions
import library.enums.Timing

class MenuItems {
    private val menuItems: HashMap<String, Item> = HashMap()

    internal fun addMenuItems(item: Item): RestaurantManagerFunctions {
        menuItems[item.foodName] = item;
        return RestaurantManagerFunctions.ITEM_ADDED;
    }

    internal fun alterMenuItemsPrice(item: Item, price: Double): RestaurantManagerFunctions {
        return item.setPrice(price)
    }

    internal fun deleteMenuItems(item: Item): RestaurantManagerFunctions {
        menuItems.remove(item.foodName);
        return RestaurantManagerFunctions.ITEM_DELETED;
    }

    internal fun setTimingForFood(item: Item, timing: Timing): RestaurantManagerFunctions {
        return item.setTiming(timing);
    }

    internal fun removeTimingForFood(item: Item, timing: Timing):RestaurantManagerFunctions{
        return item.removeTiming(timing);
    }

    internal fun getItems(timing: Timing):HashMap<String, Item>{
        val availableTimingItems = HashMap<String, Item>()
        val menuItems: Collection<Item> = menuItems.values
        for (item in menuItems) {
            if (item.timingsList.contains(timing)) {
                availableTimingItems[item.foodName] = item
            }
        }
        return availableTimingItems
    }

    internal fun getItems(): HashMap<String, Item> {
        return HashMap(menuItems)
    }

}