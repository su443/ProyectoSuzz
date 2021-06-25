package mx.edu.tesoem.isc.vjs.ProyectoSuzz.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import mx.edu.tesoem.isc.vjs.ProyectoSuzz.R;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.databinding.ActivityMainBinding;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.entidades.Usuarios;
import mx.edu.tesoem.isc.vjs.ProyectoSuzz.interfaces.Comunicacion;

public class ListaUsuariosAdaptador extends RecyclerView.Adapter<ListaUsuariosAdaptador.UsuariosViewHolder>{

    ArrayList<Usuarios> listaUsuarios;
    Comunicacion comunicacion;


    public ListaUsuariosAdaptador(ArrayList<Usuarios> listaUsuarios, Comunicacion comunicacion){
        this.listaUsuarios = listaUsuarios;
        this.comunicacion = comunicacion;
    }

    @NonNull
    @NotNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_usuario,null,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsuariosViewHolder holder, int position) {
        holder.view.setText(listaUsuarios.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
    

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {

        TextView view;

        public UsuariosViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.viewNombre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comunicacion.setotro(listaUsuarios.get(getAdapterPosition()).getId(), view);
                }
            });
        }
    }

}
