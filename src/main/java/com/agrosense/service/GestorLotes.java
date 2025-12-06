package com.agrosense.service;

import com.agrosense.model.Lote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorLotes {
    private List<Lote> lotes;

    public GestorLotes() {
        this.lotes = new ArrayList<>();
    }

    public void registrarLote(Lote lote) {
        lotes.add(lote);
    }

    public List<Lote> obtenerTodos() {
        return lotes;
    }

    public Optional<Lote> buscarPorId(String id) {
        return lotes.stream().filter(l -> l.getId().equals(id)).findFirst();
    }
}
