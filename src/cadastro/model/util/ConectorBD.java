package cadastro.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ConectorBD {

    private final Connection conn;

    public ConectorBD() throws SQLException {
        this.conn = startConnection();
    }

    private Connection startConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS:1433;"
                    + "databaseName=loja;encrypt=true;user=loja;password=loja;trustServerCertificate=true;");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Erro ao iniciar o driver de conexao.", e);
        } catch (SQLException e) {
            throw new SQLException("Verifique a URL de conexao.", e);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public PreparedStatement getPrepared(String sql) throws SQLException {
        return this.conn.prepareStatement(sql);
    }

    private Statement getStatement() throws SQLException {
        return this.conn.createStatement();
    }

    public ResultSet getSelect(String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    public void close() throws SQLException {
        this.conn.close();
    }

    public void close(Statement st) throws SQLException {
        st.close();
    }

    public void close(ResultSet rs) throws SQLException {
        rs.close();
    }
}
