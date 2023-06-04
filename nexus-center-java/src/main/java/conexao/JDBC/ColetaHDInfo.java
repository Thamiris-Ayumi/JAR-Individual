/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author thamiris
 */
public class ColetaHDInfo {

    private DateTimeFormatter formatter;
    private Integer idMetrica;
    private Double capacidade;
    private Double valorUtilizado;
    private String unidadeMedida;
    private String tipoComponente;
    private String modeloComponente;
    private String dataHora;
    private Integer fkMaquina;
    private Integer fkEmpresa;
    private Integer fkComponente;
    private Looca looca;
    private DiscoGrupo grupoDeDiscos;
    private Double bytesEscritos;
    private Double capacidadeocupada;

    public ColetaHDInfo() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        looca = new Looca();
        grupoDeDiscos = looca.getGrupoDeDiscos();
        bytesEscritos = 0.0;
        capacidadeocupada = 0.0;
//      Foi nescessário criar duas listas para prosseguir com a coleta de dados.       
        List<Disco> listadisco = grupoDeDiscos.getDiscos();
        for (Disco disco : listadisco) {
            bytesEscritos += disco.getBytesDeEscritas();

        }
        List<Volume> listavolumed = grupoDeDiscos.getVolumes();
        for (Volume volume : listavolumed) {
            capacidadeocupada += volume.getDisponivel();
        }

        this.idMetrica = null;
        this.capacidade = grupoDeDiscos.getTamanhoTotal().doubleValue() / 1000000000;
        this.valorUtilizado = this.capacidade - (capacidadeocupada / 1000000000);
        this.unidadeMedida = "GB";
        this.dataHora = LocalDateTime.now().format(formatter);
        this.tipoComponente = "HDTotal";
        this.modeloComponente = "Unknow";

    }

    public ColetaHDInfo(Integer idMetrica, Double capacidade, Double valorUtilizado, String unidadeMedida, String tipoComponente, String modeloComponente) {
        this.idMetrica = idMetrica;
        this.capacidade = capacidade;
        this.valorUtilizado = valorUtilizado;
        this.unidadeMedida = unidadeMedida;
        this.tipoComponente = tipoComponente;
        this.modeloComponente = modeloComponente;
    }

    public JdbcTemplate conectHd() {
        JdbcTemplate conection = new Conexao().getConnection();
        return conection;
    }

    public JdbcTemplate conectHdazu() {
        JdbcTemplate conection = new Conexao().getConnectionAzu();
        return conection;
    }

    public void enviaDadosTotalhd(Integer fkMaquina, Integer fkEmpresa) {
        ColetaHDInfo coleta = new ColetaHDInfo();

        this.conectHd().update("insert into Metrica values(?,?,?,?,?,?,?)",
                coleta.idMetrica = null,
                coleta.valorUtilizado,
                coleta.unidadeMedida,
                coleta.dataHora,
                fkMaquina,
                fkEmpresa,
                1);
        this.conectHdazu().update("insert into Metrica(valorUtilizado,unidadeMedida,dataHora,fkMaquina,fkEmpresa,fkComponente) values(?,?,?,?,?,?)",
                coleta.valorUtilizado,
                coleta.unidadeMedida,
                coleta.dataHora,
                fkMaquina,
                fkEmpresa.toString(),
                1);
    }

    public void enviaDadosTotalhdazu(Integer fkMaquina, Integer fkEmpresa) {
        ColetaHDInfo coleta = new ColetaHDInfo();
    }

    public void enviadadosporHD() {

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        looca = new Looca();
        grupoDeDiscos = looca.getGrupoDeDiscos();
        bytesEscritos = 0.0;
        capacidadeocupada = 0.0;
        dataHora = LocalDateTime.now().format(formatter);

        List<Disco> listadisco = grupoDeDiscos.getDiscos();
        for (Disco disco : listadisco) {
            bytesEscritos += disco.getBytesDeEscritas();

        }
        List<Volume> listavolumed = grupoDeDiscos.getVolumes();
        for (Volume volume : listavolumed) {
            capacidadeocupada += volume.getDisponivel();

        }
    }

    public Double getCapacidade() {
        return capacidade;
    }

    public Double getValorUtilizado() {
        return valorUtilizado;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public String getModeloComponente() {
        return modeloComponente;
    }

    @Override
    public String toString() {
        return "ColetaHD{" + "capacidade=" + capacidade + ", valorUtilizado=" + valorUtilizado + ", unidadeMedida=" + unidadeMedida + ", tipoComponente=" + tipoComponente + ", modeloComponente=" + modeloComponente + '}';
    }

}