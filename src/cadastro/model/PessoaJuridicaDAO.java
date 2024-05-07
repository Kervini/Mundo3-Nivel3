package cadastro.model;

import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrobd.model.PessoaJuridica;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PessoaJuridicaDAO {

    private final ConectorBD conector;
    private final SequenceManager sm;

    public PessoaJuridicaDAO() throws SQLException {
        this.conector = new ConectorBD();
        this.sm = new SequenceManager(this.conector.getConnection());
    }

    public PessoaJuridica getPessoa(int id) throws SQLException {
        PessoaJuridica pj = null;

        String sql = "select p.id, p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pj.cnpj "
                + "from pessoa p inner join pessoa_juridica pj on p.id = pj.idpessoa where p.id = ?";
        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pj = new PessoaJuridica(rs.getInt("id"), rs.getString("nome"), rs.getString("logradouro"), rs.getString("cidade"),
                        rs.getString("estado"), rs.getString("telefone"), rs.getString("email"), rs.getString("cnpj"));
            }
        }

        return pj;
    }

    public ArrayList<PessoaJuridica> getPessoas() throws SQLException {
        ArrayList<PessoaJuridica> pessoas = new ArrayList<>();
        String sql = "select p.id, p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pj.cnpj "
                + "from pessoa p inner join pessoa_juridica pj on p.id = pj.idpessoa";

        try (ResultSet rs = conector.getSelect(sql)) {
            while (rs.next()) {
                pessoas.add(new PessoaJuridica(rs.getInt("id"), rs.getString("nome"), rs.getString("logradouro"),
                        rs.getString("cidade"), rs.getString("estado"), rs.getString("telefone"),
                        rs.getString("email"), rs.getString("cnpj")));
            }
        }

        return pessoas;
    }

    public boolean incluir(PessoaJuridica pj) throws SQLException {
        boolean retorno;
        int novoid = sm.getValue("sqc_pessoa_id");
        String sql = "insert into pessoa (id, nome, logradouro, cidade, estado, telefone, email)"
                + " values (?, ?, ?, ?, ?, ?, ?); insert into pessoa_juridica (idpessoa, cnpj) values(?, ?);";

        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setInt(1, novoid);
            ps.setString(2, pj.getNome());
            ps.setString(3, pj.getLogradouro());
            ps.setString(4, pj.getCidade());
            ps.setString(5, pj.getEstado());
            ps.setString(6, pj.getTelefone());
            ps.setString(7, pj.getEmail());
            ps.setInt(8, novoid);
            ps.setString(9, pj.getCnpj());

            retorno = ps.executeUpdate() > 0;
        }

        return retorno;//Retorna true se o insert ocorreu com sucesso nas duas tabelas
    }

    public boolean alterar(PessoaJuridica pj) throws SQLException {
        boolean retorno;
        
        String sql = "update pessoa set nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ?"
                + " where id = ?; "
                + "update pessoa_juridica set cnpj = ? where idpessoa = ?;";

        try (PreparedStatement ps = conector.getPrepared(sql)) {
            ps.setString(1, pj.getNome());
            ps.setString(2, pj.getLogradouro());
            ps.setString(3, pj.getCidade());
            ps.setString(4, pj.getEstado());
            ps.setString(5, pj.getTelefone());
            ps.setString(6, pj.getEmail());
            ps.setInt(7, pj.getId());
            ps.setString(8, pj.getCnpj());
            ps.setInt(9, pj.getId());

            retorno = ps.executeUpdate() > 0;
        }
        return retorno; //Retorna true se o update ocorreu com sucesso nas duas tabelas
    }

    public boolean excluir(int id) throws SQLException {
        boolean retorno;

        if (!verificaPessoa(id)) {
            return false;
        }

        try (PreparedStatement ps = conector.getPrepared("delete from pessoa_juridica where idpessoa = ?; delete from pessoa where id = ?;")) {
            ps.setInt(1, id);
            ps.setInt(2, id);

            retorno = ps.executeUpdate() == 1;

        }

        return retorno; //Retorna true se o delete ocorreu com sucesso nas duas tabelas
    }

    public boolean verificaPessoa(int id) throws SQLException {
        boolean retorno = false;

        String sql = "select 1 from pessoa p inner join pessoa_juridica pj on p.id = pj.idpessoa where p.id = ?";

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
