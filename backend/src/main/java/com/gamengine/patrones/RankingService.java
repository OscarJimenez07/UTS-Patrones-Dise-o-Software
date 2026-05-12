package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio publico que expone Strategy para los controllers.
 */
public class RankingService {

    private final ServicioRanking servicio;
    private final Map<String, EstrategiaRanking> estrategias = new LinkedHashMap<>();

    public RankingService() {
        this.servicio = new ServicioRanking();
        estrategias.put("puntos", new RankingPorPuntos());
        estrategias.put("elo", new RankingPorELO());
        estrategias.put("victorias", new RankingPorVictorias());
        estrategias.put("ratio", new RankingPorRatio());

        sembrar();
    }

    public Map<String, Object> top(String criterio, int n) {
        EstrategiaRanking est = resolver(criterio);
        List<JugadorRanking> lista = servicio.top(est, n);

        List<Map<String, Object>> filas = new ArrayList<>();
        int pos = 1;
        for (JugadorRanking j : lista) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("posicion", pos++);
            dto.put("usuario", j.getUsuario());
            dto.put("puntos", j.getPuntos());
            dto.put("elo", j.getElo());
            dto.put("victorias", j.getVictorias());
            dto.put("derrotas", j.getDerrotas());
            dto.put("ratio", Math.round(j.getRatio() * 100.0) / 100.0);
            dto.put("valor", est.valorMostrado(j));
            filas.add(dto);
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("criterio", est.getCriterio());
        out.put("descripcion", est.getDescripcion());
        out.put("ranking", filas);
        return out;
    }

    public Map<String, Object> posicionDe(String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, EstrategiaRanking> e : estrategias.entrySet()) {
            int p = servicio.posicion(usuario, e.getValue());
            out.put(e.getKey(), p);
        }
        return out;
    }

    public List<Map<String, Object>> criterios() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map.Entry<String, EstrategiaRanking> e : estrategias.entrySet()) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("clave", e.getKey());
            dto.put("descripcion", e.getValue().getDescripcion());
            out.add(dto);
        }
        return out;
    }

    private EstrategiaRanking resolver(String criterio) {
        EstrategiaRanking est = estrategias.get(criterio == null ? "" : criterio.toLowerCase());
        return est == null ? estrategias.get("puntos") : est;
    }

    private void sembrar() {
        servicio.registrar(new JugadorRanking("oscar", 8400, 1820, 24, 11));
        servicio.registrar(new JugadorRanking("anderson", 7950, 1755, 22, 14));
        servicio.registrar(new JugadorRanking("proGamer99", 12300, 2150, 41, 9));
        servicio.registrar(new JugadorRanking("shadowKnight", 9100, 1980, 28, 18));
        servicio.registrar(new JugadorRanking("dragonSlayer", 8800, 1875, 26, 20));
        servicio.registrar(new JugadorRanking("nightRider", 6700, 1620, 17, 16));
        servicio.registrar(new JugadorRanking("lunaWolf", 5400, 1540, 14, 19));
        servicio.registrar(new JugadorRanking("recluta01", 1200, 1100, 3, 8));
    }
}
