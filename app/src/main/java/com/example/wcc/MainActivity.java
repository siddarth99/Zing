package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private androidx.appcompat.widget.Toolbar toolbar;

    private CollectionAdapter collectionAdapter;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DatabaseReference user_ref;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser  = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fire chat");

        viewPager2=findViewById(R.id.pager);
        collectionAdapter=new CollectionAdapter(this);
        viewPager2.setAdapter(collectionAdapter);
        tabLayout =findViewById(R.id.tab_layout);

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy1=new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("Requests");
                        break;
                    case 1:
                        tab.setText("Chats");
                        break;
                    case 2:
                        tab.setText("Calls");
                        break;
                }
            }
        };
        new TabLayoutMediator(tabLayout,viewPager2,tabConfigurationStrategy1).attach();

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null)

        {
            sendToStart();
        }
        else{
            String welcome="Welcome ";
            Toast.makeText(MainActivity.this,welcome,Toast.LENGTH_LONG);
            user_ref= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            user_ref.child("online").setValue(true);

        }
    }

    private void sendToStart() {
        Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override

    protected void onStop() {
        super.onStop();
        FirebaseUser currentUsers=mAuth.getCurrentUser();
        if(currentUsers!=null)
            user_ref.child("online").setValue(ServerValue.TIMESTAMP);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
      /*  if(item.getItemId()==R.id.main_logout_button)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if(item.getItemId()==R.id.main_setting)
        {
           sendToAccount();
        }*/

      switch (item.getItemId())
      {
          case R.id.main_logout_button:
              FirebaseAuth.getInstance().signOut();
              sendToStart();
              break;

          case R.id.main_all_users:
              Intent intent=new Intent(MainActivity.this,AllUsersActivity.class);
              startActivity(intent);

              break;
          case R.id.main_setting:
              sendToAccount();

              break;
      }
        return true;
    }

    private void sendToAccount() {
        Intent intent=new Intent(MainActivity.this,AccountSettings.class);
        startActivity(intent);
    }


}
