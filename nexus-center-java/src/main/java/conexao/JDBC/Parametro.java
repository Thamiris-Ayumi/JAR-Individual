/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author thamiris
 */
public class Parametro {

    private Integer idParametroAlerta;
    private Double alertaDecimal;
    private String inidadeMedida;

    public Parametro(Integer idParametroAlerta, Double alertaDecimal, String inidadeMedida) {
        this.idParametroAlerta = idParametroAlerta;
        this.alertaDecimal = alertaDecimal;
        this.inidadeMedida = inidadeMedida;
    }

    public Parametro() {
    }

    public JdbcTemplate conectloc() {
        JdbcTemplate conectloc = new Conexao().getConnection();

        return conectloc;
    }

    public JdbcTemplate conectnuv() {
        JdbcTemplate conectnuv = new Conexao().getConnectionAzu();

        return conectnuv;
    }
    public List<Parametro> listarTodos() {
        List<Parametro> todosParam = this.conectloc().query("select * from ParametroAlerta;",
                new ParametroRowMapper());
        return todosParam;
    }
    public Integer tamanhoTotalParam(){
        Integer tamanho=this.listarTodos().size();
        return tamanho;
    }
    public Parametro ultimoparametro(){
        Parametro ultimo= this.conectloc().queryForObject("select * from ParametroAlerta where tipo=?,?", 
                new ParametroRowMapper(),"usuario","senha");
        return ultimo;
    }

    public Integer getIdParametroAlerta() {
        return idParametroAlerta;
    }

    public void setIdParametroAlerta(Integer idParametroAlerta) {
        this.idParametroAlerta = idParametroAlerta;
    }

    public Double getAlertaDecimal() {
        return alertaDecimal;
    }

    public void setAlertaDecimal(Double alertaDecimal) {
        this.alertaDecimal = alertaDecimal;
    }

    public String getInidadeMedida() {
        return inidadeMedida;
    }

    public void setInidadeMedida(String inidadeMedida) {
        this.inidadeMedida = inidadeMedida;
    }

}
