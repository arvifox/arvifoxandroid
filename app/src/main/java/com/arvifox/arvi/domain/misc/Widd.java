package com.arvifox.arvi.domain.misc;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Widd {
    public interface Listener {
        void onEvent(@NotNull Widd w);
    }

    public Widd() {
        count = new ArrayList<>();
    }

    private List<Listener> count;

    public int getCount() {
        return count.size();
    }

    public void addLis(Listener l) {
        count.add(l);
    }

    public void removeLis(Listener l) {
        count.remove(l);
    }
}
