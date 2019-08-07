package it.wemake.alc_40_android_challenge_2.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.wemake.alc_40_android_challenge_2.R
import it.wemake.alc_40_android_challenge_2.adapters.DealAdapter

class MainActivity : AppCompatActivity() {

    private var mFirebaseDatabase : FirebaseDatabase? = null
    private var mTravelDealsDatabaseReference : DatabaseReference? = null
    private var firebaseAuth :FirebaseAuth? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null

    private var dealsRV: RecyclerView? = null
    private var dealsAdapter: DealAdapter? = null

    private val RC_SIGN_IN = 123
    private var userIsAdmin = false
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editor = applicationContext.getSharedPreferences("travelmantics",0).edit()
    }

    private fun signIn(){
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        val insertMenu = menu!!.findItem(R.id.item_new_travel_deal)

        insertMenu.isVisible = userIsAdmin

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.item_new_travel_deal -> {

                val intent = Intent(this, DealActivity::class.java)
                startActivity(intent)

                return true

            }

            R.id.item_log_out -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        // ...
                        userIsAdmin = false
                        editor!!.putBoolean("isAdmin", false).commit()
                        firebaseAuth?.addAuthStateListener(authListener!!)
                    }
                firebaseAuth?.removeAuthStateListener(authListener!!)
                return true

            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth?.removeAuthStateListener(authListener!!)
    }

    override fun onResume() {
        super.onResume()

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mTravelDealsDatabaseReference = mFirebaseDatabase!!.reference.child("traveldeals")
        firebaseAuth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            if(it.currentUser == null){
                signIn()
            }else{
                showMenu()
                checkAdmin(it.uid!!)
            }
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_LONG).show()
        }

        dealsRV = findViewById(R.id.rv_deals)

        dealsAdapter = DealAdapter(mTravelDealsDatabaseReference!!)
        dealsRV!!.adapter = dealsAdapter
        dealsRV!!.layoutManager = LinearLayoutManager(this)

        firebaseAuth?.addAuthStateListener(authListener!!)
    }

    private fun checkAdmin(uid: String){
        val ref =  mFirebaseDatabase!!.reference.child("administrators").child(uid)
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {

                userIsAdmin = true
                editor!!.putBoolean("isAdmin", true).commit()
                showMenu()

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }
        })
    }

    fun showMenu(){
        invalidateOptionsMenu()
    }

}
