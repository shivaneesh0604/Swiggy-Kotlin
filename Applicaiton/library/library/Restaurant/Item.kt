package library.Restaurant

import library.enums.Course
import library.enums.Dietary
import library.enums.RestaurantManagerFunctions
import library.enums.Timing

class Item(
    val foodName: String,
    internal var price: Double,
    val dietary: Dietary,
    val course: Course,
    timings: ArrayList<Timing>
) {
    val timingsList: ArrayList<Timing> = timings

    internal constructor(
        foodName: String,
        price: Double,
        dietary: Dietary,
        course: Course,
        timing: Timing
    ) : this(foodName, price, dietary, course, arrayListOf<Timing>().apply { add(timing) })

    internal fun setPrice(price: Double): RestaurantManagerFunctions {
        this.price = price
        return RestaurantManagerFunctions.PRICE_CHANGED
    }

    internal fun setTiming(timing: Timing): RestaurantManagerFunctions {
        this.timingsList.add(timing)
        return RestaurantManagerFunctions.TIMING_ADDED
    }

    internal fun removeTiming(timing: Timing): RestaurantManagerFunctions {
        this.timingsList.remove(timing)
        return RestaurantManagerFunctions.TIMING_REMOVED
    }

    fun getPrice():Double{
        return price
    }
}