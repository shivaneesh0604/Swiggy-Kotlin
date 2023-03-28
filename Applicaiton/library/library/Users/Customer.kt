package library.Users

import library.*
import library.Interfaces.CustomerApplication
import library.Order.Order
import library.Restaurant.Item
import library.Restaurant.Restaurant
import library.enums.*

class Customer(
    userID: String,
    private val customerApplication: CustomerApplication,
    role: Role,
    name: String
) :
    User(name,userID,role) {
    private lateinit var location: Location

    fun getAllRestaurant(location: Location):HashMap<Int, Restaurant>{
        return customerApplication.getAllRestaurant(location)
    }

    fun enterRestaurant(restaurant: Restaurant, timing: Timing):HashMap<String, Item>{
        return customerApplication.enterRestaurant(restaurant, timing)
    }

    fun addOrder(item: Item, restaurant: Restaurant):Status_of_Restaurant_Orders{
        return customerApplication.takeOrder(item, this.userID, restaurant);
    }

    fun removeOrder(item: Item, restaurant: Restaurant):Status_of_Restaurant_Orders{
        return customerApplication.removeOrder(item, this.userID, restaurant);
    }

    fun viewItemsInCart():HashMap<Int, Order>? {
        return customerApplication.viewItemsInCart(this.userID);
    }

    fun confirmOrder(restaurant: Restaurant): Bill {
        return customerApplication.confirmOrder(this.userID, restaurant);
    }

    fun placeOrder(restaurant: Restaurant):OrderStatus?{
        return customerApplication.placeOrder(this.userID, restaurant);
    }

    fun viewOrdersPlaced():ArrayList<Order>?{
        return customerApplication.viewOrdersPlaced(this.userID)
    }

    fun cancelOrder(orderID:Int,restaurant: Restaurant): OrderStatus? {
        return customerApplication.cancelOrder(orderID, restaurant);
    }

    fun setLocation(location: Location){
        this.location = location
    }

    fun getLocation():Location{
        return location
    }

}