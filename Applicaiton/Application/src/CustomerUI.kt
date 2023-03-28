import Enums.CustomerOptions
import Enums.CustomerOrderOptions
import library.Bill
import library.Order.LineOrder
import library.Order.Order
import library.Restaurant.Item
import library.Restaurant.Restaurant
import library.Users.Customer
import library.enums.Location
import library.enums.OrderStatus
import library.enums.Status_of_Restaurant_Orders
import library.enums.Timing
import java.util.*

class CustomerUI(private val customer: Customer) : UI {
    private var sc = Scanner(System.`in`)

    override fun entersUI() {
        print("Enter which Location you are")
        Utils.print(Location.values())
        val locationOption = Utils.inputVerification(Location.values().size)
        val location = Location.values()[locationOption]
        customer.setLocation(location)
        println("enter Which Timing you are entering")
        Utils.print(Timing.values())
        val timingOption = Utils.inputVerification(Timing.values().size)
        val timing: Timing = Timing.values()[timingOption]
        MainLoop@ while (true) {
            SecondLoop@ while (true) {
                Utils.print(CustomerOptions.values())
                val customerInput = Utils.inputVerification(CustomerOptions.values().size)
                when (CustomerOptions.values()[customerInput]) {
                    CustomerOptions.ENTERS_RESTAURANT -> {
                        entersRestaurant(location, timing)
                    }

                    CustomerOptions.VIEW_ORDER_PLACED -> {
                        viewOrder(customer.viewOrdersPlaced())
                    }

                    CustomerOptions.CANCEL_ORDER -> {
                        cancelOrder(location)
                    }

                    CustomerOptions.GO_BACK -> {
                        break@SecondLoop
                    }
                }
            }

            println("press 1 to go back of application,press another number to go back again to customer Options")
            val back: String = sc.nextLine()
            try {
                if (back.toInt() == 1) {
                    break
                } else {
                    continue@MainLoop
                }
            } catch (e: NumberFormatException) {
                continue@MainLoop
            }
        }
    }

    private fun cancelOrder(location: Location) {
        val listOfRestaurants: HashMap<Int, Restaurant> = customer.getAllRestaurant(location)
        val ordersPlaced = customer.viewOrdersPlaced()
        viewOrder(ordersPlaced)
        if (ordersPlaced != null) {
            println("enter orderID to cancelOrder")
            val orderID = sc.nextLine()
            for (order in ordersPlaced) {
                if (order.orderID == orderID.toInt()) {
                    val restaurantID: Int = order.restaurantID
                    if (order.getOrderStatus() == OrderStatus.PREPARED) {
                        println("you will be charged with 50% of bill amount press 1 to cancel the order other value to wait")
                        val orderDeletion = sc.nextLine()
                        if (orderDeletion.toInt() == 1) {
                            println(customer.cancelOrder(orderID.toInt(), listOfRestaurants[restaurantID]!!))
                        }
                        return
                    } else if (order.getOrderStatus() == OrderStatus.ORDER_PLACED) {
                        println(customer.cancelOrder(orderID.toInt(), listOfRestaurants[restaurantID]!!))
                        return
                    }
                }
            }
            println("WRONG_ORDER_ID")
        }
    }

    private fun viewOrder(ordersPlaced: ArrayList<Order>?) {
        if (ordersPlaced != null) {
            for (order in ordersPlaced) {
                println("orderID is " + order.orderID)
                val orders: HashMap<String, LineOrder> = order.orders
                val lineOrders: Collection<LineOrder> = orders.values
                for (lineOrder in lineOrders) {
                    println(" ordered food are " + lineOrder.item.foodName + " quantity is " + lineOrder.quantity)
                }
                println("ordered shop name is " + order.restaurantName + " status of food is " + order.getOrderStatus() + " rider acceptance status is " + order.getRiderFunctionalityStatus())
            }
        } else {
            println("no orders found")
        }
    }

    private fun entersRestaurant(location: Location, timing: Timing) {
        try {
            val listOfRestaurants: HashMap<Int, Restaurant> = customer.getAllRestaurant(location)
            showAllRestaurant(listOfRestaurants)
            println("enter which restaurantID to enter")
            val restaurantID = sc.nextLine()
            if (!listOfRestaurants.containsKey(restaurantID.toInt())) {
                println("wrong restaurantID")
                return
            }

            CustomerOrderOptions@ while (true) {
                val items: HashMap<String, Item> =
                    customer.enterRestaurant(listOfRestaurants[restaurantID.toInt()]!!, timing)
                Utils.print(CustomerOrderOptions.values())
                val customerOrderInput = Utils.inputVerification(CustomerOrderOptions.values().size)
                when (CustomerOrderOptions.values()[customerOrderInput]) {
                    CustomerOrderOptions.ADD_ORDER -> {
                        addOrder(items, listOfRestaurants, restaurantID)
                    }

                    CustomerOrderOptions.REMOVE_ORDER -> {
                        removeOrder(items, restaurantID, listOfRestaurants)
                    }

                    CustomerOrderOptions.VIEW_ITEMS_IN_CART -> {
                        viewItemsInCart(restaurantID)
                    }

                    CustomerOrderOptions.CONFIRM_ORDER -> {
                        confirmOrder(listOfRestaurants, restaurantID)
                    }

                    CustomerOrderOptions.BACK -> {
                        break@CustomerOrderOptions
                    }

                }
            }

        } catch (e: NumberFormatException) {
            println("wrong restaurant ID entered enter the available numbers");
        }
    }

