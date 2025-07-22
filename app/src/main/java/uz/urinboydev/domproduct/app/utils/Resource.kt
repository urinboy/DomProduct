package uz.urinboydev.domproduct.app.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val errors: Map<String, List<String>>? = null) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(data: T?, message: String, errors: Map<String, List<String>>? = null): Resource<T> {
            return Resource(Status.ERROR, data, message, errors)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
