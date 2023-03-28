import library.Users.Customer
import library.Database.DatabaseManager
import library.Users.RestaurantManager
import library.Users.Rider
import library.Users.User
import library.enums.Role
import library.enums.UserAddition
import java.util.*

class ApplicationUI(private val databaseManager: DatabaseManager) {

    val sc = Scanner(System.`in`)

    fun logIN() {
        println("enter user name")
        val userName: String = sc.nextLine()
        println("enter password")
        val passWord: String = sc.nextLine()
        val user: User? = databaseManager.getUser(userName, passWord)
        if (user != null) {
            when (user.getRole()) {
                Role.CUSTOMER -> {
                    val ui: UI = CustomerUI(user as Customer)
                    ui.entersUI()
                }

                Role.RIDER -> {
                    val ui: UI = RiderUI(user as Rider)
                    ui.entersUI()
                }

                Role.RESTAURANT_MANAGER -> {
                    val ui: UI = RestaurantManagerUI(user as RestaurantManager)
                    ui.entersUI()
                }
            }
        } else {
            println("invalid credentials")
        }
    }

    fun signUP() {
        println("welcome to login page")
        println("enter user name")
        val userName: String = sc.nextLine()
        println("enter password")
        val passWord: String = sc.nextLine()
        println("enter name")
        val name: String = sc.nextLine()
        println("enter which role you need to register")
        println("0,CUSTOMER \n" +
                "1,RIDER")
        val registerRole: Int = Utils.inputVerification(Role.values().size)
        when (val role: Role = Role.values()[registerRole]) {
            Role.CUSTOMER -> {
                val userAddition: UserAddition = databaseManager.addCustomer(userName, passWord, role, name)
                if (userAddition == UserAddition.USER_ADDED) {
                    println(userAddition)
                    logIN()
                } else {
                    println(userAddition)
                }
            }

            Role.RIDER -> {
                val userAddition1: UserAddition = databaseManager.addRider(userName, passWord, role, name)
                if (userAddition1 == UserAddition.USER_ADDED) {
                    println("added successfully")
                    logIN()
                } else {
                    println(userAddition1)
                }
            }

            else -> {
            }
        }
    }
}