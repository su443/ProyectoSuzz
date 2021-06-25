package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.informacionusuario;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.R;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.db.DbUsuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.interfaces.Comunicacion;

public class MostrarInformacionFragment extends Fragment {

    EditText txtnombre, txtedad, txtcorreo, txttelefono;
    Button btneditar, btnelminiar;
    ImageView foto;

    Usuarios usuarios;
    Comunicacion comunicacion;
    Activity activity;
    int id =0;

    private MostrarInformacionViewModel mViewModel;

    public static MostrarInformacionFragment newInstance() {
        return new MostrarInformacionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mostrar_informacion_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtnombre = view.findViewById(R.id.nombreInfo);
        txtedad = view.findViewById(R.id.edadInfo);
        txttelefono = view.findViewById(R.id.telefonoInfo);
        txtcorreo = view.findViewById(R.id.correoInfo);
        foto = view.findViewById(R.id.fotoInfo);

        btneditar = view.findViewById(R.id.btnEditarInfo);
        btnelminiar = view.findViewById(R.id.btnEliminarInfo);

        mViewModel = new ViewModelProvider(this).get(MostrarInformacionViewModel.class);

        if (mViewModel.getImage() != null) {
            foto.setImageDrawable(mViewModel.getImage());
        }

        DbUsuarios dbUsuarios = new DbUsuarios(getActivity());
        if(savedInstanceState == null){
            Bundle objeto = getArguments();
            if(objeto != null) {
                id = objeto.getInt("objeto");
                usuarios = dbUsuarios.mostrarInfoUsuarios(id);
                txtnombre.setText(usuarios.getNombre());
                txtedad.setText(Integer.toString(usuarios.getEdad()));
                txttelefono.setText(usuarios.getTelefono());
                txtcorreo.setText(usuarios.getCorreo());
                cargarfoto(usuarios.getFoto());
            }else{
                Toast.makeText(getContext(),"Selecciona un usuario",Toast.LENGTH_LONG).show();
            }
        }
        txtnombre.setInputType(InputType.TYPE_NULL);
        txtedad.setInputType(InputType.TYPE_NULL);
        txttelefono.setInputType(InputType.TYPE_NULL);
        txtcorreo.setInputType(InputType.TYPE_NULL);


        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicacion.setotroEd(id, view);
            }
        });
        btnelminiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicacion.setotroEl(id, view);
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MostrarInformacionViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.activity = (Activity) context;
            comunicacion = (Comunicacion) this.activity;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void cargarfoto(String nombrefoto){
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotosDB/" + nombrefoto;
        File file = new File(filepath);
        if(file.exists()){
            Bitmap lafoto = BitmapFactory.decodeFile(file.getAbsolutePath());
            foto.setImageBitmap(lafoto);
        }else{
            Toast.makeText(getActivity(), "No se encontro la foto",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setImage(foto.getDrawable());

    }
}