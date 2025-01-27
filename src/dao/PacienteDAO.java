package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import modelo.Paciente;
import persistencia.ConexaoBanco;


/*A classe PacienteDAO é responsável pela comunicação entre a aplicação e o banco de dados, ou seja,
ela é responsável por realizar as operações de cadastro e busca de pacientes no banco de dados.
 */
public class PacienteDAO {

    private ConexaoBanco conexao;
    private Connection con;

    /*No construtor da classe, a instância de ConexaoBanco é criada e 
    armazenada no atributo conexao. 
    Essa instância será usada posteriormente para obter a conexão com o banco de dados.
     */
    public PacienteDAO() {
        this.conexao = new ConexaoBanco();
    }

    // método cadastrarPaciente
    public void cadastrarPaciente(Paciente pac) throws SQLException {

        try {

            con = conexao.getConexao();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            // String que receberá instrução SQL
            String sql = "insert into PACIENTE(NOME, ENDERECO, DATA_NASC, TELEFONE, CPF, RG, ID_CONVENIO_FK) values(?,?,?,?,?,?,?)";

            PreparedStatement pst = this.con.prepareStatement(sql);

            // Atribuindo valores aos parâmetros
            try {
                //Campos Obrigatorios

                //Validando Nome
                validarNome(pac.getNome());
                //Se correto atribui nome
                pst.setString(1, pac.getNome());

                //validando endereco
                validarEndereco(pac.getEndereco());
                //se correto
                pst.setString(2, pac.getEndereco());

                //Validando Data de Nascimento
                validarDataNascimento(pac.getDataNascimento());
                //Se correto
                pst.setString(3, sdf.format(pac.getDataNascimento()));

                //Validando Telefone
                validarTelefone(pac.getTelefone());
                //Se correto..
                pst.setString(4, pac.getTelefone());

                if (!cpfUnico(pac.getCpf())) {
                    throw new SQLException("CPF já cadastrado!");
                }
                //Validar cpf letras,numeros
                validarCPF(pac.getCpf());
                pst.setString(5, pac.getCpf());

                pst.setString(6, pac.getRg());

                pst.setInt(7, pac.getIdConvenio());

                //Validando
            } catch (Exception e) {
                throw new SQLException("dados invalidos" + e.getMessage());
            }

            // Executando o PreparedStatement
            pst.execute();

            //Adicionei essas linhas para pegar as mensagens de erro correta
        } catch (SQLException se) {
            if (se.getMessage().contains("Nome Obrigatório")) {
                throw new SQLException("Nome obrigatório!");
            } else if (se.getMessage().contains("Endereço obrigatório")) {
                throw new SQLException("Endereço é um campo obrigatório!");
            } else if (se.getMessage().contains("Data de nascimento obrigatória")) {
                throw new SQLException("Data de nascimento é um campo obrigatório.");
            } else if (se.getMessage().contains("Telefone obrigatório")) {
                throw new SQLException("Telefone é um campo obrigatório");
            } else if (se.getMessage().contains("CPF obrigatório")) {
                throw new SQLException("CPF é um campo obrigatório.");
            } else if (se.getMessage().contains("Selecione um convênio")) {
                throw new SQLException("Selecione um convênio");
            } else if (se.getMessage().contains("CPF já cadastrado")) {
                throw new SQLException("CPF já cadastrado!");
            } else {
                throw new SQLException("Erro ao inserir dados no Banco de Dados: " + se.getMessage());
            }
        } finally {

            // Encerrando as conexões
            con.close();

        } // fecha finally
    }// fecha método cadastrarPaciente

// método buscarPaciente com condição
    public ArrayList<Paciente> buscarPacienteFiltro(String query) throws SQLException {

        /*
         * Criando obj. capaz de executar instruções
         * SQL no banco de dados
         */
        ResultSet rs;

        try {
            // Criando variável sql vazia
            String sql;

            /* Montando o sql com a consulta desejada pelo usuário.
            A consulta foi enviada para o método em uma String chamada query */
            sql = "SELECT * FROM paciente " + query;

            this.con = this.conexao.getConexao();
            PreparedStatement pst = con.prepareStatement(sql);

            rs = pst.executeQuery();


            /* Criando ArrayList para armazenar objetos do tipo Paciente */
            ArrayList<Paciente> pacientes = new ArrayList<>();

            /* Enquanto houver uma próxima linha no
           banco de dados o while roda */
            while (rs.next()) {

                // Criando um novo obj. Paciente
                Paciente pac = new Paciente();

                /* Mapeando a tabela do banco para objeto chamado pac */
                pac.setIdPaciente(rs.getInt("ID_PACIENTE"));
                pac.setNome(rs.getString("NOME"));
                pac.setEndereco(rs.getString("ENDERECO"));
                pac.setDataNascimento(rs.getDate("DATA_NASC"));
                pac.setTelefone(rs.getString("TELEFONE"));
                pac.setCpf(rs.getString("CPF"));
                pac.setRg(rs.getString("RG"));
                pac.setIdConvenio(rs.getInt("ID_CONVENIO_FK"));


                /* Inserindo o objeto Paciente no ArrayList */
                pacientes.add(pac);
            }

            // Retornando o ArrayList com todos objetos
            return pacientes;

        } catch (SQLException se) {
            throw new SQLException("Erro ao buscar dados do Banco! " + se.getMessage());
        } finally {
            con.close();
        }
    }

