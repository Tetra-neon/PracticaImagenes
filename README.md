# 📱 PracticaImagenes

Aplicación Android que demuestra el uso de componentes multimedia en Android: videos, audio e imágenes.

## 🎯 Características

- 🎥 **Video Local**: Reproducción de video almacenado en recursos con controles MediaController
- 🔊 **Audio Player**: Reproductor de audio con botones de play y stop
- 📺 **YouTube Player**: Integración del reproductor de YouTube embebido
- 🖼️ **Galería**: Selección y visualización de imágenes en RecyclerView horizontal
- 🔄 **Persistencia**: Mantiene el estado al rotar la pantalla

## 🛠️ Tecnologías

- **Lenguaje**: Java
- **IDE**: Android Studio
- **Min SDK**: 31
- **Target SDK**: 36

### Librerías:
- `com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0` - Reproductor de YouTube
- `com.github.bumptech.glide:glide:4.12.0` - Carga de imágenes
- AndroidX RecyclerView, AppCompat, ConstraintLayout

## 🚀 Funcionalidades Implementadas

### Video Local
- Usa `VideoView` para reproducir videos desde recursos
- Incluye `MediaController` para controles de reproducción
- Mantiene posición al rotar pantalla

### Reproductor de Audio
- Implementado con `MediaPlayer`
- Botones separados para reproducir y detener
- Manejo adecuado del ciclo de vida

### YouTube Player
- Integración con librería oficial de YouTube
- Manejo de errores (videos no disponibles, sin conexión, etc.)
- Compatible con lifecycle de Android

### Galería de Imágenes
- Selector de imágenes del dispositivo
- RecyclerView horizontal
- Carga eficiente con Glide
- Contador de imágenes seleccionadas

## 🎓 Aprendizajes

Este proyecto demuestra:
- ✅ Manejo de multimedia en Android
- ✅ Integración de librerías de terceros
- ✅ RecyclerView con adaptadores personalizados
- ✅ Manejo del ciclo de vida de Activities
- ✅ Persistencia de estado con `onSaveInstanceState`
- ✅ Permisos de Android (Internet, almacenamiento)

## 👨‍💻 Autor

**Tetra-neon**
- GitHub: [@Tetra-neon](https://github.com/Tetra-neon)

## 📄 Licencia

Este proyecto es para fines educativos.

---

⭐ Si te gusta este proyecto, dale una estrella en GitHub!
