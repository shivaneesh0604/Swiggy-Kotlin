package library

import library.Restaurant.Item

class Bill(val orderID:Int) {
    val items:ArrayList<BillItem> = ArrayList()
    inner class BillItem(val item: Item, val quantity:Int){

    }

    internal fun addItem(item: Item, quantity: Int){
        this.items.add(BillItem(item,quantity))
    }

    fun total():Double{
        var total = 0.0
        for (item in items){
            total += item.item.price * item.quantity
        }
        return total
    }
}