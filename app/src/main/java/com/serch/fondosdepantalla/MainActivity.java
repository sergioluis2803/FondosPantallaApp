package com.serch.fondosdepantalla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.serch.fondosdepantalla.FragmentosCliente.Info;
import com.serch.fondosdepantalla.FragmentosCliente.Share;
import com.serch.fondosdepantalla.FragmentosCliente.HomeUser;
import com.serch.fondosdepantalla.Modelo.MenuItemUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeUser()).commit();
            navigationView.setCheckedItem(R.id.InicioCliente);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuItemUser menuItemType = null;
        for (MenuItemUser type : MenuItemUser.values()) {
            if (type.getId() == item.getItemId()) {
                menuItemType = type;
                break;
            }
        }

        if (menuItemType != null) {
            switch (menuItemType) {
                case HOME_USER:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeUser()).commit();
                    break;
                case INFO:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Info()).commit();
                    break;
                case SHARE:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Share()).commit();
                    break;
                default:
                    break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }
}