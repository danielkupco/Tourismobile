package rs.ftn.pma.tourismobile.util;

/**
 * Utility class for building SPARQL queries.
 * Created by Daniel Kupčo on 07.06.2016.
 */
public class SPARQLBuilder {

    public static void main(String[] args) {
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String test = "SELECT (?Destination)  (?name)  (?wikiPageID)  (?image_uri)  (?comment)  (AVG(?latitude) as ?latitude)  (AVG(?longitude) as ?longitude)  FROM <http://dbpedia.org> WHERE { { SELECT * WHERE { ?Destination a dbo:City ; rdfs:label ?name ; rdfs:comment ?comment . FILTER(lang(?comment)=\"en\" && lang(?name)=\"en\") . } } ?Destination dbo:wikiPageID ?wikiPageID ; dbo:thumbnail ?image_uri ; geo:lat|dbp:latD ?latitude ; geo:long|dbp:longD ?longitude . } GROUP BY ?Destination ?name ?wikiPageID ?image_uri ?comment ORDER BY ?name LIMIT 10 OFFSET 0";
//        Log.e("test", sparqlBuilder.testPrettify(test));
        System.out.println(sparqlBuilder.testPrettify(test));
    }

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
        int index = 0;
        index = prettified.indexOf("FROM");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("FROM", index);
        }
        index = prettified.indexOf("WHERE");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("WHERE", index);
        }
        index = prettified.indexOf("{");
        if(index > 0) {
            prettified.insert(index + 1, "\n");
            while(prettified.charAt(index + 2) == ' ') {
                prettified.deleteCharAt(index + 2);
            }
            prettified.insert(index + 2, "  ");
//            index = prettified.indexOf("{", index);
        }

        // separators
        String indentation = "  ";
        boolean afterTriplet = false;
        int lastCurly = prettified.lastIndexOf("}");
//        while(lastCurly > 0) {
            for (int i = index; i < lastCurly; i++) {
                char c = prettified.charAt(i);
                if (c == ';') {
                    if (!afterTriplet) {
                        afterTriplet = true;
                        indentation = "    ";
                    }
                    prettified.replace(i, i + indentation.length(), ";\n" + indentation);
                } else if (c == '.') {
                    if (afterTriplet) {
                        afterTriplet = false;
                        indentation = "  ";
                    }
                    prettified.replace(i, i + indentation.length(), ".\n" + indentation);
                }
            }
//            index = lastCurly;
//            lastCurly = prettified.indexOf("}", lastCurly);
//        }
        index = prettified.indexOf("}");
        if(index > 0) {
            while(prettified.charAt(index - 1) == ' ') {
                prettified.deleteCharAt(index - 1);
                index--;
            }
//            index = prettified.indexOf("}", index);
        }
        index = prettified.indexOf("ORDER BY");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("ORDER BY", index);
        }
        index = prettified.indexOf("LIMIT");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("LIMIT", index);
        }

        return prettified.toString();
    }

    public String testPrettify(String test) {
        StringBuilder prettified = new StringBuilder(test);

        // keywords
        int index = 0;
        index = prettified.indexOf("FROM");
        while(index > 0) {
            prettified.insert(index, "\n");
            index = prettified.indexOf("FROM", index + 2);
        }
        index = prettified.indexOf("WHERE");
        while(index > 0) {
            prettified.insert(index, "\n");
            index = prettified.indexOf("WHERE", index + 2);
        }
        index = prettified.indexOf("{");
        if(index > 0) {
            prettified.insert(index + 1, "\n");
            while(prettified.charAt(index + 2) == ' ') {
                prettified.deleteCharAt(index + 2);
            }
            prettified.insert(index + 2, "  ");
//            index = prettified.indexOf("{", index);
        }

        // separators
        String indentation = "  ";
        boolean afterTriplet = false;
        int firstOpenCurly = prettified.indexOf("{");
        int lastClosedCurly = prettified.lastIndexOf("}");
//        while(lastCurly > 0) {
        for (int i = firstOpenCurly; i < lastClosedCurly; i++) {
            char c = prettified.charAt(i);
            if (c == ';') {
                if (!afterTriplet) {
                    afterTriplet = true;
                    indentation = "    ";
                }
                prettified.replace(i, i + indentation.length(), ";\n" + indentation);
            } else if (c == '.') {
                if (afterTriplet) {
                    afterTriplet = false;
                    indentation = "  ";
                }
                prettified.replace(i, i + indentation.length(), ".\n" + indentation);
            }
        }
//            index = lastCurly;
//            lastCurly = prettified.indexOf("}", lastCurly);
//        }
        index = prettified.indexOf("}");
        while(index > 0) {
//            while(prettified.charAt(index - 1) == ' ') {
//                prettified.deleteCharAt(index - 1);
//                index--;
//            }
            prettified.insert(index + 1, "\n");
            index = prettified.indexOf("}", index + 2);
        }
        index = prettified.indexOf("ORDER BY");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("ORDER BY", index);
        }
        index = prettified.indexOf("LIMIT");
        if(index > 0) {
            prettified.insert(index, "\n");
//            index = prettified.indexOf("LIMIT", index);
        }

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

}
