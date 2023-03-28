package library.Users

import library.Order.LineOrder
import library.Restaurant.Item
import library.Restaurant.Restaurant
import library.Interfaces.RestaurantManagerApplication
import library.enums.*

class RestaurantManager(userID:String, val restaurant: Restaurant, private val restaurantManagerApplication: RestaurantManagerApplication,role: Role, name:String):
    User(name, userID,role ) {
    fun addFood(foodName:String,price:Double,dietary: Dietary,course: Course,timing: Timing):RestaurantManagerFunctions{
        val item = Item(foodName,price, dietary, course, timing)
        return restaurant.menuItems.addMenuItems(item)
    }

    fun alterMenuItems(item: Item, price: Double):RestaurantManagerFunctions{
        return restaurant.menuItems.alterMenuItemsPrice(item,price)
    }

    fun setTimingForFood(item: Item, timing: Timing):RestaurantManagerFunctions{
        return restaurant.menuItems.setTimingForFood(item, timing)
    }

    fun removeTiming(item: Item, timing: Timing):RestaurantManagerFunctions{
        return restaurant.menuItems.removeTimingForFood(item, timing)
    }

    fun deleteMenuItems(item: Item):RestaurantManagerFunctions{
        return restaurant.menuItems.deleteMenuItems(item)
    }

    fun getItems(timing: Timing):HashMap<String, Item>{
        return restaurant.menuItems.getItems(timing)
    }

    fun setRestaurantStatus(restaurantStatus: RestaurantStatus):RestaurantStatus{
        return restaurant.setRestaurantStatus(restaurantStatus)
    }

    fun viewOrderGot():HashMap<Int,ArrayList<LineOrder>>{
        return restaurant.viewOrderGot()
    }

    fun setStatus(orderID:Int,orderStatus: OrderStatus):OrderStatus?{
        if(orderStatus==OrderStatus.PREPARED){
            restaurant.setOrdersCompleted(orderID)
        }

        return restaurantManagerApplication.setStatus(orderID,orderStatus)
    }

    fun getItems(): HashMap<String, Item> {
        return restaurant.menuItems.getItems()
    }

}
