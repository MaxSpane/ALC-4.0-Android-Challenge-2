package it.wemake.alc_40_android_challenge_2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import it.wemake.alc_40_android_challenge_2.R
import it.wemake.alc_40_android_challenge_2.model.TravelDeal

class AddDealActivity : AppCompatActivity() {

    private var mFirebaseDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null

    private var mTitleET : EditText? = null
    private var mDescriptionET : EditText? = null
    private var mPriceET : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_deal)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase!!.reference.child("traveldeals")

        mTitleET = findViewById(R.id.et_title)
        mDescriptionET = findViewById(R.id.et_description)
        mPriceET = findViewById(R.id.et_price)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_create_deal, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.menu_item_save ->{

                saveDeal()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()
                clean()
                return true

            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveDeal(){

        val title = mTitleET!!.text.toString()
        val description = mDescriptionET!!.text.toString()
        val price = mPriceET!!.text.toString()

        val deal = TravelDeal(title, description, price,  "")
        mDatabaseReference!!.push().setValue(deal)

    }

    private fun clean(){

        mTitleET!!.setText("")
        mDescriptionET!!.setText("")
        mPriceET!!.setText("")
        mTitleET!!.requestFocus()

    }

}
