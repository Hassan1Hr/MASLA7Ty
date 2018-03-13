package com.hassan.masla7ty.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.LoginActivity;
import com.hassan.masla7ty.pojo.MyApplication;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Class Chat is the Activity class that holds main chat screen. It shows
 * all the conversation messages between two users and also allows the user to
 * send and receive messages.
 */
public class Chat extends Activity implements View.OnClickListener
{
	public static final String EXTRA_DATA = "extraData";
	/** The Conversation list. */
	private ArrayList<Conversation> convList;

	/** The chat adapter. */
	//private ChatAdapter adp;

	/** The Editext to compose the message. */
	private EditText txt;



	/** The user name of sender. */


	/** The date of last message in conversation. */
	private Date lastMsgDate;

	/** Flag to hold if the activity is running or not. */
	private boolean isRunning;
	public static String username;
	public static String receiver;
	public static String sender;
	ImageButton btnsend;
	String Username;
	public static String getReceiver() {
		return receiver;
	}

	public static void setReceiver(String receiver) {
		Chat.receiver = receiver;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Chat.username = username;
	}
	public static String getSender() {
		return sender;
	}
	private FirebaseListAdapter<Conversation> mFirebaseAdapter1 = null;
	public void setSender(String sender) {
		this.sender = sender;
	}
	DatabaseReference firebase_chatnode =null;
	DatabaseReference ref_chatchildnode1 = null;
	DatabaseReference ref_chatchildnode2 = null;
	ListView list;
	/** The handler. */
	private static Handler handler;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		firebase_chatnode =  FirebaseDatabase.getInstance()
				.getReferenceFromUrl("https://masla7ty-1007.firebaseio.com/Chats");
		Intent intent = getIntent();
		btnsend = findViewById(R.id.btnSend);
		String rec = intent.getStringExtra("userId");
		int x= rec.indexOf("@");
		receiver = (x==-1)?rec:rec.substring(0,x);
		convList = new ArrayList<Conversation>();
		 list= (ListView) findViewById(R.id.chatlist);
		//adp = new ChatAdapter();
		//list.setAdapter(adp);
		list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);

		txt = (EditText) findViewById(R.id.txt);
		txt.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

		btnsend.setOnClickListener(this);

		SharedPreferences sharedPref =getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
		 Username= sharedPref.getString("username", LoginActivity.getUsername());
		int y= Username.indexOf("@");
		sender = (y==-1)?Username:Username.substring(0,y);
		ref_chatchildnode1 = firebase_chatnode.child(sender + " " + receiver);

		ref_chatchildnode2 = firebase_chatnode.child(receiver + " " + sender);
		handler = new Handler();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */

	@Override
	protected void onStart() {
		super.onStart();
		loadConversationList();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		isRunning = true;
		mFirebaseAdapter1.startListening();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		isRunning = false;
	}

	@Override
	protected void onStop() {
		mFirebaseAdapter1.stopListening();
		super.onStop();

	}

	/* (non-Javadoc)
         * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
         */
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSend)
		{
			sendMessage();
		}

	}

	/**
	 * Call this method to Send message to opponent. It does nothing if the text
	 * is empty otherwise it creates a Parse object for Chat message and send it
	 * to Parse server.
	 */
	private void sendMessage()
	{
		if (txt.length() == 0)
			return;






		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

		String s = txt.getText().toString();
		final Conversation c = new Conversation(s,new Date(),
				sender);

		//c.setStatus(Conversation.STATUS_SENDING);
		convList.add(c);

		txt.setText(null);
		ref_chatchildnode1.push().setValue(c);
		ref_chatchildnode2.push().setValue(c);
		//adp.notifyDataSetChanged();
		ref_chatchildnode1.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Conversation chatmsg = dataSnapshot.getValue(Conversation.class);
				convList.add(chatmsg);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		ref_chatchildnode2.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Conversation chatmsg = dataSnapshot.getValue(Conversation.class);
				convList.add(chatmsg);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});


	}

	/**
	 * Load the conversation list from Parse server and save the date of last
	 * message that will be used to load only recent new messages
	 */
	private void loadConversationList()
	{
		Query query = firebase_chatnode.child(sender + " " + receiver);
		FirebaseListOptions<Conversation> options = new FirebaseListOptions.Builder<Conversation>()
				.setQuery(query, Conversation.class)
				.setLayout(R.layout.chat_item_rcv)
				.build();

		mFirebaseAdapter1 = new FirebaseListAdapter<Conversation>(options) {
			@Override
			protected void populateView(View v, Conversation user, int position) {
				TextView sender = (TextView) v.findViewById(R.id.lbl1);
				TextView msg = (TextView) v.findViewById(R.id.lbl2);
				TextView date = (TextView) v.findViewById(R.id.lbl3);

				sender.setText(user.getSender());
				msg.setText(user.getMsg());
				date.setText(user.getDate().toString());
			}
		};
		list.setAdapter(mFirebaseAdapter1);

	}

	/**
	 * The Class ChatAdapter is the adapter class for Chat ListView. This
	 * adapter shows the Sent or Receieved Chat message in each list item.
	 */



	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
