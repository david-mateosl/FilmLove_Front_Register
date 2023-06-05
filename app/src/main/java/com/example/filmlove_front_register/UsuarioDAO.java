package com.example.filmlove_front_register;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class UsuarioDAO extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "usuarios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_IMAGEN_PERFIL = "imagen_perfil";

    public UsuarioDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_IMAGEN_PERFIL + " BLOB)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public void guardarImagenPerfil(String nombreUsuario, byte[] imagenPerfilBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGEN_PERFIL, imagenPerfilBytes);

        String whereClause = COLUMN_NOMBRE + "=?";
        String[] whereArgs = {nombreUsuario};

        int rowsAffected = db.update(TABLE_NAME, values, whereClause, whereArgs);
        if (rowsAffected == 0) {
            // No se encontró ningún usuario con el nombre especificado, insertar uno nuevo
            values.put(COLUMN_NOMBRE, nombreUsuario);
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    public byte[] obtenerImagenPerfil(String nombreUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imagenPerfilBytes = null;

        String[] columns = {COLUMN_IMAGEN_PERFIL};
        String selection = COLUMN_NOMBRE + "=?";
        String[] selectionArgs = {nombreUsuario};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            imagenPerfilBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGEN_PERFIL));
        }
        cursor.close();
        db.close();

        return imagenPerfilBytes;
    }

    public void eliminarUsuario(String nombreUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NOMBRE + "=?";
        String[] whereArgs = {nombreUsuario};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public byte[] getBytesFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
