package homework.chegg.com.chegghomework.model.parsers

import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.parsers.abs.JsonParser
import org.json.JSONArray
import org.json.JSONObject

private const val KEY_HEADER = "header"
private const val KEY_DESCRIPTION = "description"
private const val KEY_IMAGE_URL = "picture"
private const val KEY_METADATA = "metadata"
private const val KEY_INNERDATA = "innerdata"
private const val KEY_ARTICLE_WRAPPER = "articlewrapper"

class SourceBParser : JsonParser<DataModel>() {

    override fun parse(response: String) : List<DataModel> {
        val jsonObject = JSONObject(response)
        val dataModelsList = mutableListOf<DataModel>()

        if (has(KEY_METADATA, jsonObject)) {
            val metadataJson = jsonObject.getJSONObject(KEY_METADATA)
            if (has(KEY_INNERDATA, metadataJson)) {
                val innerDataJson = metadataJson.getJSONArray(KEY_INNERDATA)
                for (i in 0 until innerDataJson.length()) {
                    val newDataModel = DataModel(0)
                    val dataModelJson = innerDataJson.getJSONObject(i)
                    if (has(KEY_IMAGE_URL, dataModelJson))
                        newDataModel.imageUrl = dataModelJson.getString(KEY_IMAGE_URL)

                    if (has(KEY_ARTICLE_WRAPPER, dataModelJson)) {
                        val articleWrapperJson = dataModelJson.getJSONObject(KEY_ARTICLE_WRAPPER)
                        if (has(KEY_HEADER, articleWrapperJson))
                            newDataModel.title = articleWrapperJson.getString(KEY_HEADER)

                        if (has(KEY_DESCRIPTION, articleWrapperJson))
                            newDataModel.subtitle = articleWrapperJson.getString(KEY_DESCRIPTION)
                    }

                    dataModelsList.add(newDataModel)
                }
            }
        }

        return dataModelsList
    }
}