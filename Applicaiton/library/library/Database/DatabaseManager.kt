package library.Database

import library.Application.ApplicationFactory
import library.Database.Database
import library.Users.Customer
import library.Users.User
import library.Users.Rider
import library.enums.Role
import library.enums.UserAddition

class DatabaseManager {
    val database: Database = Database.getInstanceDatabase()
    private var userID: Int = 100

    fun addCustomer(userName: String, passWord: String, role: Role, name: String): UserAddition {
        val user: User = Customer("CUSTOMER_" + userID++, ApplicationFactory.getCustomerApplication(), role, name)
        val user1 = database.addUser(user, userName, passWord)
        return if (user1 != null) {
            UserAddition.USER_ADDED
        } else {
            UserAddition.USERNAME_ALREADY_EXIST
        }
    }

    fun addRider(userName: String, passWord: String, role: Role, name: String): UserAddition {
        val user: User = Rider("RIDER_" + userID++, ApplicationFactory.getRiderApplication(), role, name)
        val user1 = database.addUser(user, userName, passWord)
        return if (user1 != null) {
            UserAddition.USER_ADDED
        } else {
            UserAddition.USERNAME_ALREADY_EXIST
        }
    }

    fun getUser(userName: String, passWord: String): User? {
        return database.getUser(userName, passWord)
    }
}