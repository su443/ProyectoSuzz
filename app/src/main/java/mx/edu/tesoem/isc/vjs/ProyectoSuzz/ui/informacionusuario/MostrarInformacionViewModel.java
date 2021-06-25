package mx.edu.tesoem.isc.vjs.ProyectoSuzz.ui.informacionusuario;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

public class MostrarInformacionViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    Drawable image;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}