package mx.edu.tesoem.isc.vjs.ProyectoSuzz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;

public class DbUsuarios extends DbHelper{

    Context context;

    public DbUsuarios(@Nullable @org.jetbrains.annotations.Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarUsuario(String nombre, int edad,String telefono, String correo, String foto){

        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nombre", nombre);
            contentValues.put("edad", edad);
            contentValues.put("telefono", telefono);
            contentValues.put("correo", correo);
            contentValues.put("foto", foto);

            id = db.insert(TABLE_USUARIOS, null, contentValues);

        }catch (Exception ex){
            ex.toString();
        }

        return id;
    }
    public ArrayList<Usuarios> mostrarUsuarios(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
        Usuarios usuario = null;
        Cursor cursorUsuarios = null;

        cursorUsuarios = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS, null);

        if (cursorUsuarios.moveToFirst()){
            do {
                usuario = new Usuarios();
                usuario.setId(cursorUsuarios.getInt(0));
                usuario.setNombre(cursorUsuarios.getString(1));
                usuario.setEdad(cursorUsuarios.getInt(2));
                usuario.setTelefono(cursorUsuarios.getString(3));
                usuario.setCorreo(cursorUsuarios.getString(4));
                usuario.setFoto(cursorUsuarios.getString(5));
                listaUsuarios.add(usuario);
            }while (cursorUsuarios.moveToNext());

        }
        cursorUsuarios.close();
        return listaUsuarios;
    }

    public Usuarios mostrarInfoUsuarios(int id){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Usuarios usuario = null;
        Cursor cursorUsuarios;

        cursorUsuarios = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorUsuarios.moveToFirst()){
            usuario = new Usuarios();
            usuario.setId(cursorUsuarios.getInt(0));
            usuario.setNombre(cursorUsuarios.getString(1));
            usuario.setEdad(cursorUsuarios.getInt(2));
            usuario.setTelefono(cursorUsuarios.getString(3));
            usuario.setCorreo(cursorUsuarios.getString(4));
            usuario.setFoto(cursorUsuarios.getString(5));
        }
        cursorUsuarios.close();
        return usuario;
    }

    public boolean editarUsuarios(int id, String nombre, int edad, String telefono, String correo, String foto){
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            db.execSQL("UPDATE " + TABLE_USUARIOS + " SET nombre = '" + nombre + "', edad = " +
                    edad + ", telefono = " + telefono + ", correo = '" + correo + "', foto = '" + foto + "' WHERE id = " + id);
            correcto = true;
        }catch (Exception ex){
            ex.toString();
            System.out.println("El error " + ex);
            correcto = false;
        }finally {
            db.close();
        }
        return correcto;
    }

    public boolean eliminarUsuarios(int id) {
        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM  " + TABLE_USUARIOS + " WHERE id = " + id);

            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }
}
