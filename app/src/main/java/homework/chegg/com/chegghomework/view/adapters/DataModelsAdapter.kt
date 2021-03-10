package homework.chegg.com.chegghomework.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import homework.chegg.com.chegghomework.R
import homework.chegg.com.chegghomework.model.DataModel

class DataModelsAdapter : RecyclerView.Adapter<DataModelsAdapter.DataModelViewHolder>() {

    private val data: MutableList<DataModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataModelViewHolder {
        return DataModelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))
    }

    override fun onBindViewHolder(holder: DataModelViewHolder, position: Int) {
        val dataModel = data[position]
        holder.setImageUrl(dataModel.imageUrl)
        holder.setTitle(dataModel.title)
        holder.setSubtitle(dataModel.subtitle)
    }

    override fun getItemCount(): Int = data.size

    fun setData(dataModels: List<DataModel>) {
        data.clear()
        data.addAll(dataModels)
    }

    class DataModelViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setImageUrl(imageUrl: String) {
            val image = view.findViewById<ImageView>(R.id.imageView_card_item)
            image.setImageResource(0)
            ImageLoader.getInstance().displayImage(imageUrl, image)
        }

        fun setTitle(titleVal: String) {
            val title = view.findViewById<TextView>(R.id.textView_card_item_title)
            title?.text = titleVal
        }

        fun setSubtitle(subtitleVal: String) {
            val subtitle = view.findViewById<TextView>(R.id.textView_card_item_subtitle)
            subtitle?.text = subtitleVal
        }
    }
}