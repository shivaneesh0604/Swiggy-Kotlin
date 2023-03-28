package library.Application

import library.Interfaces.CustomerApplication
import library.Interfaces.RestaurantManagerApplication
import library.Interfaces.RiderApplication

internal object ApplicationFactory {
    fun getCustomerApplication(): CustomerApplication {
        return Application()
    }

    fun getRiderApplication(): RiderApplication {
        return Application()
    }

    fun getRestaurantManagerApplication(): RestaurantManagerApplication {
        return Application()
    }
}