package mx.edu.tesoem.isc.vjs.ProyectoSuzz;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.databinding.ActivityMainBinding;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.interfaces.Comunicacion;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.actualizarusuario.ActualizarUsuarioFragment;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.agregarusuario.AgregarUsuarioFragment;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.eliminarusuario.EliminarUsuarioFragment;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.informacionusuario.MostrarInformacionFragment;

public class MainActivity extends AppCompatActivity implements Comunicacion {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    MostrarInformacionFragment mostrarInformacionFragment;
    ActualizarUsuarioFragment actualizarUsuarioFragment;
    EliminarUsuarioFragment eliminarUsuarioFragment;
    AgregarUsuarioFragment agregarUsuarioFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_ProyectoSuzz_NoActionBar);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mostrarUsuariosFragment,R.id.mostrarInformacionFragment, R.id.agregarUsuarioFragment,
                R.id.actualizarUsuarioFragment, R.id.eliminarUsuarioFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void setotro(int otro, View view) {
        mostrarInformacionFragment = new MostrarInformacionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto",otro);
        mostrarInformacionFragment.setArguments(bundle);
        Navigation.findNavController(view).navigate(R.id.mostrarInformacionFragment, bundle);
    }

    @Override
    public void setotroEd(int otro, View view) {
        actualizarUsuarioFragment = new ActualizarUsuarioFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto",otro);
        actualizarUsuarioFragment.setArguments(bundle);
        Navigation.findNavController(view).navigate(R.id.actualizarUsuarioFragment, bundle);
    }

    @Override
    public void setotroEl(int otro, View view) {
        eliminarUsuarioFragment = new EliminarUsuarioFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto",otro);
        eliminarUsuarioFragment.setArguments(bundle);
        Navigation.findNavController(view).navigate(R.id.eliminarUsuarioFragment, bundle);
    }


}