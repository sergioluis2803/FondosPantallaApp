package com.serch.fondosdepantalla;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.serch.fondosdepantalla.FragmentosAdminsitrador.HomeAdmin;
import com.serch.fondosdepantalla.FragmentosAdminsitrador.ListAdmin;
import com.serch.fondosdepantalla.FragmentosAdminsitrador.ProfileAdmin;
import com.serch.fondosdepantalla.FragmentosAdminsitrador.RegisterAdmin;
import com.serch.fondosdepantalla.Modelo.MenuItemAdmin;

public class MainActivityAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        Toolbar toolbar = findViewById(R.id.toolbarA);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_A);

        NavigationView navigationView = findViewById(R.id.nav_viewA);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new HomeAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioAdmin);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuItemAdmin menuItemType = null;
        for (MenuItemAdmin type : MenuItemAdmin.values()) {
            if (type.getId() == item.getItemId()) {
                menuItemType = type;
                break;
            }
        }

        if (menuItemType != null) {
            switch (menuItemType) {
                case HOME_ADMIN:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new HomeAdmin()).commit();
                    break;
                case PROFILE_ADMIN:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new ProfileAdmin()).commit();
                    break;
                case REGISTER_ADMIN:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new RegisterAdmin()).commit();
                    break;
                case LIST_ADMIN:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new ListAdmin()).commit();
                    break;
                case SIGN_OUT:
                    closeSession();
                    break;
                default:
                    break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void checkSession() {
        if (user == null) {
            startActivity(new Intent(MainActivityAdmin.this, MainActivity.class));
            finish();
        }
    }

    private void closeSession() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivityAdmin.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }
}