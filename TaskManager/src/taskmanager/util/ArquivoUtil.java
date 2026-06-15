package taskmanager.util;

import taskmanager.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para salvar e carregar itens em arquivo de texto simples.
 * Formato por linha: TIPO|id|titulo|descricao|concluido|dataCriacao|campo5|campo6...
 */
public class ArquivoUtil {

    public static void salvar(String nomeArquivo, List<Item> itens) throws IOException {
        Path path = Paths.get(nomeArquivo);
        List<String> linhas = new ArrayList<>();
        for (Item item : itens) {
            if (item instanceof Tarefa t) {
                String prazo = (t.getPrazo() != null) ? t.getPrazo().toString() : "null";
                linhas.add(String.join("|",
                        "TAREFA",
                        t.getId(),
                        escapar(t.getTitulo()),
                        escapar(t.getDescricao()),
                        String.valueOf(t.isConcluido()),
                        t.getDataCriacao().toString(),
                        prazo,
                        t.getPrioridade().name(),
                        escapar(t.getCategoria())
                ));
            } else if (item instanceof Meta m) {
                linhas.add(String.join("|",
                        "META",
                        m.getId(),
                        escapar(m.getTitulo()),
                        escapar(m.getDescricao()),
                        String.valueOf(m.isConcluido()),
                        m.getDataCriacao().toString(),
                        String.valueOf(m.getProgressoAtual()),
                        escapar(m.getMotivacao())
                ));
            }
        }
        Files.write(path, linhas);
    }

    public static List<Item> carregar(String nomeArquivo) throws IOException {
        Path path = Paths.get(nomeArquivo);
        if (!Files.exists(path)) return new ArrayList<>();

        List<Item> resultado = new ArrayList<>();
        List<String> linhas = Files.readAllLines(path);

        for (String linha : linhas) {
            if (linha.trim().isEmpty()) continue;
            try {
                String[] p = linha.split("\\|", -1);
                String tipo = p[0];

                if ("TAREFA".equals(tipo) && p.length >= 9) {
                    LocalDate prazo = "null".equals(p[6]) ? null : LocalDate.parse(p[6]);
                    Prioridade pri = Prioridade.valueOf(p[7]);
                    Tarefa t = new Tarefa(desescapar(p[2]), desescapar(p[3]), prazo, pri, desescapar(p[8]));
                    t.setConcluido(Boolean.parseBoolean(p[4]));
                    resultado.add(t);

                } else if ("META".equals(tipo) && p.length >= 8) {
                    Meta m = new Meta(desescapar(p[2]), desescapar(p[3]), desescapar(p[7]));
                    m.setConcluido(Boolean.parseBoolean(p[4]));
                    m.setProgressoAtual(Integer.parseInt(p[6]));
                    resultado.add(m);
                }
            } catch (Exception e) {
                System.err.println("Linha ignorada (corrompida): " + linha);
            }
        }
        return resultado;
    }

    private static String escapar(String s) {
        return s == null ? "" : s.replace("|", "\\pipe").replace("\n", "\\n");
    }

    private static String desescapar(String s) {
        return s == null ? "" : s.replace("\\pipe", "|").replace("\\n", "\n");
    }
}
