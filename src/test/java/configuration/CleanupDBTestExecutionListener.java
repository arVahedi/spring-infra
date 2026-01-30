package configuration;

import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CleanupDBTestExecutionListener extends AbstractTestExecutionListener implements Ordered {

    /**
     * Run as late as possible (after transactional rollback, etc.)
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);

        try (Connection con = dataSource.getConnection(); Statement st = con.createStatement()) {

            // H2 best practice for truncating with FKs [oai_citation:1‡Stack Overflow](https://stackoverflow.com/questions/27045568/h2-how-to-truncate-all-tables?utm_source=chatgpt.com)
            st.execute("SET REFERENTIAL_INTEGRITY FALSE");

            List<String> tables = new ArrayList<>();
            try (ResultSet rs = st.executeQuery("""
                    SELECT TABLE_NAME
                    FROM INFORMATION_SCHEMA.TABLES
                    WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_TYPE = 'BASE TABLE';
                    """)) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }

            // Don't touch Flyway's metadata table unless you really mean to.
            tables.removeIf(t -> t.equalsIgnoreCase("flyway_schema_history"));

            for (String t : tables) {
                st.execute("TRUNCATE TABLE " + t);
            }

            st.execute("SET REFERENTIAL_INTEGRITY TRUE");

            // Re-seed static data
//            ScriptUtils.executeSqlScript(con, new ClassPathResource("sql/static-seed.sql"));
        }
    }
}
