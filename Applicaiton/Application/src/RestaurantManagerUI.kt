import Enums.RestaurantManagerOptions
import library.Order.LineOrder
import library.Restaurant.Item
import library.Users.RestaurantManager
import library.enums.*
import java.util.*

class RestaurantManagerUI(private val restaurantManager: RestaurantManager) : UI {
    private var sc = Scanner(System.`in`)

    override fun entersUI() {
        MainLoop@ while (true) {
            val items: HashMap<String, Item> = restaurantManager.getItems()
            Utils.print(RestaurantManagerOptions.values())
            val restaurantManagerOptionsValues = Utils.inputVerification(RestaurantManagerOptions.values().size)
            when (RestaurantManagerOptions.values()[restaurantManagerOptionsValues]) {
                RestaurantManagerOptions.ADD_FOOD_TO_RESTAURANT -> {
                    addFoodToRestaurant()
                }

                RestaurantManagerOptions.ALTER_MENU_ITEMS -> {
                    alterMenuItems(items)
                }

                RestaurantManagerOptions.ADD_TIMING_FOR_FOOD -> {
                    addTimingForFood(items)
                }

                RestaurantManagerOptions.REMOVE_TIMING_FOR_FOOD -> {
                    removeTimingForFood(items)
                }

                RestaurantManagerOptions.DELETE_MENU_ITEMS -> {
                    deleteMenuItems(items)
                }

                RestaurantManagerOptions.SHOW_ALL_FOOD_ITEMS -> {
                    printFoodItems(items)
                }

                RestaurantManagerOptions.GET_TIME_SPECIFIC_FOOD_ITEMS -> {
                    getTimeSpecificFoodItems()
                }

                RestaurantManagerOptions.SET_RESTAURANT_STATUS -> {
                    setRestaurantStatus()
                }

                RestaurantManagerOptions.VIEW_ORDERS_GOT -> {
                    viewOrder(restaurantManager.viewOrderGot())
                }

                RestaurantManagerOptions.SET_STATUS_PREPARING -> {
                    setStatus(OrderStatus.PREPARING)
                }

                RestaurantManagerOptions.SET_STATUS_PREPARED -> {
                    setStatus(OrderStatus.PREPARED)
                }

                RestaurantManagerOptions.BACK -> {
                    break@MainLoop
                }
            }
        }
    }

    private fun addFoodToRestaurant() {
        println("enter food name to add")
        val foodName: String = sc.nextLine().uppercase()
        println("enter price for $foodName")
        val price: Double = sc.nextDouble()
        sc.nextLine()
        println("enter which dietary it is")
        Utils.print(Dietary.values())
        val dietaryOption = Utils.inputVerification(Dietary.values().size)
        val dietary: Dietary = Dietary.values()[dietaryOption]
        Utils.print(Course.values())
        val courseOption = Utils.inputVerification(Course.values().size)
        val course: Course = Course.values()[courseOption]
        Utils.print(Timing.values())
        val timingOption = Utils.inputVerification(Timing.values().size)
        val timing: Timing = Timing.values()[timingOption]
        println(restaurantManager.addFood(foodName, price, dietary, course, timing))
    }

    private fun alterMenuItems(items: HashMap<String, Item>) {
        println("available items are")
        printFoodItems(items)
        println("enter foodName to which you are going to alter in restaurant")
        val foodName = sc.nextLine().uppercase(Locale.getDefault())
        if (items.containsKey(foodName)) {
            println("enter price to alter ")
            println("the original price before altering is " + items[foodName]!!.getPrice())
            val price = sc.nextDouble()
            println(restaurantManager.alterMenuItems(items[foodName]!!, price))
        } else {
            println("the foodName$foodName is not available this restaurant")
        }
    }

