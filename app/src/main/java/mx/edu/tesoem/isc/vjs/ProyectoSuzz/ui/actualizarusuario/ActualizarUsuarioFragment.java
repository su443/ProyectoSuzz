package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.actualizarusuario;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.R;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.db.DbUsuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.agregarusuario.AgregarUsuarioViewModel;

public class ActualizarUsuarioFragment extends Fragment {

    EditText txtnombre, txtedad, txtcorreo, txttelefono;
    Button btnfoto, btnactualizar;
    ImageView foto;

    Usuarios usuarios;
    boolean correcto = false;
    int id =0;

    //fotos
    private int PICK_IMAGE_REQUEST = 103;
    String nombrefoto;

    private Context TheThis;
    private final String NameOfFolder = "/PhotosDB";
    private final String NameOfFile = "imagen";


    private ActualizarUsuarioViewModel mViewModel;

    public static ActualizarUsuarioFragment newInstance() {
        return new ActualizarUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.actualizar_usuario_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtnombre = view.findViewById(R.id.nombreAC);
        txtedad = view.findViewById(R.id.edadAC);
        txtcorreo = view.findViewById(R.id.correoAC);
        txttelefono = view.findViewById(R.id.telefonoAC);
        btnfoto = view.findViewById(R.id.btnFotoAC);
        btnactualizar = view.findViewById(R.id.btnActualizarAC);
        foto = view.findViewById(R.id.fotoAC);

        mViewModel = new ViewModelProvider(this).get(ActualizarUsuarioViewModel.class);

        if (mViewModel.getImage() != null) {
            foto.setImageDrawable(mViewModel.getImage());
        }

        DbUsuarios dbUsuarios = new DbUsuarios(getActivity());
        if(savedInstanceState == null) {
            Bundle objeto = getArguments();
            if (objeto != null) {
                id = objeto.getInt("objeto");
                usuarios = dbUsuarios.mostrarInfoUsuarios(id);
                txtnombre.setText(usuarios.getNombre());
                txtedad.setText(Integer.toString(usuarios.getEdad()));
                txtcorreo.setText(usuarios.getCorreo());
                txttelefono.setText(usuarios.getTelefono());
                nombrefoto = usuarios.getFoto();
                cargarfoto(nombrefoto);
            } else {
                Toast.makeText(getContext(), "Selecciona un usuario", Toast.LENGTH_LONG).show();
            }
        }

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccione una foto"), PICK_IMAGE_REQUEST);
            }
        });

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!txtnombre.getText().toString().equals("") && !txtedad.getText().toString().equals("") &&
                            !txttelefono.getText().toString().equals("") && !txtcorreo.getText().toString().equals("")) {
                        //eliminar foto antigua
                        elimina(nombrefoto);
                        //convertir imagen
                        foto.buildDrawingCache();
                        Bitmap bmap = foto.getDrawingCache();
                        //guardar imagen
                        save(getActivity(), bmap);
                        correcto = dbUsuarios.editarUsuarios(id, txtnombre.getText().toString(), Integer.parseInt(txtedad.getText().toString()),
                                txttelefono.getText().toString(), txtcorreo.getText().toString(), nombrefoto);
                        if (correcto) {
                            Toast.makeText(getActivity(), "Registro actualizado", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.mostrarUsuariosFragment);
                        } else {
                            Toast.makeText(getActivity(), "Error al modificar", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Debe llenar los campos", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getActivity(), "No seleccionaste un usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActualizarUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                foto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public void save(Context context, Bitmap ImageToSave){

        TheThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            nombrefoto = getNameOfFile(CurrentDateAndTime);
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }
    public String getNameOfFile(String CurrentDateAndTime) {
        String filename = NameOfFile + CurrentDateAndTime + ".jpg";
        return filename;
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