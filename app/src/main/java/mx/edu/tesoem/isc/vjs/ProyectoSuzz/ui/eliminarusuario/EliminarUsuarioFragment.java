package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.eliminarusuario;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.R;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.db.DbUsuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.interfaces.Comunicacion;

public class EliminarUsuarioFragment extends Fragment {

    EditText txtnombre, txtedad, txtcorreo, txttelefono;
    ImageView foto;
    Usuarios usuarios;
    Comunicacion comunicacion;
    Activity activity;
    int id =0;
    //foto
    String nombrefoto;

    private EliminarUsuarioViewModel mViewModel;

    public static EliminarUsuarioFragment newInstance() {
        return new EliminarUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eliminar_usuario_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtnombre = view.findViewById(R.id.nombreDel);
        txtedad = view.findViewById(R.id.edadDel);
        txtcorreo = view.findViewById(R.id.correoDel);
        txttelefono = view.findViewById(R.id.telefonoDel);
        foto = view.findViewById(R.id.fotoDel);

        mViewModel = new ViewModelProvider(this).get(EliminarUsuarioViewModel.class);

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
                txtcorreo.setText(usuarios.getCorreo());
                txttelefono.setText(usuarios.getTelefono());
                nombrefoto = usuarios.getFoto();
                cargarfoto(nombrefoto);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Desea eliminar usuario " + usuarios.getNombre()+ "?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(dbUsuarios.eliminarUsuarios(id)){
                                    Toast.makeText(getActivity(),"Usuario eliminado", Toast.LENGTH_LONG).show();
                                    elimina(nombrefoto);
                                    Navigation.findNavController(view).navigate(R.id.mostrarUsuariosFragment);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                comunicacion.setotro(id, view);
                            }
                        }).show();
            }else{
                Toast.makeText(getContext(),"Selecciona un usuario",Toast.LENGTH_LONG).show();
            }
        }
        txtnombre.setInputType(InputType.TYPE_NULL);
        txtedad.setInputType(InputType.TYPE_NULL);
        txttelefono.setInputType(InputType.TYPE_NULL);
        txtcorreo.setInputType(InputType.TYPE_NULL);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarUsuarioViewModel.class);
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

    public void cargarfoto(String nombrefoto) {
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotosDB/" + nombrefoto;
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap lafoto = BitmapFactory.decodeFile(file.getAbsolutePath());
            foto.setImageBitmap(lafoto);
        } else {
            Toast.makeText(getActivity(), "No se encontro la foto", Toast.LENGTH_LONG).show();
        }
    }

    public void elimina(String foto){
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotosDB/" + foto;
        File file = new File(filepath);
        System.out.println(filepath);
        boolean delete = false;
        if (file.exists()) {
            delete = file.delete();
            if(delete){
                Toast.makeText(getActivity(),"Se elimino la imagen",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setImage(foto.getDrawable());

    }
}