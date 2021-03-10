package homework.chegg.com.chegghomework.model.network

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.parsers.SourceAParser
import homework.chegg.com.chegghomework.model.parsers.abs.JsonParser

/**
 * This class is responsible for sending a request to the specified 'url'.
 * For abstraction purposes, the response returns as a string, allowing to retrieve multiple
 * response protocols (rather than JSON).
 * If the request was successful, it passes the response to the specified 'parser' to
 * parse and create a data model.
 *
 * Constructor parameters:
 * =======================
 * url - The url string of the data source you wish to address to.
 * parser - A parser object suitable for parsing the response protocol of the source whose url is 'url'.
 */
class RequestBuilder(private val url: String, private val parser: JsonParser<DataModel>) {

    /**
     * Builds an HTTP request for a data source whose url is 'url'.
     *
     * Parameters:
     * ===========
     * onSuccess - A callback which will be called on request success to pass the
     *             final data model to the caller.
     * onFail - A callback which will be called on request failure to pass the error message
     *          to the caller
     */
    fun buildRequest(onSuccess: (List<DataModel>) -> Unit, onFail: (String?) -> Unit) {
        AndroidNetworking.get(url)
                .build()
                .getAsString(object: StringRequestListener {
                    override fun onResponse(response: String?) {
                        val dataModels = parser.parse(response!!)
                        onSuccess(dataModels)
                    }

                    override fun onError(anError: ANError?) {
                        onFail(anError?.errorBody)
                    }
                })
    }
}