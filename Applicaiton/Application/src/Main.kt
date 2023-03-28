import javafx.application.Application
import library.Database.DatabaseManager
import java.util.*

fun main(){
    val sc = Scanner(System.`in`)
    val databaseManager = DatabaseManager()
    val applicationUI = ApplicationUI(databaseManager)
    Mainloop@ while (true) {
        println("enter 1 for login 2 for signup other number for exit")
        val applicationUIAccess = sc.nextLine()
        try {
            if (applicationUIAccess.toInt() == 1) {
                applicationUI.logIN()
            } else if (applicationUIAccess.toInt() == 2) {
                applicationUI.signUP()
            } else {
                break@Mainloop
            }
        } catch (e: NumberFormatException) {
            println("enter 1 or 2")
        }
    }
}