package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.myapplication.database.DBHelperServicio
import com.example.myapplication.database.Empresa
import com.example.myapplication.database.Servicio
import java.io.ByteArrayOutputStream

class MostrarEmpresaFranciscoOrellanaServicio : AppCompatActivity(), EmpresaAdapter.ClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmpresaAdapter
    private lateinit var databaseHelper: DBHelperServicio
    private var imagenNuPropietarioS: ByteArray? = null
    private var imagenNuEmpresaS: ByteArray? = null
    private var imagenNuPropietarioS2: ByteArray? = null
    private var imagenNuEmpresaS2: ByteArray? = null
    private var imagenNuPropietarioS3: ByteArray? = null
    private var imagenNuEmpresaS3: ByteArray? = null
    private fun cargarImagenesDesdeDrawables() {
        // Cargar imagen del drawable para el propietario
        val drawablePropietario: Drawable? = ContextCompat.getDrawable(this, R.drawable.se1sachaem)
        val bitmapPropietario: Bitmap = drawableToBitmap(drawablePropietario)
        imagenNuPropietarioS = bitmapPropietario.toByteArray()

        // Cargar imagen del drawable para la empresa
        val drawableEmpresa: Drawable? = ContextCompat.getDrawable(this, R.drawable.se1sachaem)
        val bitmapEmpresa: Bitmap = drawableToBitmap(drawableEmpresa)
        imagenNuEmpresaS = bitmapEmpresa.toByteArray()
    }
    private fun cargarImagenesDesdeDrawables2() {
        // Cargar imagen del drawable para el propietario
        val drawablePropietario: Drawable? = ContextCompat.getDrawable(this, R.drawable.shuarempre)
        val bitmapPropietario: Bitmap = drawableToBitmap(drawablePropietario)
        imagenNuPropietarioS2 = bitmapPropietario.toByteArray()

        // Cargar imagen del drawable para la empresa
        val drawableEmpresa: Drawable? = ContextCompat.getDrawable(this, R.drawable.shuarempre)
        val bitmapEmpresa: Bitmap = drawableToBitmap(drawableEmpresa)
        imagenNuEmpresaS2 = bitmapEmpresa.toByteArray()
    }  private fun cargarImagenesDesdeDrawables3() {
        // Cargar imagen del drawable para el propietario
        val drawablePropietario: Drawable? = ContextCompat.getDrawable(this, R.drawable.madero1)
        val bitmapPropietario: Bitmap = drawableToBitmap(drawablePropietario)
        imagenNuPropietarioS3 = bitmapPropietario.toByteArray()

        // Cargar imagen del drawable para la empresa
        val drawableEmpresa: Drawable? = ContextCompat.getDrawable(this, R.drawable.madero2)
        val bitmapEmpresa: Bitmap = drawableToBitmap(drawableEmpresa)
        imagenNuEmpresaS3 = bitmapEmpresa.toByteArray()
    }


    private fun drawableToBitmap(drawable: Drawable?): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap: Bitmap = if (drawable!!.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_empresa_francisco_orellana_servicio)

        recyclerView = findViewById(R.id.recyclerViewEmpresas)
        databaseHelper = DBHelperServicio(this)

        // Obtener y mostrar las empresas de Francisco de Orellana
        val empresasFOP = databaseHelper.getEmpresasByCantonId(1)  // Cambia por el id_canton deseado

        // Configurar el RecyclerView
        adapter = EmpresaAdapter(empresasFOP, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (empresasFOP.isEmpty()) {
            insertarNuevaEmpresaS()
            insertarNuevaEmpresaS2()
            insertarNuevaEmpresaS3()

            // Insertar servicios solo si no existen
            if (!serviciosYaInsertados()) {
                insertarNuevoServicio("SERVICIO LIMPIESA 1", "Limpiesa con productos de diferentes colores y olores", 10.0, R.drawable.clor, 1)
                insertarNuevoServicio("SERVICIO LIMPIESA 2", "Limpiesa con productos de diferentes colores y olores", 15.0, R.drawable.clor1, 1)
                insertarNuevoServicio("SERVICIO LIMPIESA 3", "Limpiesa con productos de diferentes colores y olores", 10.0, R.drawable.clor2, 1)
                insertarNuevoServicio("SERVICIO LIMPIESA 4", "Limpiesa con productos de diferentes colores y olores", 15.0, R.drawable.clor3, 1)
                insertarNuevoServicio("SERVICIO LIMPIESA 5", "Limpiesa con productos de diferentes colores y olores", 10.0, R.drawable.clor4, 1)


                insertarNuevoServicio("SERVICIO 1", "", 15.0, R.drawable.cent, 2)
                insertarNuevoServicio("SERVICIO 2", "", 10.0, R.drawable.cent2, 2)
                insertarNuevoServicio("SERVICIO 3", "", 15.0, R.drawable.cent3, 2)
                insertarNuevoServicio("SERVICIO 4", "", 10.0, R.drawable.cent4, 2)
                insertarNuevoServicio("SERVICIO 5", "", 15.0, R.drawable.cent5, 2)


                insertarNuevoServicio("SERVICIO 1", "", 10.0, R.drawable.rest, 3)
                insertarNuevoServicio("SERVICIO 2", "", 15.0, R.drawable.rest1, 3)
                insertarNuevoServicio("SERVICIO 1", "", 10.0, R.drawable.rest2, 3)
                insertarNuevoServicio("SERVICIO 2", "", 15.0, R.drawable.rest3, 3)
                insertarNuevoServicio("SERVICIO 1", "", 10.0, R.drawable.rest4, 3)
                // ... Añadir más productos según sea necesario


            }

        }
    }

    override fun onVerEmpresaClick(position: Int) {
        val empresaSeleccionada = adapter.empresas[position]

        // Crear un Intent para la nueva actividad
        val intent = Intent(this, DetalleEmpresaServicio::class.java)

        // Puedes pasar el ID de la empresa a la nueva actividad
        intent.putExtra("empresa_id", empresaSeleccionada.id)

        // Iniciar la nueva actividad
        startActivity(intent)
    }
    private fun insertarNuevaEmpresaS() {
        cargarImagenesDesdeDrawables()
        val nuevaEmpresaS = Empresa(
            nombre = "ASERLIMEBPEV EC ",
            slogan = "Servicios de Limpieza dirigido a empresas públicas, privadas, y personas naturales, con calidad, calidez, eficiencia, honestidad y responsabilidad en la Provincia de Orellana,",
            nombrePropietario = "Maria Perez ",
            facebook = "https://www.facebook.com/AserlimebpevEC",
            instagram = "instagram.com/miempresa1",
            whatsapp = "593 98 456 9768",
            direccion = "null",
            imagen_empresa = imagenNuEmpresaS,
            imagen_propietario = imagenNuPropietarioS,
            video_url = "https://drive.google.com/file/d/1beK1gGxkpTJyx5Og-lmKNQEqKXg0d8rh/view?usp=drive_link",
            fkEmpresaCanton = 1

        )
        val idNuevaEmpresa = databaseHelper.insertarEmpresaS(nuevaEmpresaS)
        if (idNuevaEmpresa != -1L) {
            // Se insertó correctamente, haz algo si es necesario
        } else {
            // Fallo al insertar, maneja el error
        }
    }
    private fun insertarNuevaEmpresaS2() {
        cargarImagenesDesdeDrawables2()
        val nuevaEmpresaS = Empresa(
            nombre = "CENTRO TURISTICO COMUNITARIO SHUAR JEMPE ",
            slogan = "¡Descubre la esencia de la cultura Shuar y su conexión única con la naturaleza!",
            nombrePropietario = "Jose  Ramirez",
            facebook = "https://www.facebook.com/profile.php?id=61554703256013",
            instagram = "instagram.com/miempresa1",
            whatsapp = "593 93 456 5789",
            direccion = "",
            imagen_empresa = imagenNuEmpresaS2,
            imagen_propietario = imagenNuPropietarioS2,
            video_url = "https://drive.google.com/file/d/1beK1gGxkpTJyx5Og-lmKNQEqKXg0d8rh/view?usp=drive_link",
            fkEmpresaCanton = 1

        )
        val idNuevaEmpresa = databaseHelper.insertarEmpresaS(nuevaEmpresaS)
        if (idNuevaEmpresa != -1L) {
            // Se insertó correctamente, haz algo si es necesario
        } else {
            // Fallo al insertar, maneja el error
        }
    }
    private fun insertarNuevaEmpresaS3() {
        cargarImagenesDesdeDrawables3()
        val nuevaEmpresaS = Empresa(
            nombre = "RESTAURANT MADERA Y PIEDRA\n",
            slogan = "Somos un restaurante en los que nuestros clientes pueden disfrutar de productos de calidad.\n",
            nombrePropietario = "Cecilia Torres ",
            facebook = "https://www.facebook.com/profile.php?id=61553177210905",
            instagram = "https://www.instagram.com/restaurant_madera_y_piedra/",
            whatsapp = "593 98 789 0879",
            direccion = "",
            imagen_empresa = imagenNuEmpresaS3,
            imagen_propietario = imagenNuPropietarioS3,
            video_url = "https://drive.google.com/file/d/1-_OKM5hPefSy0DpEKsx3lyt9uUzdxfwI/view?usp=drive_link",
            fkEmpresaCanton = 1

        )
        val idNuevaEmpresa = databaseHelper.insertarEmpresaS(nuevaEmpresaS)
        if (idNuevaEmpresa != -1L) {
            // Se insertó correctamente, haz algo si es necesario
        } else {
            // Fallo al insertar, maneja el error
        }
    }


   //Insertar Servicio

    private fun serviciosYaInsertados(): Boolean {
        // Puedes implementar lógica para verificar si los productos ya existen en la base de datos
        // Por ejemplo, contar el número de productos para una empresa específica y verificar si es mayor que cero
        val count = databaseHelper.contarServiciosPorEmpresa(1)  // Reemplaza con el id de empresa correcto
        return count > 0
    }

    private fun insertarNuevoServicio(nombre: String, descripcion: String, precio: Double, imagenResId: Int, fkEmpresa: Long) {
        val imagenByteArray = cargarImagenDesdeDrawable(imagenResId)

        val nuevoServicio = Servicio(
            nombre = nombre,
            imagen = imagenByteArray,
            precio = precio,
            descripcion = descripcion,
            fkProductoEmpresa = fkEmpresa
        )

        val idNuevoProducto = databaseHelper.insertarServicio(nuevoServicio)
        if (idNuevoProducto != -1L) {
            // Se insertó correctamente, haz algo si es necesario
        } else {
            // Fallo al insertar, maneja el error
        }
    }

    private fun cargarImagenDesdeDrawable(drawableResId: Int): ByteArray? {
        val drawable: Drawable? = ContextCompat.getDrawable(this, drawableResId)
        val bitmap: Bitmap = drawableToBitmap(drawable)
        return bitmap.toByteArray()
    }
   //---------------------



}
