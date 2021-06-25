package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.mostrarusuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.R;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.adaptadores.ListaUsuariosAdaptador;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.db.DbUsuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.interfaces.Comunicacion;

public class MostrarUsuariosFragment extends Fragment {

    RecyclerView listaUsuarios;
    ArrayList<Usuarios> listaArrayUsuarios;

    private MostrarUsuariosViewModel mViewModel;

    public static MostrarUsuariosFragment newInstance() {
        return new MostrarUsuariosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mostrar_usuarios_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaUsuarios = view.findViewById(R.id.listausuarios);
        listaUsuarios.setLayoutManager(new LinearLayoutManager(getActivity()));

        DbUsuarios dbUsuarios = new DbUsuarios(getActivity());

        listaArrayUsuarios = new ArrayList<>();

        ListaUsuariosAdaptador adaptador = new ListaUsuariosAdaptador(dbUsuarios.mostrarUsuarios(), (Comunicacion) getActivity());

        listaUsuarios.setAdapter(adaptador);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MostrarUsuariosViewModel.class);
        // TODO: Use the ViewModel
    }

}