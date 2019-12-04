package couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Database {

    private static final String GET_ALL_QUERY = "SELECT META(users).id, firstname, lastname, email " +
            "FROM `%s` AS users";

    private static final String GET_DOCUMENT_BY_ID_QUERY = "SELECT firstname, lastname, email " +
            "FROM `%s` AS users WHERE META(users).id = $1";

    private static final String SAVE_DOCUMENT_QUERY = "UPSERT INTO `%s` (KEY, VALUE) VALUES " +
            "($1, {'firstname': $2, 'lastname': $3, 'email': $4})";

    private static final String DELETE_DOCUMENT_BY_ID_QUERY = "DELETE " +
            "FROM `%s` AS users WHERE META(users).id = $1";

    private Database() {
    }

    public static List<Map<String, Object>> getAll(final Bucket bucket) {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(String.format(GET_ALL_QUERY, bucket.name()),
                N1qlParams.build().consistency(ScanConsistency.REQUEST_PLUS)));
        return extractResultOrThrow(result);
    }

    public static List<Map<String, Object>> getByDocumentId(final Bucket bucket, String documentId) {
        return getParameterizedResults(bucket, String.format(GET_DOCUMENT_BY_ID_QUERY, bucket.name()), JsonArray.create().add(documentId));
    }

    public static List<Map<String, Object>> delete(final Bucket bucket, String documentId) {
        return getParameterizedResults(bucket, String.format(DELETE_DOCUMENT_BY_ID_QUERY, bucket.name()), JsonArray.create().add(documentId));
    }

    public static List<Map<String, Object>> save(final Bucket bucket, JsonObject data) {
        String documentId = !data.getString("document_id").equals("") ? data.getString("document_id") :
                UUID.randomUUID().toString();

        JsonArray parameters = JsonArray.create()
                .add(documentId)
                .add(data.getString("firstname"))
                .add(data.getString("lastname"))
                .add(data.getString("email"));

        return getParameterizedResults(bucket, String.format(SAVE_DOCUMENT_QUERY, bucket.name()), parameters);
    }

    private static List<Map<String, Object>> getParameterizedResults(final Bucket bucket, String queryStr, JsonArray parameters) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(queryStr, parameters);
        N1qlQueryResult result = bucket.query(query);
        return extractResultOrThrow(result);
    }

    private static List<Map<String, Object>> extractResultOrThrow(N1qlQueryResult result) {
        if (!result.finalSuccess()) {
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        }

        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        for (N1qlQueryRow row : result) {
            content.add(row.value().toMap());
        }
        return content;
    }
}
