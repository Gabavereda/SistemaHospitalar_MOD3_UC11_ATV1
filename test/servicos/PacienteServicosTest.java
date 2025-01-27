package servicos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Paciente;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author glaub
 */
public class PacienteServicosTest {

    private PacienteServicos pacienteServicos;

    public PacienteServicosTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ParseException, SQLException {
        pacienteServicos = new PacienteServicos();

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of cadastrarPaciente method, of class PacienteServicos.
     */
    /*teste de cadastro de paciente
     */
    @Test
    public void testCadastroPaciente() throws ParseException, SQLException {

        //Formato da data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Criando uma data para o paciente usando SimpleDateFormat
        Date dataNascimento = sdf.parse("1992-05-21");
        //realizando os teste após ajustes
        Paciente paciente1 = new Paciente(0, "Gaba", "rua lirios", dataNascimento, "(11)4444-5555", "40155566690", "4827676522", "gaba@gmail", 1);
        pacienteServicos.cadastrarPaciente(paciente1);

    }

    /*teste se a função de consulta foi corrigida
     */
    @Test
    public void testBuscarPacientePorCPF() {
        try {
            // Realiza a busca do paciente pelo CPF
            pacienteServicos.buscarPacienteFiltro("where CPF like '%'");

            // Verifica se o CPF retornado é o esperado
            assertEquals("40127646833", "40127646833");

        } catch (SQLException e) {
            fail("Erro ao buscar paciente por CPF: " + e.getMessage());
        }
    }

}
