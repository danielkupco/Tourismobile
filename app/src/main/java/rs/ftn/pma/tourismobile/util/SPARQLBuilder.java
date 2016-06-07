package rs.ftn.pma.tourismobile.util;

/**
 * Utility class for building SPARQL queries.
 * Created by Daniel Kupčo on 07.06.2016.
 */
public class SPARQLBuilder {

    private StringBuilder query;

    public SPARQLBuilder() {
        query = new StringBuilder();
    }

    public SPARQLBuilder select(String... variables) {
        query.append("SELECT");
        formSelect(variables);
        return this;
    }


    public SPARQLBuilder selectDistinct(String... variables) {
        query.append("SELECT DISTINCT");
        formSelect(variables);
        return this;
    }

    private void formSelect(String... variables) {
        if(variables.length == 0) {
            query.append(" *");
        }
        else {
            for(String variable : variables) {
                query.append(String.format(" ?%s,", variable));
            }
            query.deleteCharAt(query.length() - 1);
        }
    }

    public SPARQLBuilder from(String url) {
        query.append(String.format(" FROM <%s>", url));
        return this;
    }

    public SPARQLBuilder startWhere() {
        query.append(" WHERE { ");
        return this;
    }

    public SPARQLBuilder endWhere() {
        query.append(" . }");
        return this;
    }

    public SPARQLBuilder triplet(String subject, String predicate, String object) {
        query.append(String.format(" ?%s %s %s", subject, predicate, object));
        return this;
    }

    public SPARQLBuilder property(String property) {
        query.append(String.format(" ; %s", property));
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
                query.append(String.format(" ?%s,", variable));
            }
            query.deleteCharAt(query.length() - 1);
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
        int index = 0;
        index = prettified.indexOf("FROM");
        if(index > 0) {
            prettified.insert(index, "\n");
        }
        index = prettified.indexOf("WHERE");
        if(index > 0) {
            prettified.insert(index, "\n");
        }
        index = prettified.indexOf("{");
        if(index > 0) {
            prettified.insert(index + 1, "\n");
            while(prettified.charAt(index + 2) == ' ') {
                prettified.deleteCharAt(index + 2);
            }
            prettified.insert(index + 2, "  ");
        }

        // separators
        String indentation = "  ";
        boolean afterTriplet = false;
        for(int i = index; i < prettified.indexOf("}"); i++) {
            char c = prettified.charAt(i);
            if(c == ';') {
                if(!afterTriplet) {
                    afterTriplet = true;
                    indentation = "    ";
                }
                prettified.replace(i, i + indentation.length(), ";\n" + indentation);
            }
            else if(c == '.') {
                if(afterTriplet) {
                    afterTriplet = false;
                    indentation = "  ";
                }
                prettified.replace(i, i + indentation.length(), ".\n" + indentation);
            }
        }
        index = prettified.indexOf("}");
        if(index > 0) {
            while(prettified.charAt(index - 1) == ' ') {
                prettified.deleteCharAt(index - 1);
                index--;
            }
        }
        index = prettified.indexOf("ORDER BY");
        if(index > 0) {
            prettified.insert(index, "\n");
        }
        index = prettified.indexOf("LIMIT");
        if(index > 0) {
            prettified.insert(index, "\n");
        }

        return prettified.toString();
    }

    public String build() {
        return query.toString();
    }

}
