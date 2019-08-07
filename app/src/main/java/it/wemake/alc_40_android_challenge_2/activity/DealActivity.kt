package it.wemake.alc_40_android_challenge_2.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import it.wemake.alc_40_android_challenge_2.R
import it.wemake.alc_40_android_challenge_2.model.TravelDeal

class DealActivity : AppCompatActivity() {

    private var mFirebaseDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null
    private var mFirebaseStorage : FirebaseStorage? = null
    private var mStorageRefrerence : StorageReference? = null
    private var deal: TravelDeal? = null
    private var isAdmin = false

    private var mTitleET : EditText? = null
    private var mDescriptionET : EditText? = null
    private var mPriceET : EditText? = null
    private var mDealIV : ImageView? = null
    private val PICTURE_REQUEST = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase!!.reference.child("traveldeals")
        mFirebaseStorage = FirebaseStorage.getInstance()
        mStorageRefrerence = mFirebaseStorage!!.reference.child("deals_pictures")

        isAdmin = this.getSharedPreferences("travelmantics",0).getBoolean("isAdmin", false)

        mTitleET = findViewById(R.id.et_title)
        mDescriptionET = findViewById(R.id.et_description)
        mPriceET = findViewById(R.id.et_price)
        mDealIV = findViewById(R.id.iv_deal)
        val addImageBTN = findViewById<Button>(R.id.btn_add_image)

        addImageBTN.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_REQUEST)

        }

        mTitleET!!.isEnabled = isAdmin
        mDescriptionET!!.isEnabled = isAdmin
        mPriceET!!.isEnabled = isAdmin
        addImageBTN!!.isEnabled = isAdmin

        deal = intent.getParcelableExtra("deal")

        if(deal != null){
            mTitleET!!.setText(deal!!.title)
            mDescriptionET!!.setText(deal!!.description)
            mPriceET!!.setText(deal!!.price)
            showImage(deal!!.imageUri)
        }else{
            deal = TravelDeal()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_create_deal, menu)

        menu!!.findItem(R.id.item_delete).isVisible = isAdmin
        menu.findItem(R.id.item_save).isVisible = isAdmin

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.item_save ->{

                saveDeal()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()
                clean()
                finish()
                return true

            }

            R.id.item_delete ->{

                deleteDeal()
                Toast.makeText(this, "Deal has been deleted", Toast.LENGTH_LONG).show()
                finish()
                return true

            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICTURE_REQUEST && resultCode == Activity.RESULT_OK){

            val imageUri = data!!.data
            val ref = mStorageRefrerence!!.child(imageUri!!.lastPathSegment!!)
            ref.putFile(imageUri).addOnSuccessListener { task ->

                ref.downloadUrl.addOnSuccessListener {

                    showImage(it.toString())
                    deal!!.imageUri = it.toString()
                    deal!!.imageName = it.path

                }

            }

        }

    }

    private fun saveDeal(){

        deal!!.title = mTitleET!!.text.toString()
        deal!!.description = mDescriptionET!!.text.toString()
        deal!!.price = mPriceET!!.text.toString()

        if (deal!!.id == null){
            mDatabaseReference!!.push().setValue(deal)
        }else{
            mDatabaseReference!!.child(deal!!.id!!).setValue(deal)
        }

    }

    private fun deleteDeal(){

        if(deal?.id == null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_LONG).show()
            return
        }

        mDatabaseReference!!.child(deal!!.id!!).removeValue()

        if(deal!!.imageName != null && !deal!!.imageName!!.isEmpty()){

            val ref = mFirebaseStorage!!.getReference().child(deal!!.imageName!!)
            ref.delete().addOnSuccessListener {

            }.addOnFailureListener {

            }

        }

    }

    private fun clean(){

        mTitleET!!.setText("")
        mDescriptionET!!.setText("")
        mPriceET!!.setText("")
        mTitleET!!.requestFocus()

    }

    fun showImage(url: String?){
        if(url != null && !url.isEmpty()){
            val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.get()
                .load(url)
                .resize(width, width*2/3)
                .centerCrop()
                .into(mDealIV)
        }
    }

}
