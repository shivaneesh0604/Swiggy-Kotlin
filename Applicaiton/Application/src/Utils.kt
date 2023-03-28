import java.util.*

class Utils {
    companion object {
        var sc = Scanner(System.`in`)
        fun print(enums: Array<out Enum<*>>) {
            for (i in enums.indices) {
                println(i.toString() + "," + enums[i])
            }
        }

        fun inputVerification(noOfOptions: Int): Int {
            val input = sc.nextLine()
            return if (input.matches("[0-9]+".toRegex()) && input.toInt() < noOfOptions) {
                input.toInt()
            } else {
                println("enter between 0 and " + (noOfOptions - 1))
                inputVerification(noOfOptions)
            }
        }
    }
}