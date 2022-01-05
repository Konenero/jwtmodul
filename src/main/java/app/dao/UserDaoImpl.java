package app.dao;


import app.model.Role;
import app.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate,
                       ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?",
                new Object[]{id},
                new UserRowMapper());
    }

    @Override
    public User getUserByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new Object[]{username},
                new UserRowMapper());
    }

    @Override
    public User saveUser(User user) {
        jdbcTemplate.update("INSERT INTO users(user_name, age, username, password) values(?, ?, ?, ?)",
                user.getUser_name(), user.getAge(), user.getUsername(), user.getPassword());
        user = getUserByUsername(user.getUsername());
        jdbcTemplate.update("INSERT INTO users_roles(user_id, role_id) values (?, 1)", user.getUser_id());
        user.setRoles(Collections.singletonList(new Role(1, "ROLE_USER")));
        return user;
    }

    @Override
    public void updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET user_name = ?, age = ?, username = ?, password = ? WHERE user_id = ?",
                user.getUser_name(), user.getAge(), user.getUsername(), user.getPassword(), user.getUser_id());
    }

    @Override
    public void deleteUserById(int id) {
        jdbcTemplate.update("DELETE FROM users where user_id = ?", id);
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUser_id(rs.getInt("user_id"));
            user.setUser_name(rs.getString("user_name"));
            user.setAge(rs.getInt("age"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));

            List<Role> roles = jdbcTemplate.query("SELECT roles.role_id, roles.role_name FROM roles INNER JOIN users_roles USING(role_id) WHERE users_roles.user_id = " + user.getUser_id(),
                    new RoleRowMapper());
            user.setRoles(roles);
            return user;
        }
    }
    private class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setRole_id(rs.getInt("role_id"));
            role.setRole_name(rs.getString("role_name"));
            return role;
        }
    }
}
