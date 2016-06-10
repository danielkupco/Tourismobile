package rs.ftn.pma.tourismobile.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building SPARQL queries.
 * Created by Daniel Kupčo on 07.06.2016.
 */
public class SPARQLBuilder {

    private StringBuilder query;

    public SPARQLBuilder() {
        query = new StringBuilder();
    }

    public SPARQLBuilder select() {
        query.append("SELECT");
        return this;
    }

    public SPARQLBuilder selectDistinct() {
        query.append("SELECT DISTINCT");
        return this;
    }

    public SPARQLBuilder startSubquery() {
        query.append(" { ");
        return this;
    }

    public SPARQLBuilder endSubquery() {
        query.append(" }");
        return this;
    }

    public SPARQLBuilder variables(String... variables) {
        if(variables.length == 0) {
            query.append(" *");
        }
        else {
            for(String variable : variables) {
                query.append(String.format(" ?%s,", variable));
            }
            query.deleteCharAt(query.length() - 1);
        }
        return this;
    }

    public SPARQLBuilder var(String name) {
        query.append(String.format(" (?%s) ", name));
        return this;
    }

    public SPARQLBuilder varAs(String name, String as) {
        query.append(String.format(" (?%s as ?%s) ", name, as));
        return this;
    }

    public SPARQLBuilder aggregateVarAs(String aggregateFunction, String name, String as) {
        query.append(String.format(" (%s(?%s) as ?%s) ", aggregateFunction, name, as));
        return this;
    }

    public SPARQLBuilder from(String url) {
        query.append(String.format(" FROM <%s>", url));
        return this;
    }

    public SPARQLBuilder startWhere() {
        query.append(" WHERE {");
        return this;
    }

    public SPARQLBuilder endWhere() {
        query.append(" . }");
        return this;
    }

    public SPARQLBuilder triplet(String subject, String predicate, String object, boolean isObjectVariable) {
//        char lastChar = query.charAt(query.length() - 1);
        char lastChar = lastChar();
        if(!(lastChar == '{' || lastChar == '.' || lastChar == '}')) {
            query.append(" .");
        }
        final String format = isObjectVariable ? " ?%s %s ?%s" : " ?%s %s %s";
        query.append(String.format(format, subject, predicate, object));
        return this;
    }

    public SPARQLBuilder property(String property) {
        query.append(String.format(" ; %s", property));
        return this;
    }

    public SPARQLBuilder propertyChoice(String... properties) {
        query.append(" ; ");
        for(String property : properties) {
            query.append(String.format("%s|", property));
        }
        query.deleteCharAt(query.length() - 1);
        return this;
    }

    public SPARQLBuilder as(String variable) {
        query.append(String.format(" ?%s", variable));
        return this;
    }

    public SPARQLBuilder and() {
        query.append(" .");
        return this;
    }

    public SPARQLBuilder filter(String string) {
        query.append(String.format(" . FILTER(%s)", string));
        return this;
    }

    public SPARQLBuilder orderBy(String... variables) {
        if(variables.length > 0) {
            query.append(" ORDER BY");
            for(String variable : variables) {
                query.append(String.format(" ?%s", variable));
            }
//            query.deleteCharAt(query.length() - 1);
        }
        return this;
    }

    public SPARQLBuilder groupBy(String... variables) {
        if(variables.length > 0) {
            query.append(" GROUP BY");
            for(String variable : variables) {
                query.append(String.format(" ?%s", variable));
            }
//            query.deleteCharAt(query.length() - 1);
        }
        return this;
    }

    public SPARQLBuilder limit(int number) {
        query.append(String.format(" LIMIT %d", number));
        return this;
    }

    public SPARQLBuilder offset(int number) {
        query.append(String.format(" OFFSET %d", number));
        return this;
    }

    public SPARQLBuilder appendString(String string) {
        query.append(string);
        return this;
    }

    public SPARQLBuilder appendStringLine(String string) {
        query.append(string).append("\n");
        return this;
    }

