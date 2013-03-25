package net.canarymod.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.canarymod.Canary;
import net.canarymod.database.Column;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseTableInconsistencyException;
import net.canarymod.database.exceptions.DatabaseWriteException;


/**
 *
 * @author Somners
 */
public class MySQLDatabase extends Database {

    private static MySQLDatabase instance;
    private static MySQLConnectionPool pool;

    public MySQLDatabase(){
        pool = new MySQLConnectionPool();
    }

    public static MySQLDatabase getInstance() {
        if(instance == null) {
            instance = new MySQLDatabase();
        }
        return instance;
    }

    @Override
    public void insert(DataAccess data) throws DatabaseWriteException {
        Connection conn = pool.getConnectionFromPool();
        PreparedStatement ps = null;
        String fieldString = "";
        String valueString = "";

        try {
            ps = conn.prepareStatement("INSERT INTO Table (" + fieldString + ") VALUES(" + valueString + ")");
            StringBuilder fields = new StringBuilder();
            StringBuilder values = new StringBuilder();
            HashMap<Column, Object> columns = data.toDatabaseEntryList();
            Iterator<Column> it = columns.keySet().iterator();
            for (int i = 0 ; it.hasNext() ; i++) {
                Column column = it.next();
                ps.setObject(i + 1, columns.get(column));
                fields.append("`").append(column.columnName());
                if(it.hasNext()) {
                    fields.append("`, ");
                    values.append("?, ");
                }
                else{
                    fields.append("`");
                    values.append("?");
                }
            }
            fieldString = fields.toString();
            valueString = values.toString();
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseWriteException("Error inserting");
        } catch (DatabaseTableInconsistencyException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closePS(ps);
            pool.returnConnectionToPool(conn);
        }

    }

    @Override
    public void update(DataAccess data, String[] fieldNames, Object[] fieldValues) throws DatabaseWriteException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String tableName, String[] fieldNames, Object[] fieldValues) throws DatabaseWriteException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load(DataAccess dataset, String[] fieldNames, Object[] fieldValues) throws DatabaseReadException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll(DataAccess typeTemplate, List<DataAccess> datasets, String[] fieldNames, Object[] fieldValues) throws DatabaseReadException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSchema(DataAccess schemaTemplate) throws DatabaseWriteException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    public boolean doesPrimaryKeyExist(DataAccess data, String primaryKey, Object value) throws DatabaseWriteException {
        Connection conn = pool.getConnectionFromPool();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valueString = "";
        boolean toRet = false;

        try {
            ps = conn.prepareStatement("SELECT * FROM `"+ data.getName() + "` WHERE `" + primaryKey + "` = ?");
            ps.setObject(1, value);
            rs = ps.executeQuery();
            if(rs == null) {
                toRet = true;
            }

        } catch (SQLException ex) {
            throw new DatabaseWriteException("Error checking Value for MySQL Primary "
                    + "Key in Table `" + data.getName() + "` for key `" + primaryKey
                    + "` and value '" + String.valueOf(value) + "'.");
        } finally {
            this.closePS(ps);
            this.closeRS(rs);
            pool.returnConnectionToPool(conn);
        }
        return toRet;
    }

    public boolean doesEntryExist(DataAccess data) throws DatabaseWriteException {
        Connection conn = pool.getConnectionFromPool();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valueString = "";
        boolean toRet = false;

        try {
            ps = conn.prepareStatement("SELECT * FROM `"+ data.getName() + "` WHERE " + valueString);
            StringBuilder sb = new StringBuilder();
            HashMap<Column, Object> columns = data.toDatabaseEntryList();
            Iterator<Column> it = columns.keySet().iterator();
            for (int i = 0 ; it.hasNext() ; i++) {
                Column column = it.next();
                ps.setObject(i + 1, columns.get(column));
                sb.append("`").append(column.columnName());
                if(it.hasNext()) {
                    sb.append("` = ?, ");
                }
                else{
                    sb.append("` = ?");
                }
            }
            valueString = sb.toString();
            rs = ps.executeQuery();
            if(rs == null) {
                toRet = true;
            }

        } catch (SQLException ex) {
            throw new DatabaseWriteException("Error checking MySQL Entry Key in "
                    + "Table `" + data.getName() + "` for: " + data.toString());
        } catch (DatabaseTableInconsistencyException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closePS(ps);
            this.closeRS(rs);
            pool.returnConnectionToPool(conn);
        }
        return toRet;
    }

    public void closeRS(ResultSet rs) {
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException sqle) {
                Canary.logStackTrace("Error closing ResultSet in MySQL database.", sqle);
            }
        }
    }

    public void closePS(PreparedStatement ps) {
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException sqle) {
                Canary.logStackTrace("Error closing PreparedStatement in MySQL database.", sqle);
            }
        }
    }

}