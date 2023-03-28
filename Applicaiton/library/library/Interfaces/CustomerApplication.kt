package library.Interfaces

import library.Bill
import library.Restaurant.Item
import library.Order.Order
import library.Restaurant.Restaurant
import library.enums.Location
import library.enums.OrderStatus
import library.enums.Status_of_Restaurant_Orders
import library.enums.Timing
import java.util.ArrayList

interface CustomerApplication {
    fun getAllRestaurant(location: Location): HashMap<Int, Restaurant>
    fun enterRestaurant(restaurant: Restaurant, timing: Timing): HashMap<String, Item>
    fun takeOrder(item: Item, customerID: String, restaurant: Restaurant): Status_of_Restaurant_Orders
    fun removeOrder(item: Item, customerID: String, restaurant: Restaurant): Status_of_Restaurant_Orders
    fun viewItemsInCart(customerID: String): HashMap<Int, Order>?
    fun confirmOrder(customerID: String, restaurant: Restaurant): Bill
    fun placeOrder(customerID: String, restaurant: Restaurant): OrderStatus?
    fun viewOrdersPlaced(customerID: String): ArrayList<Order>?
    fun cancelOrder(orderID: Int, restaurant: Restaurant): OrderStatus?
}