package Repository;
import Domain.Masina;
import Exceptions.RepoException;
import org.sqlite.SQLiteDataSource;
import java.sql.*;

public class SQLRepositoryMasina extends MemoryRepo<Masina>{
    private Connection conn = null;
        public SQLRepositoryMasina() throws RepoException {
            initDbConnection();
            createSchema();
            getCars();
        }
        private void initDbConnection() throws RepoException {
            try {
                SQLiteDataSource ds = new SQLiteDataSource();
                String JDBC_URL = "jdbc:sqlite:masini.db";
                ds.setUrl(JDBC_URL);
                if (conn == null || conn.isClosed()) {
                    conn = ds.getConnection();
                    if (conn == null) {
                        throw new RepoException("Failed to establish a database connection.");
                    }
                }
            } catch (SQLException e) {
                throw new RepoException("Error creating DB connection"+e);
            }
        }
        public void closeConnection() {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        void createSchema() throws RepoException {
            try {
                try (final Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cars(id int, brand varchar(200), model varchar(200), PRIMARY KEY(id));");
                }
            } catch (SQLException e) {
                throw new RepoException("Error creating DB schema" + e);
            }
        }
    public void addEntity(Masina masina) throws RepoException {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO cars VALUES (?, ?, ?)")) {
                statement.setInt(1, masina.getId());
                statement.setString(2, masina.getMarca());
                statement.setString(3, masina.getModel());
                super.addEntity(masina);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifyEntity(int id, Masina masina) throws RepoException{
            try(PreparedStatement statement = conn.prepareStatement("UPDATE cars SET brand = ?, model = ? WHERE id = ?")) {
                    statement.setString(1, masina.getMarca());
                    statement.setString(2, masina.getModel());
                    statement.setInt(3, masina.getId());
                    super.modifyEntity(id,masina);
                    statement.executeUpdate();
            }
            catch (SQLException e) {
                    throw new RuntimeException(e);
            }
    }
    public void deleteEntity(int id) throws RepoException{
            try(PreparedStatement statement = conn.prepareStatement("DELETE from cars where id ="+id)) {
                super.deleteEntity(id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }
    public void getCars() {
        elems.clear();
        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from cars");
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Masina masina = new Masina(rs.getInt(1), rs.getString(2), rs.getString(3));
                    elems.add(masina);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
