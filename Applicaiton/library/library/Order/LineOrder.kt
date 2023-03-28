package library.Order

import library.Restaurant.Item

class LineOrder(val item: Item, var quantity:Int) {
    internal fun setQuantity(quantity:Int):Unit{
        this.quantity=quantity
    }
}