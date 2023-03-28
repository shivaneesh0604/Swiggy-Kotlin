import Enums.RiderOptions
import library.Notification
import library.Order.Order
import library.Users.Rider
import library.enums.Location
import library.enums.RiderStatus
import java.util.*

class RiderUI(private val rider: Rider) : UI {
    private var sc = Scanner(System.`in`)

    override fun entersUI() {
        println("enter in which location you are")
        Utils.print(Location.values())
        val locationValues = Utils.inputVerification(Location.values().size)
        val location: Location = Location.values()[locationValues]
        rider.setLocation(location)
        MainLoop@ while (true) {
            Utils.print(RiderOptions.values())
            val riderOptionValues = Utils.inputVerification(RiderOptions.values().size)
            when (RiderOptions.values()[riderOptionValues]) {
                RiderOptions.SET_RIDER_STATUS -> {
                    setRiderStatus()
                }

                RiderOptions.SHOW_AVAILABLE_NOTIFICATIONS -> {
                    if (rider.getRiderStatus() == RiderStatus.AVAILABLE) {
                        val notifications = rider.getNotification()
                        showAvailableNotifications(notifications)
                    } else if (rider.getOrder() != null) {
                        println("you have already chosen an order complete that order first")
                    } else {
                        println("you cant do any functions since you had set you status as " + rider.getRiderStatus())
                    }
                }

                RiderOptions.ACCEPT_ORDER -> {
                    acceptOrder()
                }

                RiderOptions.DECLINE_ORDER -> {
                    declineOrder()
                }

                RiderOptions.CHANGE_STATUS_TO_PICKED -> {
                    println(rider.changeStatusToPicked())
                }

                RiderOptions.CHANGE_STATUS_TO_DELIVERED -> {
                    println(rider.changeStatusToDelivered())
                }

                RiderOptions.BACK -> {
                    if (rider.getNotification().size > 0) {
                        println("There are some Orders assigned to you...Do you need to remove them all...if yes then press 1 ")
                        val confirmation = sc.nextLine()
                        try {
                            if (confirmation.toInt() == 1) {
                                for (notification in rider.getNotification()) {
                                    rider.declineOrder(notification)
                                }
                                println("REMOVED")
                                break@MainLoop
                            } else {
                                continue@MainLoop
                            }
                        } catch (e: NumberFormatException) {
                            println("Only numbers are accepted")
                        }
                    } else
                        break@MainLoop
                }
            }

        }
    }

    private fun setRiderStatus() {
        println("your status now is " + rider.getRiderStatus())
        Utils.print(RiderStatus.values())
        val riderStatusOption = Utils.inputVerification(RiderStatus.values().size)
        val riderStatus: RiderStatus = RiderStatus.values()[riderStatusOption]
        if (rider.getOrder() == null) {
            println("the status is " + rider.setRiderStatus(riderStatus))
        } else {
            println("cant change because you have picked an order first deliver that order and change status")
        }
    }

    private fun acceptOrder() {
        if (rider.getRiderStatus() == RiderStatus.AVAILABLE) {
            val notifications = rider.getNotification()
            showAvailableNotifications(notifications)
            if (notifications.size==0){
                return
            }
            println("enter orderID to accept")
            val orderID: String = sc.nextLine()
            if (notifications.size > 0) {
                for (notification1 in notifications) {
                    if (notification1.getOrder().orderID == orderID.toInt()) {
                        println(rider.acceptOrder(notification1))
                        return
                    }
                }
                println("WRONG_ORDER_ID")
            }
        } else if (rider.getOrder() != null) {
            println("you have already chosen an order complete that order first")
        } else {
            println("you cant do any functions since you had set you status as " + rider.getRiderStatus())
        }
    }

    private fun declineOrder() {
        if (rider.getRiderStatus() == RiderStatus.AVAILABLE) {
            val notifications = rider.getNotification()
            showAvailableNotifications(notifications)
            println("enter orderID to decline order")
            val orderID = sc.nextLine()
            if (notifications.size > 0) {
                for (notification1 in notifications) {
                    if (notification1.getOrder().orderID == orderID.toInt()) {
                        println("this order is " + rider.declineOrder(notification1))
                        return
                    }
                }
                println("WRONG ORDER_ID")
            }
        } else if (rider.getOrder() != null) {
            println("you have already chosen an order complete that order first")
        } else {
            println("you cant do any functions since you had set you status as " + rider.getRiderStatus() + " change your status to " + RiderStatus.AVAILABLE)
        }
    }

    private fun showAvailableNotifications(notifications: ArrayList<Notification>?) {
        if (notifications != null && notifications.size > 0) {
            for (notification in notifications) {
                val order: Order = notification.getOrder()
                println(((((((order.orderID).toString() + " is the order ID " + order.restaurantID).toString() + " is the restaurant ID " + order.customerLocation).toString() + " is the location of customer " + order.customerID).toString() + " is the customer ID " + order.restaurantLocation).toString() + " is the location of the restaurant " + order.restaurantName).toString() + " is the name of the restaurant")
            }
        } else {
            println("no orders available")
        }
    }
}