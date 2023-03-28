package library.Interfaces

import library.enums.OrderStatus

interface RestaurantManagerApplication {
    fun setStatus(orderID: Int, orderStatus: OrderStatus): OrderStatus?
}