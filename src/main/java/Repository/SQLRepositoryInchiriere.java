package Repository;
import Domain.Inchiriere;
import Domain.Masina;
import Exceptions.RepoException;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class SQLRepositoryInchiriere extends MemoryRepo<Inchiriere>{
    private Connection conn = null;
    public SQLRepositoryInchiriere() throws RepoException {
        initDbConnection();
        createSchema();
        getRents();
    }
    private void initDbConnection() throws RepoException {
        try {
            // with DataSource
            SQLiteDataSource ds = new SQLiteDataSource();
            String JDBC_URL = "jdbc:sqlite:inchirieri.db";
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
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
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rentals(id int, id_car int, brand varchar(200), model varchar(200), data_inceput varchar(200), data_sfarsit varchar(200), PRIMARY KEY(id))");
            }
        } catch (SQLException e) {
            throw new RepoException("Error creating DB schema" + e);
        }
    }
    public void addEntity(Inchiriere inchiriere) throws RepoException {

        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO rentals VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setInt(1, inchiriere.getId());
                statement.setInt(2, inchiriere.getCar().getId());
                statement.setString(3, inchiriere.getCar().getMarca());
                statement.setString(4, inchiriere.getCar().getModel());
                statement.setString(5, inchiriere.getDateIn());
                statement.setString(6, inchiriere.getDateOut());
                super.addEntity(inchiriere);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyEntity(int id, Inchiriere in) throws RepoException {
        try(PreparedStatement preparedStatement = conn.prepareStatement("UPDATE rentals SET id_car = ?, brand = ?, model = ?, data_inceput = ?, data_sfarsit = ? where id ="+id)) {
            preparedStatement.setInt(1,in.getCar().getId());
            preparedStatement.setString(2,in.getCar().getMarca());
            preparedStatement.setString(3,in.getCar().getModel());
            preparedStatement.setString(4,in.getDateIn());
            preparedStatement.setString(5,in.getDateOut());
            super.modifyEntity(id, in);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteEntity(int id) throws RepoException {
        try(PreparedStatement statement = conn.prepareStatement("DELETE from rentals where id="+id)) {
            super.deleteEntity(id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getRents() {
        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from rentals");
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Inchiriere inchiriere = new Inchiriere(rs.getInt("id"), new Masina(rs.getInt("id_car"), rs.getString("brand"), rs.getString("model")), rs.getString("data_inceput"), rs.getString("data_sfarsit"));
                    elems.add(inchiriere);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}