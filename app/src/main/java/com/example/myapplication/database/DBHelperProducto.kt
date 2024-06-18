package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class DBHelperProducto(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tablas
        db.execSQL(Canton.CREATE_TABLE)
        db.execSQL(Empresa.CREATE_TABLE)
        db.execSQL(Producto.CREATE_TABLE)

        // Insertar cantones iniciales-
        insertInitialCantones(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Manejar actualizaciones de la base de datos si es necesario
    }

    // Funciones para Cantón
    // En el método insertCanton
    fun insertCanton(canton: Canton, db: SQLiteDatabase? = null): Long {
        val writableDb = db ?: this.writableDatabase
        val values = ContentValues()
        values.put(Canton.COLUMN_NOMBRE_CANTON, canton.nombreCanton)  // Cambiado de "nombre" a "nombre_canton"
        // Otros campos...
        return writableDb.insert(Canton.TABLE_NAME, null, values)
    }

    private fun insertInitialCantones(db: SQLiteDatabase) {
        val cantones = listOf("FRANCISCO DE ORELLANA", "SACHA", "LORETO", "AGUARICO")

        for (cantonNombre in cantones) {
            val canton = Canton(nombreCanton = cantonNombre)
            insertCanton(canton, db)
        }
    }

    fun getCantonByName(nombre: String): Canton? {
        val db = this.readableDatabase
        val cursor = db.query(
            Canton.TABLE_NAME,
            arrayOf(Canton.COLUMN_ID, Canton.COLUMN_NOMBRE_CANTON),
            "${Canton.COLUMN_NOMBRE_CANTON} = ?",
            arrayOf(nombre),
            null,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(Canton.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Canton.COLUMN_NOMBRE_CANTON)

            val id = cursor.getLong(idIndex)
            val cantonNombre = cursor.getString(nombreIndex)

            Canton(id, cantonNombre)
        } else {
            null
        }
    }

    fun getAllCantones(): List<Canton> {
        val cantones = mutableListOf<Canton>()
        val db = this.readableDatabase
        val cursor = db.query(
            Canton.TABLE_NAME,
            arrayOf(Canton.COLUMN_ID, Canton.COLUMN_NOMBRE_CANTON),
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(Canton.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Canton.COLUMN_NOMBRE_CANTON)

            val id = cursor.getLong(idIndex)
            val cantonNombre = cursor.getString(nombreIndex)

            val canton = Canton(id, cantonNombre)
            cantones.add(canton)
        }

        cursor.close()
        return cantones
    }

    // Funciones para Empresa
    fun insertEmpresa(empresa: Empresa): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Empresa.COLUMN_NOMBRE, empresa.nombre)
        values.put(Empresa.COLUMN_SLOGAN, empresa.slogan)
        values.put(Empresa.COLUMN_NOMBRE_PROPIETARIO, empresa.nombrePropietario)
        values.put(Empresa.COLUMN_FACEBOOK, empresa.facebook)
        values.put(Empresa.COLUMN_INSTAGRAM, empresa.instagram)
        values.put(Empresa.COLUMN_WHATSAPP, empresa.whatsapp)
        values.put(Empresa.COLUMN_DIRECCION, empresa.direccion)
        // Conversión de imagen de ByteArray a Blob
        values.put(Empresa.COLUMN_IMAGEN_EMPRESA, empresa.imagen_empresa)
        values.put(Empresa.COLUMN_IMAGEN_PROPIETARIO, empresa.imagen_propietario)
        values.put(Empresa.COLUMN_VIDEO_URL, empresa.video_url) // Nueva columna para la URL del video
        values.put(Empresa.COLUMN_FK_EMPRESA_CANTON, empresa.fkEmpresaCanton)

        return db.insert(Empresa.TABLE_NAME, null, values)
    }

    // Obtener Empresa de Acuerdo a un Canton
    fun getEmpresasByCantonId(cantonId: Long): List<Empresa> {
        val empresas = mutableListOf<Empresa>()
        val db = this.readableDatabase
        val cursor = db.query(
            Empresa.TABLE_NAME,
            null,
            "${Empresa.COLUMN_FK_EMPRESA_CANTON} = ?",
            arrayOf(cantonId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(Empresa.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE)
            val sloganIndex = cursor.getColumnIndex(Empresa.COLUMN_SLOGAN)
            val nombrePropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE_PROPIETARIO)
            val facebookIndex = cursor.getColumnIndex(Empresa.COLUMN_FACEBOOK)
            val instagramIndex = cursor.getColumnIndex(Empresa.COLUMN_INSTAGRAM)
            val whatsappIndex = cursor.getColumnIndex(Empresa.COLUMN_WHATSAPP)
            val direccionIndex = cursor.getColumnIndex(Empresa.COLUMN_DIRECCION)
            val imagenEmpresaIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_EMPRESA)
            val imagenPropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_PROPIETARIO)
            val videoUrlIndex = cursor.getColumnIndex(Empresa.COLUMN_VIDEO_URL) // Nueva columna para la URL del video
            val fkEmpresaCantonIndex = cursor.getColumnIndex(Empresa.COLUMN_FK_EMPRESA_CANTON)

            val id = cursor.getLong(idIndex)
            val nombre = cursor.getString(nombreIndex)
            val slogan = cursor.getString(sloganIndex)
            val nombrePropietario = cursor.getString(nombrePropietarioIndex)
            val facebook = cursor.getString(facebookIndex)
            val instagram = cursor.getString(instagramIndex)
            val whatsapp = cursor.getString(whatsappIndex)
            val direccion = cursor.getString(direccionIndex)
            val imagenEmpresa = cursor.getBlob(imagenEmpresaIndex)
            val imagenPropietario = cursor.getBlob(imagenPropietarioIndex)
            val videoUrl = cursor.getString(videoUrlIndex) // Nueva variable para la URL del video
            val fkEmpresaCanton = cursor.getLong(fkEmpresaCantonIndex)

            val empresa = Empresa(
                id, nombre, slogan, nombrePropietario, facebook, instagram, whatsapp,
                direccion, imagenEmpresa, imagenPropietario, videoUrl, fkEmpresaCanton // Actualización del campo de video
            )

            empresas.add(empresa)
        }

        cursor.close()
        return empresas
    }

    // Función para obtener datos de una empresa en específico
    fun getEmpresaById(empresaId: Long): Empresa? {
        val db = this.readableDatabase
        val cursor = db.query(
            Empresa.TABLE_NAME,
            null,
            "${Empresa.COLUMN_ID} = ?",
            arrayOf(empresaId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(Empresa.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE)
            val sloganIndex = cursor.getColumnIndex(Empresa.COLUMN_SLOGAN)
            val nombrePropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE_PROPIETARIO)
            val facebookIndex = cursor.getColumnIndex(Empresa.COLUMN_FACEBOOK)
            val instagramIndex = cursor.getColumnIndex(Empresa.COLUMN_INSTAGRAM)
            val whatsappIndex = cursor.getColumnIndex(Empresa.COLUMN_WHATSAPP)
            val direccionIndex = cursor.getColumnIndex(Empresa.COLUMN_DIRECCION)
            val imagenEmpresaIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_EMPRESA)
            val imagenPropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_PROPIETARIO)
            val videoUrlIndex = cursor.getColumnIndex(Empresa.COLUMN_VIDEO_URL) // Nueva columna para la URL del video
            val fkEmpresaCantonIndex = cursor.getColumnIndex(Empresa.COLUMN_FK_EMPRESA_CANTON)

            val id = cursor.getLong(idIndex)
            val nombre = cursor.getString(nombreIndex)
            val slogan = cursor.getString(sloganIndex)
            val nombrePropietario = cursor.getString(nombrePropietarioIndex)
            val facebook = cursor.getString(facebookIndex)
            val instagram = cursor.getString(instagramIndex)
            val whatsapp = cursor.getString(whatsappIndex)
            val direccion = cursor.getString(direccionIndex)
            val imagenEmpresa = cursor.getBlob(imagenEmpresaIndex)
            val imagenPropietario = cursor.getBlob(imagenPropietarioIndex)
            val videoUrl = cursor.getString(videoUrlIndex) // Nueva variable para la URL del video
            val fkEmpresaCanton = cursor.getLong(fkEmpresaCantonIndex)

            Empresa(
                id, nombre, slogan, nombrePropietario, facebook, instagram, whatsapp,
                 direccion, imagenEmpresa, imagenPropietario, videoUrl, fkEmpresaCanton // Actualización del campo de video
            )
        } else {
            null
        }
    }

    // Fin de nueva función

    //Funcion Actualizar Empresa
    fun updateEmpresa(empresa: Empresa): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Empresa.COLUMN_NOMBRE, empresa.nombre)
            put(Empresa.COLUMN_SLOGAN, empresa.slogan)
            put(Empresa.COLUMN_NOMBRE_PROPIETARIO, empresa.nombrePropietario)
            put(Empresa.COLUMN_FACEBOOK, empresa.facebook)
            put(Empresa.COLUMN_INSTAGRAM, empresa.instagram)
            put(Empresa.COLUMN_WHATSAPP, empresa.whatsapp)
            put(Empresa.COLUMN_DIRECCION, empresa.direccion)
            put(Empresa.COLUMN_VIDEO_URL, empresa.video_url)
            put(Empresa.COLUMN_FK_EMPRESA_CANTON, empresa.fkEmpresaCanton)

            // Manejo de valores nulos
            putNullables(Empresa.COLUMN_IMAGEN_EMPRESA, empresa.imagen_empresa)
            putNullables(Empresa.COLUMN_IMAGEN_PROPIETARIO, empresa.imagen_propietario)
        }

        val selection = "${Empresa.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(empresa.id.toString())

        val rowsAffected = db.update(Empresa.TABLE_NAME, values, selection, selectionArgs)
        db.close()

        return rowsAffected
    }

    private fun ContentValues.putNullables(columnName: String, value: ByteArray?) {
        if (value == null) {
            putNull(columnName)
        } else {
            put(columnName, value)
        }
    }



    //-----------

    //funcion para actualizarempresaproducto

    fun getEmpresaByNombre(nombre: String): Empresa? {
        val db = this.readableDatabase
        val cursor = db.query(
            Empresa.TABLE_NAME,
            null,
            "${Empresa.COLUMN_NOMBRE} = ?",
            arrayOf(nombre),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(Empresa.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE)
            val sloganIndex = cursor.getColumnIndex(Empresa.COLUMN_SLOGAN)
            val nombrePropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE_PROPIETARIO)
            val facebookIndex = cursor.getColumnIndex(Empresa.COLUMN_FACEBOOK)
            val instagramIndex = cursor.getColumnIndex(Empresa.COLUMN_INSTAGRAM)
            val whatsappIndex = cursor.getColumnIndex(Empresa.COLUMN_WHATSAPP)
            val direccionIndex = cursor.getColumnIndex(Empresa.COLUMN_DIRECCION)
            val imagenEmpresaIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_EMPRESA)
            val imagenPropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_PROPIETARIO)
            val videoUrlIndex = cursor.getColumnIndex(Empresa.COLUMN_VIDEO_URL)
            val fkEmpresaCantonIndex = cursor.getColumnIndex(Empresa.COLUMN_FK_EMPRESA_CANTON)

            val id = cursor.getLong(idIndex)
            val empresaNombre = cursor.getString(nombreIndex)
            val slogan = cursor.getString(sloganIndex)
            val nombrePropietario = cursor.getString(nombrePropietarioIndex)
            val facebook = cursor.getString(facebookIndex)
            val instagram = cursor.getString(instagramIndex)
            val whatsapp = cursor.getString(whatsappIndex)
            val direccion = cursor.getString(direccionIndex)
            val imagenEmpresa = cursor.getBlob(imagenEmpresaIndex)
            val imagenPropietario = cursor.getBlob(imagenPropietarioIndex)
            val videoUrl = cursor.getString(videoUrlIndex)
            val fkEmpresaCanton = cursor.getLong(fkEmpresaCantonIndex)

            Empresa(
                id, empresaNombre, slogan, nombrePropietario, facebook, instagram, whatsapp,
                direccion, imagenEmpresa, imagenPropietario, videoUrl, fkEmpresaCanton
            )
        } else {
            null
        }
    }

    //Funcion eliminar empresaproducto
    fun eliminarEmpresaPorNombreYIdCanton(nombreEmpresa: String, idCanton: Long): Boolean {
        val db = this.writableDatabase

        // Asegúrate de manejar el caso donde la columna de nombre tenga el mismo nombre que la variable
        val whereClause = "${Empresa.COLUMN_NOMBRE} = ? AND ${Empresa.COLUMN_FK_EMPRESA_CANTON} = ?"
        val whereArgs = arrayOf(nombreEmpresa, idCanton.toString())

        // Intenta realizar la eliminación y maneja cualquier excepción
        return try {
            db.delete(Empresa.TABLE_NAME, whereClause, whereArgs) > 0
        } catch (e: Exception) {
            // Puedes imprimir el mensaje de error si es necesario
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

// En tu clase DBHelperProducto

    fun eliminarEmpresaPorNombre(nombreEmpresa: String): Boolean {
        val db = this.writableDatabase

        // Asegúrate de manejar el caso donde la columna de nombre tenga el mismo nombre que la variable
        val whereClause = "${Empresa.COLUMN_NOMBRE} = ?"
        val whereArgs = arrayOf(nombreEmpresa)

        // Intenta realizar la eliminación y maneja cualquier excepción
        return try {
            db.delete(Empresa.TABLE_NAME, whereClause, whereArgs) > 0
        } catch (e: Exception) {
            // Puedes imprimir el mensaje de error si es necesario
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }
    //--------------

    //Funcion para listar productos de empresa
    fun getProductosByEmpresaId(empresaId: Long): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = this.readableDatabase
        val cursor = db.query(
            Producto.TABLE_NAME,
            null,
            "${Producto.COLUMN_FK_PRODUCTO_EMPRESA} = ?",
            arrayOf(empresaId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(Producto.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Producto.COLUMN_NOMBRE)
            val imagenIndex = cursor.getColumnIndex(Producto.COLUMN_IMAGEN)
            val precioIndex = cursor.getColumnIndex(Producto.COLUMN_PRECIO)
            val descripcionIndex = cursor.getColumnIndex(Producto.COLUMN_DESCRIPCION)
            val fkProductoEmpresaIndex = cursor.getColumnIndex(Producto.COLUMN_FK_PRODUCTO_EMPRESA)

            val id = cursor.getLong(idIndex)
            val nombre = cursor.getString(nombreIndex)
            val imagen = cursor.getBlob(imagenIndex)
            val precio = cursor.getDouble(precioIndex)
            val descripcion = cursor.getString(descripcionIndex)
            val fkProductoEmpresa = cursor.getLong(fkProductoEmpresaIndex)

            val producto = Producto(id, nombre, imagen, precio, descripcion, fkProductoEmpresa)
            productos.add(producto)
        }

        cursor.close()
        return productos
    }

    //------------

    //-------

    //Funcion para insertar productos
    fun getEmpresaByNombreYIdCanton(nombreEmpresa: String, idCanton: Long): Empresa? {
        val db = this.readableDatabase
        val cursor = db.query(
            Empresa.TABLE_NAME,
            null,
            "${Empresa.COLUMN_NOMBRE} = ? AND ${Empresa.COLUMN_FK_EMPRESA_CANTON} = ?",
            arrayOf(nombreEmpresa, idCanton.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(Empresa.COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE)
            val sloganIndex = cursor.getColumnIndex(Empresa.COLUMN_SLOGAN)
            val nombrePropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_NOMBRE_PROPIETARIO)
            val facebookIndex = cursor.getColumnIndex(Empresa.COLUMN_FACEBOOK)
            val instagramIndex = cursor.getColumnIndex(Empresa.COLUMN_INSTAGRAM)
            val whatsappIndex = cursor.getColumnIndex(Empresa.COLUMN_WHATSAPP)
            val direccionIndex = cursor.getColumnIndex(Empresa.COLUMN_DIRECCION)
            val imagenEmpresaIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_EMPRESA)
            val imagenPropietarioIndex = cursor.getColumnIndex(Empresa.COLUMN_IMAGEN_PROPIETARIO)
            val videoUrlIndex = cursor.getColumnIndex(Empresa.COLUMN_VIDEO_URL)
            val fkEmpresaCantonIndex = cursor.getColumnIndex(Empresa.COLUMN_FK_EMPRESA_CANTON)

            val id = cursor.getLong(idIndex)
            val empresaNombre = cursor.getString(nombreIndex)
            val slogan = cursor.getString(sloganIndex)
            val nombrePropietario = cursor.getString(nombrePropietarioIndex)
            val facebook = cursor.getString(facebookIndex)
            val instagram = cursor.getString(instagramIndex)
            val whatsapp = cursor.getString(whatsappIndex)
            val direccion = cursor.getString(direccionIndex)
            val imagenEmpresa = cursor.getBlob(imagenEmpresaIndex)
            val imagenPropietario = cursor.getBlob(imagenPropietarioIndex)
            val videoUrl = cursor.getString(videoUrlIndex)
            val fkEmpresaCanton = cursor.getLong(fkEmpresaCantonIndex)

            Empresa(
                id, empresaNombre, slogan, nombrePropietario, facebook, instagram, whatsapp,
                direccion, imagenEmpresa, imagenPropietario, videoUrl, fkEmpresaCanton
            )
        } else {
            null
        }
    }




    fun insertProducto(producto: Producto): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Producto.COLUMN_NOMBRE, producto.nombre)
            put(Producto.COLUMN_IMAGEN, producto.imagen)
            put(Producto.COLUMN_PRECIO, producto.precio)
            put(Producto.COLUMN_DESCRIPCION, producto.descripcion)
            put(Producto.COLUMN_FK_PRODUCTO_EMPRESA, producto.fkProductoEmpresa)
        }

        return db.insert(Producto.TABLE_NAME, null, values)
    }


    //--------------------------

    // Funciones para Producto
    // (Similar a Cantón)
    fun insertarEmpresaP(empresa: Empresa): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put(Empresa.COLUMN_NOMBRE, empresa.nombre)
            put(Empresa.COLUMN_SLOGAN, empresa.slogan)
            put(Empresa.COLUMN_NOMBRE_PROPIETARIO, empresa.nombrePropietario)
            put(Empresa.COLUMN_FACEBOOK, empresa.facebook)
            put(Empresa.COLUMN_INSTAGRAM, empresa.instagram)
            put(Empresa.COLUMN_WHATSAPP, empresa.whatsapp)
            put(Empresa.COLUMN_DIRECCION, empresa.direccion)
            put(Empresa.COLUMN_IMAGEN_EMPRESA, empresa.imagen_empresa)
            put(Empresa.COLUMN_IMAGEN_PROPIETARIO, empresa.imagen_propietario)
            put(Empresa.COLUMN_VIDEO_URL, empresa.video_url)
            put(Empresa.COLUMN_FK_EMPRESA_CANTON, empresa.fkEmpresaCanton)
        }

        // Insertar la nueva fila, devolviendo el valor de la clave primaria de la nueva fila
        val id = db.insert(Empresa.TABLE_NAME, null, valores)

        // Cerrar la conexión a la base de datos
        db.close()

        return id
    }

    //Insertar Producto en Codigo

    fun contarProductosPorEmpresa(idEmpresa: Long): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM ${Producto.TABLE_NAME} WHERE ${Producto.COLUMN_FK_PRODUCTO_EMPRESA} = $idEmpresa"
        val cursor = db.rawQuery(query, null)
        var count = 0

        try {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return count
    }

    fun insertarProducto(producto: Producto): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Producto.COLUMN_NOMBRE, producto.nombre)
            put(Producto.COLUMN_IMAGEN, producto.imagen)
            put(Producto.COLUMN_PRECIO, producto.precio)
            put(Producto.COLUMN_DESCRIPCION, producto.descripcion)
            put(Producto.COLUMN_FK_PRODUCTO_EMPRESA, producto.fkProductoEmpresa)
        }

        val idNuevoProducto = db.insert(Producto.TABLE_NAME, null, values)
        db.close()
        return idNuevoProducto
    }
    //----------------------------


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "data_producto"
    }

    // Función auxiliar para convertir Bitmap a ByteArray
    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}
