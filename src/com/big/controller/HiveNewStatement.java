import org.apache.hive.jdbc.HiveConnection;
import org.apache.hive.jdbc.HiveStatement;
import org.apache.hive.service.cli.thrift.TCLIService;
import org.apache.hive.service.cli.thrift.TSessionHandle;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sandeep on 23/8/15.
 */
public class HiveNewStatement extends HiveStatement{

    private ResultSet resultSet;
    public HiveNewStatement(HiveConnection connection, TCLIService.Iface client, TSessionHandle sessHandle) {
        super(connection, client, sessHandle);
    }

    public HiveNewStatement(HiveConnection connection, TCLIService.Iface client, TSessionHandle sessHandle, boolean isScrollableResultset) {
        super(connection, client, sessHandle, isScrollableResultset);



    }

    public ResultSet executeQuery(String sql) throws SQLException {

        this.execute(sql);
        if(!this.execute(sql)) {
            throw new SQLException("The query did not generate a result set!");
        } else {
            return this.resultSet;
        }
    }



}
