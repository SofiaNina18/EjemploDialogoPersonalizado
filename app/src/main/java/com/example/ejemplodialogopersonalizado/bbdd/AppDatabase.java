package com.example.ejemplodialogopersonalizado.bbdd;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ejemplodialogopersonalizado.model.Usuario;

@Database(entities = {Usuario.class}, version = 1, exportSchema = false)
//Una clase abstracta es una clase que no se puede instanciar
public abstract class AppDatabase extends RoomDatabase
{
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME  = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static AppDatabase sIntance;

    public static AppDatabase getInstance(Context context)
    {
        if (sIntance == null) {
            synchronized (LOCK)
            {
                Log.d(LOG_TAG, "Creando la base de datos");
                sIntance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();
            }
        }
        return sIntance;
    }
    public abstract UsuariosDao usuariosDao();

}














