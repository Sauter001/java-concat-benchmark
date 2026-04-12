package benchmark;

record MoveRecord(String symbol, int fromRow, int fromCol, int toRow, int toCol) {

    String toNotationPlus() {
        return symbol + " (" + fromRow + "," + fromCol + ") → (" + toRow + "," + toCol + ")";
    }

    String toNotationFormat() {
        return String.format("%s (%d,%d) → (%d,%d)", symbol, fromRow, fromCol, toRow, toCol);
    }

    String toNotationFormatted() {
        return "%s (%d,%d) → (%d,%d)".formatted(symbol, fromRow, fromCol, toRow, toCol);
    }

    void appendTo(StringBuilder sb) {
        sb.append(symbol).append(" (")
                .append(fromRow).append(",").append(fromCol)
                .append(") → (")
                .append(toRow).append(",").append(toCol).append(")");
    }
}
