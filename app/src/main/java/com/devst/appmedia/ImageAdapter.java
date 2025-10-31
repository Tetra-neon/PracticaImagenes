package com.devst.appmedia;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Adaptador para mostrar imágenes en un RecyclerView
 * Utiliza Glide para cargar imágenes de manera eficiente
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    // Lista de URIs de las imágenes
    private List<Uri> imageUriList;

    // Contexto de la aplicación
    private Context context;

  // Constructor del adaptador
    public ImageAdapter(Context context, List<Uri> imageUriList) {
        this.context = context;
        this.imageUriList = imageUriList;
    }

    /// Crea una nueva vista (invocado por el layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    /// Vincula los datos a una vista (invocado por el layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener la URI de la imagen en esta posición
        Uri imageUri = imageUriList.get(position);

        // Cargar la imagen usando Glide
        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_launcher_background) // Imagen mientras carga
                .error(R.drawable.ic_launcher_foreground) // Imagen si hay error
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cachear la imagen
                .centerCrop() // Recortar la imagen para ajustarla
                .into(holder.imageView);
    }

    /// Devuelve el número de elementos en la list
    @Override
    public int getItemCount() {
        return imageUriList.size();
    }

   // Clase interna para representar cada elemento de la list
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

       // Constructor del ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontrar el ImageView en el layout
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    // Método opcional para agregar una imagen a la list
    public void addImage(Uri imageUri) {
        imageUriList.add(imageUri);
        notifyItemInserted(imageUriList.size() - 1);
    }
    // Método opcional para eliminar una imagen de la list
    public void removeImage(int position) {
        if (position >= 0 && position < imageUriList.size()) {
            imageUriList.remove(position);
            notifyItemRemoved(position);
        }
    }
   // Método opcional para limpiar la lista de imágenes
    public void clearImages() {
        int size = imageUriList.size();
        imageUriList.clear();
        notifyItemRangeRemoved(0, size);
    }
}