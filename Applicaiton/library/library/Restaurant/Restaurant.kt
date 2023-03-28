package library.Restaurant

import library.Order.LineOrder
import library.Order.Order
import library.enums.Location
import library.enums.RestaurantStatus

class Restaurant(val location: Location, val restaurantName: String, val restaurantID: Int) {
    internal val menuItems: MenuItems = MenuItems()
    private val ordersGot: HashMap<Int, ArrayList<LineOrder>> = HashMap()
    private val ordersCompleted: java.util.HashMap<Int, ArrayList<LineOrder>> = HashMap()
    internal var restaurantStatus: RestaurantStatus = RestaurantStatus.AVAILABLE

    internal fun receiveOrders(orderID: Int, lineOrders: ArrayList<LineOrder>) {
        this.ordersGot[orderID] = lineOrders
    }

    internal fun viewOrderGot(): HashMap<Int, ArrayList<LineOrder>> {
        return HashMap(ordersGot)
    }

    internal fun setOrdersCompleted(orderID: Int) {
        val orders: ArrayList<LineOrder>? = ordersGot[orderID]
        if (orders != null) {
            ordersCompleted[orderID] = orders
            ordersGot.remove(orderID)
        }
    }

    internal fun removeOrder(orderID: Int) {
        if (ordersGot.containsKey(orderID)) {
            ordersGot.remove(orderID)
        }
    }

    internal fun setRestaurantStatus(restaurantStatus: RestaurantStatus): RestaurantStatus {
        this.restaurantStatus = restaurantStatus
        return this.restaurantStatus
    }

    fun getRestaurantStatus(): RestaurantStatus {
        return restaurantStatus
    }


}