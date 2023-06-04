/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author thamiris
 */

public class ParametroRowMapper implements RowMapper<Parametro> {

    @Override
    public Parametro mapRow(ResultSet rs, int rowNum) throws SQLException {
        Parametro parametro = new Parametro();
        parametro.setIdParametroAlerta(rs.getInt("idMetricaMouse"));
        parametro.setAlertaDecimal(rs.getDouble("cordenadaX"));
        parametro.setInidadeMedida(rs.getString("cordenadaY"));

        return parametro;

    }
}
