package BD;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Esta clase Representa la Base de Datos propiamente Dicha de la ROOM
 */

@Database(entities = {MisListasCompraTabla.class, ProductoTabla.class, DetalleListaCompraTabla.class}, version = 2, exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
