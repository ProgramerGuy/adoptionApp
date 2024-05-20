package com.expeditors.adoptionapp.dao.jdbc;

import com.expeditors.adoptionapp.dao.AdopterDAO;
import com.expeditors.adoptionapp.dao.BaseDAO;
import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import com.expeditors.adoptionapp.domain.Pet;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JDBCAdopterDao extends BaseDAO implements AdopterDAO {

    public JDBCAdopterDao() {
        System.out.println("JPA Dao");
    }

    @Override
    public Adopter insert(Adopter newAdopter) {
        //adopters.put(newAdopter.getId(), newAdopter);

        String sql = "INSERT INTO Adopter(name,phone_number,email) VALUES(?,?,?)";

        try(
                Connection con = getConnection();
                PreparedStatement prepStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ){
            prepStmt.setString(1, newAdopter.getName());
            prepStmt.setString(2, newAdopter.getPhoneNumber());
            prepStmt.setString(3, newAdopter.getEmail());
            prepStmt.executeUpdate();

            try (ResultSet getKeys = prepStmt.getGeneratedKeys()) {
                if (getKeys.next()) {
                    newAdopter.setAdopterId(getKeys.getInt(1));
                }
            }

        }catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return newAdopter;
    }

    @Override
    public boolean delete(int id) {
        int rowsAffected = 0;
        String sql = "DELETE FROM Adopter WHERE adopter_id = ?";

        try(
                Connection con = getConnection();
                PreparedStatement prepStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            prepStmt.setInt(1, id);
            rowsAffected = prepStmt.executeUpdate();

        }catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean update(Adopter adopter) {
        String sql = "UPDATE Adopter SET name=?, phone_number = ?, email = ? WHERE adopter_id = ?";

        try(
                Connection con = getConnection();
                PreparedStatement prepStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            prepStmt.setString(1, adopter.getName());
            prepStmt.setString(2, adopter.getPhoneNumber());
            prepStmt.setString(3, adopter.getEmail());
            prepStmt.setInt(4, adopter.getAdopterId());
            prepStmt.executeUpdate();

        }catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public AdoptionRegister creatAdoptionRegister(AdoptionRegister adoptionRegister){
        JdbcClient template = JdbcClient.create(getDataSource());

        int adopteId = 0;
        int petId = 0;

        String insertAdopter = "INSERT INTO adopter(name,phone_number,email) VALUES(?,?,?)";
        String insertPet = "INSERT INTO pet(name,type,breed) VALUES(?,?,?)";
        String insertRegister = "INSERT INTO adoption_register(adopter_id,pet_id,adoption_date) VALUES(?,?,?)";

        List<Object[]> adoptersParams = new ArrayList<>();
        adoptersParams.add(new Object[]{adoptionRegister.getAdopter().getName(), adoptionRegister.getAdopter().getPhoneNumber(), adoptionRegister.getAdopter().getEmail()});

        List<Object[]> petsParams = new ArrayList<>();
        petsParams.add(new Object[]{adoptionRegister.getPet().getName(), adoptionRegister.getPet().getType().toString(), adoptionRegister.getPet().getBreed()});

        List<Object[]> registerParams = new ArrayList<>();

        var keyHolder = new GeneratedKeyHolder();

        for (Object[] params : adoptersParams) {
            template.sql(insertAdopter)
                    .params(params)
                    .update(keyHolder);
        }

        adopteId = (int)keyHolder.getKeys().get("adopter_id");

        for (Object[] params : petsParams) {
            template.sql(insertPet)
                    .params(params)
                    .update(keyHolder);
        }

        petId = (int)keyHolder.getKeys().get("pet_id");
        LocalDate currentDate = LocalDate.now();
        registerParams.add(new Object[]{adopteId, petId, currentDate});

        for (Object[] params : registerParams) {
            template.sql(insertRegister)
                    .params(params)
                    .update(keyHolder);
        }

        adoptionRegister.setAdoptionRegisterId((int)keyHolder.getKeys().get("adoption_register_id"));
        adoptionRegister.getPet().setPetId(petId);
        adoptionRegister.getAdopter().setAdopterId(adopteId);
        adoptionRegister.setAdoptionDate(currentDate);
        return adoptionRegister;
    }

    @Override
    public Adopter findById(int id) {
        //return adopters.get(id);
        return null;
    }

    @Override
    public Optional<Adopter> findByName(String name) {
        return null;
        //return (adopters.values().stream().filter(adopter -> adopter.getName().equals(name)).findFirst());
    }

    @Override
    public List<Adopter> findAll() {
        List<Adopter> adopters = Collections.emptyList();
        String sql = "SELECT * FROM adopter";

        try (
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
        )
        {
            adopters = new ArrayList<>();

            while (rset.next()) {
                Adopter adopter = new Adopter();
                adopter.setAdopterId(rset.getInt("adopter_id"));
                adopter.setName(rset.getString("name"));
                adopter.setPhoneNumber(rset.getString("phone_number"));
                adopter.setEmail(rset.getString("email"));
                adopters.add(adopter);
            }
        }
        catch(Exception sqlException){
            System.out.println(sqlException.getMessage());
        }
        return adopters.stream().sorted((adopter1, adopter2) -> adopter1.getName().compareTo(adopter2.getName())).toList();
    }

    public List<AdoptionRegister> getAdoptions() {
        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sql = "select *" +
                "from adoption_register reg left join adopter ad on reg.adopter_id = ad.adopter_id left join pet pt on reg.pet_id = pt.pet_id";

        List<AdoptionRegister> result = new ArrayList<>();
        ResultSetExtractor<List<AdoptionRegister>> rowMapper = (resultSet) -> {
            int lastId = -1;
            AdoptionRegister currentRegister = null;

            while (resultSet.next()) {
                int id = resultSet.getInt("adoption_register_id");
                if (id != lastId) {
                    lastId = id;

                    LocalDate adoptDate = resultSet.getObject("adoption_date", LocalDate.class);

                    Adopter adopter = new Adopter(
                            resultSet.getInt("adopter_id"),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7)
                    );

                    String type = null;
                    if (resultSet.getString("type") != null) {
                        type = resultSet.getString("type").toLowerCase();
                        type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                    }

                    Pet pet = new Pet.PetBuilder(type != null ? Pet.PetType.valueOf(type) : null).setBreed(resultSet.getString(11)).setName(resultSet.getString(9)).build();
                    pet.setPetId(resultSet.getInt(3));

                    currentRegister = new AdoptionRegister(lastId,adopter,pet,adoptDate);

                    result.add(currentRegister);
                }

            }
            return result;
        };

        List<AdoptionRegister> registers = template.query(sql, rowMapper);

        return registers;
    }

    public final static String Get() {
        return "";
    }
}
