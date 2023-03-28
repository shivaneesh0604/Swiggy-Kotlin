package library

import library.Order.Order

class Notification(private var order: Order) {

    private var cancelledRiderIds: ArrayList<String> = ArrayList()

    internal fun checkCancelledRiderIds(userID: String): Boolean {
        return !cancelledRiderIds.contains(userID)
    }

    internal fun setCancelledRiderIds(cancelledRiderIds: String) {
        this.cancelledRiderIds.add(cancelledRiderIds)
    }

    fun getOrder(): Order {
        return order
    }
}