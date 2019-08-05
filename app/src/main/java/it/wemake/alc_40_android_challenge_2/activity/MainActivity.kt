package it.wemake.alc_40_android_challenge_2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import it.wemake.alc_40_android_challenge_2.R
import it.wemake.alc_40_android_challenge_2.adapters.DealAdapter

class MainActivity : AppCompatActivity() {

    private var mFirebaseDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null

    private var dealsRV: RecyclerView? = null
    private var dealsAdapter: DealAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase!!.reference.child("traveldeals")

        dealsAdapter = DealAdapter(mDatabaseReference!!)
        dealsRV!!.adapter = dealsAdapter
        dealsRV!!.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.item_new_travel_deal ->{

                val intent = Intent(this, AddDealActivity::class.java)
                startActivity(intent)

                return true

            }

        }

        return super.onOptionsItemSelected(item)
    }

}
