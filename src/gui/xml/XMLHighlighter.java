package gui.xml;

public class XMLHighlighter {
    private static final String start = "<html>\n" +
            "<font color=#333333>\n" +
            "<pre>";
    private static final String end = "\n</pre>\n" +
            "</font>\n" +
            "</html>";

    private static final String color = "<font color=";
    private static final String colorEnd = "</font>";

    private static StringBuilder stringBuilder;

    public static String highlight(String s) {
        stringBuilder = new StringBuilder(s
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("\t", "    "));
        stringBuilder.insert(0, start);

        wrapColor("data", "#E91E63");
        wrapColor("node", "#E91E63");
        wrapColor("name", "#E91E63");
        wrapColor("depends", "#E91E63");
        wrapColor("id", "#4CAF50");
        wrapColor("&lt;/", "#9C27B0");
        wrapColor("&lt;", "#9C27B0");
        wrapColor("&gt;", "#9C27B0");
        wrapColor("&quot;", "#FF5722");

        stringBuilder.append(end);

        return stringBuilder.toString();
    }

    private static void wrapColor(String wrap, String fontColor) {
        int index = stringBuilder.indexOf(wrap);
        String localColor = new String(color);
        localColor += fontColor + ">";
        while (index != -1) {
            int offset = index + wrap.length() + localColor.length();
            stringBuilder.insert(index, localColor);
            stringBuilder.insert(offset, colorEnd);

            index = stringBuilder.indexOf(wrap, offset + 1);
        }
    }

}