    private fun addTimingForFood(items: HashMap<String, Item>) {
        println("available items are")
        printFoodItems(items)
        println("enter which food you need to set timing for")
        val foodName = sc.nextLine().uppercase(Locale.getDefault())
        val item: Item? = restaurantManager.getItems()[foodName]
        if (item != null) {
            val timing3: ArrayList<Timing> = item.timingsList
            if (timing3.size > 0) {
                println("$foodName has timing of ")
                for (timing1 in timing3) {
                    print("$timing1, ")
                }
            } else {
                println("no timing set for this foodItem")
            }
            println("which timing to add")
            Utils.print(Timing.values())
            val timingOption = Utils.inputVerification(Timing.values().size)
            val timing = Timing.values()[timingOption]
            if (timing3.contains(timing)) {
                println("already has this timing so select another timing other than $timing")
                return
            }
            println(restaurantManager.setTimingForFood(item, timing))
        } else {
            println("wrong foodName")
        }
    }

    private fun removeTimingForFood(items: HashMap<String, Item>) {
        println("available items are")
        printFoodItems(items)
        println("enter which food you need to remove timing for")
        val foodName = sc.nextLine().uppercase()
        val timings: ArrayList<Timing>? = restaurantManager.getItems()[foodName]?.timingsList
        if (timings != null && timings.size > 0) {
            println("$foodName has timing of ")
            for (timing1 in timings) {
                println(timing1)
            }
        } else {
            println("no timing set for this foodItem")
            return
        }

        println("which timing to change")
        Utils.print(Timing.values())
        val timingOption = Utils.inputVerification(Timing.values().size)
        val timing = Timing.values()[timingOption]
        if (timings.contains(timing)) {
            println(restaurantManager.removeTiming(items[foodName]!!, timing))
        } else {
            println("cant process since this Timing is not set for this food Item")
        }
    }


    private fun deleteMenuItems(items: HashMap<String, Item>) {
        println("available items are")
        printFoodItems(items)
        println("enter which food to delete from menu")
        val foodName = sc.nextLine().uppercase()
        if (items.containsKey(foodName)) {
            println(restaurantManager.deleteMenuItems(items[foodName]!!))
        } else {
            println("no food with this foodName to delete")
        }
    }

    private fun getTimeSpecificFoodItems() {
        println("enter which timing you need the food Items")
        Utils.print(Timing.values())
        val timingOption1 = Utils.inputVerification(Timing.values().size)
        val timing1 = Timing.values()[timingOption1]
        val items = restaurantManager.getItems(timing1)
        if (items.size > 0) {
            printFoodItems(items)
        } else {
            println("no items found in this timing")
        }
    }

    private fun setRestaurantStatus() {
        println("${restaurantManager.restaurant.getRestaurantStatus()} is the status of restaurant")
        println("enter to which status to change")
        Utils.print(RestaurantStatus.values())
        val restaurantStatusOption = Utils.inputVerification(RestaurantStatus.values().size)
        val restaurantStatus: RestaurantStatus = RestaurantStatus.values()[restaurantStatusOption]
        println("status is " + restaurantManager.setRestaurantStatus(restaurantStatus))
    }

    private fun viewOrder(order: HashMap<Int, ArrayList<LineOrder>>) {
        if (order.size == 0) {
            println("no orders found ")
            return
        }
        println("orders got are")
        for ((key, lineOrder1) in order) {
            println("orderID is : $key")
            for (lineOrder2 in lineOrder1) {
                println(
                    "food name is " + lineOrder2.item.foodName + " quantity is " + lineOrder2.quantity
                )
            }
        }
    }

    private fun setStatus(orderStatus: OrderStatus) {
        val order = restaurantManager.viewOrderGot()
        if (order.size == 0) {
            println("no orders found")
            return
        }
        viewOrder(order)
        println("enter orderID to set order as preparing")
        val orderID = sc.nextLine()
        try {
            val orderStatusChanged: OrderStatus? = restaurantManager.setStatus(orderID.toInt(), orderStatus)
            if (orderStatusChanged == null) {
                println("wrong orderID")
                return
            }
            println("order status changed to $orderStatusChanged")
        } catch (e: NumberFormatException) {
            println("Enter only numbers ")
        }
    }

    private fun printFoodItems(items: HashMap<String, Item>) {
        val items1: Collection<Item> = items.values
        for (item in items1) {
            println("foodName is " + item.foodName + " price is " + item.getPrice() + " course is " + item.course + " dietary is " + item.dietary + " timing is " + item.timingsList)
        }
    }


}