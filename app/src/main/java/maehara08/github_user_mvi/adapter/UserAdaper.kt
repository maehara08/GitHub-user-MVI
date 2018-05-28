package maehara08.github_user_mvi.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import maehara08.github_user_mvi.R
import maehara08.github_user_mvi.data.User

class UserAdaper(private val context: Context,
//                 private val itemClickListener: RecyclerViewHolder.ItemClickListener,
                 private var userList: List<User>
) : RecyclerView.Adapter<UserAdaper.RecyclerViewHolder>() {

    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
        holder?.let {
            userList.get(position).run {
                it.nameText.text = login
                Picasso.get().load(avatarUrl).into(it.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val mView: CardView = layoutInflater.inflate(R.layout.item_user_view, parent, false) as CardView

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                //                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return RecyclerViewHolder(mView)
    }

    fun replaceData(users: List<User>) {
        this.userList = users
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // 独自に作成したListener
        interface ItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

        val nameText: TextView = view.findViewById(R.id.item_user_name)
        val image: ImageView = view.findViewById(R.id.item_user_image)

        init {
//            nameText.text =
//            image.setImageResource(R.mipmap.ic_launcher)
        }

    }
}