package library.Users

import library.Order.Order
import library.Notification
import library.Interfaces.RiderApplication
import library.enums.*

class Rider(userID: String, private val riderApplication: RiderApplication, role: Role, name:String) : User(name,userID,role) {
    private var order: Order? = null
    private var riderStatus: RiderStatus
    private var notification: ArrayList<Notification>
    private lateinit var location: Location

    init {
        riderStatus = RiderStatus.NOT_AVAILABLE
        notification = ArrayList()
    }

    fun acceptOrder(notification: Notification): RiderFunctionalityStatus {
        this.order = notification.getOrder()
        val riderFunctionalityStatus: RiderFunctionalityStatus? = riderApplication.acceptOrder(this, order!!.orderID)
        return if (riderFunctionalityStatus == RiderFunctionalityStatus.ACCEPTED) {
            this.riderStatus = RiderStatus.NOT_AVAILABLE
            this.notification = ArrayList()
            order!!.riderFunctionalityStatus
        } else {
            RiderFunctionalityStatus.NOT_ACCEPTED
        }
    }

    fun declineOrder(notification: Notification): RiderFunctionalityStatus {
        return riderApplication.declineOrder(this, notification)
    }

    fun changeStatusToPicked(): RiderReturnFunctionalities {
        if (order != null) {
            val orderStatus: OrderStatus = order!!.orderStatus
            if (orderStatus == OrderStatus.PREPARED) {
                riderApplication.changeStatusByRider(order!!, RiderFunctionalityStatus.PICKED,this)
                return RiderReturnFunctionalities.PICKED
            } else if (orderStatus == OrderStatus.CANCELLED) {
                this.order = null
                setRiderStatus(RiderStatus.AVAILABLE)
                return RiderReturnFunctionalities.THAT_ORDER_IS_CANCELLED
            } else if (orderStatus == OrderStatus.ORDER_PLACED) {
                return RiderReturnFunctionalities.WAIT_TILL_ORDER_IS_PREPARED
            }
        }
        return RiderReturnFunctionalities.CANT_PROCESS_SINCE_NO_ORDER_IS_ACCEPTED
    }

    fun changeStatusToDelivered(): RiderReturnFunctionalities {
        if (order != null) {
            val riderFunctionalityStatus: RiderFunctionalityStatus = order!!.riderFunctionalityStatus
            if (riderFunctionalityStatus == RiderFunctionalityStatus.PICKED) {
                riderApplication.changeStatusByRider(order!!, RiderFunctionalityStatus.DELIVERED,this)
                this.order = null
                return RiderReturnFunctionalities.DELIVERED
            } else if (order!!.orderStatus == OrderStatus.CANCELLED) {
                this.order = null
                setRiderStatus(RiderStatus.AVAILABLE)
                return RiderReturnFunctionalities.THAT_ORDER_IS_CANCELLED
            }else if (riderFunctionalityStatus == RiderFunctionalityStatus.ACCEPTED){
                return RiderReturnFunctionalities.PICK_THAT_ORDER_FIRST
            }
        }
        return RiderReturnFunctionalities.CANT_PROCESS_SINCE_NO_ORDER_IS_ACCEPTED
    }

    fun setLocation(location: Location) {
        this.location = location
        if (riderStatus == RiderStatus.AVAILABLE){
            riderApplication.setNotification(this)
        }
    }

    internal fun addNotification(notification: Notification) {
        if (riderStatus == RiderStatus.AVAILABLE) this.notification.add(notification)
    }

    fun getNotification(): ArrayList<Notification> {
        return ArrayList(notification)
    }

    internal fun removeNotification(orderID: Int) {
        notification.removeIf { notification: Notification -> notification.getOrder().orderID == orderID }
    }

    fun getOrder(): Order? {
        return order
    }

    fun setRiderStatus(riderStatus: RiderStatus): RiderStatus {
        this.riderStatus = riderStatus
        riderApplication.setNotification(this)
        return this.riderStatus
    }

    fun getRiderStatus(): RiderStatus {
        return riderStatus
    }

    fun getLocation(): Location {
        return location
    }
}