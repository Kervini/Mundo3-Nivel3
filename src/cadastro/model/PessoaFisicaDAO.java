package cadastro.model;

import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrobd.model.PessoaFisica;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PessoaFisicaDAO {

    private final ConectorBD conector;
    private final SequenceManager sm;

    public PessoaFisicaDAO() throws SQLException {
        conector = new ConectorBD();
        sm = new SequenceManager(conector.getConnection());
    }

    public PessoaFisica getPessoa(int id) throws SQLException {
        PessoaFisica pf = null;

        String sql = "select p.id, p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pf.cpf "
                + "from pessoa p inner join pessoa_fisica pf on p.id = pf.idpessoa where p.id = ?";
        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pf = new PessoaFisica(rs.getInt("id"), rs.getString("nome"), rs.getString("logradouro"), rs.getString("cidade"),
                        rs.getString("estado"), rs.getString("telefone"), rs.getString("email"), rs.getString("cpf"));
            }
        }

        return pf;
    }

    public ArrayList<PessoaFisica> getPessoas() throws SQLException {
        ArrayList<PessoaFisica> pessoas = new ArrayList<>();

        String sql = "select p.id, p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pf.cpf "
                + "from pessoa p inner join pessoa_fisica pf on p.id = pf.idpessoa";

        try (ResultSet rs = conector.getSelect(sql)) {
            while (rs.next()) {
                pessoas.add(new PessoaFisica(rs.getInt("id"), rs.getString("nome"), rs.getString("logradouro"),
                        rs.getString("cidade"), rs.getString("estado"), rs.getString("telefone"),
                        rs.getString("email"), rs.getString("cpf")));
            }
        }

        return pessoas;
    }

    public boolean incluir(PessoaFisica pf) throws SQLException {
        boolean retorno;
        int novoid = sm.getValue("sqc_pessoa_id");
        String sql = "insert into pessoa (id, nome, logradouro, cidade, estado, telefone, email) values (?, ?, ?, ?, ?, ?, ?);"
                + " insert into pessoa_fisica(idpessoa, cpf) values (?, ?)";

        try (PreparedStatement ps = conector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, novoid);
            ps.setString(2, pf.getNome());
            ps.setString(3, pf.getLogradouro());
            ps.setString(4, pf.getCidade());
            ps.setString(5, pf.getEstado());
            ps.setString(6, pf.getTelefone());
            ps.setString(7, pf.getEmail());
            ps.setInt(8, novoid);
            ps.setString(9, pf.getCpf());

            retorno = ps.executeUpdate() > 0;
        }

        return retorno;//Retorna true se o insert ocorreu com sucesso nas duas tabelas
    }

    public boolean alterar(PessoaFisica pf) throws SQLException {
        boolean retorno;

        String sql = "update pessoa set nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ?"
                + " where id = ?; "
                + "update pessoa_fisica set cpf = ? where idpessoa = ?;";

        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setString(1, pf.getNome());
            ps.setString(2, pf.getLogradouro());
            ps.setString(3, pf.getCidade());
            ps.setString(4, pf.getEstado());
            ps.setString(5, pf.getTelefone());
            ps.setString(6, pf.getEmail());
            ps.setInt(7, pf.getId());
            ps.setString(8, pf.getCpf());
            ps.setInt(9, pf.getId());

            retorno = ps.executeUpdate() > 0;
        }

        return retorno;//Retorna true se o update ocorreu com sucesso nas duas tabelas
    }

    public boolean excluir(int id) throws SQLException {
        boolean retorno;

        if (!verificaPessoa(id)) {
            return false;
        }

        try (PreparedStatement ps = conector.getPrepared("delete from pessoa_fisica where idpessoa = ?; delete from pessoa where id = ?;")) {
            ps.setInt(1, id);
            ps.setInt(2, id);

            retorno = ps.executeUpdate() == 1;
        }

        return retorno; //Retorna true se o delete ocorreu com sucesso nas duas tabelas
    }

    public boolean verificaPessoa(int id) throws SQLException {
        boolean retorno = false;

        String sql = "select 1 from pessoa p inner join pessoa_fisica pf on p.id = pf.idpessoa where p.id = ?";

        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                retorno = true;
            }
        }
        return retorno;
    }

    public void fecharConexao() throws SQLException {
        conector.close();
    }
}
