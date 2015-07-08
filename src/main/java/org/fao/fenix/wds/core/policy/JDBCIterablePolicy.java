package org.fao.fenix.wds.core.policy;

import org.fao.fenix.wds.core.jdbc.JDBCIterable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JDBCIterablePolicy extends JDBCIterable {

    @Override
    public List<String> next() {

        List<String> l = null;

        if (this.isHasNext()) {
            l = new ArrayList<String>();
            try {
                for (int i = 1 ; i <= this.getResultSet().getMetaData().getColumnCount() ; i++) {
                    try {
                        l.add(this.getResultSet().getString(i).trim());
                    } catch (NullPointerException e) {
                        l.add(this.getResultSet().getString(i));
                    }
                }
                this.setHasNext(this.getResultSet().next());
            } catch(SQLException ignored) {

            }
        }

        if (!this.isHasNext()) {
            try {
                this.getResultSet().close();
                this.getStatement().close();
                this.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return l;
    }

    public void closeConnection(){
        try {
            this.getResultSet().close();
            this.getStatement().close();
            this.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}