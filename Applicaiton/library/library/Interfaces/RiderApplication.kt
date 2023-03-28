package library.Interfaces

import library.Users.Rider
import library.Order.Order
import library.Notification
import library.enums.RiderFunctionalityStatus

interface RiderApplication {
    fun setNotification(rider: Rider)
    fun acceptOrder(rider: Rider, orderID: Int): RiderFunctionalityStatus?
    fun declineOrder(rider: Rider, notification: Notification): RiderFunctionalityStatus
    fun changeStatusByRider(order: Order, riderFunctionalityStatus: RiderFunctionalityStatus,rider: Rider)
}