    public ArrayList<Paciente> buscarPaciente() throws SQLException {


        /*
         * Criando obj. capaz de executar instruções
         * SQL no banco de dados
         */
        ResultSet rs;

        try {

            // String que receberá instrução SQL
            String sql = "SELECT * FROM PACIENTE";

            this.con = this.conexao.getConexao();
            PreparedStatement pst = con.prepareStatement(sql);

            rs = pst.executeQuery();


            /* Criando ArrayList para armazenar objetos do tipo Paciente */
            ArrayList<Paciente> pacientes = new ArrayList<>();

            /* Enquanto houver uma próxima linha no banco de dados o while roda */
            while (rs.next()) {

                // Criando um novo objeto Paciente
                Paciente pac = new Paciente();

                /* Mapeando a tabela do banco para objeto chamado pac */
                pac.setIdPaciente(rs.getInt("ID_PACIENTE"));
                pac.setNome(rs.getString("NOME"));
                pac.setEndereco(rs.getString("ENDERECO"));
                pac.setDataNascimento(rs.getDate("DATA_NASC"));
                pac.setTelefone(rs.getString("TELEFONE"));
                pac.setCpf(rs.getString("CPF"));
                pac.setRg(rs.getString("RG"));
                pac.setIdConvenio(rs.getInt("ID_CONVENIO_FK"));

                /* Inserindo o objeto pac no ArrayList */
                pacientes.add(pac);
            }

            // Retornando o ArrayList com todos objetos
            return pacientes;
        } catch (SQLException se) {

            throw new SQLException("Erro ao buscar dados do Banco! " + se.getMessage());
        } finally {
            con.close();
        }
    }

///Aqui criei os metodos de validação
    private void validarNome(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Nome é obrigatorio.");
        }
        if (nome.length() > 55) {
            throw new Exception("Nome deve ter no máximo 55 caracteres.");
        }
    }

    private void validarCPF(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("CPF é um campo obrigatório.");
        }
        if (cpf.length() != 11 || !cpf.matches("\\d+")) {
            throw new Exception("CPF deve conter 11 dígitos numéricos.");
        }
        if (!cpfUnico(cpf)) { // Verificação se o CPF é único no sistema
            throw new Exception("CPF já está cadastrado no sistema.");
        }
    }

    private void validarDataNascimento(Date dataNascimento) throws Exception {
        if (dataNascimento == null) {
            throw new Exception("Data de nascimento é um campo obrigatório.");
        }
    }

    private void validarEndereco(String endereco) throws Exception {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new Exception("Endereço é um campo obrigatório.");
        }
        if (endereco.length() > 200) {
            throw new Exception("Endereço deve ter no máximo 200 caracteres.");
        }
    }

    private void validarTelefone(String telefone) throws Exception {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new Exception("Telefone é um campo obrigatório.");
        }
        if (telefone.length() > 15 || !telefone.matches("\\(\\d{2}\\)\\d{4}-\\d{4}")) {
            throw new Exception("Telefone deve estar no formato (xx)xxxx-xxxx e conter no máximo 15 caracteres.");
        }
    }

    private void validarEmail(String email) throws Exception {
        if (email != null && !email.trim().isEmpty() && !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new Exception("E-mail inválido.");
        }
    }

// Método  para verificar se o CPF é único no sistema
    private boolean cpfUnico(String cpf) throws SQLException {
        ResultSet rs = null;
        try {

            con = conexao.getConexao();

            // SQL para verificar se já existe um paciente com o mesmo CPF
            String sql = "SELECT CPF FROM PACIENTE WHERE CPF = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, cpf);

            rs = pst.executeQuery();

            return !rs.next(); // Retorna true se não existe CPF cadastrado, false se já existe
        } catch (SQLException se) {
            throw new SQLException("Erro ao verificar CPF no Banco de Dados: " + se.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
