package com.example.messenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.example.messenger.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"
        //recyclerview_newmessage.adapter
        //val adapter = GroupAdapter<ViewHolder>()
        // adapter.add(UserItem())
        // recyclerview_newmessage.adapter = adapter
        fetchUsers()
    }
    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    //transforming data into user and setting onto recycler view.
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                //displaying username on top on chat.this allows to send username to next activity that chatlog activity
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
//       intent.putExtra(USER_KEY,  userItem.user.username)
                    //pasing entire user instead of just user name so we can get users id profile pic and name
                    //so we use parsel to send entire object.using annotation paeseliz in user class
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()

                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}
 class UserItem(val user: User): Item<GroupieViewHolder>() {
     override fun bind(viewHolder: GroupieViewHolder, position: Int) {
         //getting text from class of user than displaying on text view
         viewHolder.itemView.username_textview_new_message.text = user.username

         Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
     }


     override fun getLayout(): Int {
         return R.layout.user_row_new_message
     }



}