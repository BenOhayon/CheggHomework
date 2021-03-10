package homework.chegg.com.chegghomework.model.parsers.abs

import org.json.JSONObject

/**
 * This class represents a json parser.
 * It has a method for checking an existence of a key inside the JSON.
 */
abstract class JsonParser<T> : Parser<T> {

    fun has(key: String, jsonObject: JSONObject) : Boolean = !jsonObject.isNull(key)
}