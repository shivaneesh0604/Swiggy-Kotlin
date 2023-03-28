package library.Order

import library.Bill
import library.Restaurant.Item
import library.enums.Location
import library.enums.OrderStatus
import library.enums.RiderFunctionalityStatus
import library.enums.Status_of_Restaurant_Orders

class Order(
    val restaurantName: String,
    val restaurantID: Int,
    val restaurantLocation: Location,
    val customerID: String,
    val customerLocation: Location
) {
    val orders = HashMap<String, LineOrder>()

    companion object {
        private var orderCount = 1000
    }

    internal lateinit var orderStatus: OrderStatus
    val orderID: Int = orderCount++
    internal val bill = Bill(orderID)
    internal var riderFunctionalityStatus: RiderFunctionalityStatus = RiderFunctionalityStatus.NOT_ACCEPTED

    internal fun addOrders(item: Item) {
        val lineOrder: LineOrder? = orders[item.foodName]
        if (lineOrder != null) {
            lineOrder.setQuantity(lineOrder.quantity + 1)
            return
        }
        this.orders[item.foodName] = LineOrder(item, 1)
    }

    internal fun deleteOrder(item: Item): Status_of_Restaurant_Orders {
        val lineOrder: LineOrder? = orders[item.foodName]
        if (lineOrder != null) {
            return if (lineOrder.quantity > 1) {
                lineOrder.setQuantity(lineOrder.quantity - 1)
                Status_of_Restaurant_Orders.CHANGED_QUANTITY
            } else if (lineOrder.quantity == 1) {
                orders.remove(item.foodName)
                Status_of_Restaurant_Orders.TOTALLY_DELETED
            } else {
                Status_of_Restaurant_Orders.CANT_DELETE
            }
        }
        return Status_of_Restaurant_Orders.NO_ORDER_FOUND
    }

    internal fun setRiderAcceptance(riderFunctionalityStatus: RiderFunctionalityStatus) {
        this.riderFunctionalityStatus = riderFunctionalityStatus
    }

    internal fun setStatus(orderStatus: OrderStatus) {
        this.orderStatus = orderStatus
    }

    fun getOrderStatus():OrderStatus{
        return orderStatus
    }

    fun getRiderFunctionalityStatus():RiderFunctionalityStatus{
        return riderFunctionalityStatus
    }


}
