package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.agregarusuario;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


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
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.db.*;

public class AgregarUsuarioFragment extends Fragment {

    EditText txtnombre, txtedad, txtcorreo, txttelefono;
    Button agregar, btnfoto;
    ImageView foto;

    //para las fotos
    private int PICK_IMAGE_REQUEST = 103;
    String nombrefoto;

    private Context TheThis;
    private final String NameOfFolder = "/PhotosDB";
    private final String NameOfFile = "imagen";

    private AgregarUsuarioViewModel mViewModel;

    public static AgregarUsuarioFragment newInstance() {
        return new AgregarUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agregar_usuario_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtnombre = view.findViewById(R.id.nombreAdd);
        txtedad = view.findViewById(R.id.edadAdd);
        txtcorreo = view.findViewById(R.id.correoAdd);
        txttelefono = view.findViewById(R.id.telefonoAdd);

        agregar = view.findViewById(R.id.btnUsuarioAdd);
        btnfoto = view.findViewById(R.id.btnFotoAdd);

        foto = view.findViewById(R.id.fotoAdd);

        mViewModel = new ViewModelProvider(this).get(AgregarUsuarioViewModel.class);

        if (mViewModel.getImage() != null) {
            foto.setImageDrawable(mViewModel.getImage());
        }

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccione una foto"), PICK_IMAGE_REQUEST);
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                verificarPermisos();


                DbUsuarios dbUsuarios = new DbUsuarios(getActivity());
                long id = 0;
                try {
                    //convertir imagen
                    foto.buildDrawingCache();
                    Bitmap bmap = foto.getDrawingCache();
                    //guardar imagen
                    save(getActivity(), bmap);

                    id = dbUsuarios.insertarUsuario(txtnombre.getText().toString(), Integer.parseInt(txtedad.getText().toString()),
                            txtcorreo.getText().toString(), txttelefono.getText().toString(), nombrefoto);
                }catch (Exception ex){
                    Toast.makeText(getActivity(), "No se han llenado todos los campos", Toast.LENGTH_LONG).show();
                }
                if(id>0) {
                    Toast.makeText(getActivity(), "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                    limpiar();
                }else{
                    Toast.makeText(getActivity(), "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show();
                    }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            System.out.println();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                foto = (ImageView) getActivity().findViewById(R.id.fotoAdd);
                foto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AgregarUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    private void limpiar(){
        txtnombre.setText("");
        txtedad.setText("");
        txtcorreo.setText("");
        txttelefono.setText("");
        foto.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_gallery));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int permisos = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permisos == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
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
            System.out.println(nombrefoto);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setImage(foto.getDrawable());

    }

}