package library.enums

enum class RiderReturnFunctionalities {
    CANT_PROCESS_SINCE_NO_ORDER_IS_ACCEPTED,
    THAT_ORDER_IS_CANCELLED,
    WAIT_TILL_ORDER_IS_PREPARED,
    PICK_THAT_ORDER_FIRST,
    PICKED,
    DELIVERED
}