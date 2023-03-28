package library.Database

import library.Application.ApplicationFactory
import library.Users.Customer
import library.Users.User
import library.Order.Order
import library.Restaurant.Item
import library.Restaurant.Restaurant
import library.UserCredentials
import library.Users.RestaurantManager
import library.Users.Rider
import library.enums.*
import library.pojo.CustomerDetails
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Database private constructor() {
    companion object {
        private val instance = Database()
        private val users: HashMap<UserCredentials, User> = HashMap<UserCredentials, User>().also {
            it[UserCredentials("shiva1234", "123456789")] =
                Customer("CUSTOMER_1001", ApplicationFactory.getCustomerApplication(), Role.CUSTOMER, "shiva")
        }
        private val listOfRestaurants: HashMap<Int, Restaurant> = HashMap()
        private val orders: HashMap<String, HashMap<Int, ArrayList<Order>>> = HashMap()
        private val locations: ArrayList<Location> = ArrayList()

        init {
//            val restaurant = Restaurant(Location.AREA2, "anandha bhavan", 1)
//            listOfRestaurants[1] = restaurant
//            users[UserCredentials("shiva1234", "123456789")] =
//                Customer("CUSTOMER_1001", ApplicationFactory.getCustomerApplication(), Role.CUSTOMER, "shiva");
//            listOfRestaurants[2] = Restaurant(Location.AREA3,"Aaryas",2)
//
//            users[UserCredentials("sathya1234","123456789")] = Rider("RIDER_1000",ApplicationFactory.getRiderApplication(),Role.RIDER,"sathya")
//            users[UserCredentials("sankar1234","123456789")] = RestaurantManager("RESTAURANT_MANAGER_1002",restaurant,ApplicationFactory.getRestaurantManagerApplication(),Role.RESTAURANT_MANAGER,"sankar")
            val restaurant = Restaurant(Location.AREA2, "anandha bhavan", 1)
            listOfRestaurants[1] = restaurant
            val rider = Rider("RIDER_1000", ApplicationFactory.getRiderApplication(), Role.RIDER, "sathya")
            rider.setLocation(Location.AREA1)
            users[UserCredentials("sathya1234", "123456789")] = rider
            users[UserCredentials("sankar1234", "123456789")] = RestaurantManager(
                "RESTAURANT_MANAGER_1002",
                restaurant,
                ApplicationFactory.getRestaurantManagerApplication(),
                Role.RESTAURANT_MANAGER,
                "sankar"
            )
            val rider1 = Rider("RIDER_1003", ApplicationFactory.getRiderApplication(), Role.RIDER, "durga_devi")
            rider1.setLocation(Location.AREA2)
            users[UserCredentials("dd1234", "123456789")] = rider1
            val rider2 = Rider("RIDER_1004", ApplicationFactory.getRiderApplication(), Role.RIDER, "devi")
            rider2.setLocation(Location.AREA2)
            users[UserCredentials("devi1234", "123456789")] = rider2
            val item = Item("rice".uppercase(Locale.getDefault()), 100.0, Dietary.VEG, Course.MAINCOURSE, Timing.AFTERNOON)
            val item2 = Item("chicken".uppercase(Locale.getDefault()), 120.0, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON)
            locations.add(Location.AREA1)
            locations.add(Location.AREA2)
            locations.add(Location.AREA3)
            locations.add(Location.AREA4)
            locations.add(Location.AREA5)
            restaurant.menuItems.addMenuItems(item)
            restaurant.menuItems.addMenuItems(item2)

            val restaurant1 = Restaurant(Location.AREA1, "Aaryas", 2)
            listOfRestaurants[2] = restaurant1

            users[UserCredentials("kavs1234", "123456789")] = RestaurantManager(
                "RESTAURANT_MANAGER_1003",
                restaurant1,
                ApplicationFactory.getRestaurantManagerApplication(),
                Role.RESTAURANT_MANAGER,
                "kaviya"
            )
            val item4 = Item("rice".uppercase(Locale.getDefault()), 100.0, Dietary.VEG, Course.MAINCOURSE, Timing.AFTERNOON)
            val item3 = Item("chickenRice".uppercase(Locale.getDefault()), 180.0, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON)
            restaurant1.menuItems.addMenuItems(item4)
            restaurant1.menuItems.addMenuItems(item3)

            val restaurant2 = Restaurant(Location.AREA3, "Al Taj", 3)
            listOfRestaurants[3] = restaurant2

            users[UserCredentials("dinesh1234", "123456789")] = RestaurantManager(
                "RESTAURANT_MANAGER_1004",
                restaurant2,
                ApplicationFactory.getRestaurantManagerApplication(),
                Role.RESTAURANT_MANAGER,
                "dinesh"
            )
            val item5 = Item("mutton".uppercase(Locale.getDefault()), 200.0, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON)
            val item6 = Item("beef".uppercase(Locale.getDefault()), 180.0, Dietary.NON_VEG, Course.MAINCOURSE, Timing.NIGHT)

            restaurant2.menuItems.addMenuItems(item5)
            restaurant2.menuItems.addMenuItems(item6)

        }

        internal fun getInstanceDatabase(): Database {
            return instance
        }

    }

    internal fun addUser(user: User, userName:String, passWord:String): User? {
        val userCredentials: Collection<UserCredentials> = users.keys
        for (userCredential in userCredentials) {
            if (userCredential.userName == userName)
                return null
        }
        users[UserCredentials(userName, passWord)] = user
        return user

    }

    internal fun getUser(userName: String,passWord: String): User?{
        val userCredentials: Collection<UserCredentials> = users.keys
        for (userCredential in userCredentials) {
            if (userCredential.userName == userName) {
                if (userCredential.passWord == passWord) {
                    return users[userCredential]
                }
            }
        }
        return null
    }

    internal fun getAllRestaurants():HashMap<Int,Restaurant>{
        return HashMap(listOfRestaurants)
    }

    internal fun getCustomerDetails(customerID:String):CustomerDetails{
        val users:Collection<User> = users.values
        lateinit var customerDetails:CustomerDetails
        for (user in users){
            if (user.userID == customerID){
                val customer = user as Customer;
                customerDetails = CustomerDetails(customer.getLocation(),customer.userID)
            }
        }
        return customerDetails

    }

    internal fun addOrder(customerID:String,tempOrders:Order,restaurantID:Int){
        val ordersCustomer : HashMap<Int,ArrayList<Order>>? = orders[customerID]
        if(ordersCustomer!=null){
            val ordersRestaurant = ordersCustomer[restaurantID]
            if (ordersRestaurant!=null){
                ordersRestaurant.add(tempOrders)
            }
            else{
                ordersCustomer[restaurantID] = ArrayList()
                ordersCustomer[restaurantID]!!.add(tempOrders)
            }
        }
        else{
            orders[customerID] = HashMap()
            val orders1 = orders[customerID]!!
            orders1[restaurantID] = java.util.ArrayList()
            val orders3 = orders1[restaurantID]!!
            orders3.add(tempOrders)
        }
    }

    internal fun getOrderPlaced(customerID: String):HashMap<Int, ArrayList<Order>>?{
        val orderPlaced : HashMap<Int,ArrayList<Order>>? = orders[customerID]
        if (orderPlaced!=null){
            return HashMap(orderPlaced)
        }
        return null


    }

    internal fun setStatusByRider(riderFunctionalityStatus: RiderFunctionalityStatus,orderID:Int):RiderFunctionalityStatus?{
        for (customerOrder in orders.values) {
            for (customerRestaurantOrder in customerOrder.values) {
                for (order in customerRestaurantOrder ) {
                    if (order.orderID == orderID) {
                        order.setRiderAcceptance(riderFunctionalityStatus)
                        return riderFunctionalityStatus
                    }
                }
            }
        }
        return null

    }

    internal fun setStatusByRestaurantManager(orderStatus: OrderStatus, orderID: Int):OrderStatus?{
        for (customerOrder in orders.values) {
            for (customerRestaurantOrder in customerOrder.values) {
                for (order in customerRestaurantOrder) {
                    if (order.orderID == orderID) {
                        order.setStatus(orderStatus)
                        return orderStatus
                    }
                }
            }
        }
        return null


    }

    internal fun getAllRiders():ArrayList<Rider>{
        val users2 = ArrayList<Rider>()
        for (user in users.values) {
            if (user.getRole() == Role.RIDER) {
                users2.add(user as Rider)
            }
        }
        return users2
    }

    internal fun getOrder(orderID: Int): Order? {
        for (innerMap in orders.values) {
            for (list in innerMap.values) {
                for (order in list) {
                    if (order.orderID == orderID) {
                        return order
                    }
                }
            }
        }
        return null
    }

    internal fun getLocations():ArrayList<Location>{
        return ArrayList(locations)
    }

}



