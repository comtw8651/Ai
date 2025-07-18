package com.example.demo.service;

import com.example.demo.entity.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 自定義 RowMapper 來映射 ResultSet 到 Theme 對象
    private static final class ThemeRowMapper implements RowMapper<Theme> {
        @Override
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            Theme theme = new Theme();
            theme.setId(rs.getLong("id"));
            theme.setThemeName(rs.getString("theme_name"));
            theme.setDisplayName(rs.getString("display_name"));
            theme.setPrice(rs.getInt("price"));

            // 映射 created_at 欄位
            java.sql.Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
            if (createdAtTimestamp != null) {
                theme.setCreatedAt(createdAtTimestamp.toLocalDateTime());
            }

            // 映射 page 欄位 (之前為 pageImage，現已更名為 page)
            byte[] pageData = rs.getBytes("page");
            theme.setPage(pageData); // 使用新的 setter 方法
            return theme;
        }
    }

    public List<Theme> getAllThemes() {
        // SELECT 語句中加入 created_at 欄位
        String sql = "SELECT id, theme_name, display_name, price, created_at, page FROM themes";
        return jdbcTemplate.query(sql, new ThemeRowMapper());
    }

    public Optional<Theme> findByThemeName(String themeName) {
        // SELECT 語句中加入 created_at 欄位
        String sql = "SELECT id, theme_name, display_name, price, created_at, page FROM themes WHERE theme_name = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, new ThemeRowMapper(), themeName);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Theme> findById(Long id) {
        // SELECT 語句中加入 created_at 欄位
        String sql = "SELECT id, theme_name, display_name, price, created_at, page FROM themes WHERE id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, new ThemeRowMapper(), id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 可以在這裡新增方法來插入預設主題，以防資料庫中沒有
    public void createDefaultThemeIfNotExists() {
        if (findByThemeName("default").isEmpty()) {
            // INSERT 語句現在包含了 page 欄位
            // created_at 由於設定了 DEFAULT current_timestamp()，無需在 INSERT 中明確指定
            String sql = "INSERT INTO themes (theme_name, display_name, price, page) VALUES (?, ?, ?, ?)";
            // 為 page 欄位提供一個空字節陣列，因為它是 NOT NULL
            jdbcTemplate.update(sql, "default", "預設主題", 0, new byte[0]);
            System.out.println("Default theme 'default' inserted into themes table.");
        }
    }
}