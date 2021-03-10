package homework.chegg.com.chegghomework.model.parsers

import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.parsers.abs.JsonParser
import org.json.JSONArray
import org.json.JSONObject

private const val KEY_TOPLINE = "topLine"
private const val KEY_SUBLINE_1 = "subLine1"
private const val KEY_SUBLINE_2 = "subline2"
private const val KEY_IMAGE = "image"

class SourceCParser : JsonParser<DataModel>() {

    override fun parse(response: String) : List<DataModel> {
        val jsonArray = JSONArray(response)
        val dataModelsList = mutableListOf<DataModel>()

        for (i in 0 until jsonArray.length()) {
            val newDataModel = DataModel(0)
            val dataModelJson = jsonArray.getJSONObject(i)
            if (has(KEY_TOPLINE, dataModelJson))
                newDataModel.title = dataModelJson.getString(KEY_TOPLINE)

            if (has(KEY_SUBLINE_1, dataModelJson))
                newDataModel.subtitle = dataModelJson.getString(KEY_SUBLINE_1)

            if (has(KEY_SUBLINE_2, dataModelJson))
                newDataModel.subtitle += dataModelJson.getString(KEY_SUBLINE_2)

            if (has(KEY_IMAGE, dataModelJson))
                newDataModel.imageUrl = dataModelJson.getString(KEY_IMAGE)

            dataModelsList.add(newDataModel)
        }

        return dataModelsList
    }
}