    public String prettify() {
        StringBuilder prettified = new StringBuilder(query.toString());

        // keywords
        List<String> keywords = new ArrayList<>();
        keywords.add("SELECT");
        keywords.add("FROM");
        keywords.add("WHERE");
        keywords.add("FILTER");
        keywords.add("GROUP BY");
        keywords.add("ORDER BY");
        keywords.add("LIMIT");
        keywords.add("OFFSET");

        for(String keyword : keywords) {
            breakLineOnKeyword(prettified, keyword);
        }

        // separators and indentation
        String indentation = "";
        int firstOpenCurly = prettified.indexOf("{");
        // must be updated because query length is getting bigger
        int lastClosedCurly = prettified.lastIndexOf("}") + 1;
        boolean afterTriplet = false;

        for (int i = firstOpenCurly; i < lastClosedCurly; i++) {
            char c = prettified.charAt(i);
            if(c == '{') {
                prettified.replace(i - 1, i, "\n" + indentation); // insert new line and indentation
                lastClosedCurly += indentation.length() + 1; // update last closed curly bracket index
                i += indentation.length(); // skip indentation, new line is already skipped with i++ in the loop
                indentation += "  "; // increase indentation
                if(prettified.indexOf("{ ?", i) == i) { // if there is triplet break it to the next line
                    prettified.replace(i + 1, i + 2, "\n" + indentation);
                    lastClosedCurly += indentation.length() + 1;
                    i += indentation.length();
                }
            }
            else if(c == '}') {
                indentation = indentation.substring(0, indentation.length() - 2); // first decrease the indentation
                prettified.replace(i - 1, i, "\n" + indentation); // same process as above...
                lastClosedCurly += indentation.length() + 1;
                i += indentation.length();
                if(prettified.indexOf("} ?", i) == i) {
                    prettified.replace(i + 1, i + 2, "\n" + indentation);
                    lastClosedCurly += indentation.length() + 1;
                    i += indentation.length();
                }
            }

            // for every keyword in the process (while we have valid indentation in each moment)
            for(String keyword : keywords) {
                int index = prettified.indexOf(keyword, i); // get the keyword index form current position
                if(i == index) { // if we are at the keyword
                    prettified.insert(i, indentation); // indent it
                    lastClosedCurly += indentation.length();
                    i += indentation.length() + keyword.length(); // skip the indentation and the keyword
                }
            }

            // separators
            if (c == ';') {
                if (!afterTriplet) { // if after triplet indent a bit more
                    afterTriplet = true;
                    indentation += "  ";
                }
                prettified.replace(i + 1, i + 2, "\n" + indentation);
                lastClosedCurly += indentation.length() + 1;
            } else if (c == '.') {
                if (afterTriplet) { // if another condition and we are in triplet decrease indentation
                    afterTriplet = false;
                    indentation = indentation.substring(0, indentation.length() - 2);
                }
                prettified.replace(i + 1, i + 2, "\n" + indentation);
                lastClosedCurly += indentation.length() + 1;
            }
        }

        // in the end clear empty lines
        clearEmptyLines(prettified);

        return prettified.toString();
    }

    public String build() {
        return query.toString();
    }

    public char lastChar() {
        int i = 1;
        int length = query.length();
        char last = ' ';
        while(last == ' ') {
            last = query.charAt(length - i);
            i++;
        }
        return last;
    }

    public void breakLineOnKeyword(StringBuilder stringBuilder, String keyword) {
        int index = stringBuilder.indexOf(keyword);
        while(index > -1) {
            stringBuilder.insert(index, "\n");
            index = stringBuilder.indexOf(keyword, index + 2);
        }
    }

    public void clearEmptyLines(StringBuilder stringBuilder) {
        int firstNewLine = stringBuilder.indexOf("\n");
        int secondNewLine = stringBuilder.indexOf("\n", firstNewLine + 1);
        while(secondNewLine > -1 && secondNewLine < stringBuilder.length()) {
            // if there is empty line remove it
            // first new line index automatically takes the value of the second
            if(stringBuilder.substring(firstNewLine, secondNewLine).trim().isEmpty()) {
                stringBuilder.replace(firstNewLine, secondNewLine, "");
            }
            // otherwise go to the next new line character
            else{
                firstNewLine = secondNewLine;
            }
            secondNewLine = stringBuilder.indexOf("\n", firstNewLine + 1);
        }
    }

}
