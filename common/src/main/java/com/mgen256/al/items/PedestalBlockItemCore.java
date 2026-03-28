package com.mgen256.al.items;

import java.util.function.Consumer;

public class PedestalBlockItemCore<T> implements PedestalBlockItemTrait {
    private boolean cached;
    private T txtShift;
    private T txtTips;
    private T txtRightClick;
    private T txtSneaking;
    private T txtSignals;

    public interface Translator<T> {
        boolean exists(String key);

        T translate(String key);
    }

    private void ensureCached(Translator<T> tr) {
        if (cached) {
            return;
        }
        if (!tr.exists(KEY_SHIFT)) {
            return;
        }
        txtShift = tr.translate(KEY_SHIFT);
        txtTips = tr.translate(KEY_TIPS);
        txtRightClick = tr.translate(KEY_RIGHTCLICK);
        txtSneaking = tr.translate(KEY_SNEAKING);
        txtSignals = tr.translate(KEY_SIGNALS);
        cached = true;
    }

    public void appendTooltip(boolean shiftDown, Consumer<T> adder, Translator<T> tr) {
        ensureCached(tr);
        if (!cached) {
            return;
        }
        buildPedestalTooltip(shiftDown, key -> {
            if (key.equals(KEY_TIPS)) adder.accept(txtTips);
            else if (key.equals(KEY_RIGHTCLICK)) adder.accept(txtRightClick);
            else if (key.equals(KEY_SNEAKING)) adder.accept(txtSneaking);
            else if (key.equals(KEY_SIGNALS)) adder.accept(txtSignals);
            else if (key.equals(KEY_SHIFT)) adder.accept(txtShift);
        });
    }
}
