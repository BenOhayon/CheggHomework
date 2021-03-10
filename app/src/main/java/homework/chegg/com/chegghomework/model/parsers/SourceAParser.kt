package homework.chegg.com.chegghomework.model.parsers

import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.parsers.abs.JsonParser
import org.json.JSONArray
import org.json.JSONObject

private const val KEY_STORIES = "stories"
private const val KEY_TITLE = "title"
private const val KEY_SUBTITLE = "subtitle"
private const val KEY_IMAGE_URL = "imageUrl"

class SourceAParser : JsonParser<DataModel>() {

    override fun parse(response: String) : List<DataModel> {
        val jsonObject = JSONObject(response)
        val dataModelList = mutableListOf<DataModel>()

        if (has(KEY_STORIES, jsonObject)) {
            val storiesJson = jsonObject.getJSONArray(KEY_STORIES)

            for (i in 0 until storiesJson.length()) {
                val newDataModel = DataModel(0)
                val dataModelJson = storiesJson.getJSONObject(i)

                if (has(KEY_TITLE, dataModelJson))
                    newDataModel.title = dataModelJson.getString(KEY_TITLE)

                if (has(KEY_SUBTITLE, dataModelJson))
                    newDataModel.subtitle = dataModelJson.getString(KEY_SUBTITLE)

                if (has(KEY_IMAGE_URL, dataModelJson))
                    newDataModel.imageUrl = dataModelJson.getString(KEY_IMAGE_URL)

                dataModelList.add(newDataModel)
            }
        }

        return dataModelList
    }
}