    private fun addOrder(
        items: HashMap<String, Item>,
        listOfRestaurants: HashMap<Int, Restaurant>,
        restaurantID: String
    ) {
        showAvailableMenu(items.values)
        val cartItems: HashMap<Int, Order>? = customer.viewItemsInCart()
        println("enter which food to add")
        val foodName = sc.nextLine().uppercase()

        if (!items.containsKey(foodName)) {
            println("wrong foodName to add")
            return
        }

        if (cartItems == null || cartItems.size == 0 || cartItems.containsKey(restaurantID.toInt())) {
            println(
                customer.addOrder(
                    items[foodName]!!,
                    listOfRestaurants[restaurantID.toInt()]!!
                )
            )
            return
        } else {
            val order: Order = cartItems.values.iterator().next()
            println("" + Status_of_Restaurant_Orders.ANOTHER_ORDER_ADDED_IN_A_RESTAURANT + " they are")
            cartItems[order.restaurantID]?.let { showCartItems(it) }
            println("your cart contains dishes from restaurantID " + order.restaurantID + " do you want to discard that selection and add dishes from restaurantID " + restaurantID + "... if yes press 1 else other")
            val orderConfirmation = sc.nextLine()

            if (orderConfirmation.toInt() == 1) {
                println(
                    customer.addOrder(
                        items[foodName]!!,
                        listOfRestaurants[restaurantID.toInt()]!!
                    )
                )
            }
        }
//
//        if (cartItems == null || cartItems.size == 0) {
//            println("no cart items found")
//        } else {
//            val order: Order = cartItems.values.iterator().next()
//            println("there are some other food items ordered in another restaurant")
//            cartItems[order.restaurantID]?.let { showCartItems(it) }
//        }
//
//        println("enter which food to add")
//        val foodName = sc.nextLine().uppercase()
//
//        if (!items.containsKey(foodName)) {
//            println("wrong foodName to add")
//            return
//        }
//
//        if (cartItems == null || cartItems.size == 0 || cartItems.containsKey(restaurantID.toInt())) {
//            println(
//                customer.addOrder(
//                    items[foodName]!!,
//                    listOfRestaurants[restaurantID.toInt()]!!
//                )
//            )
//            return
//        }
//
//        println(Status_of_Restaurant_Orders.ANOTHER_ORDER_ADDED_IN_A_RESTAURANT)
//        val order: Order = cartItems.values.iterator().next()
//
//        println(order)
//        println("your cart contains dishes from restaurantID " + order.restaurantID + " do you want to discard that selection and add dishes from" + restaurantID + "... if yes press 1 else other")
//        val orderConfirmation = sc.nextLine()
//
//        if (orderConfirmation.toInt() == 1) {
//            println(
//                customer.addOrder(
//                    items[foodName]!!,
//                    listOfRestaurants[restaurantID.toInt()]!!
//                )
//            )
//        }


    }

    private fun removeOrder(
        items: HashMap<String, Item>,
        restaurantID: String,
        listOfRestaurants: HashMap<Int, Restaurant>
    ) {
        val cartItems = customer.viewItemsInCart()
        if (cartItems == null || cartItems.size == 0) {
            println("no items in cart")
            return
        }
        val order = cartItems[restaurantID.toInt()]
        if (order != null) {
            showCartItems(order)
        }
        println("enter which food to delete")
        val foodName1 = sc.nextLine().uppercase()
        if (items.containsKey(foodName1)) {
            println(customer.removeOrder(items[foodName1]!!, listOfRestaurants[restaurantID.toInt()]!!))
        } else {
            println("wrong foodName to delete")
        }
    }

    private fun viewItemsInCart(restaurantID: String) {
        val cartItems = customer.viewItemsInCart()
        if (cartItems == null || cartItems.size == 0) {
            println("no cart items found")
        } else {
            showCartItems(cartItems[restaurantID.toInt()]!!)
        }
    }

    private fun confirmOrder(listOfRestaurants: HashMap<Int, Restaurant>, restaurantID: String) {
        val cartItems = customer.viewItemsInCart()
        if (cartItems == null || cartItems.size == 0) {
            println("no orders found first add an order")
            return
        }

        val bill = customer.confirmOrder(listOfRestaurants[restaurantID.toInt()]!!)
        showBill(bill)

        println("press 1 to place order")
        val payBill = sc.nextLine()
        try {
            if (payBill.toInt() != 1) {
                println("not placed the order")
                return
            }
            val orderStatus: OrderStatus? = customer.placeOrder(listOfRestaurants[restaurantID.toInt()]!!)
            println("order placed and status of food is $orderStatus")
        } catch (e: NumberFormatException) {
            println("enter numbers to process")
            return
        }
        return
    }

    private fun showAllRestaurant(listOfRestaurant: HashMap<Int, Restaurant>) {
        if (listOfRestaurant.size == 0) {
            println("no restaurants found in this timing")
            return
        }
        println("restaurantID    restaurantName    Location")
        for (restaurant in listOfRestaurant.values) {
            println("${restaurant.restaurantID} \t\t ${restaurant.restaurantName} \t\t ${restaurant.location}")
        }
    }

    private fun showAvailableMenu(availableItems: Collection<Item>) {
        println("Food Name     Price")
        for (item in availableItems) {
            println(item.foodName + "   \t" + item.getPrice())
        }
    }

    private fun showCartItems(cartItems: Order) {
        println("food items ordered in restaurantID "+cartItems.restaurantID+" are")
        val lineOrders: HashMap<String, LineOrder> = cartItems.orders
        println("FoodName \t  Quantity")
        for (lineOrder in lineOrders.values) {
            println(lineOrder.item.foodName + "\t " + lineOrder.quantity)
        }
    }

    private fun showBill(bill: Bill) {
        val items = bill.items
        println("food name    quantity   price")
        for (billItem in items) {
            println(billItem.item.foodName + "\t" + billItem.quantity + "\t" + billItem.item.getPrice())
        }
        println("the total is " + bill.total())
    }
}