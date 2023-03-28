package library.Application

import library.Bill
import library.Database.Database
import library.Interfaces.CustomerApplication
import library.Order.LineOrder
import library.Order.Order
import library.Restaurant.Item
import library.Restaurant.Restaurant
import library.Interfaces.RestaurantManagerApplication
import library.Users.Rider
import library.Interfaces.RiderApplication
import library.Notification
import library.enums.*
import library.pojo.CustomerDetails

internal class Application : CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private val cartItems: HashMap<String, HashMap<Int, Order>> = HashMap()

    companion object {
        private val tempNotifications: ArrayList<Notification> = ArrayList()
    }

    override fun getAllRestaurant(location: Location): HashMap<Int, Restaurant> {
        val listOfRestaurant: HashMap<Int, Restaurant> = Database.getInstanceDatabase().getAllRestaurants()
        val restaurants = java.util.HashMap<Int, Restaurant>()
        val locations: ArrayList<Location> = Database.getInstanceDatabase().getLocations()
        val index = locations.indexOf(location)
        val previousIndex = if (index < 1) null else locations[index - 1]
        val nextIndex = if (index > locations.size - 2) null else locations[index + 1]
        for (restaurant in listOfRestaurant.values) {
            if (previousIndex == null) {
                if (restaurant.restaurantStatus == RestaurantStatus.AVAILABLE && (restaurant.location) == location || restaurant.location == nextIndex) {
                    restaurants[restaurant.restaurantID] = restaurant
                }
            } else if (nextIndex == null) {
                if (restaurant.restaurantStatus == RestaurantStatus.AVAILABLE && (restaurant.location == location || restaurant.location == previousIndex)) {
                    restaurants[restaurant.restaurantID] = restaurant
                }
            } else if (restaurant.restaurantStatus == RestaurantStatus.AVAILABLE && (restaurant.location == location || restaurant.location == previousIndex) || restaurant.location == nextIndex) {
                restaurants[restaurant.restaurantID] = restaurant
            }
        }
        return restaurants
    }

    override fun enterRestaurant(restaurant: Restaurant, timing: Timing): HashMap<String, Item> {
        return restaurant.menuItems.getItems(timing)
    }

    override fun takeOrder(item: Item, customerID: String, restaurant: Restaurant): Status_of_Restaurant_Orders {
        val customerOrder: HashMap<Int, Order>? = cartItems[customerID]
        val customerDetails: CustomerDetails = Database.getInstanceDatabase().getCustomerDetails(customerID)
        if (customerOrder != null) {
            val restaurantOrder = customerOrder[restaurant.restaurantID]
            if (restaurantOrder != null) {
                restaurantOrder.addOrders(item)
            } else {
                customerOrder.clear()
                val order2 = Order(
                    restaurant.restaurantName,
                    restaurant.restaurantID,
                    restaurant.location,
                    customerDetails.userID,
                    customerDetails.location
                )
                order2.addOrders(item)
                customerOrder[restaurant.restaurantID] = order2
            }
        } else {
            val orderList2 = java.util.HashMap<Int, Order>()
            val order = Order(
                restaurant.restaurantName,
                restaurant.restaurantID,
                restaurant.location,
                customerDetails.userID,
                customerDetails.location
            )
            order.addOrders(item)
            orderList2[restaurant.restaurantID] = order
            cartItems[customerID] = orderList2
        }
        return Status_of_Restaurant_Orders.ORDER_ADDED
    }

    override fun removeOrder(item: Item, customerID: String, restaurant: Restaurant): Status_of_Restaurant_Orders {
        val orders: Order? = cartItems[customerID]?.get(restaurant.restaurantID)
        if (orders != null) {
            val orderDeletion: Status_of_Restaurant_Orders = orders.deleteOrder(item)
            return if (orderDeletion == Status_of_Restaurant_Orders.TOTALLY_DELETED) {
                cartItems[customerID]!!.clear()
                orderDeletion
            } else {
                orderDeletion
            }
        }
        return Status_of_Restaurant_Orders.NO_ORDER_FOUND_IN_THIS_RESTAURANT
    }

    override fun viewItemsInCart(customerID: String): HashMap<Int, Order>? {
        return if (cartItems[customerID] == null) {
            null
        } else HashMap(cartItems[customerID])
    }

    override fun confirmOrder(customerID: String, restaurant: Restaurant): Bill {
        val order = cartItems[customerID]!![restaurant.restaurantID]!!
        val bill: Bill = order.bill

        if (bill.items.size > 0) {
            return bill
        }

        val orders: HashMap<String, LineOrder> = order.orders
        val lineOrders: Collection<LineOrder> = orders.values
        for (lineOrder in lineOrders) {
            bill.addItem(lineOrder.item, lineOrder.quantity)
        }
        return bill
    }

    override fun placeOrder(customerID: String, restaurant: Restaurant): OrderStatus? {

        val order: Order? = if (cartItems.contains(customerID)) cartItems[customerID]!!.getOrDefault(
            restaurant.restaurantID,
            null
        ) else null

        if (order != null) {
            Database.getInstanceDatabase().addOrder(customerID, order, restaurant.restaurantID)
            val allRiders: Collection<Rider> = Database.getInstanceDatabase().getAllRiders()
            setNotification(allRiders, order.restaurantLocation, Notification(order))
            cartItems.remove(customerID)
            val orders: HashMap<String, LineOrder> = order.orders
            val lineOrders: ArrayList<LineOrder> = ArrayList(orders.values)
            restaurant.receiveOrders(order.orderID, lineOrders)
            order.setStatus(OrderStatus.ORDER_PLACED)
            return OrderStatus.ORDER_PLACED
        }
        return null
    }

    override fun viewOrdersPlaced(customerID: String): ArrayList<Order>? {
        val ordersPlaced: ArrayList<Order> = ArrayList()
        val ordersPlacedByCustomer: HashMap<Int, ArrayList<Order>>? =
            Database.getInstanceDatabase().getOrderPlaced(customerID)

        if (ordersPlacedByCustomer != null) {
            val nonCancelledOrders: HashMap<Int, ArrayList<Order>> = getNonCancelledOrders(ordersPlacedByCustomer)
            if (nonCancelledOrders.size > 0) {
                for (orders1 in nonCancelledOrders.values) {
                    for (order in orders1) {
                        if (order.orderStatus != OrderStatus.CANCELLED) {
                            ordersPlaced.add(order)
                        }
                    }
                }
                return ordersPlaced
            }
        }
        return null
    }

    override fun cancelOrder(orderID: Int, restaurant: Restaurant): OrderStatus? {
        val order = Database.getInstanceDatabase().getOrder(orderID)
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED)
            val riders = Database.getInstanceDatabase().getAllRiders()
            for (rider in riders) {
                rider.removeNotification(orderID)
            }
            restaurant.removeOrder(orderID)
            return OrderStatus.CANCELLED
        }
        return null
    }

    override fun setNotification(rider: Rider) {
        val riders = Database.getInstanceDatabase().getAllRiders()
        if (rider.getRiderStatus() == RiderStatus.AVAILABLE) {
            for (notification in ArrayList(tempNotifications)) {
                setNotification(riders, notification.getOrder().restaurantLocation, notification)
            }
            return
        }
        riders.remove(rider)
        for (notification in rider.getNotification()) {
            setNotification(riders, notification.getOrder().restaurantLocation, notification)
            rider.removeNotification(notification.getOrder().orderID)
        }
    }

    override fun acceptOrder(rider: Rider, orderID: Int): RiderFunctionalityStatus? {
        val order = Database.getInstanceDatabase().getOrder(orderID)
        if (order != null && order.orderID == orderID && order.orderStatus != OrderStatus.CANCELLED) {
            order.setRiderAcceptance(RiderFunctionalityStatus.ACCEPTED)
            val allRiders: MutableCollection<Rider> = Database.getInstanceDatabase().getAllRiders()
            allRiders.remove(rider)
            rider.removeNotification(orderID)
            val notifications = rider.getNotification()
            for (notification1 in notifications) {
                setNotification(allRiders, order.restaurantLocation, notification1)
            }
            return order.riderFunctionalityStatus
        }
        return null
    }

    override fun declineOrder(rider: Rider, notification: Notification): RiderFunctionalityStatus {
        val riders = Database.getInstanceDatabase().getAllRiders()
        notification.setCancelledRiderIds(rider.userID)
        rider.removeNotification(notification.getOrder().orderID)
        setNotification(riders, notification.getOrder().restaurantLocation, notification)
        return RiderFunctionalityStatus.NOT_ACCEPTED
    }

    override fun changeStatusByRider(order: Order, riderFunctionalityStatus: RiderFunctionalityStatus, rider: Rider) {
        Database.getInstanceDatabase().setStatusByRider(riderFunctionalityStatus, order.orderID)
        if (riderFunctionalityStatus == RiderFunctionalityStatus.DELIVERED) {
            rider.setRiderStatus(RiderStatus.AVAILABLE)
            setNotification(rider)
        }
    }

    override fun setStatus(orderID: Int, orderStatus: OrderStatus): OrderStatus? {
        return Database.getInstanceDatabase().setStatusByRestaurantManager(orderStatus, orderID)
    }

    private fun setNotification(riders: Collection<Rider>, location: Location, notification: Notification) {
        val locations = Database.getInstanceDatabase().getLocations()
        val index = locations.indexOf(location)
        val previousIndex = if (index < 1) null else locations[index - 1]
        val nextIndex = if (index > locations.size - 2) null else locations[index + 1]
        for (rider in riders) {
            if (previousIndex == null) {
                if ((rider.getLocation() == location || rider.getLocation() == nextIndex) && notification.checkCancelledRiderIds(
                        rider.userID
                    ) && rider.getRiderStatus() == RiderStatus.AVAILABLE
                ) {
                    rider.addNotification(notification)
                    tempNotifications.remove(notification)
                    return
                }
            } else if (nextIndex == null) {
                if ((rider.getLocation() == location || rider.getLocation() == previousIndex) && notification.checkCancelledRiderIds(
                        rider.userID
                    ) && rider.getRiderStatus() == RiderStatus.AVAILABLE
                ) {
                    rider.addNotification(notification)
                    tempNotifications.remove(notification)
                    return
                }
            } else if (notification.checkCancelledRiderIds(rider.userID) && rider.getRiderStatus() == RiderStatus.AVAILABLE && (rider.getLocation() == location || rider.getLocation() == previousIndex || rider.getLocation() == nextIndex)
            ) {
                rider.addNotification(notification)
                tempNotifications.remove(notification)
                return
            }
        }
        if (tempNotifications.contains(notification)) {
            return
        }
        tempNotifications.add(notification)
    }

    private fun getNonCancelledOrders(ordersPlacedByCustomer: HashMap<Int, java.util.ArrayList<Order>>): java.util.HashMap<Int, java.util.ArrayList<Order>> {
        val ordersPlaced = HashMap<Int, java.util.ArrayList<Order>>()
        for (orders1 in ordersPlacedByCustomer.values) {
            for (order in orders1) {
                if (order.orderStatus != OrderStatus.CANCELLED) {
                    if (ordersPlaced.containsKey(order.orderID)) {
                        ordersPlaced[order.orderID]!!.add(order)
                    } else {
                        ordersPlaced[order.orderID] = java.util.ArrayList()
                        ordersPlaced[order.orderID]!!.add(order)
                    }
                }
            }
        }
        return ordersPlaced
    }
}