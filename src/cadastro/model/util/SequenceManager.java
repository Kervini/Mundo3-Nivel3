package cadastro.model.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SequenceManager {
    private final Connection conn;
    
    public SequenceManager(Connection conn){
        this.conn = conn;
    }
    
    public int getValue(String sequenceNome) throws SQLException{
        String sql = "select next value for "+ sequenceNome + " as id";
        
        try(Statement st = conn.createStatement()){
            ResultSet rs = st.executeQuery(sql);
                    
            if(rs.next())
                return rs.getInt(1);
        }
        
        return 0;
    }
}
