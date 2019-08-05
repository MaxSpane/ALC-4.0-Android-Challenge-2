package it.wemake.alc_40_android_challenge_2.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import it.wemake.alc_40_android_challenge_2.R
import it.wemake.alc_40_android_challenge_2.activity.AddDealActivity
import it.wemake.alc_40_android_challenge_2.model.TravelDeal

class DealAdapter(

    private val databaseReference: DatabaseReference

) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    private val deals: ArrayList<TravelDeal> = ArrayList()

    init {

        databaseReference.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {

                val td = dataSnapshot.getValue(TravelDeal::class.java)
                td!!.id = dataSnapshot.key
                deals.add(td)
                notifyItemInserted(deals.size - 1)

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                notifyItemRemoved(deals.indexOf(dataSnapshot.getValue(TravelDeal::class.java)))
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_deal, parent, false)
        return DealViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        holder.bind(deal)
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var titleTV : TextView? = null
        var descriptionTV : TextView? = null
        var priceTV : TextView? = null
        var deal : TravelDeal? = null

        init {
            titleTV = itemView.findViewById(R.id.tv_title)
            descriptionTV = itemView.findViewById(R.id.tv_description)
            priceTV = itemView.findViewById(R.id.tv_price)

            itemView.setOnClickListener(this)
        }

        fun bind(deal: TravelDeal){
            this.deal = deal
            titleTV!!.text = deal.title
            descriptionTV!!.text = deal.description
            priceTV!!.text = deal.price
        }

        override fun onClick(v: View?) {
            val position = adapterPosition

            val intent = Intent(v!!.context, AddDealActivity::class.java)
            intent.putExtra("deal", deal)
            itemView.context.startActivity(intent)

        }
    }

}