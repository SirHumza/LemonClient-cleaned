/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

private static class OptimizedTheme.ParameterTuple<S, T> {
    private final S type;
    private final int logicalLevel;
    private final int graphicalLevel;
    private final T container;

    public OptimizedTheme.ParameterTuple(S type, int logicalLevel, int graphicalLevel, T container) {
        this.type = type;
        this.logicalLevel = logicalLevel;
        this.graphicalLevel = graphicalLevel;
        this.container = container;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof OptimizedTheme.ParameterTuple) {
            return this.toString().equals(o.toString());
        }
        return false;
    }

    public String toString() {
        return "(" + this.type + "," + this.logicalLevel + "," + this.graphicalLevel + "," + this.container + ")";
    }
}
