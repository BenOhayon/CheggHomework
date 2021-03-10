package homework.chegg.com.chegghomework.model.parsers.abs

/**
 * A contract for a general parser that accepts a response string and returns a list of data.
 * This data is generic and is specified by 'T'.
 */
interface Parser<T> {
    fun parse(response: String) : List<T>
}